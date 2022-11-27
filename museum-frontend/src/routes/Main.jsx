import React, { useState } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Home from "../pages/Home";
import { Navigation } from "../layouts/Navigation";
import { Login } from "../pages/Login";
import { Signup } from "../pages/Signup";
import { Profile } from "../pages/Profile";
import { LoginContext } from "../Contexts/LoginContext";
import ArtworkBrowsing from "../pages/ArtworkBrowsing";
import ArtworkDetails from "../pages/ArtworkDetails";
import TicketViewing from "../pages/TicketViewing";
import { Schedule } from "../pages/Schedule";
import { ViewEmployees } from "../pages/ViewEmployees";

export function Main() {
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
            element={loggedIn ? <Profile /> : <Navigate to="/" />}
          />
          <Route path="/browse/room/:roomId" element={<ArtworkBrowsing />} />
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
          {/* <Route
            path="/employee"
            element={
              userRole === "employee" ? <Employee /> : <Navigate to="/login" />
            }
          /> */}
          {/* <Route
            path="/manager"
            element={
              userRole === "employee" ? <Manager /> : <Navigate to="/login" />
            }
          /> */}
        </Routes>
      </BrowserRouter>
    </LoginContext.Provider>
  );
}
