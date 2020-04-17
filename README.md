## about
a simple customer service chat back end demonstrating the Spring MVC REST framework. create customer service representatives, customer users, and chat messages between them. then, 
retrieve messages between representatives and customers in paginated chunks.
in-memory application caching is implemented with [Ehcache](http://www.ehcache.org) as a proof-of-concept for horizontal application-layer scalability with reduced database access.

## usage
#### prerequisites
1. MySQL-compatible server([MySQL Community Server](https://dev.mysql.com/downloads/mysql), [MariaDB](https://downloads.mariadb.org), [Percona](https://www.percona.com/downloads)) installed locally or on an accessible host.
1. MySQL user with permissions to create users and schemas.

#### setup
1. `git clone git@github.com:mikerodonnell/spring_rest_cache_demo.git`
1. import the project schema. for example:<br/>
`mysql -h localhost -u myAdminUser -p myAdminPassword < /somewhere/git/projects/spring_rest_cache_demo/setup/schema.sql`
1. edit the JDBC URL `jdbc:mysql://localhost:3306/chat` in src/main/webapp/WEB-INF/spring/root-context.xml as needed.
1. from spring_rest_cache_demo/, run `mvn clean install`.
1. deploy the spring_rest_cache_demo/target/spring_rest_cache_demo.war file to any Java EE server (Java 1.8+).

## examples
create a customer user (customer is the default user type):
```
curl -X PUT --header "Content-Type: application/json" --data '{"username":"kbania", "password":"gold"}' "http://localhost:8080/spring_rest_cache_demo/user/create"
```

create a customer service user:
```
curl -X PUT --header "Content-Type: application/json" --data '{"username":"uleo", "password":"jeffrey", "userType":"CUSTOMER_SERVICE"}' "http://localhost:8080/spring_rest_cache_demo/user/create"
```

get a user:
```
curl "http://localhost:8080/spring_rest_cache_demo/user/kbania"
```

create a message from a customer to customer service (text-only is the default message type):
```
curl -X PUT --header "Content-Type: application/json" --data '{"senderUsername":"kbania", "recipientUsername":"uleo", "messageBody":"hello, i have a question"}' "http://localhost:8080/spring_rest_cache_demo/message/create"
```

create a message from a customer representative to a customer:
```
curl -X PUT --header "Content-Type: application/json" --data '{"senderUsername":"kbania", "recipientUsername":"uleo", "messageBody":"sure, how can i help?"}' "http://localhost:8080/spring_rest_cache_demo/message/create"
```

create a video link message:
```
curl -X PUT --header "Content-Type: application/json" --data '{"senderUsername":"kbania", "recipientUsername":"uleo", "messageBody":"https://www.youtube.com/watch?v=s13dLaTIHSg", "messageType":"VIDEO_LINK"}' "http://localhost:8080/spring_rest_cache_demo/message/create"
```

create an image link message:
```
curl -X PUT --header "Content-Type: application/json" --data '{"senderUsername":"kbania", "recipientUsername":"uleo", "messageBody":"https://s-media-cache-ak0.pinimg.com/originals/07/c5/b2/07c5b236ccf2658b37d8061c3327615b.jpg", "messageType":"IMAGE_LINK"}' "http://localhost:8080/spring_rest_cache_demo/message/create"
```

get historical messages between a customer user and a customer service representative:
```
curl "http://localhost:8080/spring_rest_cache_demo/message/?customerUsername=kbania&customerServiceUsername=uleo&startIndex=0&offset=5
```

## implementation notes
- caching is implemented with Spring's [cache abstraction](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/cache.html) with [Ehcache](http://www.ehcache.org) as the underlying provider.
objects are retrieved from the database for the initial request, then from the in-memory cache afterwards. messages are selectively evicted when a new message is created between
the same two people. cached messages between other users are unaffected. this is preferable to, for example, MySQL query cache, where inserts invalidate all cached records for the type. 
this is more for proof of concept than practicality; chat applications requires lots writes relative to reads, so caching is of limited value.
- Ehcache supports in-memory and persistent (file system) storage. only in-memory is implemented in this demo -- the cache is lost upon server restart. objects expire after an hour as a safeguard; if all
goes according to plan our cache should never return stale data per the 'smart' selective eviction described above. see ehcache.xml.
- it'd make sense to turn off MySQL query caching for optimization, since it'll be mostly writes that reach the DB layer. an exception could be if MySQL bug #26513 gets implemented; our user and message tables have very different access patterns.
- pagination with Spring's [Pageable](http://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Pageable.html) interface goes down to the database layer with LIMIT and OFFSET 
in the underlying query -- not just pulling all entities into memory and then hiding some from the response.
- [H2](http://www.h2database.com/) in-memory database is used rather than mocking, for comprehensive tests including SQL logic.
- the GET /messages endpoint takes two users, without distinguishing sender vs recipient. since the query is bidirectional, the message table is indexed on sender_id and recipient_id, but not a composite index on {sender_id, recipient_id} or {recipient_id, sender_id}

## stack
- [Ehcache](http://www.ehcache.org) _application layer caching for reduced database access_
- [H2](http://www.h2database.com/) _embedded database instance for tests_
- [Hibernate](http://hibernate.org/orm/) _JPA implementation_
- [Jackson](https://github.com/FasterXML/jackson) _JSON serialization_
- [JUnit](http://junit.org/junit4) _tests_
- [MariaDB](https://mariadb.org) _open source MySQL Java client_
- [Maven](https://maven.apache.org) with [Surefire](http://maven.apache.org/components/surefire/maven-surefire-plugin/) plugin _build and dependency management_
- [Spring](https://projects.spring.io/spring-framework)
  * spring-beans _dependency injection_
  * spring-data-jpa and spring-orm _lightweight JPA implementation with pagination_
  * spring-test _JUnit integration_
  * spring-webmvc _REST endpoint framework_
