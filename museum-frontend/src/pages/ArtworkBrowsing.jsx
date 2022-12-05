import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import {
  Box,
  Button,
  Card,
  CardContent,
  CardMedia,
  Grid,
  Tooltip,
  Typography,
} from "@mui/material";
import { Container } from "@mui/system";
import { LoginContext } from "../Contexts/LoginContext";
import { Link } from "react-router-dom";
import "./LoanStatus.css";
import "./ArtworkBrowsing.css";
import { RoomDetails } from "./RoomDetails";
import { RoomCreation } from "./RoomCreation";
import { EditRoom } from "./EditRoom";
import { RoomDeleteConfirmation } from "./RoomDeleteConfirmation";
import { ArtworkCreation } from "./ArtworkCreation";

/**
 * Function to get the artworks from the server
 *
 * @param roomId - The room ID to get the artworks from
 * @returns The fetched artworks
 * @author Siger
 */
function getArtworks(roomId) {
  return axios
    .get(`/api/artwork/${roomId === "all" ? "" : `room/${roomId}`}`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
}

/**
 * Function to get the room from the server
 *
 * @param roomId - The room ID to get the room from
 * @returns The fetched room
 * @author Siger
 */
function getRoom(roomId) {
  if (roomId === "all") {
    return Promise.resolve({ roomName: "All Rooms" });
  } else {
    return axios
      .get(`/api/room/${roomId}`)
      .then((response) => {
        return response.data;
      })
      .catch((error) => {
        console.log(error);
      });
  }
}

/**
 * Loan Status message
 *
 * @param isAvailableForLoan - If artwork is loanable
 * @param isOnLoan - If artwork is on loan
 * @returns The loan status message
 * @author Kevin
 */
export function LoanStatus({ isAvailableForLoan, isOnLoan }) {
  if (!isAvailableForLoan) {
    return (
      <Typography variant="caption" color={"gray"}>
        <span className="red-dot"></span> Unavailable for loan
      </Typography>
    );
  } else if (isAvailableForLoan && !isOnLoan) {
    return (
      <Typography variant="caption" color={"gray"}>
        <span className="green-dot"></span> Available for loan
      </Typography>
    );
  } else if (isOnLoan) {
    return (
      <Typography variant="caption" color={"gray"}>
        <span className="orange-dot"></span> Currently on loan
      </Typography>
    );
  }
}

/**
 * More detailed Loan Status message for staff
 *
 * @param isAvailableForLoan - If artwork is loanable
 * @param isOnLoan - If artwork is on loan
 * @param artworkId - The artwork ID
 * @returns The loan status message
 * @author Kevin
 * @author Siger
 */
export function LoanStatusForStaff({
  isAvailableForLoan,
  isOnLoan,
  artworkId,
}) {
  const [LoanRequested, setLoanRequested] = useState(false);

  // Check if loan is requested
  useEffect(() => {
    axios
      .get(`/api/loan/artwork/${artworkId}`)
      .then((response) => {
        if (response.data.length > 0) {
          for (let i = 0; i < response.data.length; i++) {
            if (response.data[i].requestAccepted === null) {
              setLoanRequested(true);
            }
          }
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }, [artworkId]);

  if (LoanRequested) {
    return (
      <Typography
        variant="caption"
        color={"gray"}
        title="See loan requests"
        style={{ cursor: "pointer" }}
        onClick={() => (window.location = "/loan")}
      >
        <span className="yellow-dot"></span> Loan requested
      </Typography>
    );
  } else if (!isAvailableForLoan) {
    return (
      <Typography variant="caption" color={"gray"}>
        <span className="red-dot"></span> Unavailable for loan
      </Typography>
    );
  } else if (isAvailableForLoan && !isOnLoan) {
    return (
      <Typography variant="caption" color={"gray"}>
        <span className="green-dot"></span> Available for loan
      </Typography>
    );
  } else if (isOnLoan) {
    return (
      <Typography
        variant="caption"
        color={"gray"}
        title="See artworks on loan"
        style={{ cursor: "pointer" }}
        onClick={() => (window.location = "/loan")}
      >
        <span className="orange-dot"></span> Currently on loan
      </Typography>
    );
  }
}

/**
 * This component displays a list of artworks
 *
 * @param artworks - The artworks to display
 * @param room - The room to display
 * @returns List of artworks
 * @author Siger
 * @author Kevin
 */
function ArtworkList({ artworks }) {
  const { loggedIn, userRole } = useContext(LoginContext);

  return (
    <Grid container spacing={4}>
      {artworks.map((card) => (
        <Grid item key={card.artworkId} xs={12} sm={6} md={3}>
          <Card
            sx={{
              display: "flex",
              flexDirection: "column",
              height: "400px",
              width: "100%",
              position: "relative",
            }}
            className="card"
          >
            <Link
              style={{
                fontStyle: "none",
                textDecoration: "none",
                color: "black",
              }}
              to={`/browse/artwork/${card.artworkId}`}
            >
              <CardMedia
                component="img"
                style={{
                  objectFit: "cover",
                  height: "200px",
                }}
                image={card.image}
                alt="Artwork image"
              />
              <CardContent
                sx={{
                  flexGrow: 1,
                }}
              >
                <Typography
                  gutterBottom
                  variant="subtitle1"
                  style={{ fontWeight: "bold" }}
                >
                  {card.name}
                </Typography>
                <Typography variant="subtitle2">
                  {"by " + card.artist}
                </Typography>
                <div
                  style={
                    !card.isAvailableForLoan
                      ? {
                          position: "absolute",
                          bottom: "65px",
                          width: "90%",
                          margin: "auto 0px",
                        }
                      : {
                          position: "absolute",
                          bottom: "65px",
                          width: "88%",
                          margin: "auto 0px",
                        }
                  }
                >
                  {loggedIn &&
                  (userRole === "manager" || userRole === "employee") ? (
                    <LoanStatusForStaff
                      isAvailableForLoan={card.isAvailableForLoan}
                      isOnLoan={card.isOnLoan}
                      artworkId={card.artworkId}
                    />
                  ) : (
                    <LoanStatus
                      isAvailableForLoan={card.isAvailableForLoan}
                      isOnLoan={card.isOnLoan}
                    />
                  )}
                </div>
              </CardContent>
            </Link>
          </Card>
        </Grid>
      ))}
    </Grid>
  );
}

/**
 * VisitorArtworkBrowsing component
 * Artwork browsing as viewed by the visitor
 *
 * @param artworks - The artworks to display
 * @param room - The room to display
 * @author Siger
 */
function VisitorArtworkBrowsing({ artworks, room }) {
  // Room details modal
  const [roomDetailModalOpen, setRoomDetailModalOpen] = useState(false);
  const handleRoomDetailModalOpen = () => setRoomDetailModalOpen(true);
  const handleRoomDetailModalClose = () => setRoomDetailModalOpen(false);

  return (
    <>
      <div style={{ lineHeight: "14px" }}>
        {room.roomName === "All Rooms" ? (
          <Box item xs={12} sm={6}>
            <Typography variant="h4" component="h1" marginTop={5}>
              {room.roomName}
            </Typography>
            <Typography>{artworks.length} artworks</Typography>
          </Box>
        ) : (
          <Tooltip
            title="See more information about this room"
            placement="right"
            arrow
            followCursor
          >
            <Box
              style={{ cursor: "pointer" }}
              onClick={handleRoomDetailModalOpen}
            >
              <Typography variant="h4" component="h1" marginTop={5}>
                {room.roomName}
              </Typography>
              <Typography>{artworks.length} artworks</Typography>
            </Box>
          </Tooltip>
        )}
      </div>

      <Container sx={{ py: 5 }}>
        <ArtworkList artworks={artworks} />
      </Container>

      {room && room.museum && (
        <RoomDetails
          room={room}
          open={roomDetailModalOpen}
          handleClose={handleRoomDetailModalClose}
        />
      )}
    </>
  );
}

/**
 * StaffArtworkBrowsing component
 * Artwork browsing as viewed by the staff
 *
 * @param artworks - The artworks to display
 * @param room - The room to display
 * @author Siger
 */
function StaffArtworkBrowsing({ artworks, room, roomChanged, setRoomChanged }) {
  // Room details modal
  const [roomDetailModalOpen, setRoomDetailModalOpen] = useState(false);
  const handleRoomDetailModalOpen = () => setRoomDetailModalOpen(true);
  const handleRoomDetailModalClose = () => setRoomDetailModalOpen(false);

  // Add room modal
  const [addRoomModalOpen, setAddRoomModalOpen] = useState(false);
  const handleAddRoomModalOpen = () => setAddRoomModalOpen(true);
  const handleAddRoomModalClose = () => setAddRoomModalOpen(false);

  // Edit room modal
  const [editRoomModalOpen, setEditRoomModalOpen] = useState(false);
  const handleEditRoomModalOpen = () => setEditRoomModalOpen(true);
  const handleEditRoomModalClose = () => setEditRoomModalOpen(false);

  // Delete room modal
  const [deleteRoomModalOpen, setDeleteRoomModalOpen] = useState(false);
  const handleDeleteRoomModalOpen = () => setDeleteRoomModalOpen(true);
  const handleDeleteRoomModalClose = () => setDeleteRoomModalOpen(false);

  // Add artwork modal
  const [addArtworkModalOpen, setAddArtworkModalOpen] = useState(false);
  const handleAddArtworkModalOpen = () => setAddArtworkModalOpen(true);
  const handleAddArtworkModalClose = () => setAddArtworkModalOpen(false);

  return (
    <>
      <div style={{ lineHeight: "14px" }}>
        <Grid container spacing={2}>
          {room.roomName === "All Rooms" ? (
            <Grid item xs={12} sm={6}>
              <Typography variant="h4" component="h1" marginTop={5}>
                {room.roomName}
              </Typography>
              <Typography>{artworks.length} artworks</Typography>
            </Grid>
          ) : (
            <Tooltip
              title="See more information about this room"
              placement="right"
              arrow
              followCursor
            >
              <Grid
                item
                xs={12}
                sm={6}
                style={{ cursor: "pointer" }}
                onClick={handleRoomDetailModalOpen}
              >
                <Typography variant="h4" component="h1" marginTop={5}>
                  {room.roomName}
                </Typography>
                <Typography>{artworks.length} artworks</Typography>
              </Grid>
            </Tooltip>
          )}
          <Grid item xs={12} sm={6}>
            <Button
              variant="contained"
              color="success"
              sx={{ marginTop: 5 }}
              onClick={handleAddRoomModalOpen}
            >
              New Room
            </Button>
            <Button
              variant="contained"
              color="primary"
              disabled={room.roomName === "All Rooms"}
              sx={{ marginTop: 5, marginLeft: 2 }}
              onClick={handleEditRoomModalOpen}
            >
              Edit Room
            </Button>
            <Button
              variant="contained"
              color="error"
              disabled={room.roomName === "All Rooms"}
              sx={{ marginTop: 5, marginLeft: 2 }}
              onClick={handleDeleteRoomModalOpen}
            >
              Delete Room
            </Button>
          </Grid>
        </Grid>
      </div>

      <Container sx={{ py: 5 }}>
        <ArtworkList artworks={artworks} />
        <Button
          variant="contained"
          color="success"
          sx={{ marginTop: 5 }}
          onClick={handleAddArtworkModalOpen}
        >
          New Artwork
        </Button>
      </Container>

      {room && room.museum && (
        <>
          <RoomDetails
            room={room}
            open={roomDetailModalOpen}
            handleClose={handleRoomDetailModalClose}
          />

          <RoomCreation
            open={addRoomModalOpen}
            handleClose={handleAddRoomModalClose}
            museum={room.museum}
          />

          <EditRoom
            room={room}
            roomChanged={roomChanged}
            setRoomChanged={setRoomChanged}
            open={editRoomModalOpen}
            handleClose={handleEditRoomModalClose}
          />
        </>
      )}

      {room && room.roomId && (
        <>
          <RoomDeleteConfirmation
            room={room}
            open={deleteRoomModalOpen}
            handleClose={handleDeleteRoomModalClose}
          />

          <ArtworkCreation
            room={room}
            open={addArtworkModalOpen}
            handleClose={handleAddArtworkModalClose}
          />
        </>
      )}
    </>
  );
}

/**
 * Main function
 *
 * @returns The ArtworkBrowsing component
 * @author Siger
 */
function ArtworkBrowsing() {
  // Parse the roomId from the URL
  let { roomId } = useParams();

  // Get the artworks from the server
  const [artworks, setArtworks] = useState([]);
  useEffect(() => {
    getArtworks(roomId).then((artworks) => {
      setArtworks(artworks);
    });
  }, [roomId]);

  // Get the room from the server
  const [room, setRoom] = useState({});
  const [roomChanged, setRoomChanged] = useState(false);
  useEffect(() => {
    getRoom(roomId).then((room) => {
      setRoom(room);
    });
  }, [roomId, roomChanged]);

  const { loggedIn, userRole } = useContext(LoginContext);
  if (loggedIn && (userRole === "manager" || userRole === "employee")) {
    return (
      <StaffArtworkBrowsing
        artworks={artworks}
        room={room}
        roomChanged={roomChanged}
        setRoomChanged={setRoomChanged}
      />
    );
  } else {
    return <VisitorArtworkBrowsing artworks={artworks} room={room} />;
  }
}

export default ArtworkBrowsing;
