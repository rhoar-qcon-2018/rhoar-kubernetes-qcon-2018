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
1. Create a Vert.x microservice project to work with these existing services and communicate with each other
   1. Implement a new [service proxy](https://vertx.io/docs/vertx-service-proxy/java/)
   1. Implement clients for the noun and adjective services using OpenAPI specifications
   1. Implement a REST API using OpenAPI 3 specifications
   1. Implement a reactive Kafka system to stream "liked" insults
   1. Generate JavaScript code to integrate our application into the UI
   1. Implement circuit breakers to prevent poor user experience
   1. Implement some BDD tests for our service
1. Run our application code through the DevOps pipeline and resolve issues QUICKLY!
1. Perform code quality analysis and unit/integration testing using SonarQube and HoverFly
1. Analyze our web application for security vulnerabilities using OWASP Zed Attack Proxy
1. Analyze our the library dependencies of our services to check for vulnerable libraries
