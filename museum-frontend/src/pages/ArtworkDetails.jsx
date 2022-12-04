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
  Grid,
} from "@mui/material";
import { LoginContext } from "../Contexts/LoginContext";
import { LoanStatus, LoanStatusForStaff } from "./ArtworkBrowsing";
import LockIcon from "@mui/icons-material/Lock";
import "./LoanStatus.css";
import { EditArtworkInfo } from "./EditArtworkInfo";
import { EditArtworkLoanInfo } from "./EditArtworkLoanInfo";
import { MoveArtwork } from "./MoveArtwork";
import { ArtworkImageChanging } from "./ArtworkImageChanging";

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
 * @author Siger
 * @author Kevin
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
            <a href="/login" style={{ textDecoration: "underline" }}>
              <Typography>Click here to login</Typography>
            </a>
          </div>
        )}
      </div>
    );
  } else {
    return null;
  }
}

/**
 * VisitorArtworkDetails component
 * Shows the details of the artwork and the artwork itself as viewed by a visitor
 *
 * @param artwork - The artwork object
 * @param userRole - The user role
 * @param loggedIn - The logged in status
 * @returns The visitor artwork detail browsing section
 * @author Siger
 * @author Kevin
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

/**
 * Staff artwork info section
 *
 * @param artwork - The artwork object
 * @returns The staff artwork info section
 * @author Siger
 * @author Kevin
 */
function StaffArtworkInfo({ artwork, setArtwork }) {
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

  // Dialog for editing artwork info
  const [editArtworkInfoDialogOpen, setEditArtworkInfoDialogOpen] =
    useState(false);
  const handleEditArtworkInfoDialogOpen = () => {
    setEditArtworkInfoDialogOpen(true);
  };
  const handleEditArtworkInfoDialogClose = () => {
    setEditArtworkInfoDialogOpen(false);
  };

  return (
    <div style={{ margin: "50px auto" }}>
      <Grid
        container
        spacing={2}
        sx={{
          width: "70%",
          margin: "auto",
        }}
      >
        <Grid item xs={6}>
          <Typography variant="h5" margin={2}>
            Artwork information
          </Typography>
        </Grid>
        <Grid item xs={6}>
          <Button
            variant="contained"
            style={{ margin: 2 }}
            onClick={handleEditArtworkInfoDialogOpen}
          >
            Edit artwork info
          </Button>
        </Grid>
      </Grid>
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
            <ListItemText primary="Image" secondary={artwork.image} />
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

      {artwork && artwork.artworkId ? (
        <EditArtworkInfo
          artwork={artwork}
          setArtwork={setArtwork}
          open={editArtworkInfoDialogOpen}
          handleClose={handleEditArtworkInfoDialogClose}
        />
      ) : null}
    </div>
  );
}

/**
 * Staff artwork loan section
 *
 * @param artwork - The artwork object
 * @returns The staff artwork loan section
 * @author Siger
 * @author Kevin
 */
function StaffArtworkLoan({ artwork, setArtwork }) {
  // Dialog for editing artwork loan info
  const [editArtworkLoanInfoDialogOpen, setEditArtworkLoanInfoDialogOpen] =
    useState(false);
  const handleEditArtworkLoanInfoDialogOpen = () => {
    setEditArtworkLoanInfoDialogOpen(true);
  };
  const handleEditArtworkLoanInfoDialogClose = () => {
    setEditArtworkLoanInfoDialogOpen(false);
  };

  return (
    <div style={{ margin: "50px auto" }}>
      <Grid
        container
        spacing={2}
        sx={{
          width: "70%",
          margin: "auto",
        }}
      >
        <Grid item xs={6}>
          <Typography variant="h5" margin={2}>
            Loan information
          </Typography>
        </Grid>
        <Grid item xs={6}>
          <Button
            variant="contained"
            style={{ margin: 2 }}
            onClick={handleEditArtworkLoanInfoDialogOpen}
          >
            Edit loan info
          </Button>
        </Grid>
      </Grid>
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
            <ListItemText
              primary="Loan fee"
              secondary={artwork.loanFee ? artwork.loanFee : "N/A"}
            />
          </ListItem>
          <Divider variant="middle" />
        </div>
        <div style={{ width: "80%" }}>
          <ListItem>
            <ListItemText
              primary="Loan status"
              secondary={
                <LoanStatusForStaff
                  isAvailableForLoan={artwork.isAvailableForLoan}
                  isOnLoan={artwork.isOnLoan}
                  artworkId={artwork.artworkId}
                />
              }
            ></ListItemText>
          </ListItem>
          <Divider variant="middle" />
        </div>
      </List>

      {artwork && artwork.artworkId ? (
        <EditArtworkLoanInfo
          artwork={artwork}
          setArtwork={setArtwork}
          open={editArtworkLoanInfoDialogOpen}
          handleClose={handleEditArtworkLoanInfoDialogClose}
        />
      ) : null}
    </div>
  );
}

