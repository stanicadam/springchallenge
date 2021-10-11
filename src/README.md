# CodePool challenge

API for vending machines

## To run locally

* run ./mvnw spring-boot:run -Drun.profiles=local

* if it does not run at first, then run "mvn -N io.takari:maven:wrapper" and repeat the command above

* open the url 'http://localhost:8080/swagger-ui.html#' in your browser to access swagger

* if necessary, install the Lombok plugin into your intellij to remove the IDE warnings for missing fields

* import the db dump 'dbdump.sql'. I used mysql version 5.7 even though it's a bit outdated,
due to having to connect to a legacy db often times. The dump should run fine on new versions as well. 
If necessary, running a small docker container with this mysql server version will work fine, but I've tested it
and it looks OK.


## Description

The app is built for the needs of the vetting process for CodePool io

## App structure

#### The common package: 
This package unifies all the pojo's and methods that are shared across services and controllers within the app.
Not directly returning jpa models from the db to the front end/mobile app is a good practice,
therefore we implemented our own mapper that lives within this package. It wraps our entities into 
contracts that are used for communicating data from the back end to the client.
This could have also been solved with Jackson itself as an alternative.
We also keep our web security configs here.
I've been in positions where small apps quickly grow into a big microservice based beast, 
so the common package could then easily be externalized and imported into all other microservices, if necessary.

# The security package:
This package hosts our web security configuration where we have defined our
authorization and authentication related components.
We've implemented our own login response service as well as the user details service
which should make for a good foundation for possible future changes and upgrades.
Security is based around JWT. I did not go with default spring security or other methods,
because I wanted to keep the security stateless. We only use the JWT to authenticate.
No roles or important user info is kept in the JWT. 
Authorization is done in the backend,
whereby we take the authenticated customer that sent us the JWT and pull their roles from the system.
Roles are represented by enums in our system for now, but this would not be the ideal solution in a real project.


#### The config package:
This package contains all configuration classes. In our case it only hosts the JPA configuration.
It could also be a place to host thread pool configurations, cache configs etc.
I've added the Hikari pool config here as well. This might be an overkill for our system,
but it's a good practice to have it as it allows for better performance and utilization of
db and its connections. If in the future our project grows and needs a db connection pool,
it would be more difficult implementing it then, than right at the beginning.
It's usually a good idea to plan for these scenarios.

#### The controller package:
This package contains the controllers that our front end/mobile app connects to.
It creates an interface for all the CRUD operations our system allows upon the entities.

#### The dao package:
This package unifies the models and the repositories of our db entities.
We used spring data jpa here for easy access and object manipulation.
All models extend the BaseModel where we define our audit columns.


#### The service package:
The contents of this package are injected into our controllers.

#### The tests:
There are 20 unit tests covering most of the core CRUD functionalities of our system.
For each of the functionalities listed in the specs, 
We have tested both the happy path and the not-so-happy path :).
For each functionality we tested a lot of possible error responses,
utilizing our own exception classes.
We could write additional tests to cover every little case possible,
but for time's sake we will work with what we have.
For testing the functionalities that require the user to be logged in,
we added our own TestConfiguration where we defined the user we are simulating being logged in with.
The unit test code could be refactored to look prettier if I had more time available.


## Developer notes:
I had fun building this project, which can be observed from 
the possible overkills I've implemented (mainly the hikari pool, the custom mapper, the coin change problem etc).
As mentioned before, some shortcuts were taken for time's sake.

If this were a real project, and I had more time I would have probably doubled up on the testing 
and done some refactoring there to make it look better.
Additionally we would definitely be looking into better security features.


Some choices made during development could have been different, for example:
- when deleting users/products do we really delete them or just set them as inactive ? deleting from db is sometimes a bad idea.
- specs said that update and delete on products should only be made by users who are sellers of the same product,
it does not specifically say that update and delete on user should also be limited to owners of these entities.


I have plenty of ideas on where to take the project from here, if it were a real one, and if we
were preparing for a production deployment, but let's leave that for discussion upon the review.
Thanks for the time and the fun challenge.





