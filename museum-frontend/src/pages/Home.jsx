import React, { useEffect, useState } from "react";
import Typography from "@mui/material/Typography";
import "./Home.css";

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
          <div class="centered">
            <Typography
              variant="h4"
              noWrap
              component="h1"
              style={{
                color: "white",
                fontFamily: "system-ui, sans-serif;",
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
