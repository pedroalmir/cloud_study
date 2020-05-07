## Cloud Study

Repository used to store the practical works of the Cloud Development course by professors Fernando Trinta and Paulo Rego (MDCC / UFC) 2020.1.

* * *

### 1. SSNetwork (AWS - IaaS)

Homework 1 was to implement an application similar to Instagram, in which the user can send photos to a small social network. The user data to be saved during registration are full name, nickname, password, password, and profile image.

The system must have the following features: create a user, change user profile (full name, password, and profile image), publish photos, like/unlike photos, view other users' profiles, search for users based on their nickname, and list photos considering a time interval.

Restrictions:
1. User information must be recorded in a relational database instance, created by the Amazon RDS service.
2. The photos must be stored using the Amazon S3 service.
3. Likes must be saved using Amazon DynamoDB or Amazon DocumentDB.

Part 2: Using the application developed in Part 1, configure the LoadBalancing and Auto Scaling Services to provide elasticity.

[SSNetwork (AWS) Link](http://ssnetwork.pedroalmir.com/aws/frontend)

**Update April 21, 2020**: this application is offline to avoid costs on AWS.

* * *

### 2. SSNetwork (GAE - PaaS)

Homework 2 was to implement an application similar to Instagram, in which the user can send photos to a small social network. The user data to be saved during registration are full name, nickname, password, password, and profile image.

The system must have the following features: create a user, change user profile (full name, password, and profile image), publish photos, like/unlike photos, view other users' profiles, search for users based on their nickname, and list photos considering a time interval.

Restrictions:
1. User information must be recorded in a relational database instance, created by the Google Cloud SQL.
2. The photos must be stored using the Google Cloud Storage.
3. Likes must be saved using Google Cloud Datastorage.

[SSNetwork (GAE) Link](http://ssnetwork.pedroalmir.com/appengine/frontend)

**Update May 7, 2020**: this application is offline to avoid costs on App Engine.

* * *

### 3. SSNetwork (AWS - Docker)

For the third homework, it was required to deploy the application developed in assignment 1 or 2 using docker and docker-compose.

Restrictions:
1. User information must be recorded in a relational database service created by docker.
2. The photos must be stored using the Amazon S3 service.
3. Likes must be saved using Amazon DynamoDB or Amazon DocumentDB.
4. Use dockerfile to create the application's backend and frontend image.
5. Use the MySQL docker image to execute the relational database.
6. Use docker-compose to configure services to run and start: DB, backend and frontend.

[SSNetwork-Docker Frontend](http://http://18.229.202.214)
[SSNetwork-Docker Backend](http://18.229.202.214:8080/ssnetwork-docker)

We created a script to automate the download of the code, generation of images and startup of the services (see the image below). 

![ssn-config-script.sh](https://raw.githubusercontent.com/pedroalmir/cloud_study/master/docker/ssn-config-script.png)

To execute this script, you need to observe some requirements (docker, docker-compose and unzip packages installed) and run two commands:

Commands:
1. `$ wget https://raw.githubusercontent.com/pedroalmir/cloud_study/master/docker/ssn-config-script.sh`
2. `$ \. ssn-config-script.sh`

The script will create the necessary folder structure, download the application code, then build the images using the docker files and, finally, start the services as defined by the docker-compose file. To stop them, use the command: `$ docker-compose down` or `$ docker-compose stop`. The image below shows the services running with the command `$ docker ps`.

![running-services.png](https://raw.githubusercontent.com/pedroalmir/cloud_study/master/docker/running-services.png)
