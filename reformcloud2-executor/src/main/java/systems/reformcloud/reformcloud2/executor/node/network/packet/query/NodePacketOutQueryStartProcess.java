package systems.reformcloud.reformcloud2.executor.node.network.packet.query;

import systems.reformcloud.reformcloud2.executor.api.common.configuration.JsonConfiguration;
import systems.reformcloud.reformcloud2.executor.api.common.groups.ProcessGroup;
import systems.reformcloud.reformcloud2.executor.api.common.groups.utils.Template;
import systems.reformcloud.reformcloud2.executor.api.common.network.NetworkUtil;
import systems.reformcloud.reformcloud2.executor.api.common.network.packet.DefaultPacket;

public class NodePacketOutQueryStartProcess extends DefaultPacket {

    public NodePacketOutQueryStartProcess(ProcessGroup processGroup, Template template, JsonConfiguration data) {
        super(NetworkUtil.NODE_TO_NODE_QUERY_BUS + 2, new JsonConfiguration()
                .add("group", processGroup)
                .add("template", template)
                .add("data", data)
        );
    }
}
