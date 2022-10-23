const { Client } = require("pg");

const pgclient = new Client({
  host: process.env.POSTGRES_HOST,
  port: process.env.POSTGRES_PORT,
  user: "postgres",
  password: "postgres",
  database: "museum",
});

pgclient.connect();

const table =
  "CREATE TABLE visitor(visitorId SERIAL PRIMARY KEY, email VARCHAR(40) NOT NULL, name VARCHAR(40) NOT NULL, password VARCHAR(40), numOfPass INT)";
const text =
  "INSERT INTO student(email, name, password, numOfPass) VALUES($1, $2, $3, $4) RETURNING *";
const values = ["octocat@github.com", "Mona the Octocat", "password123", 4];

pgclient.query(table, (err, res) => {
  if (err) throw err;
});

pgclient.query(text, values, (err, res) => {
  if (err) throw err;
});

pgclient.query("SELECT * FROM visitor", (err, res) => {
  if (err) throw err;
  console.log(err, res.rows); // Print the data in visitor table
  pgclient.end();
});
