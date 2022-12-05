import React from "react";
import axios from "axios";
import { Dialog, DialogTitle, DialogActions, Button } from "@mui/material";

/**
 * Function for artwork delete confirmation dialog
 *
 * @returns  A dialog to confirm the deletion of an artwork
 * @author Siger
 */
export function ArtworkDeleteConfirmation({ artwork, open, handleClose }) {
  // Function which is called when we submit the form to delete the artwork
  const handleSubmit = async (event) => {
    event.preventDefault();
    axios
      .delete(`/api/artwork/${artwork.artworkId}`)
      .then((response) => {
        if (response.status === 200) {
          handleClose();
          window.location = "/browse/room/" + artwork.room.roomId;
        } else {
          console.log("Something went wrong");
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
      <DialogTitle>
        Are you sure you want to delete the artwork {artwork.name}?
      </DialogTitle>

      <DialogActions>
        <Button onClick={handleClose}>Cancel</Button>
        <Button onClick={handleSubmit} color="error">
          Delete
        </Button>
      </DialogActions>
    </Dialog>
  );
}
