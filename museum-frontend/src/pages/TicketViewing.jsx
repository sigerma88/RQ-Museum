import React, { useState, useContext } from "react";
import { Button, Divider, Typography } from "@mui/material";
import { LoginContext } from "../Contexts/LoginContext";
import { Login } from "./Login";
import { GenerateTicketPasses } from "./Ticket";
import { TicketBuying } from "./TicketBuying";

/**
 * Visitor ticket viewing component
 *
 * @returns The page displaying the visitor's tickets
 * @author Siger
 */
function VisitorTicket() {
  // Get the visitor ID from the context
  const { userId } = useContext(LoginContext);
  const visitorId = userId;

  // Dialog state
  const [open, setOpen] = useState(false);
  const handleClose = () => setOpen(false);
  const handleOpen = () => setOpen(true);

  return (
    <>
      <Typography variant="h4" component="h1" marginTop={5} marginBottom={5}>
        My Tickets
      </Typography>
      <Button
        onClick={handleOpen}
        variant="contained"
        size="large"
        sx={{ position: "relative", top: "-80px", right: "-40%" }}
      >
        Buy tickets
      </Button>
      <Divider variant="middle" />
      <GenerateTicketPasses visitorId={visitorId} />
      <TicketBuying
        open={open}
        handleClose={handleClose}
        visitorId={visitorId}
      />
    </>
  );
}

function ManagerTicket() {
  return <p>Admin</p>;
}

function EmployeeTicket() {
  return <p>Employee</p>;
}

/**
 * Main function
 *
 * @returns The page to view tickets
 * @author Siger
 */
function TicketViewing() {
  const { loggedIn, userRole } = useContext(LoginContext);
  if (userRole === "visitor" && loggedIn) {
    return <VisitorTicket />;
  } else if (userRole === "manager" && loggedIn) {
    return <ManagerTicket />;
  } else if (userRole === "employee" && loggedIn) {
    return <EmployeeTicket />;
  } else {
    return <Login />;
  }
}

export default TicketViewing;
