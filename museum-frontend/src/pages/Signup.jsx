import React, { useEffect, useState } from "react";
import { Typography, TextField, Button, Box, Paper } from "@mui/material";
import axios from "axios";
import "./Login.css";
import { LoginContext } from "../Contexts/LoginContext";
import { useNavigate } from "react-router-dom";

export function Signup() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);
  const { setLoggedIn, setUserRole, setUserName, setUserEmail, setUserId } =
    React.useContext(LoginContext);

  const navigate = useNavigate();

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
          setUserRole(response.data.role);
          setUserName(response.data.name);
          setUserEmail(response.data.email);
          setUserId(response.data.id);
          setLoggedIn(true);
          localStorage.setItem("userName", response.data.name);
          localStorage.setItem("userRole", response.data.role);
          localStorage.setItem("loggedIn", true);
          localStorage.setItem("userEmail", response.data.email);
          localStorage.setItem("userId", response.data.id);
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
                  isFormInvalid && errorMessage.includes("email")
                    ? errorMessage
                    : ""
                }
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
                  isFormInvalid && errorMessage.includes("Password")
                    ? errorMessage
                    : ""
                }
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
