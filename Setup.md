# Setup for the application

## Prerequisites

### PostgreSQL and Java

- Download PostgreSQL (version 15 is better as it is the newest version as of now)
  - In the application, the current default username and password to access the local database are both `postgres`
  - You can set the same username and password when you download PostgreSQL if you do not want to change them in the code (see section _Build and start the back-end application_ for how to change it to your username and password)
- Download Java (the project is currently using Java version 17)

**For Mac users:**
It's possible to install postgresql with [homebrew](https://formulae.brew.sh/formula/postgresql@14).
Run the following command on the terminal `brew install postgresql`

### Node.js, npm and React

- Download Node.js
- Open the terminal and cd to the frontend project folder `cd museum-frontend`
- Run `npm i` to install all the dependencies

## Start PostgreSQL Server Locally

### Windows

1. Open the command line
2. Go to the PostgreSQL folder
   1. Usual path should be `C:\Program Files\PostgreSQL\15\bin`
   2. Command: `cd "C:\Program Files\PostgreSQL\15\bin"`
3. Start the PostgreSQL server
   1. Command: `pg_ctl -D "C:\Program Files\PostgreSQL\15\data" start`
   2. Other possible commands instead of `start` are `stop` or `restart`

### Mac

1. Open terminal
2. Run the command `brew services start postgresql`

## Set up the database (Usually only need to do this once the first time)

**Note: You should have started the PostgreSQL server before this, see previous section for how.**

### Windows

1. Open the command line
2. Go to the PostgreSQL folder
   1. Usual path should be `C:\Program Files\PostgreSQL\15\bin`
   2. Command: `cd "C:\Program Files\PostgreSQL\15\bin"`
3. Accesss the PostgreSQL server and create a `museum` database (if it is the first time on this computer)
   1. Command: `psql -U postgres`
   2. Enter your password
   3. Command: `CREATE DATABASE museum;`
4. Exit the PostgreSQL server
   1. Command: `\q`
5. Import the database dump
   1. Command: `psql -U postgres museum < FULL PATH TO THE SQL FILE`
      - The relative path to the SQL file in question is `sql_dumps\museum_base_dump`

### MacOS

1. Open Terminal
2. Asses the PostgreSQL server
   1. Command: `psql postgres`     
3. Create a `museum` database (if it is the first time on this computer)
   1. Command: `CREATE DATABASE museum;`
4. Switch to `museum` database
   1. Command: `\c museum`
5. Import the database dump
   1. Command: `\i FULL PATH TO THE SQL FILE`
      - The relative path to the SQL file in question is `sql_dumps\museum_base_dump`

## Build and start the back-end application locally

_Note:_

- For the application to access the local database, the username and password need to be validated
  - Username and password need to be the same between your local PostgreSQL server and the properties of the application
- For the application, they are set in `Museum-Backend/src/main/resources/application.properties`

### First step

1. Open the file `application.properties` and change the username and password to your local PostgreSQL server username and password

### Second step

From the project folder, run the command: `gradle build` or `./gradlew build`

### Third step options

1. From the IDE, run the application from the file `Museum-Backend/src/main/java/ca/mcgill/ecse321/museum/MuseumApplication.java` by clicking on the run or play button beside the code.
2. From the project folder, run the command: `gradle bootRun` or `gradle run` or `./gradlew bootRun` or `./gradlew run`
3. The application should be running on `localhost:8080`

### To run Front End

1. Run the Backend
2. cd into museum-frontend folder `cd museum-frontend`
3. Write the `npm start` command in the terminal to start the frontend
4. The application should be running on `localhost:3000`
