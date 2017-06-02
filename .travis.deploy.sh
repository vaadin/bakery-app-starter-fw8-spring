#!/bin/bash

scp -o StrictHostKeyChecking=no -P 5188 target/bakery-app-starter-fw8-spring-*.war dev@app.fi:orders.war
ssh -o StrictHostKeyChecking=no -p 5188 dev@app.fi mv orders.war tomcat/webapps/ROOT.war

