package systems.reformcloud.reformcloud2.executor.controller.packet.in;

import systems.reformcloud.reformcloud2.executor.api.common.ExecutorAPI;
import systems.reformcloud.reformcloud2.executor.api.common.api.basic.ExternalAPIImplementation;
import systems.reformcloud.reformcloud2.executor.api.common.api.basic.packets.out.ExternalAPIPacketOutAPIAction;
import systems.reformcloud.reformcloud2.executor.api.common.network.channel.PacketSender;
import systems.reformcloud.reformcloud2.executor.api.common.network.channel.handler.NetworkHandler;
import systems.reformcloud.reformcloud2.executor.api.common.network.packet.Packet;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.function.Consumer;

public final class ControllerPacketInAPIAction implements NetworkHandler {

    @Override
    public int getHandlingPacketID() {
        return ExternalAPIImplementation.EXTERNAL_PACKET_ID + 26;
    }

    @Override
    public void handlePacket(@Nonnull PacketSender packetSender, @Nonnull Packet packet, @Nonnull Consumer<Packet> responses) {
        UUID targetPlayer = packet.content().get("1", UUID.class);

        switch (packet.content().get("action", ExternalAPIPacketOutAPIAction.APIAction.class)) {
            case CONNECT: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().connect(targetPlayer, packet.content().getString("2"));
                break;
            }

            case CONNECT_PLAYER: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().connect(targetPlayer, packet.content().get("2", UUID.class));
                break;
            }

            case RESPAWN: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().respawn(targetPlayer);
                break;
            }

            case PLAY_SOUND: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().playSound(targetPlayer,
                        packet.content().getString("2"),
                        packet.content().get("3", Float.class),
                        packet.content().get("4", Float.class)
                );
                break;
            }

            case SEND_TITLE: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().sendTitle(targetPlayer,
                        packet.content().getString("2"),
                        packet.content().getString("3"),
                        packet.content().getInteger("4"),
                        packet.content().getInteger("5"),
                        packet.content().getInteger("6")
                );
                break;
            }

            case KICK_PLAYER: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().kickPlayer(targetPlayer, packet.content().getString("2"));
                break;
            }

            case KICK_SERVER: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().kickPlayerFromServer(targetPlayer, packet.content().getString("2"));
                break;
            }

            case PLAY_EFFECT: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().playEffect(targetPlayer,
                        packet.content().getString("2"),
                        packet.content().get("3", Object.class)
                );
                break;
            }

            case PLAY_ENTITY_EFFECT: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().playEffect(targetPlayer, packet.content().getString("2"));
                break;
            }

            case SEND_MESSAGE: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().sendMessage(targetPlayer, packet.content().getString("2"));
                break;
            }

            case LOCATION_TELEPORT: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().teleport(targetPlayer,
                        packet.content().getString("2"),
                        packet.content().get("3", Double.class),
                        packet.content().get("4", Double.class),
                        packet.content().get("5", Double.class),
                        packet.content().get("6", Float.class),
                        packet.content().get("7", Float.class)
                );
                break;
            }

            case SET_RESOURCE_PACK: {
                ExecutorAPI.getInstance().getSyncAPI().getPlayerSyncAPI().setResourcePack(targetPlayer, packet.content().getString("2"));
                break;
            }
        }
    }
}
