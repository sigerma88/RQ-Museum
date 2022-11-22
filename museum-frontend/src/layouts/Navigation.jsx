import React, { useState } from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Button from "@mui/material/Button";
import axios from "axios";

const pages = ["Visit", "Exhibitions", "Collections"];

export function Navigation() {
  const [status, setStatus] = useState(localStorage.getItem("status"));

  const handleLogout = () => {
    setStatus("loggedOut");
    localStorage.setItem("status", "loggedOut");
    axios
      .post("/api/auth/logout")
      .then(function (response) {
        console.log(response);
        if (response.status === 200) {
          window.location.href = "/";
        }
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  };

  const notSignedInButton = () => {
    return (
      <>
        <Box>
          <Button style={{ color: "black" }} href="/login">
            Login
          </Button>
          <Button style={{ color: "black" }} href="/signup">
            Sign up
          </Button>
        </Box>
      </>
    );
  };

  const signedInButton = () => {
    return (
      <>
        <Box>
          <Button
            style={{ color: "black" }}
            href="/logout"
            onClick={handleLogout}
          >
            Logout
          </Button>
        </Box>
      </>
    );
  };

  console.log(localStorage.getItem("status"));

  return (
    <AppBar position="static" style={{ background: "#fff" }}>
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          <Typography
            variant="h6"
            noWrap
            component="a"
            href="/"
            sx={{
              mr: 2,
              display: { xs: "none", md: "flex" },
              fontFamily: "system-ui, sans-serif",
              fontWeight: 700,
              letterSpacing: ".3rem",
              color: "#000",
              textDecoration: "none",
            }}
          >
            Museum
          </Typography>

          <Box sx={{ flexGrow: 1, display: { xs: "none", md: "flex" } }}>
            {pages.map((page) => (
              <Button
                key={page}
                href={`/${page.toLowerCase()}`}
                sx={{ my: 2, color: "black", display: "block" }}
              >
                {page}
              </Button>
            ))}
          </Box>
          {status === "loggedIn" ? signedInButton() : notSignedInButton()}
        </Toolbar>
      </Container>
    </AppBar>
  );
}
