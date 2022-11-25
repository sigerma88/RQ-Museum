import React, { useState, useContext, useEffect } from "react";
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

/**
 *  Edit profile page for visitors
 * @returns Edit profile page for visitors
 */

export function EditVisitor() {
  const [password, setPassword] = useState(null);
  const [oldPassword, setOldPassword] = useState(null);
  const [email, setEmail] = useState(null);
  const [name, setName] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);
  const { userName, userEmail, userId, userRole, setUserName, setUserEmail } =
    useContext(LoginContext);

  useEffect(() => {
    if (password != null && password.trim().length === 0) {
      setPassword(null);
    }

    if (oldPassword != null && oldPassword.trim().length === 0) {
      setOldPassword(null);
    }

    if (
      email != null &&
      (email.trim().length === 0 || email.trim() === userEmail)
    ) {
      setEmail(null);
    }

    if (
      name != null &&
      (name.trim().length === 0 || name.trim() === userName)
    ) {
      setName(null);
    }
  }, [password, oldPassword, email, name]);

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

          window.location.reload();
        }
      })
      .catch(function (error) {
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
      });
  };

  return (
    <>
      <div className="Login" style={{ marginTop: "3%" }}>
        <Typography style={{ fontSize: "36px" }} gutterBottom>
          My Profile
        </Typography>

        <div
          style={{
            display: "flex",
            justifyContent: "space-evenly",
            alignContent: "center",
            marginTop: "3%",
          }}
        >
          <Paper
            elevation={3}
            style={{
              width: "60%",
              padding: "50px 50px",
            }}
          >
            <Avatar
              sx={{
                bgcolor: deepOrange[500],
                width: 80,
                height: 80,
                margin: "auto",
              }}
            >
              {userName.charAt(0)}
            </Avatar>
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
                  defaultValue={userName}
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
                  defaultValue={userEmail}
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

/**
 *  Edit profile page for staff members
 * @returns Edit profile page for staff members
 */

function StaffMember() {
  const [password, setPassword] = useState(null);
  const [oldPassword, setOldPassword] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);
  const { userName, userEmail, userId, userRole } = useContext(LoginContext);

  useEffect(() => {
    if (password != null && password.trim().length === 0) {
      setPassword(null);
    }

    if (oldPassword != null && oldPassword.trim().length === 0) {
      setOldPassword(null);
    }
  }, [password, oldPassword]);

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
            window.location.reload();
          }
        })
        .catch(function (error) {
          setErrorMessage(error.response.data);
          setIsFormInvalid(true);
        });
    } else if (userRole === "manager") {
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
            window.location.reload();
          }
        })
        .catch(function (error) {
          setErrorMessage(error.response.data);
          setIsFormInvalid(true);
        });
    }
  };

  return (
    <>
      <div className="Login" style={{ marginTop: "3%" }}>
        <Typography style={{ fontSize: "36px" }} gutterBottom>
          My Profile
        </Typography>

        <div
          style={{
            display: "flex",
            justifyContent: "space-evenly",
            alignContent: "center",
            marginTop: "3%",
          }}
        >
          <Paper
            elevation={3}
            style={{
              width: "60%",
              padding: "50px 50px",
            }}
          >
            <Avatar
              sx={{
                bgcolor: deepOrange[500],
                width: 80,
                height: 80,
                margin: "auto",
              }}
            >
              {userName.charAt(0)}
            </Avatar>
            <Stack mt={2} spacing={1}>
              <Typography variant="h5" style={{ fontWeight: "bold" }}>
                {userName}
              </Typography>
              <Typography style={{ fontSize: "15px" }}>{userEmail}</Typography>
            </Stack>

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
