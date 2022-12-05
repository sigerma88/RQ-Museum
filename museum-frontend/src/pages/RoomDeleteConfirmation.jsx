import React, { useState } from "react";
import axios from "axios";
import {
  Dialog,
  DialogTitle,
  DialogActions,
  Button,
  Typography,
  DialogContent,
} from "@mui/material";

/**
 * Function for room delete confirmation dialog
 *
 * @returns  A dialog to confirm the deletion of a room
 * @author Siger
 */
export function RoomDeleteConfirmation({ room, open, handleClose }) {
  const [errorMessage, setErrorMessage] = useState("");

  // Function which is called when we submit the form to delete the room
  const handleSubmit = async (event) => {
    event.preventDefault();
    axios
      .delete(`/api/room/${room.roomId}`)
      .then((response) => {
        if (response.status === 200) {
          setErrorMessage("");
          handleClose();
          window.location = "/";
        } else {
          console.log("Something went wrong");
          setErrorMessage("Something went wrong");
        }
      })
      .catch((error) => {
        console.log(error);
        setErrorMessage(error.response.data);
      });
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
      <DialogTitle>
        Are you sure you want to delete the room {room.roomName}?
      </DialogTitle>

      <DialogContent>
        <Typography variant="p" component="p" color="red">
          {errorMessage}
        </Typography>
      </DialogContent>

      <DialogActions>
        <Button onClick={handleClose}>Cancel</Button>
        <Button onClick={handleSubmit} color="error">
          Delete
        </Button>
      </DialogActions>
    </Dialog>
  );
}
