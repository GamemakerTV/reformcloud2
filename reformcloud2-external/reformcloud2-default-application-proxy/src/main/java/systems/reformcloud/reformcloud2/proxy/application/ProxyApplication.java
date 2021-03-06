package systems.reformcloud.reformcloud2.proxy.application;

import systems.reformcloud.reformcloud2.executor.api.common.ExecutorAPI;
import systems.reformcloud.reformcloud2.executor.api.common.application.api.Application;
import systems.reformcloud.reformcloud2.executor.api.common.network.NetworkUtil;
import systems.reformcloud.reformcloud2.proxy.application.network.PacketQueryInRequestConfig;

public class ProxyApplication extends Application {

    @Override
    public void onLoad() {
        ConfigHelper.init(dataFolder());
        ExecutorAPI.getInstance().getPacketHandler().registerHandler(new PacketQueryInRequestConfig());
    }

    @Override
    public void onDisable() {
        ExecutorAPI.getInstance().getPacketHandler().unregisterNetworkHandlers(NetworkUtil.EXTERNAL_BUS + 2);
    }
}
