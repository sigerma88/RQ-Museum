import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  Box,
  TextField,
  FormControl,
  InputLabel,
  Select,
  Typography,
  MenuItem,
  Grid,
} from "@mui/material";
import LoadingButton from "@mui/lab/LoadingButton";
import { isCurrency } from "./EditArtworkLoanInfo";
import { isValidImageURL } from "./ArtworkImageChanging";

/**
 * Function which gets all the rooms and their information
 *
 * @returns  A list of all the rooms and their information
 * @author kieyanmamiche
 * @author Siger
 */
function getRooms() {
  return axios.get("/api/room").then((response) => {
    if (response.status === 200) {
      return response.data;
    } else {
      return null;
    }
  });
}

/**
 * Function for the dialog to create an artwork
 *
 * @returns  A form to create an artwork
 * @author Siger
 */
export function ArtworkCreation({ open, handleClose, room }) {
  const [name, setName] = useState("");
  const [artist, setArtist] = useState("");
  const [isAvailableForLoan, setIsAvailableForLoan] = useState(false);
  const [loanFee, setLoanFee] = useState("");
  const [image, setImage] = useState("");
  const isOnLoan = false;
  const [roomId, setRoomId] = useState(room.roomId);
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  // To get the room data on the pages initialization
  useEffect(() => {
    getRooms().then((data) => {
      setRooms(data);
    });
  }, []);

  // Function which is called when we submit the form to create the artwork
  const handleSubmit = async (event) => {
    event.preventDefault();

    // Check if image is a valid image URL and if loan fee is a valid currency
    if (!isValidImageURL(document.getElementById("imagePreview"))) {
      setErrorMessage("Image must be a valid image URL");
      setIsFormInvalid(true);
      setLoading(false);
    } else if (!isCurrency(loanFee)) {
      setErrorMessage("Loan fee must be in currency format");
      setIsFormInvalid(true);
      setLoading(false);
    } else {
      axios
        .post("/api/artwork", {
          name: name,
          artist: artist,
          isAvailableForLoan: isAvailableForLoan,
          loanFee: loanFee,
          image: image,
          isOnLoan: isOnLoan,
          roomId: roomId,
        })
        .then((response) => {
          if (response.status === 200) {
            setErrorMessage(null);
            setIsFormInvalid(false);
            handleClose();
            setLoading(false);
            window.location = "/browse/artwork/" + response.data.artworkId;
          } else {
            setErrorMessage("Something went wrong");
            setIsFormInvalid(true);
            setLoading(false);
          }
        })
        .catch((error) => {
          console.log(error);
          setErrorMessage(error.response.data);
          setIsFormInvalid(true);
          setLoading(false);
        });
    }
  };

  return (
    <>
      <Dialog open={open} onClose={handleClose} maxWidth="xl" fullWidth>
        <DialogTitle>Create Artwork</DialogTitle>
        <DialogContent>
          <Grid container spacing={2}>
            <Grid item xs={4}>
              <form
                onSubmit={(event) => {
                  setLoading(true);
                  handleSubmit(event);
                }}
              >
                <TextField
                  id="name"
                  label="Name"
                  margin="normal"
                  autoFocus
                  sx={{ width: "80%" }}
                  value={name}
                  onChange={(event) => {
                    setName(event.target.value);
                  }}
                  error={isFormInvalid}
                />
                <TextField
                  id="artist"
                  label="Artist"
                  margin="normal"
                  autoFocus
                  sx={{ width: "80%" }}
                  value={artist}
                  onChange={(event) => {
                    setArtist(event.target.value);
                  }}
                  error={isFormInvalid}
                />
                <FormControl sx={{ width: "80%", marginTop: 3 }}>
                  <InputLabel>Available for loan?</InputLabel>
                  <Select
                    labelId="isAvailableForLoan"
                    id="isAvailableForLoan"
                    label="Available for loan?"
                    value={isAvailableForLoan}
                    onChange={(e) => setIsAvailableForLoan(e.target.value)}
                    error={isFormInvalid}
                  >
                    <MenuItem value={"true"}>Available for loan</MenuItem>
                    <MenuItem value={"false"}>Not available for loan</MenuItem>
                  </Select>
                </FormControl>
                <TextField
                  id="loanFee"
                  label="Loan Fee"
                  margin="normal"
                  autoFocus
                  sx={{ width: "80%" }}
                  value={loanFee}
                  onChange={(event) => {
                    setLoanFee(event.target.value);
                  }}
                  error={isFormInvalid}
                />
                <TextField
                  id="image"
                  label="Image"
                  margin="normal"
                  autoFocus
                  sx={{ width: "80%" }}
                  value={image}
                  onChange={(event) => {
                    setImage(event.target.value);
                  }}
                  error={isFormInvalid}
                />
                <FormControl sx={{ width: "80%", marginTop: 3 }}>
                  <InputLabel>Select room</InputLabel>
                  <Select
                    labelId="rooms"
                    id="rooms"
                    label="Select room"
                    value={roomId}
                    onChange={(e) => setRoomId(e.target.value)}
                    error={isFormInvalid}
                  >
                    {rooms.map((room) => (
                      <MenuItem
                        key={room.roomId}
                        value={String(room["roomId"])}
                      >
                        {room["roomName"]}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <Typography variant="p" component="p" color="red">
                  {isFormInvalid && errorMessage}
                </Typography>
                <LoadingButton
                  variant="contained"
                  loading={loading}
                  sx={{ mt: 3, mb: 2, width: "20%" }}
                  type="submit"
                >
                  Submit
                </LoadingButton>
              </form>
            </Grid>
            <Grid item xs={8}>
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  height: "100%",
                }}
              >
                <img src={image} alt="" width="100%" id="imagePreview" />
              </Box>
            </Grid>
          </Grid>
        </DialogContent>
      </Dialog>
    </>
  );
}
