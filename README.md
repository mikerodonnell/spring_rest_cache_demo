## about
a simple customer service chat back end demonstrating Spring MVC REST framework. create customer service representatives, customer users, and chat messages between them. then, 
retrieve messages between any given representative and customer, either all at once or in paginated chunks.
in-memory application caching is implemented with [Ehcache](http://www.ehcache.org) as a proof-of-concept for horizontal application-layer scalability with reduced database access.


## usage
####prerequisites
1. a MySQL-compatible server([MySQL Community Server](https://dev.mysql.com/downloads/mysql), [MariaDB](https://downloads.mariadb.org), [Percona](https://www.percona.com/downloads)) is installed either locally or another accessible host.
2. MySQL root user access, or another user with permissions to create users and schemas.

####steps
1. clone the repository:<br/>
`git clone git@github.com:mikerodonnell/spring_rest_cache_demo.git`
2. import the project schema. for example:<br/>
`mysql -h localhost -u myAdminUser -p myAdminPassword < /somewhere/git/projects/spring_rest_cache_demo/setup/schema.sql`
3. if using a database other than MySQL Server, or on another host, change the default JDBC URL `jdbc:mysql://localhost:3306/chat` in src/main/webapp/WEB-INF/spring/root-context.xml as needed.
4. from within the spring_rest_cache_demo/ directory, run `mvn clean install` to build and run tests.
5. deploy the spring_rest_cache_demo/target/spring_rest_cache_demo.war file to any Java EE server container.
6. make API requests using cURL or any HTTP client as shown in the examples.


## examples

create a new customer user (customer is the default user type):<br/>
`curl -X PUT --header "Content-Type: application/json" --data '{"username":"kbania", "password":"gold"}' "http://localhost:8080/spring_rest_cache_demo/user/create"`

create a new customer service user:<br/>
`curl -X PUT --header "Content-Type: application/json" --data '{"username":"uleo", "password":"jeffrey", "userType":"CUSTOMER_SERVICE"}' "http://localhost:8080/spring_rest_cache_demo/user/create"`

get an existing user:<br/>
`curl "http://localhost:8080/spring_rest_cache_demo/user/kbania"`

create a new message between an existing customer user and an existing customer service user (text-only is the default message type):<br/>
`curl -X PUT --header "Content-Type: application/json" --data '{"customerUsername":"kbania", "customerServiceUsername":"uleo", "messageBody":"hello"}' "http://localhost:8080/spring_rest_cache_demo/message/create"`

create a new video link message between an existing customer user and an existing customer service user:<br/>
`curl -X PUT --header "Content-Type: application/json" --data '{"customerUsername":"kbania", "customerServiceUsername":"uleo", "messageBody":"https://www.youtube.com/watch?v=s13dLaTIHSg", "messageType":"VIDEO_LINK"}' "http://localhost:8080/spring_rest_cache_demo/message/create"`

create a new image link message between an existing customer user and an existing customer service user:<br/>
`curl -X PUT --header "Content-Type: application/json" --data '{"customerUsername":"kbania", "customerServiceUsername":"uleo", "messageBody":"https://s-media-cache-ak0.pinimg.com/originals/07/c5/b2/07c5b236ccf2658b37d8061c3327615b.jpg", "messageType":"IMAGE_LINK"}' "http://localhost:8080/spring_rest_cache_demo/message/create"`

get all existing message between a customer user and a customer service user:<br/>
`curl "http://localhost:8080/spring_rest_cache_demo/message/?customerUsername=kbania&customerServiceUsername=uleo"`

get a paginated subset of existing messages between a customer user and a customer service user:<br/>
`curl "http://localhost:8080/spring_rest_cache_demo/message/?customerUsername=kbania&customerServiceUsername=uleo&startIndex=0&offset=5`


## implementation notes
- caching is implemented with Spring's [cache abstraction](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/cache.html) with [Ehcache](http://www.ehcache.org) as the underlying provider.
users and messages are retrieved from the database for the first request, then from the application's in-memory cache for future requests. cached messages are selectively evicted when a new message is created between
the same two people. cached messages between other users are unaffected. this is preferable to, for example, MySQL query cache, where all cached records are cleared whenever the table is inserted into. 
this is more for proof of concept than practicality; the use pattern of a chat application includes lots writes relative to reads, so caching is of limited value.
- Ehcache supports in-memory and persistent (file system) storage. only in-memory is used in this demo -- the cache is lost upon server restart. cached objects expire after an hour just as a safeguard; if all
goes according to plan our cache should never return stale data per the 'smart' selective eviction described above. see ehcache.xml.
- it'd make sense to turn off MySQL query caching for optimization, since it'll be mostly writes that reach the DB layer. an exception could be if MySQL bug #26513 gets implemented; our user and message tables have very different access patterns.
- pagination with Spring's [Pageable](http://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Pageable.html) interface goes down to the database layer with LIMIT and OFFSET 
in the underlying query -- not just pulling all entities into memory and then hiding some from the response.
- [H2](http://www.h2database.com/) in-memory database is used in the unit tests, which allows for more effective testing of database integration than a mocking library.


## tools used
- [Ehcache](http://www.ehcache.org) _application layer caching for reduced database access_
- [H2](http://www.h2database.com/) _embedded database instance for tests_
- [Hibernate](http://hibernate.org/orm/) _JPA implementation_
- [Jackson](https://github.com/FasterXML/jackson) _serialization and deserialization JSON<=>POJO_
- [JUnit](http://junit.org/junit4) _test framework_
- [Log4j](http://logging.apache.org/log4j/2.x) _logging framework_
- [MariaDB](https://mariadb.org) _open source MySQL Java client_
- [Maven](https://maven.apache.org) with [Surefire](http://maven.apache.org/components/surefire/maven-surefire-plugin/) plugin _build and dependency management_
- [Spring](https://projects.spring.io/spring-framework)
  * spring-beans _dependency injection_
  * spring-data-jpa and spring-orm _lightweight JPA implementation with pagination_
  * spring-test _JUnit integration_
  * spring-webmvc _REST endpoint framework_

