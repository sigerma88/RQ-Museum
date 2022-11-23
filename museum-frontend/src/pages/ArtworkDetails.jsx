import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import {
  Button,
  Divider,
  Grid,
  List,
  ListItem,
  ListItemText,
  Typography,
} from "@mui/material";
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

// Function to get the artwork status from the server
function getArtworkStatus(artworkId) {
  return axios
    .get(`/api/artwork/getArtworkStatus/${artworkId}`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
}

// Funtion to compute the artwork status
function computeArtworkStatus(artworkStatus) {
  let status = "Not Available";
  if (artworkStatus.length > 0) {
    if (artworkStatus === "display") {
      status = "On display";
    } else if (artworkStatus === "storage") {
      status = "In storage";
    } else if (artworkStatus === "loan") {
      status = "On loan";
    }
  }
  return status;
}

// VisitorArtworkBrowsing component
function VisitorArtworkDetails({ artwork }) {
  const imageHeight = window.innerHeight * 0.89;

  // Get the artwork status from the server
  const [artworkStatus, setArtworkStatus] = useState({});
  useEffect(() => {
    if (artwork.artworkId !== undefined) {
      getArtworkStatus(artwork.artworkId).then((artworkStatus) => {
        setArtworkStatus(artworkStatus);
      });
    } else {
      setArtworkStatus({});
    }
  });

  return (
    <>
      <img
        src={artwork.image}
        alt="artwork"
        style={{ marginTop: 30, height: imageHeight }}
      />
      <List>
        <ListItem>
          <ListItemText primary="Name" secondary={artwork.name} />
        </ListItem>
        <Divider variant="middle" />
        <ListItem>
          <ListItemText primary="Artist" secondary={artwork.artist} />
        </ListItem>
        <Divider variant="middle" />
        <ListItem>
          <ListItemText
            primary="Room"
            secondary={artwork.room ? artwork.room.roomName : "None"}
          />
        </ListItem>
        <Divider variant="middle" />
        <ListItem>
          <ListItemText
            primary="Artwork status"
            secondary={computeArtworkStatus(artworkStatus.toString())}
          />
        </ListItem>
      </List>
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
  const [artwork, setArtwork] = useState({});
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
