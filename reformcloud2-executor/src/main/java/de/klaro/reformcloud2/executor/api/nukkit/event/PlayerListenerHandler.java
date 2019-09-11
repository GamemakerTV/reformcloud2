package de.klaro.reformcloud2.executor.api.nukkit.event;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import de.klaro.reformcloud2.executor.api.common.ExecutorAPI;
import de.klaro.reformcloud2.executor.api.common.groups.utils.PlayerAccessConfiguration;
import de.klaro.reformcloud2.executor.api.common.network.channel.PacketSender;
import de.klaro.reformcloud2.executor.api.common.network.channel.manager.DefaultChannelManager;
import de.klaro.reformcloud2.executor.api.common.network.packet.Packet;
import de.klaro.reformcloud2.executor.api.common.process.ProcessInformation;
import de.klaro.reformcloud2.executor.api.common.process.ProcessState;
import de.klaro.reformcloud2.executor.api.nukkit.NukkitExecutor;
import de.klaro.reformcloud2.executor.api.packets.out.APIPacketOutHasPlayerAccess;

import java.util.concurrent.TimeUnit;

public final class PlayerListenerHandler implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void handle(final PlayerLoginEvent event) {
        if (ExecutorAPI.getInstance().getThisProcessInformation().getProcessGroup().getPlayerAccessConfiguration().isOnlyProxyJoin()) {
            PacketSender packetSender = DefaultChannelManager.INSTANCE.get("Controller").orElse(null);
            if (packetSender == null) {
                event.setCancelled(true);
                event.setKickMessage("§4§lThe current server is not connected to the controller");
                return;
            }

            Packet result = NukkitExecutor.getInstance().packetHandler().getQueryHandler().sendQueryAsync(packetSender, new APIPacketOutHasPlayerAccess(
                    event.getPlayer().getUniqueId(),
                    event.getPlayer().getName()
            )).getTask().getUninterruptedly(TimeUnit.SECONDS, 2);
            if (result != null && result.content().getBoolean("access")) {
                event.setCancelled(false);
            } else {
                event.setKickMessage("§4You have to connect through an internal proxy server");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void handle(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final ProcessInformation current = ExecutorAPI.getInstance().getThisProcessInformation();
        final PlayerAccessConfiguration configuration = current.getProcessGroup().getPlayerAccessConfiguration();

        if (configuration.isUseCloudPlayerLimit()
                && configuration.getMaxPlayers() < current.getOnlineCount() + 1
                && !player.hasPermission("reformcloud.join.full")) {
            player.kick("§4§lThe server is full");
            return;
        }

        if (configuration.isJoinOnlyPerPermission()
                && configuration.getJoinPermission() != null
                && !player.hasPermission(configuration.getJoinPermission())) {
            player.kick("§4§lYou do not have permission to enter this server");
            return;
        }

        if (configuration.isMaintenance()
                && configuration.getMaintenanceJoinPermission() != null
                && !player.hasPermission(configuration.getMaintenanceJoinPermission())) {
            player.kick("§4§lThis server is currently in maintenance");
            return;
        }

        if (current.getProcessState().equals(ProcessState.FULL) && !player.hasPermission("reformcloud.join.full")) {
            player.kick("§4§lYou are not allowed to join this server in the current state");
            return;
        }

        if (Server.getInstance().getOnlinePlayers().size() >= current.getMaxPlayers()
                && !current.getProcessState().equals(ProcessState.FULL)
                && !current.getProcessState().equals(ProcessState.INVISIBLE)) {
            current.setProcessState(ProcessState.FULL);
        }

        current.updateRuntimeInformation();
        current.onLogin(player.getUniqueId(), player.getName());
        NukkitExecutor.getInstance().setThisProcessInformation(current);
        ExecutorAPI.getInstance().update(current);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void handle(final PlayerQuitEvent event) {
        ProcessInformation current = ExecutorAPI.getInstance().getThisProcessInformation();
        if (!current.isPlayerOnline(event.getPlayer().getUniqueId())) {
            return;
        }

        if (Server.getInstance().getOnlinePlayers().size() < current.getMaxPlayers()
                && !current.getProcessState().equals(ProcessState.READY)
                && !current.getProcessState().equals(ProcessState.INVISIBLE)) {
            current.setProcessState(ProcessState.READY);
        }

        current.updateRuntimeInformation();
        current.onLogout(event.getPlayer().getUniqueId());
        NukkitExecutor.getInstance().setThisProcessInformation(current);
        ExecutorAPI.getInstance().update(current);
    }
}