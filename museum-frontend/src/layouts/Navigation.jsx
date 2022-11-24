import React, { useContext } from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import Avatar from "@mui/material/Avatar";
import axios from "axios";
import { LoginContext } from "../Contexts/LoginContext";
import { useNavigate } from "react-router-dom";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import RQ_logo from "../assets/RQ_logo.svg";

// const visitorPage = ["Visit", "Exhibitions", "Collections", "Ticket"];
// const managerPage = ["Room", "Artwork", "Employee", "Schedule"];
// const employeePage = ["Room", "Artwork", "Schedule"];
const generalPage = ["Home", "Ticket", "Loan"];
const settings = ["Profile", "Account", "Dashboard", "Logout"];

export function Navigation() {
  const { loggedIn, setLoggedIn } = useContext(LoginContext);
  const { userRole } = useContext(LoginContext);
  const [anchorElNav, setAnchorElNav] = React.useState(null);
  const [anchorElUser, setAnchorElUser] = React.useState(null);
  const navigate = useNavigate();

  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };
  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  let page = generalPage;
  // if (userRole === "visitor") {
  //   page = visitorPage;
  // } else if (userRole === "employee") {
  //   page = employeePage;
  // } else if (userRole === "manager") {
  //   page = managerPage;
  // } else {
  //   page = generalPage;
  // }

  const handleLogout = () => {
    axios
      .post("/api/auth/logout")
      .then(function (response) {
        if (response.status === 200) {
          setLoggedIn(false);
          sessionStorage.clear();
          navigate("/");
        }
      })
      .catch(function (error) {
        setLoggedIn(true);
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
          <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
            <Avatar alt="Remy Sharp" />
          </IconButton>
          <Menu
            sx={{ mt: "45px" }}
            id="menu-appbar"
            anchorEl={anchorElUser}
            anchorOrigin={{
              vertical: "top",
              horizontal: "right",
            }}
            keepMounted
            transformOrigin={{
              vertical: "top",
              horizontal: "right",
            }}
            onClose={handleCloseUserMenu}
            open={Boolean(anchorElUser)}
          >
            <MenuItem onClick={handleCloseUserMenu}>
              <Button style={{ color: "black" }}>Edit Profile</Button>
            </MenuItem>
            <MenuItem onClick={handleCloseUserMenu}>
              <Button style={{ color: "black" }} onClick={handleLogout}>
                Logout
              </Button>
            </MenuItem>
          </Menu>
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
            <img src={RQ_logo}></img> {/*RQ Museum logo*/}
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
