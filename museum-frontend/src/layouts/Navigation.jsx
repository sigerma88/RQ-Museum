import React, { useContext, useEffect, useState } from "react";
import {
  Button,
  AppBar,
  Box,
  Toolbar,
  Typography,
  Container,
  Menu,
  MenuItem,
  Link,
} from "@mui/material";

import axios from "axios";
import { LoginContext } from "../Contexts/LoginContext";
import { useNavigate } from "react-router-dom";
import RQ_logo from "../assets/RQ_logo.svg";

const generalPage = ["Ticket", "Loan"];

/**
 * Navigation bar
 * @returns Navigation bar
 * @author Kevin
 */

export function Navigation() {
  const { loggedIn, setLoggedIn } = useContext(LoginContext);
  const [rooms, setRooms] = useState([]);
  const [anchorElNav, setAnchorElNav] = useState(null);
  const [anchorElUser, setAnchorElUser] = useState(null);

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  useEffect(() => {
    axios
      .get("/api/room")
      .then((response) => {
        setRooms(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  const navigate = useNavigate();

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
            <Button
              style={{ color: "black" }}
              onClick={handleOpenUserMenu}
              sx={{ p: 0 }}
            >
              Rooms
            </Button>
            <Menu
              sx={{ mt: "45px" }}
              id="menu-appbar"
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              transformOrigin={{
                vertical: "top",
                horizontal: "right",
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
              {rooms.map((room) => (
                <Link
                  style={{ color: "black", textDecoration: "none" }}
                  href={`/browse/room/${room.roomId}`}
                  key={room.roomId}
                >
                  <MenuItem key={room.roomId}>{room.roomName}</MenuItem>
                </Link>
              ))}
              <Link
                style={{ color: "black", textDecoration: "none" }}
                href="/browse/room/all"
              >
                <MenuItem>All Rooms</MenuItem>
              </Link>
            </Menu>
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
