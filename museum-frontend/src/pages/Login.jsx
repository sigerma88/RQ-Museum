import React, { useState } from "react";
import axios from "axios";
import { Typography, TextField, Button, Box } from "@mui/material";
import "./Login.css";

export function Login() {
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();

    axios
      .post("/api/auth/login", {
        email: email,
        password: password,
      })
      .then(function (response) {
        console.log(response);
        if (response.status === 200) {
          localStorage.setItem("status", "loggedIn");
          window.location.href = "/";
        }
      })
      .catch(function (error) {
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
        console.log(error.response.data);
      });
  };
  return (
    <>
      <div className="Login" style={{ marginTop: "10%" }}>
        <Typography variant="h4" component="h1">
          Log in
        </Typography>
        <form onSubmit={handleSubmit}>
          <Box
            sx={{
              marginTop: 3,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <TextField
              margin="normal"
              required
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              onChange={(e) => setEmail(e.target.value)}
              className="login-field"
              helperText={
                isFormInvalid && errorMessage.includes("email") && errorMessage
              }
              error={isFormInvalid && errorMessage.includes("email")}
            />
            <TextField
              margin="normal"
              required
              id="password"
              label="Password"
              type={"password"}
              name="email"
              autoComplete="email"
              autoFocus
              onChange={(e) => setPassword(e.target.value)}
              className="login-field"
              helperText={
                isFormInvalid &&
                errorMessage.includes("password") &&
                errorMessage
              }
              error={isFormInvalid && errorMessage.includes("password")}
            />
          </Box>
          <p style={{ color: "red" }}>{errorMessage}</p>
          <Button
            type="submit"
            variant="contained"
            sx={{ mt: 3, mb: 2, width: "40%" }}
          >
            Sign up
          </Button>
        </form>
      </div>
    </>
  );
}
