import React, { useState } from "react";
import axios from "axios";
import {
  Typography,
  TextField,
  Button,
  Paper,
  Stack,
  Box,
  Alert,
  AlertTitle,
  List,
  ListItem,
} from "@mui/material";
import Avatar from "@mui/material/Avatar";
import PersonAddAltRoundedIcon from "@mui/icons-material/PersonAddAltRounded";
import "../../Profile/Login/Login.css";

/**
 * Page where manager creates employees
 * @returns Employee creation page
 * @author Kevin
 */

export default function EmployeeCreation() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  function handleSubmit(event) {
    event.preventDefault();

    axios
      .post("/api/profile/employee/register", {
        name: firstName.trim() + " " + lastName.trim(),
      })
      .then(function (response) {
        if (response.status === 200) {
          setName(response.data.name);
          setEmail(response.data.email);
          setPassword(response.data.password);
          setErrorMessage("");
          setIsFormInvalid(false);
        }
      })
      .catch(function (error) {
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
      });
  }

  return (
    <div style={{ marginTop: "3%" }}>
      <Typography style={{ fontSize: "36px" }} gutterBottom>
        Create Employee
      </Typography>
      <Paper
        elevation={3}
        style={{
          width: "60%",
          padding: "50px 50px",
          margin: "60px auto",
          height: "80%",
        }}
      >
        <Avatar
          sx={{
            margin: "auto",
            marginBottom: "20px",
            width: "50px",
            height: "50px",
            bgcolor: "black",
          }}
        >
          <PersonAddAltRoundedIcon />
        </Avatar>
        <form
          onSubmit={handleSubmit}
          style={{ margin: "auto", padding: "auto" }}
        >
          <Stack spacing={4}>
            <TextField
              id="outlined-basic"
              label="First Name"
              variant="outlined"
              onChange={(e) => setFirstName(e.target.value)}
              required
              style={{ width: "40%", margin: " 10px auto" }}
              helperText={
                isFormInvalid && errorMessage.includes("name") && errorMessage
              }
              error={isFormInvalid && errorMessage.includes("name")}
            />
            <TextField
              id="outlined-basic"
              label="Last Name"
              variant="outlined"
              onChange={(e) => setLastName(e.target.value)}
              style={{ width: "40%", margin: "10px auto" }}
              required
              helperText={
                isFormInvalid && errorMessage.includes("name") && errorMessage
              }
              error={isFormInvalid && errorMessage.includes("name")}
            />

            <Button
              variant="contained"
              type="submit"
              style={{ width: "40%", margin: " 30px auto" }}
            >
              Create Employee
            </Button>
          </Stack>
        </form>
      </Paper>
      <Box>
        {name && email && password ? (
          <div>
            <Alert
              severity="success"
              style={{
                width: "30%",
                margin: "auto",
                padding: "auto",
              }}
            >
              <AlertTitle>Account successfully created</AlertTitle>
              <Box style={{ justifyContent: "center" }}>
                <List>
                  <ListItem>
                    <Typography>
                      <span style={{ fontWeight: "bold" }}>Name: </span> {name}
                    </Typography>
                  </ListItem>
                  <ListItem>
                    <Typography>
                      <span style={{ fontWeight: "bold" }}>Email: </span>{" "}
                      {email}
                    </Typography>
                  </ListItem>
                  <ListItem>
                    <Typography>
                      <span style={{ fontWeight: "bold" }}>Password: </span>
                      {password}
                    </Typography>
                  </ListItem>
                </List>
              </Box>
              <Button
                style={{ float: "right" }}
                variant="contained"
                onClick={() =>
                  navigator.clipboard.writeText(
                    `Name: ${name}, Email: ${email}, Password: ${password}`
                  )
                }
              >
                Copy
              </Button>
            </Alert>
          </div>
        ) : (
          ""
        )}
      </Box>
    </div>
  );
}
