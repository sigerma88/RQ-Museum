import React, { useContext } from "react";
import Typography from "@mui/material/Typography";
import "./Home.css";
import { LoginContext } from "../Contexts/LoginContext";
import { Link } from "react-router-dom";
import MuseumPhotoBackground from "../assets/MuseumPhotoBackground.jpg";

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
            width: "85%",
            height: backgroundImageHeight,
            marginTop: "20px",
            borderRadius: "15px",
            filter: "brightness(0.6)",
          }}
          className="feature-image"
          src={MuseumPhotoBackground}
          alt="feature"
        />
        <div className="centered">
          <Typography
            variant="h4"
            noWrap
            component="h1"
            style={{
              color: "white",
              fontFamily: "system-ui, sans-serif",
              fontWeight: 600,
            }}
          >
            Welcome to Rougon-Macquart Museum
          </Typography>
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

function VisitorHomePage() {
  return (
    <>
      {LandingPage()}
      <p>Visitor</p>
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
      <Link to="/employee/schedule/">View your schedule</Link>
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
  if (userRole === "visitor" && loggedIn) {
    return <VisitorHomePage />;
  } else if (userRole === "manager" && loggedIn) {
    return <ManagerHomePage />;
  } else if (userRole === "employee" && loggedIn) {
    return <EmployeeHomePage />;
  } else {
    return <LandingPage />;
  }
}

export default Home;
