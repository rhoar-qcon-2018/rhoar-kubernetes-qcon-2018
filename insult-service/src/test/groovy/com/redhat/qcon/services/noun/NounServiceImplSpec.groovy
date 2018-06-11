package com.redhat.qcon.services.noun

import io.netty.handler.codec.http.HttpHeaderValues
import io.netty.handler.codec.http.HttpResponseStatus
import io.specto.hoverfly.junit.core.SimulationSource
import io.specto.hoverfly.junit.dsl.ResponseCreators
import io.specto.hoverfly.junit.rule.HoverflyRule
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.AsyncConditions

import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON
import static io.specto.hoverfly.junit.core.SimulationSource.*
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.*
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success

class NounServiceImplSpec extends Specification {

    @Shared
    @ClassRule
    HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode()

    @Shared
    final String RESPONSE_BODY = new JsonObject().put('noun', 'fat-head').encodePrettily()

    @Shared
    final SimulationSource RESPONSE_ONE = dsl(
            service("localhost")
                    .get("/api/v1/noun")
                    .willReturn(success(RESPONSE_BODY, APPLICATION_JSON)))

    def setup() {
        hoverflyRule.resetJournal()
    }

    def 'Test hoverfly integration'() {
        setup: 'Configure service under test'
            NounServiceImpl underTest = new NounServiceImpl(Vertx.vertx())
        and: 'AsyncConditions'
            def async = new AsyncConditions(1)
        and: 'Service virtualization has been configured'
            hoverflyRule.simulate(simulation)

        expect: 'The appropriate response to REST calls'
        underTest.get({ resp ->
            async.evaluate {
                resp.get('noun') == 'fat-head'
            }
        })
        async.await(10)

        where: 'The following data is applied'
            simulation   || responseCode | responseMsg | body
            RESPONSE_ONE || 200          | 'OK'        | RESPONSE_BODY
    }
}
