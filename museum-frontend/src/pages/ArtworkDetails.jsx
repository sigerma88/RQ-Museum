import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import {
  Button,
  Divider,
  List,
  ListItem,
  ListItemText,
  Typography,
} from "@mui/material";
import { LoginContext } from "../Contexts/LoginContext";
import { Navigation } from "../layouts/Navigation";

/**
 * Function to get the artwork from the server
 *
 * @param artworkId - The artwork id
 * @returns The fetched artwork
 * @author Siger
 */
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

/**
 * Function to get the artwork status from the server
 *
 * @param artworkId - The artwork id
 * @returns The fetched artwork status
 * @author Siger
 */
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

/**
 * Funtion to compute the artwork status
 *
 * @param artworkStatus - The artwork status from the object
 * @returns string - The computed artwork status
 * @author Siger
 */
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

/**
 * Visitor artwork loan section
 *
 * @param artwork - The artwork object
 * @param userRole - The user role
 * @param loggedIn - The logged in status
 * @returns The visitor artwork loan section
 * @author Siger
 */
function VisitorArtworkLoan({ artwork, userRole, loggedIn }) {
  if (artwork.isAvailableForLoan && userRole === "visitor" && loggedIn) {
    return (
      <>
        <Divider variant="middle" />
        <Typography variant="h5" margin={2}>
          Loan information
        </Typography>
        <List>
          <ListItem>
            <ListItemText primary="Loan fee" secondary={artwork.loanFee} />
          </ListItem>
          <Divider variant="middle" />
          <ListItem>
            <ListItemText
              primary="Loan status"
              secondary={artwork.isOnLoan ? "Currently on loan" : "Not on loan"}
            />
          </ListItem>
        </List>
        <Divider variant="middle" />
        {/* TODO: Add loan button action */}
        <Button
          variant="contained"
          disabled={artwork.isOnLoan}
          style={{ margin: 10 }}
        >
          Loan this
        </Button>
      </>
    );
  } else {
    return null;
  }
}

/**
 * VisitorArtworkBrowsing component
 *
 * @param artwork - The artwork object
 * @param userRole - The user role
 * @param loggedIn - The logged in status
 * @returns The visitor artwork browsing section
 * @author Siger
 */
function VisitorArtworkDetails({ artwork, userRole, loggedIn }) {
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
  }, [artwork.artworkId]);

  return (
    <>
      <img
        src={artwork.image}
        alt="artwork"
        style={{ marginTop: 30, height: imageHeight }}
      />
      <Typography variant="h5" margin={2}>
        Artwork information
      </Typography>
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
      <VisitorArtworkLoan
        artwork={artwork}
        userRole={userRole}
        loggedIn={loggedIn}
      />
    </>
  );
}

function ManagerArtworkDetails() {
  return <p>Admin</p>;
}

function EmployeeArtworkDetails() {
  return <p>Employee</p>;
}

/**
 * Main function
 *
 * @returns The artwork details page
 * @author Siger
 */
function ArtworkDetails() {
  // Parse the roomId from the URL
  let { artworkId } = useParams();

  // Get the artworks from the server
  const [artwork, setArtwork] = useState({});
  useEffect(() => {
    getArtwork(artworkId).then((artwork) => {
      setArtwork(artwork);
    });
  }, [artworkId]);

  const { loggedIn, userRole } = useContext(LoginContext);
  if (userRole === "manager" && loggedIn) {
    return <ManagerArtworkDetails />;
  } else if (userRole === "employee" && loggedIn) {
    return <EmployeeArtworkDetails />;
  } else {
    return (
      <VisitorArtworkDetails
        artwork={artwork}
        userRole={userRole}
        loggedIn={loggedIn}
      />
    );
  }
}

export default ArtworkDetails;