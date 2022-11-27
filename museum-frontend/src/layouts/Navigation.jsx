import React, { useContext } from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Button from "@mui/material/Button";
import axios from "axios";
import { LoginContext } from "../Contexts/LoginContext";
import { useNavigate } from "react-router-dom";
import RQ_logo from "../assets/RQ_logo.svg";

const visitorPage = ["Visit", "Exhibitions", "Collections", "Ticket"];
const managerPage = ["Room", "Artwork", "Employee", "Schedule"];
const employeePage = ["Room", "Artwork", "Schedule"];
const generalPage = ["Home", "Ticket", "Loan"];

/**
 * Navigation bar
 * @returns Navigation bar
 * @author Kevin
 */

export function Navigation() {
  const { loggedIn, setLoggedIn } = useContext(LoginContext);
  const { userRole } = useContext(LoginContext);

  const navigate = useNavigate();

  let page = generalPage;
  if (userRole === "visitor") {
    page = visitorPage;
  } else if (userRole === "employee") {
    page = employeePage;
  } else if (userRole === "manager") {
    page = managerPage;
  } else {
    page = generalPage;
  }

  /**
   * Logs user out and redirects to home page
   * @author Kevin
   */

  const handleLogout = () => {
    axios
      .post("/api/auth/logout")
      .then(function (response) {
        if (response.status === 200) {
          setLoggedIn(false);
          localStorage.clear();
          navigate("/");
        }
      })
      .catch(function (error) {
        setLoggedIn(true);
      });
  };

  /**
   * Navigation bar when user is not logged in
   * @author Kevin
   */

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

  /**
   * Navigation bar when user is  logged in
   * @author Kevin
   */

  const signedInButton = () => {
    return (
      <>
        <Box>
          <Button href="/profile" style={{ color: "black" }}>
            Profile
          </Button>
          <Button style={{ color: "black" }} onClick={handleLogout}>
            Logout
          </Button>
        </Box>
      </>
    );
  };

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
            <img src={RQ_logo} alt="RQ logo"></img> {/*RQ Museum logo*/}
          </Typography>

          <Box sx={{ flexGrow: 1, display: { xs: "none", md: "flex" } }}>
            {generalPage.map((page) => (
              <Button
                key={page}
                href={page === "Home" ? "/" : `/${page.toLowerCase()}`}
                sx={{ my: 2, color: "black", display: "block" }}
              >
                {page}
              </Button>
            ))}
          </Box>
          {loggedIn ? signedInButton() : notSignedInButton()}
        </Toolbar>
      </Container>
    </AppBar>
  );
}
