import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import { Box, Modal, Typography } from "@mui/material";
import { LoginContext } from "../Contexts/LoginContext";

// Box styling
const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 800,
  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  p: 4,
};

/**
 * Function to seem more information about a room
 *
 * @param room - The room to display
 * @author Siger
 */
export function RoomDetails({ room, open, handleClose }) {
  const { loggedIn, userRole } = useContext(LoginContext);
  const [roomCapacity, setRoomCapacity] = useState(0);
  const [remainingCapacity, setRemainingCapacity] = useState(0);

  // Get room max capacity and remaining capacity
  useEffect(() => {
    axios
      .get(`/api/room/maxArtworks/${room.roomId}`)
      .then((response) => {
        if (response.data === -1) {
          setRoomCapacity("Unlimited");
        } else {
          setRoomCapacity(response.data);
        }
      })
      .catch((error) => {
        console.log(error);
      });

    axios
      .get(`/api/room/getRoomCapacity/${room.roomId}`)
      .then((response) => {
        if (response.data === -1) {
          setRemainingCapacity("Unlimited");
        } else {
          setRemainingCapacity(response.data);
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }, [room]);

  if (loggedIn && (userRole === "manager" || userRole === "employee")) {
    return (
      <Modal open={open} onClose={handleClose}>
        <Box sx={style}>
          <Typography variant="h4" component="h1" marginTop={5}>
            {room.roomName}
          </Typography>
          <Typography>ID: {room.roomId}</Typography>
          <Typography>Museum: {room.museum.name}</Typography>
          <Typography>Room type: {room.roomType}</Typography>
          <Typography>
            Number of artworks: {room.currentNumberOfArtwork}
          </Typography>
          <Typography>Max capacity: {roomCapacity}</Typography>
          <Typography>Remaining capacity: {remainingCapacity}</Typography>
        </Box>
      </Modal>
    );
  } else {
    return (
      <Modal open={open} onClose={handleClose}>
        <Box sx={style}>
          <Typography variant="h4" component="h1" marginTop={5}>
            {room.roomName}
          </Typography>
          <Typography>Museum: {room.museum.name}</Typography>
          <Typography>Room type: {room.roomType}</Typography>
          <Typography>
            Number of artworks: {room.currentNumberOfArtwork}
          </Typography>
        </Box>
      </Modal>
    );
  }
}
