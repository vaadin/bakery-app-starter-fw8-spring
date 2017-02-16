#!/bin/bash

scp -o StrictHostKeyChecking=no -P 5188 target/orders-1.0-SNAPSHOT.war dev@app.fi:orders.war
ssh -o StrictHostKeyChecking=no -p 5188 dev@app.fi mv orders.war tomcat/webapps/ROOT.war

