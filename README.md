## Cloud Study

Repository used to store the practical works of the Cloud Development course by professors Fernando Trinta and Paulo Rego (MDCC / UFC) 2020.1.

### SSNetwork (AWS - IaaS)

Homework 1 was to implement an application similar to Instagram, in which the user can send photos to a small social network. The user data to be saved during registration are full name, nickname, password, password, and profile image.

The system must have the following features: create a user, change user profile (full name, password, and profile image), publish photos, like/unlike photos, view other users' profiles, search for users based on their nickname, and list photos considering a time interval.

Restrictions:
1. User information must be recorded in a relational database instance, created by the Amazon RDS service;
2. The photos must be stored using the Amazon S3 service;
3. Likes must be saved using Amazon DynamoDB or Amazon DocumentDB

Part 2: Using the application developed in Part 1, configure the LoadBalancing and Auto Scaling Services to provide elasticity.

[SSNetwork (AWS) Link](http://ssnetwork.pedroalmir.com/aws/frontend)
Update April 21, 2020: this application is offline to avoid costs on AWS.

### SSNetwork (GAE - PaaS)

Homework 2 was to implement an application similar to Instagram, in which the user can send photos to a small social network. The user data to be saved during registration are full name, nickname, password, password, and profile image.

The system must have the following features: create a user, change user profile (full name, password, and profile image), publish photos, like/unlike photos, view other users' profiles, search for users based on their nickname, and list photos considering a time interval.

Restrictions:
1. User information must be recorded in a relational database instance, created by the Google Cloud SQL;
2. The photos must be stored using the Google Cloud Storage;
3. Likes must be saved using Google Cloud Datastorage.

[SSNetwork (GAE) Link](http://ssnetwork.pedroalmir.com/appengine/frontend)
