import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import {
  Button,
  Card,
  CardContent,
  Divider,
  Grid,
  List,
  ListItem,
  ListItemText,
  Typography,
} from "@mui/material";
import { Container } from "@mui/system";
import "./TicketViewing.css";
import { LoginContext } from "../Contexts/LoginContext";
import { Navigation } from "../layouts/Navigation";
import { Login } from "./Login";

/**
 * Function to get the tickets of a visitor from the server
 *
 * @param visitorId - The visitor ID to get the tickets from
 * @returns The fetched tickets
 * @author Siger
 */
function getTickets(visitorId) {
  return axios
    .get(`/api/ticket/visitor/${visitorId}`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
}

/**
 * Function to generate the ticket passes
 *
 * @param tickets - The tickets to generate the passes for
 * @returns The generated passes
 * @author Siger
 */
function GenerateTicketPasses({ tickets }) {
  return (
    <Container sx={{ py: 5 }}>
      <Grid container spacing={4}>
        {tickets.map((ticket) => (
          <Grid item xs={12} key={ticket.ticketId}>
            <Card>
              <CardContent className="ticket-card-content">
                <Typography variant="h6" component="h2">
                  {"Ticket ID: " + ticket.ticketId}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {"Date: " + ticket.visitDate}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}

/**
 * Visitor ticket viewing component
 *
 * @returns The page displaying the visitor's tickets
 * @author Siger
 */
function VisitorTicket() {
  // Get the tickets from the server
  const [tickets, setTickets] = useState([]); // TODO: Get user ID
  useEffect(() => {
    getTickets(1).then((tickets) => {
      setTickets(tickets);
    });
  }, [1]);

  // TODO: Get user information to display on the tickets

  return (
    <>
      <Typography variant="h4" component="h1" marginTop={5} marginBottom={5}>
        My Tickets
      </Typography>
      <Divider variant="middle" />
      <GenerateTicketPasses tickets={tickets} />
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
