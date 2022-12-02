import React, { useContext } from "react";
import { LoginContext } from "../Contexts/LoginContext";

import {
  ManagerViewMuseumSchedule,
  AnyoneViewMuseumSchedule,
} from "./MuseumSchedule";

export function ViewMuseumOpeningHours() {
  const { loggedIn, userRole } = useContext(LoginContext);
  if (userRole === "manager" && loggedIn) {
    return <ManagerViewMuseumSchedule />;
  } else {
    return <AnyoneViewMuseumSchedule />;
  }
}
