# Reactive Microservices on Kubernetes
## Using Red Hat OpenShift Application Runtimes

## Overview
Microservices and Containers have changed the entire landscape of software
development in the last few years. We are now able to decompose our development
work into smaller and more digestible components which are easier to understand
and easier to split amongst developer teams for better parallel workflow.

Just as Linux has pretty much won the hearts and minds of most developers
for the operating system of the cloud, Kubernetes has become the de-facto
standard for orchestrating containers at scale.

In this workshop, you will learn how to leverage Containers and Kubernetes
to build productive DevOps workflow using Kubernetes (Wrapped by OpenShift)
to build, test, deploy, and validate microservices quickly and reliably.

Some of the tools we will leverage in this workshop are listed below:

* [Kubernetes](https://kubernetes.io/) (In the form of OpenShift Container Platform 3.9)
* [Jenkins](https://jenkins.io/) (For continuous integration and continuous delivery)
* [SonarQube](https://www.sonarqube.org/) (For code quality analysis)
* [Sonatype Nexus](https://www.sonatype.com/nexus-repository-sonatype) (For an artifact repository)
* [OWASP Zed Attack Proxy](https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project) (For security analysis of web applications)
* [OWASP Dependency Check](https://www.owasp.org/index.php/OWASP_Dependency_Check)
* [Vert.x](http://vertx.io/) - A toolkit for developing reactive applications on the JVM
* [Java](http://openjdk.org/) - A modern and high-performance programming language
* [Node.js](https://nodejs.org/) - A programming language well suited to quickly develop applications
* [Spring Boot](https://projects.spring.io/spring-boot/) An alternative to writing applications using JavaEE
* [HoverFly](https://hoverfly.readthedocs.io/en/latest/) A service virtualization tool which simplifies testing Microservices

## What we are going to do during today's workshop

1. Load up a complete DevOps environment in Kubernetes using Ansible automation
1. Load our existing application code into this DevOps environment and wire it up to our GitHub repos
1. [▼](#create-a-new-vertx-project) Create a Vert.x microservice project to work with these existing services and communicate with each other
   1. [▼](#basic-vertx-concepts) Familiarize ourselves with some basic Vert.x concepts
   1. [▼](#implement-kubernetes-config) Implement Vert.x Kubernetes Config
   1. [▼](#implement-rest-clients) Implement clients for the noun and adjective services using OpenAPI specifications
   1. [▼](#implement-rest-api) Implement a REST API using OpenAPI 3 specifications and service proxies
   1. Implement a new [service proxy](https://vertx.io/docs/vertx-service-proxy/java/)
   1. Implement a reactive Kafka system to stream "liked" insults
   1. Generate JavaScript code to integrate our application into the UI
   1. Implement circuit breakers to prevent poor user experience
   1. Implement some BDD tests for our service
1. Run our application code through the DevOps pipeline and resolve issues QUICKLY!
1. Perform code quality analysis and unit/integration testing using SonarQube and HoverFly
1. Analyze our web application for security vulnerabilities using OWASP Zed Attack Proxy
1. Analyze our the library dependencies of our services to check for vulnerable libraries

### Create a new Vert.x project
1. Ensure that you have Apache Maven >= 3.3.9
1. From the `insult-service` directory, run the following command
```bash
$ mvn io.fabric8:vertx-maven-plugin:1.0.13:setup -DvertxVersion=3.5.1
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] Building Maven Stub Project (No POM) 1
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- vertx-maven-plugin:1.0.13:setup (default-cli) @ standalone-pom ---
[INFO] No pom.xml found, creating it in /home/dphillips/Documents/RedHat/Workspace/rhoar-kubernetes-qcon-2018/insult-service
Set the project groupId [io.vertx.example]: com.redhat.qcon
Set the project artifactId [my-vertx-project]: insult-service
Set the project version [1.0-SNAPSHOT]: 1.0.0-SNAPSHOT
Set the vertcile class name [com.redhat.qcon.MainVerticle]: 
[INFO] Creating verticle com.redhat.qcon.MainVerticle
[INFO] Creating directory /home/dphillips/Documents/RedHat/Workspace/rhoar-kubernetes-qcon-2018/insult-service/src/main/java/com/redhat/qcon
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 34.510 s
[INFO] Finished at: 2018-05-21T12:07:46-04:00
[INFO] Final Memory: 9M/166M
[INFO] ------------------------------------------------------------------------
```

This will create a new Maven POM file populated based on the values you entered during the setup.

### Basic Vert.x Concepts

The [Vert.x Core Documentation](https://vertx.io/docs/vertx-core/java/) is a really great reference to some of the basic
concepts in Vert.x. We'll cover a few of these things here, but please feel free to go to the official docs for more
in-depth information.

Vert.x implements a *fluent* SPI. This means that for most Vert.x components, you can chain calls together in a nicely
readable manner.

```java
vertx.eventBus()
     .consumer("some-address")
     .toObservable()
     .doOnError(this::errorHandler)
     .subscribe(this::messageHandler);
```

Another core concept of Vert.x is that everything which is done in a Verticle should be done in a non-blocking way. 
To support this, Vert.x provides non-blocking implementations of many common functionalities such as:
* File I/O
* Network I/O
* Database Access
* Message Queues
* HTTP Clients/Servers
* Authentication/Authorization/Audit (AAA)
* Metrics

From the new project we generated via Maven, we can see that a class called `MainVerticle` was created. 
[Verticles](https://vertx.io/docs/vertx-core/java/#_verticles) are the basic unit of an application in Vert.x. By default,
Verticles are run single-threaded on an event loop (Reactor Pattern). The one difference between this and other Reactor 
Pattern implementations you may have seen before is that Vert.x runs MULTIPLE event loops in parallel, calling it 
[Multi-Reactor](https://vertx.io/docs/vertx-core/java/#_reactor_and_multi_reactor).

The basic contents of a Vertical are a class definition and a `start` method, as shown here:

```java
package com.redhat.qcon;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        startFuture.complete(); // Called once the Vertical is ready
    }
}
```

Because Vert.x uses event loops for Verticles, we must always ensure that we do not call blocking code and thus block
the event loop. Since Vert.x does not have non-blocking APIs for every situation, it provides a means for use to 
implement traditional blocking Java code using the `vertx.executeBlocking` method. For example, if we wanted to make a
call via [JNDI](http://www.oracle.com/technetwork/java/jndi/index.html) to look up something in an LDAP directory, we
might do something like:

```java
vertx.executeBlocking(future -> {
    // Make our JNDI calls here!
    future.complete(result);
}, result -> {
    // Handle the results of the blocking operation once it completes.
});
```

The final concept we should introduce for Vert.x is the Event Bus. Since all of the Verticles are implemented to 
run single-threaded and potentially across multiple threads/cores in parallel, we need a safe way to share data which
will not cause race conditions or concurrency problems. To facilitate this, Vert.x has an Event Bus through which we
can send/receive messages between Verticles. A simple example of using the event bus might look like:

```java
// Create a consumer and reply when we get PING messages
vertx.eventBus()
    .consumer("ping-timer")
    .toFlowable()
    .doOnEach(m -> System.out.println(m.getValue().body()))
    .subscribe(m -> m.reply(new JsonObject().put("action", "PONG")));

// Set a period timer to send a "PING" message every 300 milliseconds
vertx.timerStream(300)
    .toObservable()
    .map(t -> new JsonObject().put("action", "PING"))
    .subscribe(ping -> vertx.eventBus()
            .rxSend("ping-timer", ping)
            .subscribe(m -> System.out.println(m.body())));
```

### Implement Kubernetes Config
Following one of the tenets of [12 Factor Applications](https://12factor.net/config), we will want to store our
application's configuration in the deployment environment instead of in our code. Vert.x makes this somewhat painless
by providing a comprehensive set of APIs for loading the application's configuration. In our case, since we are
deploying to Kubernetes, we will use Kubernetes ConfigMaps for our configuration. Here's how I would do it.

```java
package com.redhat.qcon;

import io.reactivex.Maybe;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {

        initConfigRetriever()
                .doOnError(startFuture::fail)
                .doOnComplete(startFuture::complete)
                .subscribe(c -> {
                    context.config().mergeIn(c);
                });
    }

    Maybe<JsonObject> initConfigRetriever() {
        // Load the default configuration from the classpath
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

        // Add the default and container config options into the ConfigRetriever
        ConfigRetrieverOptions retrieverOptions = new ConfigRetrieverOptions()
                .addStore(defaultOpts)
                .addStore(localConfig);

        // Check to see if we are running on Kubernetes/OCP
        if (System.getenv().containsKey("KUBERNETES_NAMESPACE")) {

            // When running inside of Kubernetes, configure the application to also load from a ConfigMap
            ConfigStoreOptions confOpts = new ConfigStoreOptions()
                    .setType("configmap")
                    .setConfig(new JsonObject()
                            .put("name", "insult-config")
                            .put("optional", true)
                    );
            retrieverOptions.addStore(confOpts);
        }

        // Create the ConfigRetriever and return the Maybe when complete
        return ConfigRetriever.create(vertx, retrieverOptions).rxGetConfig().toMaybe();
    }
}
```

This example replaces the generic Verticle type with one which has been refactored to use
Reactive Extensions. Most of the rest of this Workshop with rely on using ReactiveX for 
our Vert.x code.

### Implement a new [Service Proxy](https://vertx.io/docs/vertx-service-proxy/java/)
Vert.x provides a facility to make it easier to consume/produce messages on the Event Bus.

### Implement REST clients
Vert.x recently introduced significant support for the [OpenAPI v3 Specification](https://www.openapis.org/) language. 
OpenAPI 3 allows us to describe a REST API using YAML or JSON. From those specifications, we can create both server
and client implementations for the REST API. We will use this facility in Vert.x to create REST API clients for the
Noun and Adjective services which were previously implemented using NodeJS and Spring Boot. Here's how:

1. Create a new class 

### Implement REST API
#### TODO: Using OpenAPI 3 Spec file to create REST API in Vert.x
