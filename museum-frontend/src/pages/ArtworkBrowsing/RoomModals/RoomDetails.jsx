import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import { Dialog, DialogContent, DialogTitle, Typography } from "@mui/material";
import { LoginContext } from "../../../Contexts/LoginContext";

/**
 * Function to seem more information about a room
 *
 * @param room - The room to display
 * @author Siger
 */
export function RoomDetails({ room, open, handleClose }) {
  const { loggedIn, userRole } = useContext(LoginContext);
  const [roomCapacity, setRoomCapacity] = useState("Loading...");
  const [remainingCapacity, setRemainingCapacity] = useState("Loading...");

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
      <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
        <DialogTitle>{room.roomName}</DialogTitle>
        <DialogContent>
          <Typography>ID: {room.roomId}</Typography>
          <Typography>Museum: {room.museum.name}</Typography>
          <Typography>Room type: {room.roomType}</Typography>
          <Typography>
            Number of artworks: {room.currentNumberOfArtwork}
          </Typography>
          <Typography>Max capacity: {roomCapacity}</Typography>
          <Typography>Remaining capacity: {remainingCapacity}</Typography>
        </DialogContent>
      </Dialog>
    );
  } else {
    return (
      <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
        <DialogTitle>{room.roomName}</DialogTitle>
        <DialogContent>
          <Typography>Museum: {room.museum.name}</Typography>
          <Typography>Room type: {room.roomType}</Typography>
          <Typography>
            Number of artworks: {room.currentNumberOfArtwork}
          </Typography>
        </DialogContent>
      </Dialog>
    );
  }
}
