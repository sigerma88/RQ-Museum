import React, { useState, useEffect, useContext } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import {
  Button,
  Divider,
  List,
  ListItem,
  ListItemText,
  Typography,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Avatar,
} from "@mui/material";
import { LoginContext } from "../Contexts/LoginContext";
import { LoanStatus } from "./ArtworkBrowsing";
import LockIcon from "@mui/icons-material/Lock";
import "./LoanStatus.css";

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
 * Component for the loan dialog
 * @param open - The open status of the dialog
 * @param handleClose - The function to close the dialog
 * @param artwork - The artwork object
 * @param userId - The user id
 * @returns The loan dialog component
 * @author Kevin
 * */

function LoanConfirmation({ open, close, userId, artwork }) {
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  function handleLoan() {
    axios
      .post("/api/loan/create", {
        visitorDto: {
          museumUserId: userId,
        },
        artworkDto: artwork,
      })
      .then((response) => {
        navigate("/loan");
      })
      .catch((error) => {
        setErrorMessage(error.response.data);
      });
  }

  const handleClose = () => {
    close();
    setErrorMessage("");
  };

  return (
    <Dialog open={open} onClose={close}>
      <DialogTitle>Loan confirmation</DialogTitle>
      <DialogContent>
        <Typography>
          Are you sure you want to loan this artwork? You will be charged once
          the request is accepted.
        </Typography>
        <p style={{ color: "red" }}>{errorMessage}</p>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose}>Cancel</Button>
        <Button onClick={handleLoan}>Confirm</Button>
      </DialogActions>
    </Dialog>
  );
}

/**
 * Visitor artwork loan section
 *
 * @param artwork - The artwork object
 * @param userRole - The user role
 * @param loggedIn - The logged in status
 * @returns The visitor artwork loan section
 * @author Siger, Kevin (redesign)
 */
function VisitorArtworkLoan({ artwork, userRole, loggedIn, userId }) {
  const [open, setOpen] = useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  if (artwork.isAvailableForLoan) {
    return (
      <div>
        <Typography variant="h5" margin={2}>
          Loan information
        </Typography>
        <List
          style={{
            width: "70%",
            padding: "auto",
            margin: "auto",
            display: "flex",
            justifyContent: "space-evenly",
          }}
        >
          <div style={{ width: "80%" }}>
            <ListItem>
              <ListItemText primary="Loan fee" secondary={artwork.loanFee} />
            </ListItem>
            <Divider variant="middle" />
          </div>
          <div style={{ width: "80%" }}>
            <ListItem>
              <ListItemText
                primary="Loan status"
                secondary={
                  <LoanStatus
                    isAvailableForLoan={artwork.isAvailableForLoan}
                    isOnLoan={artwork.isOnLoan}
                  />
                }
              ></ListItemText>
            </ListItem>
            <Divider variant="middle" />
          </div>
        </List>
        {artwork.isAvailableForLoan && loggedIn && userRole === "visitor" ? (
          <div>
            <Button
              variant="contained"
              disabled={artwork.isOnLoan}
              style={{ margin: 50 }}
              onClick={handleClickOpen}
            >
              Loan this
            </Button>
            <LoanConfirmation
              open={open}
              close={handleClose}
              userId={userId}
              artwork={artwork}
            />
          </div>
        ) : (
          <div style={{ margin: 50 }}>
            <Avatar
              sx={{
                margin: "auto",
                marginBottom: "20px",
                width: "50px",
                height: "50px",
                bgcolor: "black",
              }}
            >
              <LockIcon />
            </Avatar>
            <Typography variant="h4">Login to request a loan</Typography>
            <a href="/login">
              <Typography>Click here to login</Typography>
            </a>
          </div>
        )}
        {/* TODO: Add form for manager or employee to edit artwork  */}
      </div>
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
 * @author Siger, Kevin
 */
function VisitorArtworkDetails({ artwork, userRole, loggedIn, userId }) {
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
      <Typography variant="h4" margin={5}>
        {artwork.name}
      </Typography>
      <img
        src={artwork.image}
        alt="artwork"
        style={{ height: imageHeight, borderRadius: 10 }}
      />
      <div style={{ margin: "50px auto" }}>
        <Typography variant="h5" margin={2}>
          Artwork information
        </Typography>
        <List
          style={{
            display: "flex",
            margin: "auto",
            justifyContent: "space-evenly",
            padding: "auto",
            width: "70%",
          }}
        >
          <div style={{ width: "80%" }}>
            <ListItem className="artwork-info">
              <ListItemText primary="Name" secondary={artwork.name} />
            </ListItem>
            <Divider variant="middle" />
            <ListItem>
              <ListItemText primary="Artist" secondary={artwork.artist} />
            </ListItem>
            <Divider variant="middle" />
          </div>
          <div style={{ width: "80%" }}>
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
                secondary={computeArtworkStatus(artworkStatus)}
              />
            </ListItem>
            <Divider variant="middle" />
          </div>
        </List>
      </div>

      <VisitorArtworkLoan
        artwork={artwork}
        userRole={userRole}
        loggedIn={loggedIn}
        userId={userId}
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

  const { loggedIn, userRole, userId } = useContext(LoginContext);
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
        userId={userId}
      />
    );
  }
}

export default ArtworkDetails;
