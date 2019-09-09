package de.klaro.reformcloud2.executor.api.packets.out;

import de.klaro.reformcloud2.executor.api.common.configuration.JsonConfiguration;
import de.klaro.reformcloud2.executor.api.common.network.NetworkUtil;
import de.klaro.reformcloud2.executor.api.common.network.packet.DefaultPacket;

import java.util.UUID;

public final class APIPacketOutPlayerCommandExecute extends DefaultPacket {

    public APIPacketOutPlayerCommandExecute(String name, UUID uuid, String message) {
        super(NetworkUtil.PLAYER_INFORMATION_BUS + 5, new JsonConfiguration()
                .add("name", name)
                .add("uuid", uuid)
                .add("command", message)
        );
    }
}
