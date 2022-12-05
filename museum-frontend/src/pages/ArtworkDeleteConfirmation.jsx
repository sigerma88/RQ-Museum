import React, { useState } from "react";
import axios from "axios";
import {
  Dialog,
  DialogTitle,
  DialogActions,
  Button,
  DialogContent,
  Typography,
} from "@mui/material";

/**
 * Function for artwork delete confirmation dialog
 *
 * @returns  A dialog to confirm the deletion of an artwork
 * @author Siger
 */
export function ArtworkDeleteConfirmation({ artwork, open, handleClose }) {
  const [errorMessage, setErrorMessage] = useState("");

  // Function which is called when we submit the form to delete the artwork
  const handleSubmit = async (event) => {
    event.preventDefault();
    axios
      .delete(`/api/artwork/${artwork.artworkId}`)
      .then((response) => {
        if (response.status === 200) {
          setErrorMessage("");
          handleClose();
          window.location = "/browse/room/" + artwork.room.roomId;
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
        Are you sure you want to delete the artwork {artwork.name}?
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
