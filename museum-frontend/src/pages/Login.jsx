import React, { useState, useContext } from "react";
import axios from "axios";
import {
  Typography,
  TextField,
  Button,
  Box,
  Paper,
  Avatar,
} from "@mui/material";
import "./Login.css";
import { LoginContext } from "../Contexts/LoginContext";
import { useNavigate } from "react-router-dom";

/**
 * Login page
 * @returns Login page
 * @author Kevin
 */

export function Login() {
  // State variables for the form
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);
  const { setLoggedIn, setUserEmail, setUserRole, setUserName, setUserId } =
    useContext(LoginContext);

  const navigate = useNavigate();

  /**
   * Logs in a user when the form is submitted
   * @author Kevin
   **/

  const handleSubmit = async (event) => {
    event.preventDefault();
    axios
      .post("/api/auth/login", {
        email: email,
        password: password,
      })
      .then(function (response) {
        if (response.status === 200) {
          setUserRole(response.data.role);
          setUserName(response.data.name);
          setUserEmail(response.data.email);
          setUserId(response.data.id);
          setLoggedIn(true);
          localStorage.setItem("userName", response.data.name);
          localStorage.setItem("userEmail", response.data.email);
          localStorage.setItem("userRole", response.data.role);
          localStorage.setItem("loggedIn", true);
          localStorage.setItem("userId", response.data.museumUserId);

          navigate("/");
        }
      })
      .catch(function (error) {
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
        setLoggedIn(false);
      });
  };
  return (
    <>
      <div className="Login" style={{ marginTop: "10%" }}>
        <Paper
          elevation={3}
          style={{ width: "50%", margin: "auto", padding: "50px" }}
        >
          <Avatar
            sx={{
              margin: "auto",
              marginBottom: "20px",
              width: "50px",
              height: "50px",
            }}
          ></Avatar>
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
                  isFormInvalid &&
                  errorMessage.includes("email") &&
                  errorMessage
                }
                error={isFormInvalid}
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
                helperText={
                  isFormInvalid &&
                  errorMessage.includes("password") &&
                  errorMessage
                }
                error={isFormInvalid}
                className="login-field"
              />
            </Box>
            <a style={{ margin: "10px" }} href="/signup">
              Need an account? Click here
            </a>
            <p style={{ color: "red" }}>{errorMessage}</p>
            <Button
              type="submit"
              variant="contained"
              sx={{ mt: 3, mb: 2, width: "40%" }}
            >
              Log in
            </Button>
          </form>
        </Paper>
      </div>
    </>
  );
}
