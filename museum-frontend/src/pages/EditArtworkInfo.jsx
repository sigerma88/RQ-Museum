import React, { useState } from "react";
import axios from "axios";
import {
  Typography,
  TextField,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import LoadingButton from "@mui/lab/LoadingButton";

/**
 * Function for the dialog to edit an artwork's info
 *
 * @returns A form to edit the artwork's info
 * @author kieyanmamiche
 * @author Siger
 */

export function EditArtworkInfo({ open, handleClose, artwork, setArtwork }) {
  const artworkId = artwork.artworkId;
  const [artworkName, setArtworkName] = useState(artwork.name);
  const [artworkArtist, setArtworkArtist] = useState(artwork.artist);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  // Function which is called when we submit the form
  const handleSubmit = async (event) => {
    event.preventDefault();
    await axios
      .put(`/api/artwork/info/${artworkId}`, {
        name: artworkName,
        artist: artworkArtist,
      })
      .then(function (response) {
        if (response.status === 200) {
          setErrorMessage(null);
          setIsFormInvalid(false);
          artwork.name = artworkName;
          artwork.artist = artworkArtist;
          setArtwork(artwork);
          handleClose();
        } else {
          setErrorMessage("Something went wrong");
          setIsFormInvalid(true);
        }
      })
      .catch(function (error) {
        console.log(error);
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
      });

    await setLoading(false);
  };

  return (
    <>
      <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
        <DialogTitle>Edit Artwork Information</DialogTitle>

        <DialogContent
          style={{
            display: "flex",
            justifyContent: "space-evenly",
            alignContent: "center",
            marginTop: "3%",
          }}
        >
          <form
            style={{ width: "100%", alignContent: "flex-end" }}
            onSubmit={(event) => {
              setLoading(true);
              handleSubmit(event);
            }}
          >
            <Box
              sx={{
                marginTop: 3,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
              }}
            >
              <Typography>Artwork ID: {artworkId}</Typography>
              <TextField
                id="artworkName"
                label="Artwork Name"
                margin="normal"
                autoFocus
                sx={{ width: "60%" }}
                value={artworkName}
                onChange={(e) => setArtworkName(e.target.value)}
                error={isFormInvalid}
              />
              <TextField
                id="artist"
                label="Artist"
                margin="normal"
                autoFocus
                sx={{ width: "60%" }}
                value={artworkArtist}
                onChange={(e) => setArtworkArtist(e.target.value)}
                error={isFormInvalid}
              />
              <Typography variant="p" component="p" color="red">
                {isFormInvalid && errorMessage}
              </Typography>
            </Box>

            <DialogActions>
              <LoadingButton
                variant="contained"
                loading={loading}
                loadingPosition="end"
                sx={{ mt: 3, mb: 2, width: "50%" }}
                type="submit"
              >
                Submit
              </LoadingButton>
            </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </>
  );
}
