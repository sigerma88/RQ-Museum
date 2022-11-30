import React, { useContext } from "react";
import { Typography } from "@mui/material/";
import { LoginContext } from "../Contexts/LoginContext";
import {
  ManagerViewEmployeeSchedule,
  EmployeeViewEmployeeSchedule,
} from "./EmployeeSchedule";

/**
 * Main function for the ViewSchedule page
 *
 * @returns the ViewSchedule page
 * @author Victor
 */
function ViewSchedule() {
  const { loggedIn, userRole } = useContext(LoginContext);
  if (userRole === "manager" && loggedIn) {
    return <ManagerViewEmployeeSchedule />;
  } else if (userRole === "employee" && loggedIn) {
    return <EmployeeViewEmployeeSchedule />;
  } else {
    return (
      <Typography variant="h3" style={{ margin: "auto", padding: "auto" }}>
        You are not authorized to view this page
      </Typography>
    );
  }
}

export default ViewSchedule;
