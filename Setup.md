# Prerequisites

## PostgreSQL and Java

- Download PostgreSQL (version 15 is better as it is the newest version as of now)
  - In the application, the current default username and password to access the local database are both `postgres`
  - You can set the same username and password when you download PostgreSQL if you do not want to change them in the code (see section _Build and start the back-end application_ for how to change it to your username and password)
- Download Java (the project is currently using Java version 17)

**For Mac users:**
It's possible to install postgresql with [homebrew](https://formulae.brew.sh/formula/postgresql@14).
Run the following command on the terminal `brew install postgresql@14`


# Start PostgreSQL Server Locally

## Windows

1. Open the command line
2. Go to the PostgreSQL folder
   1. Usual path should be `C:\Program Files\PostgreSQL\15\bin`
   2. Command: `cd "C:\Program Files\PostgreSQL\15\bin"`
3. Start the PostgreSQL server
   1. Command: `pg_ctl -D "C:\Program Files\PostgreSQL\15\data" start`
   2. Other possible commands instead of `start` are `stop` or `restart`
4. If you have the PgAdmin application downloaded, you can access and view the database there after starting the PostgreSQL server

## Mac

1. Open terminal
2. Run the command `brew services start postgresql`

# Build and start the back-end application

_Note:_

- For the application to access the local database, the username and password need to be validated
  - Username and password need to be the same between your local PostgreSQL server and the properties of the application
- For the application, they are set in `Museum-Backend/src/main/resources/application.properties`
  - You can change `spring.datasource.username` and `spring.datasource.password` to your own

## Different options

1. From the IDE, run the application from the file `Museum-Backend/src/main/java/ca/mcgill/ecse321/museum/MuseumApplication.java` by clicking on the run or play button beside the code.
2. From the project folder, run the command: `gradle bootRun`
