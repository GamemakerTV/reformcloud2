package systems.reformcloud.reformcloud2.executor.api.bungee.event;

import net.md_5.bungee.api.ProxyServer;
import systems.reformcloud.reformcloud2.executor.api.bungee.BungeeExecutor;
import systems.reformcloud.reformcloud2.executor.api.common.api.basic.events.ProcessStartedEvent;
import systems.reformcloud.reformcloud2.executor.api.common.api.basic.events.ProcessStoppedEvent;
import systems.reformcloud.reformcloud2.executor.api.common.api.basic.events.ProcessUpdatedEvent;
import systems.reformcloud.reformcloud2.executor.api.common.event.handler.Listener;

public final class ProcessEventHandler {

    @Listener
    public void handleStart(ProcessStartedEvent event) {
        BungeeExecutor.getInstance().publishNotification(
                BungeeExecutor.getInstance().getMessages().getProcessStarted(),
                event.getProcessInformation().getName()
        );
    }

    @Listener
    public void handleUpdate(ProcessUpdatedEvent event) {
        BungeeExecutor.registerServer(event.getProcessInformation());
    }

    @Listener
    public void handleRemove(ProcessStoppedEvent event) {
        ProxyServer.getInstance().getServers().remove(event.getProcessInformation().getName());
        if (event.getProcessInformation().isLobby()) {
            ProxyServer.getInstance().getConfig().getListeners().forEach(listenerInfo -> listenerInfo.getServerPriority().remove(event.getProcessInformation().getName()));
        }

        BungeeExecutor.getInstance().publishNotification(
                BungeeExecutor.getInstance().getMessages().getProcessStopped(),
                event.getProcessInformation().getName()
        );
    }
}