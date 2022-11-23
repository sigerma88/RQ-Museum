import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Grid,
  Typography,
} from "@mui/material";
import { Container } from "@mui/system";
import { LoginContext } from "../Contexts/LoginContext";
import { Navigation } from "../layouts/Navigation";
import Home from "./Home";

// Function to get the artworks from the server
function getArtworks(roomId) {
  return axios
    .get(`/api/artwork/${roomId === "all" ? "" : `room/${roomId}`}`)
    .then((response) => {
      console.log(response.data);
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
}

// Function to get the room from the server
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

// VisitorArtworkBrowsing component
function VisitorArtworkBrowsing({ artworks, room }) {
  return (
    <>
      <Typography variant="h4" component="h1" marginTop={5}>
        {room.roomName}
      </Typography>
      <Container sx={{ py: 5 }}>
        <Grid container spacing={4}>
          {artworks.map((card) => (
            <Grid item key={card} xs={12} md={6}>
              <Card
                sx={{
                  height: "100%",
                  display: "flex",
                  flexDirection: "column",
                }}
              >
                <CardMedia
                  component="img"
                  image={card.image}
                  alt="Artwork image"
                  sx={{ height: 500 }}
                />
                <CardContent sx={{ flexGrow: 1 }}>
                  <Typography gutterBottom variant="h5" component="h2">
                    {card.name}
                  </Typography>
                  <Typography variant="h6">{"by " + card.artist}</Typography>
                  <Typography color={"gray"}>
                    {`${card.isAvailableForLoan ? "Available for loan" : ""}` +
                      `${card.isOnLoan ? " but is currently on loan" : ""}`}
                  </Typography>
                </CardContent>
                <CardActions>
                  {/* TODO: Add link to artwork page */}
                  <Button size="small">View</Button>
                  {/* TODO: Add link to loan page */}
                  <Button size="small" disabled={!card.isAvailableForLoan}>
                    Loan
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>
    </>
  );
}

function ManagerArtworkBrowsing() {
  return <p>Admin</p>;
}

function EmployeeArtworkBrowsing() {
  return <p>Employee</p>;
}

// Main function
function ArtworkBrowsing() {
  // Parse the roomId from the URL
  let { roomId } = useParams();

  // Get the artworks from the server
  const [artworks, setArtworks] = useState([]);
  useEffect(() => {
    getArtworks(roomId).then((artworks) => {
      setArtworks(artworks);
    });
  });

  // Get the room from the server
  const [room, setRoom] = useState({});
  useEffect(() => {
    getRoom(roomId).then((room) => {
      setRoom(room);
    });
  });

  const { loggedIn, userRole } = useContext(LoginContext);
  if (userRole === "visitor" && loggedIn) {
    return <VisitorArtworkBrowsing artworks={artworks} room={room} />;
  } else if (userRole === "manager" && loggedIn) {
    return <ManagerArtworkBrowsing />;
  } else if (userRole === "employee" && loggedIn) {
    return <EmployeeArtworkBrowsing />;
  } else {
    return <Home />;
  }
}

export default ArtworkBrowsing;