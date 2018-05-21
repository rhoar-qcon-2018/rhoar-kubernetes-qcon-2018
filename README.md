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
1. Create a Vert.x microservice project to work with these existing services and communicate with each other [&#128279;
](#create-a-new-vertx-project)
   1. Implement Vert.x Kubernetes Config [&#128279;](#implement-kubernetes-config)
   1. Implement clients for the noun and adjective services using OpenAPI specifications [&#128279;](#implement-rest-clients)
   1. Implement a REST API using OpenAPI 3 specifications and service proxies [&#128279;](#implement-rest-api)
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

### Implement Kubernetes Config
#### TODO: Using vertx-config-kubernetes-configmap

### Implement REST clients
#### TODO: Using OpenAPI 3 Spec file to create REST Clients

### Implement REST API
#### TODO: Using OpenAPI 3 Spec file to create REST API in Vert.x

### Implement a new [Service Proxy](https://vertx.io/docs/vertx-service-proxy/java/)
#### TODO: Implement Service Proxy
