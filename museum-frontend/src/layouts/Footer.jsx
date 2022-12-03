import React from "react";
import { BottomNavigation, Divider } from "@mui/material";

export function Footer() {
  return (
    <div style={{ marginTop: "50px", height: "150px" }}>
      <Divider />
      <BottomNavigation>
        <p>Footer</p>
      </BottomNavigation>
    </div>
  );
}
