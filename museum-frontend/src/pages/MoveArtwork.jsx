import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Typography,
  Box,
  FormControl,
  Select,
  InputLabel,
  MenuItem,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import LoadingButton from "@mui/lab/LoadingButton";

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
 * Function for the dialog to move an artwork
 *
 * @returns  A form to move the artwork's to a different room
 * @author kieyanmamiche
 * @author Siger
 */

export function MoveArtwork({ open, handleClose, artwork, setArtwork }) {
  const artworkId = artwork.artworkId;
  const [roomId, setRoomId] = useState(artwork.room.roomId);
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

  // Function which is called when we submit the form to move the artwork
  const handleSubmit = async (event) => {
    event.preventDefault();

    axios
      .post(`/api/artwork//moveArtworkToRoom/${artworkId}/${roomId}`, null)
      .then((response) => {
        if (response.status === 200) {
          setErrorMessage(null);
          setIsFormInvalid(false);
          artwork.room.roomId = roomId;
          setArtwork(artwork);
          setLoading(false);
          handleClose();
        } else {
          setLoading(false);
          setErrorMessage("Something went wrong");
          setIsFormInvalid(true);
        }
      })
      .catch((error) => {
        console.log(error);
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
        setLoading(false);
      });
  };

  return (
    <>
      <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
        <DialogTitle>Move the artwork</DialogTitle>

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
              <FormControl sx={{ width: "90%" }}>
                <InputLabel id="demo-simple-select-label">
                  Select room
                </InputLabel>
                <Select
                  labelId="rooms"
                  id="rooms"
                  label="Select room"
                  value={roomId}
                  onChange={(e) => setRoomId(e.target.value)}
                >
                  {rooms.map((room) => (
                    <MenuItem key={room.roomId} value={String(room["roomId"])}>
                      {room["roomName"]}
                    </MenuItem>
                  ))}
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
                sx={{ mt: 3, mb: 2, width: "50%" }}
                type="submit"
              >
                Move artwork
              </LoadingButton>
            </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </>
  );
}
