import React, { useState, useEffect } from "react";
import axios from "axios";
import { Typography, TextField, Button, Box, Paper } from "@mui/material";
import "./Login.css";

/**
 * Edit Artwork Info
 * @returns A form to edit the artwork's info
 * @author kieyanmamiche
 */

export function EditArtworkInfo() {
  const [artworkId, setArtworkId] = useState(null);
  const [artworkName, setArtworkName] = useState(null);
  const [artist, setArtist] = useState(null);
  const [image, setImage] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  /**
   * To set EditArtworkInfo parameters to null when text input is just whitespace
   * @author kieyanmamiche
   */
  useEffect(() => {
    if (artworkId != null && artworkId.trim().length === 0) {
      setArtworkId(null);
    }

    if (artworkName != null && artworkName.trim().length === 0) {
      setArtworkName(null);
    }

    if (artist != null && artist.trim().length === 0) {
      setArtist(null);
    }

    if (image != null && image.trim().length === 0) {
      setImage(null);
    }
  }, [artworkId, artworkName, artist, image]);

  /**
   * Function which is called when we submit the form
   * @author kieyanmamiche
   */
  const handleSubmit = async (event) => {
    event.preventDefault();

    if (artworkId == null) {
      setErrorMessage("No artwork id entered");
      setIsFormInvalid(true);
    } else {
      axios
        .put(`/api/artwork/info/${artworkId}`, {
          name: artworkName,
          artist: artist,
          image: image,
        })
        .then(function (response) {
          if (response.status === 200) {
            setErrorMessage(null);
            setIsFormInvalid(false);

            if (artworkName !== null) {
              setArtworkName(artworkName);
              localStorage.setItem("artworkName", artworkName);
            }

            if (artist !== null) {
              setArtist(artist);
              localStorage.setItem("artist", artist);
            }

            if (image !== null) {
              setImage(image);
              localStorage.setItem("image", image);
            }
          }
        })
        .catch(function (error) {
          setErrorMessage(error.response.data);
          setIsFormInvalid(true);
        });
    }
  };

  /**
   * The component that we return which represents the EditArtworkInfo Form
   * @author kieyanmamiche
   */
  return (
    <>
      <div className="EditArtworkInfo" style={{ marginTop: "3%" }}>
        <Typography style={{ fontSize: "36px" }} gutterBottom>
          Edit Artwork Info
        </Typography>

        <div
          style={{
            display: "flex",
            justifyContent: "space-evenly",
            alignContent: "center",
            marginTop: "3%",
          }}
        >
          <Paper
            elevation={3}
            style={{
              width: "60%",
              padding: "50px 50px",
            }}
          >
            <form
              style={{ width: "100%", alignContent: "flex-end" }}
              onSubmit={handleSubmit}
            >
              <Box
                sx={{
                  marginTop: 3,
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                }}
              >
                <TextField
                  margin="normal"
                  id="artworkId"
                  label="ArtworkId"
                  name="artworkId"
                  autoComplete="artworkId"
                  autoFocus
                  onChange={(e) => setArtworkId(e.target.value)}
                  defaultValue={artworkId}
                  className="login-field"
                />
                <TextField
                  margin="normal"
                  id="artworkName"
                  label="Artwork Name"
                  name="Artwork Name"
                  autoComplete="Artwork Name"
                  autoFocus
                  onChange={(e) => setArtworkName(e.target.value)}
                  defaultValue={artworkName}
                  className="login-field"
                />
                <TextField
                  margin="normal"
                  id="artist"
                  label="Artist"
                  name="artist"
                  autoComplete="artist"
                  autoFocus
                  onChange={(e) => setArtist(e.target.value)}
                  defaultValue={artist}
                  className="login-field"
                />
                <TextField
                  margin="normal"
                  id="image"
                  label="Image"
                  name="image"
                  autoComplete="image"
                  autoFocus
                  onChange={(e) => setImage(e.target.value)}
                  className="login-field"
                />
                <div
                  style={{
                    height: 20,
                  }}
                >
                  {" "}
                </div>
                <Typography variant="p" component="p" color="red">
                  {isFormInvalid && errorMessage}
                </Typography>
              </Box>
              <Button
                type="submit"
                variant="contained"
                sx={{ mt: 3, mb: 2, width: "40%" }}
              >
                Edit Artwork Info
              </Button>
            </form>
          </Paper>
        </div>
      </div>
    </>
  );
}
