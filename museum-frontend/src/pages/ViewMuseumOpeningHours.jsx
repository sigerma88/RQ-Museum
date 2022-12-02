import React, { useContext } from "react";
import { LoginContext } from "../Contexts/LoginContext";

import {
  ManagerViewMuseumSchedule,
  AnyoneViewMuseumSchedule,
} from "./MuseumSchedule";

/**
 * Main function for the ViewMuseumOpeningHours page,
 * @author VZ
 * @returns the function either for manager who can edit openings, or anyone else who can only view
 */
export function ViewMuseumOpeningHours() {
  const { loggedIn, userRole } = useContext(LoginContext);
  if (userRole === "manager" && loggedIn) {
    return <ManagerViewMuseumSchedule />;
  } else {
    return <AnyoneViewMuseumSchedule />;
  }
}
