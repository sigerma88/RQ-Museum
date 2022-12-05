import React, { useState } from "react";
import axios from "axios";
import {
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  TextField,
  FormControl,
  InputLabel,
  Select,
  Typography,
  MenuItem,
  DialogActions,
} from "@mui/material";
import LoadingButton from "@mui/lab/LoadingButton";

/**
 * Function for the dialog to create a room
 *
 * @returns  A form to create a room
 * @author Siger
 */
export function RoomCreation({ open, handleClose, museum }) {
  const [roomName, setRoomName] = useState("");
  const [roomType, setRoomType] = useState("");
  const currentNumberOfArtwork = 0;
  const museumId = museum.museumId;
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  // Function which is called when we submit the form to create the room
  const handleSubmit = async (event) => {
    event.preventDefault();

    if (roomName.trim() === "" || roomType.trim() === "") {
      setErrorMessage("Please fill in all the fields");
      setIsFormInvalid(true);
      setLoading(false);
    } else {
      axios
        .post("/api/room", {
          roomName: roomName,
          roomType: roomType,
          currentNumberOfArtwork: currentNumberOfArtwork,
          museumId: museumId,
        })
        .then((response) => {
          if (response.status === 200) {
            setErrorMessage(null);
            setIsFormInvalid(false);
            handleClose();
            setLoading(false);
            window.location = "/browse/room/" + response.data.roomId;
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
      <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
        <DialogTitle>Create Room</DialogTitle>

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
              <Typography>Museum: {museum.name}</Typography>
              <TextField
                id="roomName"
                label="Room Name"
                name="roomName"
                margin="normal"
                autoFocus
                sx={{ width: "60%", marginBottom: 3, marginTop: 3 }}
                value={roomName}
                onChange={(e) => setRoomName(e.target.value)}
                error={isFormInvalid}
              />
              <FormControl sx={{ width: "60%" }}>
                <InputLabel>Room Type</InputLabel>
                <Select
                  labelId="roomType"
                  id="roomType"
                  label="Room Type"
                  value={roomType}
                  onChange={(e) => setRoomType(e.target.value)}
                  error={isFormInvalid}
                >
                  <MenuItem value={"Small"}>Small</MenuItem>
                  <MenuItem value={"Large"}>Large</MenuItem>
                  <MenuItem value={"Storage"}>Storage</MenuItem>
                </Select>
              </FormControl>
              <Typography variant="p" component="p" color="red">
                {isFormInvalid && errorMessage}
              </Typography>
            </Box>

            <DialogActions>
              <LoadingButton
                variant="contained"
                loading={loading}
                loadingPosition="end"
                sx={{ mt: 3, mb: 2, width: "30%" }}
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
