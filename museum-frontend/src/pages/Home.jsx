import React, { useEffect, useState, useContext } from "react";
import Typography from "@mui/material/Typography";
import "./Home.css";
import { LoginContext } from "../Contexts/LoginContext";
import { Navigation } from "../layouts/Navigation";

function Home() {
  return (
    <>
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
    </>
  );
}

export default Home;
