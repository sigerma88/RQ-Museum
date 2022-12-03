import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Typography,
  TextField,
  Button,
  Box,
  Paper,
  FormControl,
  InputLabel,
  Select,
  MenuItem
} from "@mui/material";
import "./Common.css";

/**
 * Edit room
 * @returns  Edit room page
 * @author kieyanmamiche
 */

export function EditRoom() {

  const [roomId, setRoomId] = useState(null);
  const [roomName, setRoomName] = useState(null);
  const [roomType, setRoomType] = useState(null);
  const [museumId, setMuseumId] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

 /**
  * Set room to null when text input is just whitespace
  * @author kieyanmamiche
  */
  useEffect(() => {
      if (roomId != null && roomId.trim().length === 0) {
        setRoomId(null);
      }

      if (roomName != null && roomName.trim().length === 0) {
        setRoomName(null);
      }

      if (roomType != null && roomType.trim().length === 0) {
        setRoomType(null);
      }

      if (museumId != null && museumId.trim().length === 0) {
        setMuseumId(null);
      }
  }, [roomId, roomName, roomType, museumId]);

 /**
  * Function which is called when we submit the form
  * @author kieyanmamiche
  */
  const handleSubmit = async (event) => {

    event.preventDefault();

    if (roomId == null){
        setErrorMessage("No room id entered");
        setIsFormInvalid(true);
    }else{
            axios
              .put(`/api/room/${roomId}`, {
                roomName: roomName,
                roomType: roomType,
                museumId: museumId,
              })
              .then(function (response) {
                if (response.status === 200) {

                  setErrorMessage(null);
                  setIsFormInvalid(false);

                  if (roomName !== null) {
                    setRoomName(roomName);
                    localStorage.setItem("roomName", roomName);
                  }

                  if (roomType !== null) {
                    setRoomType(roomType);
                    localStorage.setItem("roomType", roomType);
                  }

                  if (museumId !== null) {
                    setMuseumId(museumId);
                    localStorage.setItem("museumId", museumId);
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
  * The component that we return which represents the EditRoom Form
  * @author kieyanmamiche
  */
    return (<>
        <div className="EditRoom" style={{ marginTop: "3%" }}>
        <Typography style={{ fontSize: "36px" }} gutterBottom>
          Edit Room
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
                  id="roomId"
                  label="RoomId"
                  name="roomId"
                  autoComplete="roomId"
                  autoFocus
                  onChange={(e) => setRoomId(e.target.value)}
                  defaultValue={roomId}
                  className = "text-field"
                />
                <TextField
                  margin="normal"
                  id="roomName"
                  label="Room Name"
                  name="room name"
                  autoComplete="room name"
                  autoFocus
                  onChange={(e) => setRoomName(e.target.value)}
                  defaultValue={roomName}
                  className = "text-field"
                />
                <FormControl className = "text-field">
                  <InputLabel id="demo-simple-select-label">Room Type</InputLabel>
                  <Select
                    labelId="roomType"
                    id="roomType"
                    label="Room Type"
                    onChange={(e) => setRoomType(e.target.value)}
                  >
                    <MenuItem value={"0"}>Small</MenuItem>
                    <MenuItem value={"1"}>Large</MenuItem>
                    <MenuItem value={"2"}>Storage</MenuItem>
                  </Select>
                </FormControl>
                <TextField
                  margin="normal"
                  id="museumId"
                  label="Museum Id"
                  name="museum id"
                  autoComplete="museum id"
                  autoFocus
                  onChange={(e) => setMuseumId(e.target.value)}
                  className = "text-field"
                />
                <div
                    style={{
                    height: 20
                    }}
                > </div>
                <Typography variant="p" component="p" color="red">
                  {isFormInvalid && errorMessage}
                </Typography>

              </Box>
              <Button
                type="submit"
                variant="contained"
                sx={{ mt: 3, mb: 2, width: "40%" }}
              >
                Edit Room
              </Button>
            </form>
          </Paper>
        </div>
      </div>
    </>);
}


