import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { Card, CardContent, CardMedia, Grid, Typography } from "@mui/material";
import { Container } from "@mui/system";
import { LoginContext } from "../Contexts/LoginContext";
import "./LoanStatus.css";
import "./ArtworkBrowsing.css";

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

function ArtworkList({ artworks, room }) {
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
            <a
              style={{
                fontStyle: "none",
                textDecoration: "none",
                color: "black",
              }}
              href={`/browse/artwork/${card.artworkId}`}
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
                  <LoanStatus
                    isAvailableForLoan={card.isAvailableForLoan}
                    isOnLoan={card.isOnLoan}
                  />
                </div>
              </CardContent>
            </a>
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
 * @author Siger, Kevin(redesign)
 */
function VisitorArtworkBrowsing({ artworks, room }) {
  return (
    <>
      <div style={{ lineHeight: "14px" }}>
        <Typography variant="h4" component="h1" marginTop={5}>
          {room.roomName}
        </Typography>
        <Typography>{artworks.length} artworks</Typography>
      </div>

      <Container sx={{ py: 5 }}>
        <ArtworkList artworks={artworks} room={room} />
      </Container>
    </>
  );
}

/**
 * StaffArtworkBrowsing component
 * Artwork browsing as viewed by the staff
 *
 * @param artworks - The artworks to display
 * @param room - The room to display
 * @author Siger, Kevin(redesign)
 */
function StaffArtworkBrowsing() {
  return (
    <>
      <div style={{ lineHeight: "14px" }}>
        <Typography variant="h4" component="h1" marginTop={5}>
          {room.roomName}
        </Typography>
        <Typography>{artworks.length} artworks</Typography>
      </div>

      <Container sx={{ py: 5 }}>
        <ArtworkList artworks={artworks} room={room} />
      </Container>
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
  useEffect(() => {
    getRoom(roomId).then((room) => {
      setRoom(room);
    });
  }, [roomId]);

  const { loggedIn, userRole } = useContext(LoginContext);
  if (loggedIn && (userRole === "manager" || userRole === "employee")) {
    return <StaffArtworkBrowsing />;
  } else {
    return <VisitorArtworkBrowsing artworks={artworks} room={room} />;
  }
}

export default ArtworkBrowsing;
