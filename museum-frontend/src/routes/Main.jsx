import React, { useState } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { Typography } from "@mui/material";
import { LoginContext } from "../Contexts/LoginContext";
import Home from "../pages/Home";
import { Navigation } from "../layouts/Navigation";
import { Login } from "../pages/Login";
import { Signup } from "../pages/Signup";
import { Profile } from "../pages/Profile";
import ArtworkBrowsing from "../pages/ArtworkBrowsing";
import ArtworkDetails from "../pages/ArtworkDetails";
import EmployeeCreation from "../pages/EmployeeCreation";
import TicketViewing from "../pages/TicketViewing";
import { Schedule } from "../pages/Schedule";
import { ViewEmployees } from "../pages/ViewEmployees";
import { Rooms } from "../pages/Rooms";

/**
 * Function that routes the user to the correct page depending on the url
 * @author Kevin
 */
export function Main() {
  //Setting necessary information to be used in other components
  const [loggedIn, setLoggedIn] = useState(
    localStorage.getItem("loggedIn") ?? false
  );
  const [userRole, setUserRole] = useState(
    localStorage.getItem("userRole") ?? ""
  );

  const [userName, setUserName] = useState(
    localStorage.getItem("userName") ?? ""
  );

  const [userEmail, setUserEmail] = useState(
    localStorage.getItem("userEmail") ?? ""
  );

  const [userId, setUserId] = useState(localStorage.getItem("userId") ?? "");

  return (
    <LoginContext.Provider
      value={{
        loggedIn,
        setLoggedIn,
        userRole,
        setUserRole,
        userName,
        setUserName,
        userEmail,
        setUserEmail,
        userId,
        setUserId,
      }}
    >
      <BrowserRouter>
        <Navigation />
        <Routes>
          <Route path="/" index element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route
            path="/profile"
            element={loggedIn ? <Profile /> : <Navigate to="/login" />}
          />
          <Route
            path="/employee/create"
            element={
              loggedIn && userRole === "manager" ? (
                <EmployeeCreation />
              ) : (
                <Typography
                  variant="h3"
                  style={{ margin: "auto", padding: "auto" }}
                >
                  You are not authorized to view this page
                </Typography>
              )
            }
          />
          <Route path="/browse/room/:roomId" element={<ArtworkBrowsing />} />
          <Route path="/rooms" element={<Rooms />} />
          <Route
            path="/browse/artwork/:artworkId"
            element={<ArtworkDetails />}
          />
          <Route
            path="/ticket"
            element={loggedIn ? <TicketViewing /> : <Navigate to="/login" />}
          />
          <Route path="/schedule/:id" element={<Schedule />} />
          <Route path="/employees" element={<ViewEmployees />} />s
        </Routes>
      </BrowserRouter>
    </LoginContext.Provider>
  );
}
