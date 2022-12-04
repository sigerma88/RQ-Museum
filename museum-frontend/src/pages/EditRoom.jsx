import React, { useState } from "react";
import axios from "axios";
import {
  Typography,
  TextField,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import LoadingButton from "@mui/lab/LoadingButton";
import "./Login.css";

/**
 * Dialog with the form to edit a room
 *
 * @returns  Edit room form
 * @author kieyanmamiche
 * @author Siger
 */

export function EditRoom({ open, handleClose, room, setRoom }) {
  const roomId = room.roomId;
  const museumName = room.museum.name;
  const [roomName, setRoomName] = useState(room.roomName);
  const [roomType, setRoomType] = useState(room.roomType);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);
  const [loading, setLoading] = useState(false);

  // Function which is called when we submit the form
  const handleSubmit = async (event) => {
    event.preventDefault();

    axios
      .put(`/api/room/${roomId}`, {
        roomName: roomName,
        roomType: roomType,
      })
      .then((response) => {
        if (response.status === 200) {
          setErrorMessage(null);
          setIsFormInvalid(false);
          room.roomName = roomName;
          room.roomType = roomType;
          setRoom(room);
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
      <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
        <DialogTitle>Edit Room</DialogTitle>

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
              <Typography>Room ID: {roomId}</Typography>
              <Typography>Museum: {museumName}</Typography>
              <TextField
                id="roomName"
                label="Room Name"
                name="roomName"
                margin="normal"
                autoFocus
                className="login-field"
                value={roomName}
                onChange={(e) => setRoomName(e.target.value)}
                error={isFormInvalid}
              />
              <FormControl className="login-field">
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
                Edit Room
              </LoadingButton>
            </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </>
  );
}