/**
 * Staff artwork room section
 *
 * @param artwork - The artwork object
 * @returns The staff artwork room section
 * @author Siger
 */
function StaffArtworkRoom({ artwork, setArtwork }) {
  // Dialog for moving artwork
  const [moveArtworkDialogOpen, setMoveArtworkDialogOpen] = useState(false);
  const handleMoveArtworkDialogOpen = () => {
    setMoveArtworkDialogOpen(true);
  };
  const handleMoveArtworkDialogClose = () => {
    setMoveArtworkDialogOpen(false);
  };

  return (
    <div style={{ margin: "50px auto" }}>
      <Grid
        container
        spacing={2}
        sx={{
          width: "70%",
          margin: "auto",
        }}
      >
        <Grid item xs={6}>
          <Typography variant="h5" margin={2}>
            Room
          </Typography>
        </Grid>
        <Grid item xs={6}>
          <Button
            variant="contained"
            style={{ margin: 2 }}
            onClick={handleMoveArtworkDialogOpen}
          >
            Move artwork
          </Button>
        </Grid>
      </Grid>
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
            <ListItemText
              primary="Museum"
              secondary={artwork.room ? artwork.room.museum.name : "None"}
            />
          </ListItem>
          <Divider variant="middle" />
        </div>
        <div style={{ width: "80%" }}>
          <ListItem>
            <ListItemText
              primary="Room"
              secondary={artwork.room ? artwork.room.roomName : "None"}
            ></ListItemText>
          </ListItem>
          <Divider variant="middle" />
        </div>
      </List>

      {artwork && artwork.room ? (
        <MoveArtwork
          open={moveArtworkDialogOpen}
          handleClose={handleMoveArtworkDialogClose}
          artwork={artwork}
          setArtwork={setArtwork}
        />
      ) : null}
    </div>
  );
}

/**
 * StaffArtworkDetails component
 * Shows the details of the artwork and the artwork itself as viewed by a staff member
 *
 * @param artwork - The artwork object
 * @returns The staff artwork detail browsing section
 * @author Siger
 * @author Kevin
 */
function StaffArtworkDetails({ artwork, setArtwork }) {
  const imageHeight = window.innerHeight * 0.89;

  // Dialog for changing artwork image
  const [artworkImageDialogOpen, setArtworkImageDialogOpen] = useState(false);
  const handleArtworkImageDialogOpen = () => {
    setArtworkImageDialogOpen(true);
  };
  const handleArtworkImageDialogClose = () => {
    setArtworkImageDialogOpen(false);
  };

  return (
    <>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6}>
          <Typography variant="h4" margin={5}>
            {artwork.name}
          </Typography>
        </Grid>
        <Grid item xs={12} sm={6}>
          <Button
            variant="contained"
            color="primary"
            sx={{ marginTop: 5, marginLeft: 2 }}
            onClick={handleArtworkImageDialogOpen}
          >
            Change image
          </Button>
          <Button
            variant="contained"
            color="error"
            sx={{ marginTop: 5, marginLeft: 2 }}
            onClick={() => {
              // TODO: Delete artwork
            }}
          >
            Delete artwork
          </Button>
        </Grid>
      </Grid>
      <img
        src={artwork.image}
        alt="artwork"
        style={{ height: imageHeight, borderRadius: 10 }}
      />

      <StaffArtworkInfo artwork={artwork} setArtwork={setArtwork} />

      <StaffArtworkLoan artwork={artwork} setArtwork={setArtwork} />

      <StaffArtworkRoom artwork={artwork} setArtwork={setArtwork} />

      {artwork && artwork.artworkId ? (
        <ArtworkImageChanging
          artwork={artwork}
          setArtwork={setArtwork}
          open={artworkImageDialogOpen}
          handleClose={handleArtworkImageDialogClose}
        />
      ) : null}
    </>
  );
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
  }, [artworkId, artwork]);

  const { loggedIn, userRole, userId } = useContext(LoginContext);
  if (loggedIn && (userRole === "manager" || userRole === "employee")) {
    return <StaffArtworkDetails artwork={artwork} setArtwork={setArtwork} />;
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
