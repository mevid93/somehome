#!/bin/bash

#########################################
#####  Java 17 and Maven required!  #####
#########################################

# set working directory
cd "$(dirname "$0")"

# create jar file
mvn clean
mvn package -DskipTests

# remove existing image
docker rmi mevid93/somehome:v1

# build the docker image
docker build -t mevid93/somehome:v1 .