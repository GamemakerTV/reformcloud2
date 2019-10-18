package systems.reformcloud.reformcloud2.executor.node.network.packet.in.api;

import systems.reformcloud.reformcloud2.executor.api.common.ExecutorAPI;
import systems.reformcloud.reformcloud2.executor.api.common.api.basic.ExternalAPIImplementation;
import systems.reformcloud.reformcloud2.executor.api.common.network.channel.PacketSender;
import systems.reformcloud.reformcloud2.executor.api.common.network.channel.handler.NetworkHandler;
import systems.reformcloud.reformcloud2.executor.api.common.network.packet.Packet;

import java.util.function.Consumer;

public final class PacketInDatabaseRemoveDocument implements NetworkHandler {

    @Override
    public int getHandlingPacketID() {
        return ExternalAPIImplementation.EXTERNAL_PACKET_ID + 13;
    }

    @Override
    public void handlePacket(PacketSender packetSender, Packet packet, Consumer<Packet> responses) {
        String table = packet.content().getString("table");
        String key = packet.content().getString("key");

        switch (packet.content().getString("action")) {
            case "remove_key": {
                ExecutorAPI.getInstance().remove(table, key);
                break;
            }

            case "remove_identifier": {
                ExecutorAPI.getInstance().removeIfAbsent(table, key);
                break;
            }
        }
    }
}
