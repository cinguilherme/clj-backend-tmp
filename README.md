# web-app-crux

This is a template application for Web development using Clojure

this aims to provide a way of simply writing an application focosing on its business and not on its tooling so much

Using Stuart Sierra (Alessandra Sierra) great component framework and several other comunity libraries to integrate with external dependencies such as DBs message brokers etc

All sample components already pluged into the system map, just remove what not gonna be used and good to go.
- Steps to remove unused dependecy on system map (requires a little knowlede of component framework)

Also using the already super popular Pedestal way of building an web app with Jetty as the server

Architecture style aligned with the Hexagonal Architecture with a little sprincles of personal bias.


There is a docker-compose file here with most of the dependecies ready to roll, except for Datomic. 
This one you have to figure out. [Datomic Dev](https://docs.datomic.com/on-prem/getting-started/dev-setup.html)

quick setup for datomic after download the local-dev-server 
run `bin/maven-install` so that the datomic.api can be available since it's not public.

## Releases and Dependency Information

[Leiningen] dependency information:

    [web-app-crux "0.1.0-SNAPSHOT"]

[Maven] dependency information:

    <dependency>
      <groupId>web-app-crux</groupId>
      <artifactId>web-app-crux</artifactId>
      <version>0.1.0-SNAPSHOT</version>
    </dependency>

[Leiningen]: http://leiningen.org/
[Maven]: http://maven.apache.org/


## Usage

DEV - lein with-profile +dev

TODO



## Change Log

* Version 0.1.0-SNAPSHOT


## Copyright and License

Copyright © 2022 TODO_INSERT_NAME

[MIT](https://choosealicense.com/licenses/mit/)
