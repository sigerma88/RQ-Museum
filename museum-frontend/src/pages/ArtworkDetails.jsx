import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { Button, Grid, Typography } from "@mui/material";
import { Container } from "@mui/system";
import { LoginContext } from "../Contexts/LoginContext";
import { Navigation } from "../layouts/Navigation";
import Home from "./Home";

// Function to get the artworks from the server
function getArtwork(artworkId) {
  return axios
    .get(`/api/artwork/${artworkId}`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
}

// VisitorArtworkBrowsing component
function VisitorArtworkDetails({ artwork }) {
  const imageHeight = window.innerHeight * 0.89;

  return (
    <>
      <img
        src={artwork ? artwork.image : ""}
        alt="artwork"
        style={{ marginTop: 30, height: imageHeight }}
      />
      <Typography variant="h4" component="h1">
        {artwork ? artwork.name : ""}
      </Typography>
      <Typography variant="h6" component="h2">
        {artwork ? artwork.artist : ""}
      </Typography>
    </>
  );
}

function ManagerArtworkDetails() {
  return <p>Admin</p>;
}

function EmployeeArtworkDetails() {
  return <p>Employee</p>;
}

// Main function
function ArtworkDetails() {
  // Parse the roomId from the URL
  let { artworkId } = useParams();

  // Get the artworks from the server
  const [artwork, setArtwork] = useState(null);
  useEffect(() => {
    getArtwork(artworkId).then((artwork) => {
      setArtwork(artwork);
    });
  });

  const { loggedIn, userRole } = useContext(LoginContext);
  if (userRole === "visitor" && loggedIn) {
    return <VisitorArtworkDetails artwork={artwork} />;
  } else if (userRole === "manager" && loggedIn) {
    return <ManagerArtworkDetails />;
  } else if (userRole === "employee" && loggedIn) {
    return <EmployeeArtworkDetails />;
  } else {
    return <Home />;
  }
}

export default ArtworkDetails;
