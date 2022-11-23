import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { LoginContext } from "../../Contexts/LoginContext";
import { Navigation } from "../../layouts/Navigation";
import Home from "../Home";

// Function to get the artworks from the server
function getArtworks(roomId) {
  return axios
    .get(`/api/artwork/${roomId === "all" ? "" : `room/${roomId}`}`)
    .then((response) => {
      console.log(response.data);
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
}

// Function to get the room from the server
function getRoom(roomId) {
  if (roomId === "all") {
    return Promise.resolve({ roomName: "All Rooms" });
  } else {
    return axios
      .get(`/api/room/${roomId}`)
      .then((response) => {
        return response.data;
      })
      .catch((error) => {
        console.log(error);
      });
  }
}

// VisitorArtworkBrowsing component
function VisitorArtworkBrowsing({ artworks, room }) {
  return <p>Visitor browsing room {room.roomName}</p>;
}

function ManagerArtworkBrowsing() {
  return <p>Admin</p>;
}

function EmployeeArtworkBrowsing() {
  return <p>Employee</p>;
}

// Main function
function ArtworkBrowsing() {
  // Parse the roomId from the URL
  let { roomId } = useParams();

  // Get the artworks from the server
  const [artworks, setArtworks] = useState([]);
  useEffect(() => {
    getArtworks(roomId).then((artworks) => {
      setArtworks(artworks);
    });
  }, []);

  // Get the room from the server
  const [room, setRoom] = useState({});
  useEffect(() => {
    getRoom(roomId).then((room) => {
      setRoom(room);
    });
  }, []);

  const { loggedIn, userRole } = useContext(LoginContext);
  if (userRole === "visitor" && loggedIn) {
    return <VisitorArtworkBrowsing artworks={artworks} room={room} />;
  } else if (userRole === "manager" && loggedIn) {
    return <ManagerArtworkBrowsing />;
  } else if (userRole === "employee" && loggedIn) {
    return <EmployeeArtworkBrowsing />;
  } else {
    return <Home />;
  }
}

export default ArtworkBrowsing;
