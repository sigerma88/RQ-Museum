import axios from "axios";
import React, { useEffect } from "react";
import {
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Grid,
  Typography,
} from "@mui/material";
import { fontStyle } from "@mui/system";

export function Rooms() {
  const [rooms, setRooms] = React.useState([]);
  useEffect(() => {
    axios
      .get("/api/room")
      .then((response) => {
        console.log(response.data);
        const rooms = response.data;
        rooms.push({ roomId: "all", roomName: "All Rooms" });
        setRooms(rooms);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  return (
    <div style={{ margin: "50px 50px" }}>
      <Grid container spacing={4}>
        {rooms.map((room) => (
          <Grid item key={room.roomId} xs={12} sm={6} md={4}>
            <Card style={{ position: "relative" }} className="card">
              <CardContent>
                <a
                  href={`/browse/room/${room.roomId}`}
                  style={{ textDecoration: "none", color: "black" }}
                >
                  <Typography style={{ fontWeight: "bold" }}>
                    {" "}
                    {room.roomName}
                  </Typography>
                </a>
                <Typography>{room.currentNumberOfArtwork} artworks</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </div>
  );
}
