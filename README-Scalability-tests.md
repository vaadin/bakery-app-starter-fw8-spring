Orders' scalability tests
-------------------------

Scalability tests of Orders app template are recorded with Gatling tool (gatling.io). You can run them from command line with Maven by following the Quick Start Guide below. 

Quick Start Guide
-----------------

1. Make sure Orders is running in the load test mode by setting load test mode true in the application.properties:
	```orders.loadtestmode.enabled=true```
	
2. Configure wished amout of concurrent users with suitable ramp up time in end of the *.scala files, e.g.: 
	```setUp(scn.inject( rampUsers(50) over (60 seconds)) ).protocols(httpProtocol)```

3. Configure correct baseUrl in the beginning of the *.scala scripts, e.g.:
	```val baseUrl = "http://localhost:8080"```

4. Start the server

5. Start a test from the command line, e.g.:
	 ```mvn gatling:execute -Dgatling.simulationClass=com.vaadin.template.orders.Barista```
	 
6. Test results are stored into target folder, e.g.:
	```target/gatling/Barista-1487784042461/index.html```