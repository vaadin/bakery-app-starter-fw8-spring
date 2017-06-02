Scalability tests
-------------------------

The scalability tests are recorded with Gatling tool (gatling.io). You can run them from command line with Maven by following the Quick Start Guide below.

Quick Start Guide
-----------------

1. Make sure the server is running in the load test mode by setting load test mode true in the application.properties:
	```loadtestmode.enabled=true```

2. Configure the wished number of concurrent users with a suitable ramp up time in the end of the *.scala files, e.g.:
	```setUp(scn.inject( rampUsers(50) over (60 seconds)) ).protocols(httpProtocol)```

3. Configure correct baseUrl in the beginning of the *.scala scripts, e.g.:
	```val baseUrl = "http://localhost:8080"```

4. Start the server

5. Start a test from the command line, e.g.:
	 ```mvn gatling:execute -Dgatling.simulationClass=com.vaadin.starter.bakery.Barista```

6. Test results are stored into target folder, e.g.:
	```target/gatling/Barista-1487784042461/index.html```
