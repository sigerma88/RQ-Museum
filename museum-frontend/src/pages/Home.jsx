import React, { useEffect, useState, useContext } from "react";
import Typography from "@mui/material/Typography";
import "./Home.css";
import { LoginContext } from "../Contexts/LoginContext";
import { Navigation } from "../layouts/Navigation";
import Admin from "./ViewEmployees";
import ViewEmployees from "./ViewEmployees";
import { Link } from "react-router-dom";

function LandingPage() {
  return (
    <div className="Home">
      <div className="lander">
        <img
          style={{
            width: "85%",
            height: "85%",
            marginTop: "20px",
            borderRadius: "15px",
            filter: "brightness(0.6)",
          }}
          className="feature-image"
          src="https://source.unsplash.com/Tjio9DgtIls"
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

function VisitorHomePage() {
  return <p>Visitor</p>;
}

function ManagerHomePage() {
  return <Link to="/employees">View all employees</Link>;
}

function EmployeeHomePage() {
  return <p>Employee</p>;
}

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
