package com.github.atomfrede.vertx.publisher;

import com.hazelcast.config.Config;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.NetworkConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class Publisher extends AbstractVerticle {

    @Override
    public void start() {

        HazelcastClusterManager mgr = new HazelcastClusterManager();

        Config conf = new Config();

        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.setPort(5702);
        networkConfig.setPortAutoIncrement(false);

        networkConfig.setInterfaces(new InterfacesConfig().setEnabled(true).addInterface("192.168.0.111"));

        conf.setNetworkConfig(networkConfig);
        mgr.setConfig(conf);

        mgr.setConfig(conf);

        VertxOptions options = new VertxOptions()
                .setClustered(true)
                .setClusterHost("192.168.0.111")
                .setClusterManager(mgr);

        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();

                vertx.setPeriodic(10000, t -> vertx.eventBus().publish("test.ping", "ping from publisher"));

            } else {
                // failed!
            }
        });
    }
}
