import React, { useContext } from "react";
import { LoginContext } from "../../Contexts/LoginContext";
import Home from "../Home";
import { Navigation } from "../../layouts/Navigation";
import { useParams } from "react-router-dom";

function VisitorArtworkBrowsing({roomId}) {
  return <p>Visitor browsing room {roomId}</p>;
}

function ManagerArtworkBrowsing() {
  return <p>Admin</p>;
}

function EmployeeArtworkBrowsing() {
  return <p>Employee</p>;
}

function ArtworkBrowsing() {
  // Parse the roomId from the URL
  let { roomId } = useParams();

  const { loggedIn, userRole } = useContext(LoginContext);
  if (userRole === "visitor" && loggedIn) {
    return <VisitorArtworkBrowsing roomId={roomId}/>;
  } else if (userRole === "manager" && loggedIn) {
    return <ManagerArtworkBrowsing />;
  } else if (userRole === "employee" && loggedIn) {
    return <EmployeeArtworkBrowsing />;
  } else {
    return <Home />;
  }
}

export default ArtworkBrowsing;
