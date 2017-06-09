# Running the project

`mvn spring-boot:run`

Wait for the application to start

Open http://localhost:8080/ to view the application.

Default credentials are admin@vaadin.com/admin for admin access and
barista@vaadin.com/barista for normal user access.

# Running integration tests

Integration tests are implemented using TestBench. The tests take tens of minutes to run and are therefore included in a separate profile. To run the tests, execute

`mvn verify -Pit`

and make sure you have a valid TestBench license installed.

# Running scalability tests

Scalability tests can be run as follows

1. Configure the server for load testing by setting `loadtestmode.enabled=true` in `src/main/resources/application.properties`

2. Configure the number of concurrent users and a suitable ramp up time in the end of the `src/test/scala/*.scala` files, e.g.:
	```setUp(scn.inject( rampUsers(50) over (60 seconds)) ).protocols(httpProtocol)```

3. If you are not running on localhost, configure the baseUrl in the beginning of the `src/test/scala/*.scala` files, e.g.:
	```val baseUrl = "http://my.server.com"```

4. Make sure the server is running at the given URL

5. Start a test from the command line, e.g.:
	 ```mvn -Pscalability gatling:execute -Dgatling.simulationClass=com.vaadin.starter.bakery.Barista```

6. Test results are stored into target folder, e.g.:
	```target/gatling/Barista-1487784042461/index.html```

# Developing the project

The project can be imported into the IDE of your choice as a Maven project

The views are created using Vaadin Designer. To edit the views visually,
you need to install the Vaadin Designer plug-in.

In Eclipse, open Marketplace, search for "vaadin" and install Vaadin
Designer 2.x

In IntelliJ, go to "Preferences" -> "Plugins" -> "Browse Repositories",
search for "Vaadin Designer 2" and install "Vaadin Designer"
