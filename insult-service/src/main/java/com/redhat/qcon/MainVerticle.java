package com.redhat.qcon;

import io.reactivex.Single;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

    Single<JsonObject> initConfigRetriever() {
        // Load the default configuration from the classpath
        LOG.info("Configuration store loading.");
        ConfigStoreOptions defaultOpts = new ConfigStoreOptions()
                .setType("file")
                .setFormat("json")
                .setConfig(new JsonObject().put("path", "insult_default_config.json"));

        // Load container specific configuration from a specific file path inside of the container
        ConfigStoreOptions localConfig = new ConfigStoreOptions()
                .setType("file")
                .setFormat("json")
                .setConfig(new JsonObject().put("path", "/opt/docker_config.json"))
                .setOptional(true);

        // When running inside of Kubernetes, configure the application to also load from a ConfigMap
        // This config is ONLY loaded when the environment variable KUBERNETES_NAMESPACE is set.
        ConfigStoreOptions confOpts = new ConfigStoreOptions()
                .setType("configmap")
                .setConfig(new JsonObject()
                        .put("name", "insult-config")
                        .put("optional", true)
                );

        // Add the default and container config options into the ConfigRetriever
        ConfigRetrieverOptions retrieverOptions = new ConfigRetrieverOptions()
                .addStore(defaultOpts)
                .addStore(localConfig)
                .addStore(confOpts);

        // Create the ConfigRetriever and return the Maybe when complete
        return ConfigRetriever.create(vertx, retrieverOptions).rxGetConfig();
    }

    @Override
    public void start(Future<Void> startFuture) {

        initConfigRetriever()
                .doOnError(startFuture::fail)
                .subscribe(c -> {
                    LOG.info(c.encodePrettily());
                    context.config().mergeIn(c);
                    startFuture.complete();
                });
    }
}
