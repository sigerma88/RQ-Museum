import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Typography,
  Button,
  Box,
  Paper,
  CircularProgress,
  FormControl,
  Select,
  InputLabel,
  TextField,
  MenuItem,
} from "@mui/material";
import "./Common.css";

/**
 * Move Artwork
 * @returns  A form to move the artwork's to a different room
 * @author kieyanmamiche
 */

export function MoveArtwork() {
  const [hasLoaded, setHasLoaded] = useState(false);
  const [artworkId, setArtworkId] = useState(null);
  const [roomId, setRoomId] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);
  const [rooms, setRooms] = useState([]);

  /**
   * To set the fields to move the artwork to null when SELECT is empty
   * @author kieyanmamiche
   */
  useEffect(() => {
    if (roomId != null && roomId.trim().length === 0) {
      setRoomId(null);
    }

    if (artworkId != null && artworkId.trim().length === 0) {
      setArtworkId(null);
    }
  }, [artworkId, roomId]);

  /**
   * To get the room data on the pages initialization
   * @author kieyanmamiche
   */
  useEffect(() => {
    getRoomData();
  }, []);

  /**
   * Function which gets all the rooms
   * @author kieyanmamiche
   */
  const getRoomData = async (event) => {
    axios
      .get(`/api/room/`)
      .then(function (response) {
        if (response.status === 200) {
          setErrorMessage(null);
          setIsFormInvalid(false);

          console.log(response.data);
          setRooms(response.data);
          setHasLoaded(true);
        }
      })
      .catch(function (error) {
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
      });
  };

  /**
   * Function which is called when we submit the form to move the artwork
   * @author kieyanmamiche
   */
  const handleSubmit = async (event) => {
    event.preventDefault();

    if (artworkId == null) {
      setErrorMessage("No artwork id entered");
      setIsFormInvalid(true);
    } else {
      axios
        .post(`/api/artwork//moveArtworkToRoom/${artworkId}/${roomId}`, null)
        .then(function (response) {
          if (response.status === 200) {
            setErrorMessage(null);
            setIsFormInvalid(false);

            if (artworkId !== null) {
              setArtworkId(artworkId);
              localStorage.setItem("artworkId", artworkId);
            }

            if (roomId !== null) {
              setRoomId(roomId);
              localStorage.setItem("roomId", roomId);
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
   * The component that we return which represents the MoveArtwork Form
   * @author kieyanmamiche
   */

  return (
    <>
      <div className="Move Artwork" style={{ marginTop: "3%" }}>
        <Typography style={{ fontSize: "36px" }} gutterBottom>
          Move the artwork
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
                {hasLoaded ? (
                  <FormControl className="login-field">
                    <InputLabel id="demo-simple-select-label">
                      Select room
                    </InputLabel>
                    <Select
                      labelId="rooms"
                      id="rooms"
                      label="Select room"
                      onChange={(e) => setRoomId(e.target.value)}
                    >
                      {rooms.map((room) => (
                        <MenuItem value={String(room["roomId"])}>
                          {room["roomName"]}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                ) : (
                  <CircularProgress />
                )}
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
              {hasLoaded ? (
                <Button
                  type="submit"
                  variant="contained"
                  sx={{ mt: 3, mb: 2, width: "40%" }}
                >
                  Move Artwork
                </Button>
              ) : null}
            </form>
          </Paper>
        </div>
      </div>
    </>
  );
}
