import React, { useState, useContext, useEffect } from "react";
import {
  Typography,
  Button,
  Card,
  CardContent,
  Stack,
  CardMedia,
} from "@mui/material";
import "./Home.css";
import { LoginContext } from "../Contexts/LoginContext";
import { Link } from "react-router-dom";
import MuseumPhotoBackground from "../assets/MuseumPhotoBackground.jpg";
import KeyboardArrowRightIcon from "@mui/icons-material/KeyboardArrowRight";
import { Carousel } from "react-bootstrap";
import HomePainting from "../assets/HomePainting.jpg";
import RoomImage from "../assets/RoomImage.jpg";
import axios from "axios";

function RoomCard({ room }) {
  return (
    <a href={`/browse/room/${room.roomId}`}>
      <Card className="carousel-card">
        <CardMedia
          component="img"
          height="100%"
          image={RoomImage}
          style={{ objectFit: "cover", filter: "brightness(0.4)" }}
          alt="Paella dish"
        />
        <CardContent
          className="room-content"
          style={{
            position: "absolute",
            top: "40%",
            width: "100%",
            textAlign: "center",
            color: "white",
          }}
        >
          <Typography
            variant="h5"
            component="div"
            style={{ fontWeight: 500, fontSize: "32px" }}
          >
            {room.roomName}
          </Typography>
        </CardContent>
      </Card>
    </a>
  );
}

function RoomCarousel() {
  const [rooms, setRooms] = useState([]);
  useEffect(() => {
    axios
      .get(`/api/room`)
      .then((response) => {
        setRooms(response.data);
      })
      .catch((error) => {
        console.log(error.response.data);
      });
  }, []);

  return (
    <Carousel variant="dark" className="carousel">
      <Carousel.Item className="carousel-item">
        <Stack direction="row" justifyContent="center" spacing={2}>
          {rooms.slice(0, rooms.length / 2).map((room) => (
            <RoomCard key={room.roomId} room={room} />
          ))}
        </Stack>
      </Carousel.Item>
      <Carousel.Item className="carousel-item">
        <Stack direction="row" justifyContent="center" spacing={2}>
          {rooms.slice(rooms.length / 2, rooms.length).map((room) => (
            <RoomCard key={room.roomId} room={room} />
          ))}
        </Stack>
      </Carousel.Item>
    </Carousel>
  );
}

/**
 * General page when user goes to the website
 * @returns Home page
 * @author Kevin
 */

function LandingPage() {
  const backgroundImageHeight = window.innerHeight * 0.85;
  return (
    <div className="Home">
      <div className="lander">
        <img
          style={{
            width: "100%",
            height: backgroundImageHeight,
            filter: "brightness(0.5)",
            objectFit: "cover",
          }}
          className="feature-image"
          src={MuseumPhotoBackground}
          alt="feature"
        />
        <div className="centered">
          <Typography
            variant="h4"
            component="h1"
            noWrap
            style={{
              color: "white",
              fontWeight: 500,
              letterSpacing: "1px",
              fontSize: "3rem",
            }}
          >
            Welcome to Rougon-Macquart Museum
          </Typography>
        </div>
        <div style={{ padding: "50px 0px" }}>
          <Typography variant="h3">Explore our rooms</Typography>
          <RoomCarousel />
        </div>
      </div>
    </div>
  );
}

/**
 * Visitor home page when logged in as a visitor
 * @returns Visitor Home page
 * @author Kevin
 */

function VisitorHomePage({ isLoggedIn }) {
  return (
    <>
      <LandingPage />
      <div style={{ paddingBottom: "110px" }}>
        <Typography variant="h3" style={{ paddingBottom: "20px" }}>
          Buy your tickets
        </Typography>
        <Button href="/ticket" variant="outlined">
          Book your ticket <KeyboardArrowRightIcon />
        </Button>
      </div>
      {isLoggedIn ? (
        <div style={{ paddingBottom: "110px" }}>
          <Typography variant="h3" style={{ paddingBottom: "20px" }}>
            See your loans
          </Typography>
          <Button href="/loan" variant="outlined">
            View your loans <KeyboardArrowRightIcon />
          </Button>
        </div>
      ) : (
        ""
      )}
      <img
        src={HomePainting}
        style={{ borderRadius: "10px", height: "55%", width: "55%" }}
      />
    </>
  );
}

/**
 * Manager home page when logged in as a manager
 * @returns Manager Home page
 * @author Kevin
 */

function ManagerHomePage() {
  return (
    <>
      {LandingPage()}
      <Typography variant="h3" style={{ paddingBottom: "20px" }}>
        Manage your employees
      </Typography>
      <Link to="/employee">View all employees</Link>
    </>
  );
}

/**
 * Employee home page when logged in as a employee
 * @returns Employee Home page
 * @author Kevin
 */

function EmployeeHomePage() {
  return (
    <>
      {LandingPage()}
      <a href="/employee/schedule/">View your schedule</a>
    </>
  );
}

/**
 * Home page where we insert the different home pages depending on the user role
 * @returns Home page
 * @author Kevin
 */

function Home() {
  const { loggedIn, userRole } = useContext(LoginContext);

  if (userRole === "manager" && loggedIn) {
    return <ManagerHomePage />;
  } else if (userRole === "employee" && loggedIn) {
    return <EmployeeHomePage />;
  } else {
    return <VisitorHomePage isLoggedIn={loggedIn} />;
  }
}

export default Home;
