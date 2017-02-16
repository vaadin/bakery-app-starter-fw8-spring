#!/bin/bash

scp -P 5188 target/orders-1.0-SNAPSHOT.war dev@app.fi:orders.war
ssh -p 5188 dev@app.fi mv orders.war tomcat/webapps/