import React from "react";
import { Button } from "@mui/material";

/**
 * Footer
 * @author Kevin
 * @returns The footer
 */

export function Footer() {
  const footerNavigation = ["Home", "Room", "Ticket", "Loan", "Info"];

  return (
    <footer
      style={{
        bottom: 0,
        backgroundColor: "black",
        marginTop: "100px",
        width: "100%",
        padding: "50px",
      }}
    >
      <div
        style={{
          display: "flex",
          alignContent: "center",
          justifyContent: "center",
        }}
      >
        {footerNavigation.map((item) => (
          <Button
            key={item}
            href={
              item === "Home"
                ? "/"
                : item === "Room"
                ? `/browse/room/all`
                : item === "Info"
                ? "/museum/info"
                : `/${item.toLowerCase()}`
            }
            sx={{ my: 2, color: "white", display: "block", fontSize: "16px" }}
          >
            {item}
          </Button>
        ))}
      </div>
    </footer>
  );
}
