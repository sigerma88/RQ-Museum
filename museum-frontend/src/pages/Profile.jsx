import React, { useState, useContext } from "react";
import axios from "axios";
import {
  Typography,
  TextField,
  Button,
  Box,
  Paper,
  Stack,
} from "@mui/material";
import "./Login.css";
import { LoginContext } from "../Contexts/LoginContext";
import { useNavigate } from "react-router-dom";
import Avatar from "@mui/material/Avatar";
import { deepOrange } from "@mui/material/colors";

export function EditVisitor() {
  const [password, setPassword] = useState(null);
  const [oldPassword, setOldPassword] = useState(null);
  const [email, setEmail] = useState(null);
  const [name, setName] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);
  const { userName, userEmail, userId, setUserName, setUserEmail } =
    useContext(LoginContext);

  const navigate = useNavigate();
  const handleSubmit = async (event) => {
    event.preventDefault();

    axios
      .put(`/api/profile/visitor/edit/${userId}`, {
        name: name,
        email: email,
        oldPassword: oldPassword,
        newPassword: password,
      })
      .then(function (response) {
        if (response.status === 200) {
          if (name !== null) {
            setUserName(name);
            localStorage.setItem("userName", name);
          }

          if (email !== null) {
            setUserEmail(email);
            localStorage.setItem("userEmail", email);
          }

          navigate("/profile");
        }
      })
      .catch(function (error) {
        console.log(error.response.data);
        setErrorMessage(error.response.data);
        setEmail(null);
        setName(null);
        setPassword(null);
        setOldPassword(null);

        setIsFormInvalid(true);
      });
  };

  return (
    <>
      <div className="Login" style={{ marginTop: "10%" }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-evenly",
            alignContent: "center",
          }}
        >
          <Paper elevation={3} style={{ padding: "150px" }}>
            <Stack style={{ marginTop: 20 }} spacing={2}>
              <Avatar
                sx={{
                  bgcolor: deepOrange[500],
                  width: 80,
                  height: 80,
                  marginLeft: 2.5,
                }}
              >
                {userName.charAt(0)}
              </Avatar>
              <h4>{userName}</h4>
              <h4>{userEmail}</h4>
            </Stack>
          </Paper>
          <Paper
            elevation={3}
            style={{
              width: "50%",
              padding: "20px 20px",
            }}
          >
            <form
              style={{ width: "100%", alignContent: "flex-end" }}
              onSubmit={handleSubmit}
            >
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
                  id="email"
                  label="Name"
                  name="name"
                  autoComplete="name"
                  autoFocus
                  onChange={(e) => setName(e.target.value)}
                  className="login-field"
                  helperText={
                    isFormInvalid &&
                    errorMessage.includes("name") &&
                    errorMessage
                  }
                  error={isFormInvalid && errorMessage.includes("name")}
                />
                <TextField
                  margin="normal"
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
                  id="oldPassword"
                  label="Old Password"
                  type={"password"}
                  name="email"
                  autoComplete="oldPassword"
                  autoFocus
                  onChange={(e) => setOldPassword(e.target.value)}
                  className="login-field"
                  helperText={
                    isFormInvalid &&
                    errorMessage.includes("Old password") &&
                    errorMessage
                  }
                  error={isFormInvalid && errorMessage.includes("Old password")}
                />
                <TextField
                  margin="normal"
                  id="newPassword"
                  label="New Password"
                  type={"password"}
                  name="email"
                  autoComplete="newPassword"
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
                Edit Profile
              </Button>
            </form>
          </Paper>
        </div>
      </div>
    </>
  );
}

function StaffMember() {
  const [password, setPassword] = useState(null);
  const [oldPassword, setOldPassword] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);
  const { userName, userEmail, userId, userRole } = useContext(LoginContext);

  const navigate = useNavigate();
  const handleSubmit = async (event) => {
    event.preventDefault();

    if (userRole === "employee") {
      axios
        .put(`/api/profile/employee/edit/${userId}`, {
          oldPassword: oldPassword,
          newPassword: password,
        })
        .then(function (response) {
          if (response.status === 200) {
            setPassword(null);
            setOldPassword(null);
            setErrorMessage("");
          }
        })
        .catch(function (error) {
          console.log(error.response.data);
          setErrorMessage(error.response.data);
          setIsFormInvalid(true);
          setPassword(null);
          setOldPassword(null);
        });
    }

    if (userRole === "manager") {
      axios
        .put(`/api/profile/manager/edit/${userId}`, {
          oldPassword: oldPassword,
          newPassword: password,
        })
        .then(function (response) {
          if (response.status === 200) {
            setPassword(null);
            setOldPassword(null);
            setErrorMessage("");
          }
        })
        .catch(function (error) {
          console.log(error.response.data);
          setErrorMessage(error.response.data);
          setIsFormInvalid(true);
          setPassword(null);
          setOldPassword(null);
        });
    }
  };

  return (
    <>
      <div className="Login" style={{ marginTop: "10%" }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-evenly",
            alignContent: "center",
          }}
        >
          <Paper elevation={3} style={{ padding: "150px" }}>
            <Stack style={{ marginTop: 20 }} spacing={2}>
              <Avatar
                sx={{
                  bgcolor: deepOrange[500],
                  width: 80,
                  height: 80,
                  marginLeft: 2.5,
                }}
              >
                {userName.charAt(0)}
              </Avatar>
              <h4>{userName}</h4>
              <h4>{userEmail}</h4>
            </Stack>
          </Paper>
          <Paper
            elevation={3}
            style={{
              width: "50%",
              padding: "20px 20px",
            }}
          >
            <form
              style={{ width: "100%", alignContent: "flex-end" }}
              onSubmit={handleSubmit}
            >
              <Box
                sx={{
                  marginTop: 11,
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                }}
              >
                <TextField
                  margin="normal"
                  id="oldPassword"
                  label="Old Password"
                  type={"password"}
                  name="email"
                  autoComplete="oldPassword"
                  autoFocus
                  onChange={(e) => setOldPassword(e.target.value)}
                  className="login-field"
                  helperText={
                    isFormInvalid &&
                    errorMessage.includes("Old password") &&
                    errorMessage
                  }
                  error={isFormInvalid && errorMessage.includes("Old password")}
                />
                <TextField
                  margin="normal"
                  id="newPassword"
                  label="New Password"
                  type={"password"}
                  name="email"
                  autoComplete="newPassword"
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
                Edit Profile
              </Button>
            </form>
          </Paper>
        </div>
      </div>
    </>
  );
}

export function Profile() {
  const { userRole } = useContext(LoginContext);

  if (userRole === "visitor") {
    return <EditVisitor />;
  } else if (userRole === "employee" || userRole === "manager") {
    return <StaffMember />;
  }
}
