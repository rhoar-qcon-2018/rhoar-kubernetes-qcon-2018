package com.redhat.qcon.services.noun;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;

public class NounServiceImpl implements NounService {

    Vertx vertx;
    HttpClient client;

    public NounServiceImpl(Vertx vertx) {
        this.vertx = vertx;
        HttpClientOptions httpOptions = new HttpClientOptions(vertx.getOrCreateContext().config().getJsonObject("noun"));
        vertx.createHttpClient(httpOptions);
    }

    void handleBody(Handler<AsyncResult<JsonObject>> nounGetHandler, Buffer b) {
        nounGetHandler.handle(Future.succeededFuture(b.toJsonObject()));
    }

    void handleClientResponse(Handler<AsyncResult<JsonObject>> nounGetHandler, HttpClientResponse response) {
            if (response.statusCode() == HttpResponseStatus.OK.code()) {
                response.bodyHandler(b -> this.handleBody(nounGetHandler, b));
            } else {
                nounGetHandler.handle(Future.failedFuture(response.statusMessage()));
            }
    }

    @Override
    public void get(Handler<AsyncResult<JsonObject>> nounGetHandler) {
        client.get("/api/v1/noun", r -> this.handleClientResponse(nounGetHandler, r));
    }

    @Override
    public void save(String noun, Handler<AsyncResult<JsonObject>> nounSaveHandler) {

    }

    @Override
    public NounService healthCheck(Handler<AsyncResult<Boolean>> nounHealthCheckHandler) {
        return this;
    }
}
