package com.redhat.qcon.services.noun;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@ProxyGen
@VertxGen
public interface NounService {

    static NounService create(Vertx vertx) {
        return new NounServiceImpl(vertx);
    }

    static NounService createProxy(Vertx vertx, String address) {
        return new NounServiceVertxEBProxy(vertx, address);
    }

    // Business logic methods here!!

    void get(Handler<AsyncResult<JsonObject>> nounGetHandler);

    void save(String noun, Handler<AsyncResult<JsonObject>> nounSaveHandler);

    @Fluent
    NounService healthCheck(Handler<AsyncResult<Boolean>> nounHealthCheckHandler);
}
