import React, { useState } from "react";
import {
  Typography,
  TextField,
  Button,
  Box,
  Paper,
  Avatar,
} from "@mui/material";
import axios from "axios";
import "./Login.css";
import { LoginContext } from "../Contexts/LoginContext";
import { useNavigate } from "react-router-dom";
import LoginIcon from "@mui/icons-material/Login";

/**
 * Sign in page
 * @returns  Sign in page
 * @author Kevin
 */

export function Signup() {
  // State variables for the form
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);
  const { setLoggedIn, setUserRole, setUserName, setUserEmail, setUserId } =
    React.useContext(LoginContext);

  const navigate = useNavigate();

  /**
   * Registers a new user when the form is submitted
   * @author Kevin
   */

  const handleSubmit = async (event) => {
    event.preventDefault();

    axios
      .post("/api/profile/visitor/register", {
        name: name,
        email: email,
        password: password,
      })
      .then(function (response) {
        if (response.status === 200) {
          // Setting information about the user once logged in to be used in the app
          setUserRole(response.data.role);
          setUserName(response.data.name);
          setUserEmail(response.data.email);
          setUserId(response.data.museumUserId);
          setLoggedIn(true);
          localStorage.setItem("userName", response.data.name);
          localStorage.setItem("userRole", response.data.role);
          localStorage.setItem("loggedIn", "true");
          localStorage.setItem("userEmail", response.data.email);
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
      <div className="Signup" style={{ marginTop: "10%" }}>
        <Paper
          elevation={3}
          style={{
            width: "50%",
            margin: "auto",
            padding: "50px",
            align: "space-between",
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
            <LoginIcon />
          </Avatar>
          <Typography variant="h4" component="h1">
            Sign up
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
                id="name"
                label="Name"
                name="name"
                autoComplete="name"
                autoFocus
                onChange={(e) => setName(e.target.value)}
                className="login-field"
              />
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
                  errorMessage.includes("Password") &&
                  errorMessage
                }
                error={isFormInvalid && errorMessage.includes("Password")}
              />
            </Box>
            <Button
              type="submit"
              variant="contained"
              sx={{ mt: 3, mb: 2, width: "40%" }}
            >
              Sign up
            </Button>
          </form>
        </Paper>
      </div>
    </>
  );
}
