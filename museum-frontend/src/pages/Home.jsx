import React, { useContext } from "react";
import Typography from "@mui/material/Typography";
import "./Home.css";
import { LoginContext } from "../Contexts/LoginContext";
import Admin from "./ViewEmployees";
import ViewEmployees from "./ViewEmployees";
import { Link } from "react-router-dom";

/**
 * General page when user goes to the website
 * @returns Home page
 * @author Kevin
 */

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

/**
 * Visitor home page when logged in as a visitor
 * @returns Visitor Home page
 * @author Kevin
 */

function VisitorHomePage() {
  return <p>Visitor</p>;
}

/**
 * Manager home page when logged in as a manager
 * @returns Manager Home page
 * @author Kevin
 */

function ManagerHomePage() {
  return <Link to="/employees">View all employees</Link>;
}

/**
 * Employee home page when logged in as a employee
 * @returns Employee Home page
 * @author Kevin
 */

function EmployeeHomePage() {
  return <p>Employee</p>;
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
