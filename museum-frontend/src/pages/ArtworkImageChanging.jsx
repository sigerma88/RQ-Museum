import React, { useState } from "react";
import axios from "axios";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Box,
  Typography,
} from "@mui/material";
import LoadingButton from "@mui/lab/LoadingButton";

/**
 * Function to check if the input is a valid image URL
 *
 * @param value - The value to check
 * @returns True if the value is a valid image URL, false otherwise
 * @author Siger
 */
export function isValidImageURL(value) {
  return /^(https?:\/\/.*\.(?:png|jpg|jpeg))$/.test(value);
}

/**
 * Function to change the image of the artwork
 *
 * @param artwork - The artwork to edit
 * @param setArtwork - The function to set the artwork
 * @returns A form to edit the artwork's image
 * @author Siger
 */
export function ArtworkImageChanging({
  artwork,
  setArtwork,
  open,
  handleClose,
}) {
  const artworkId = artwork.artworkId;
  const [image, setImage] = useState(artwork.image);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  const imageHeight = window.innerHeight * 0.5;

  // Function which is called when we submit the form
  const handleSubmit = async (event) => {
    event.preventDefault();

    // Check if image is a valid image URL
    if (!isValidImageURL(image)) {
      setErrorMessage("Image must be a valid image URL");
      setIsFormInvalid(true);
    } else {
      await axios
        .put(`/api/artwork/info/${artworkId}`, {
          image: image,
        })
        .then(function (response) {
          if (response.status === 200) {
            setErrorMessage(null);
            setIsFormInvalid(false);
            artwork.image = image;
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
    }

    await setLoading(false);
  };

  return (
    <>
      <Dialog open={open} onClose={handleClose} maxWidth="lg" fullWidth>
        <DialogTitle>Change Artwork Image</DialogTitle>

        <DialogContent>
          <form
            onSubmit={(event) => {
              setLoading(true);
              handleSubmit(event);
            }}
          >
            <TextField
              label="Image URL"
              variant="standard"
              value={image}
              onChange={(e) => setImage(e.target.value)}
              fullWidth
              error={isFormInvalid}
              helperText={errorMessage}
            />

            <Typography
              variant="h6"
              sx={{
                display: "flex",
                justifyContent: "center",
                alignContent: "center",
                marginTop: "1rem",
              }}
            >
              Preview:
            </Typography>
            <Box
              sx={{
                display: "flex",
                justifyContent: "center",
                alignContent: "center",
                marginTop: "1rem",
              }}
            >
              <img src={image} alt="Artwork" height={imageHeight} />
            </Box>

            <DialogActions>
              <LoadingButton
                variant="contained"
                loading={loading}
                loadingPosition="end"
                sx={{ mt: 3, mb: 2, width: "20%" }}
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
