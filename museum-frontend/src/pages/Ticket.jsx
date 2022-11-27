import React, { useState, useEffect } from "react";
import axios from "axios";
import { Alert, Box, Card, CardContent, Grid, Typography } from "@mui/material";
import { Container } from "@mui/system";
import "./Ticket.css";

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
      return {
        error: error,
        message: error.response.data.error
          ? error.response.data.error
          : error.response.data,
      };
    });
}

/**
 * Function to generate the ticket passes
 *
 * @param tickets - The tickets to generate the passes for
 * @returns The generated passes
 * @author Siger
 */
export function GenerateTicketPasses({ visitorId }) {
  const [errorMessage, setErrorMessage] = useState("");

  // Get the tickets from the server
  const [validPasses, setValidPasses] = useState([]);
  const [expiredPasses, setExpiredPasses] = useState([]);
  useEffect(() => {
    getTickets(visitorId).then((tickets) => {
      if (tickets.error) {
        setErrorMessage(tickets.message);
      } else {
        setErrorMessage("");
        const currentDate = new Date();
        setValidPasses(
          tickets.filter(
            (ticket) =>
              ticket.visitDate >=
              currentDate.toLocaleDateString("en-CA", {
                timeZone: "America/Montreal",
              })
          )
        );
        setExpiredPasses(
          tickets.filter(
            (ticket) =>
              ticket.visitDate <
              currentDate.toLocaleDateString("en-CA", {
                timeZone: "America/Montreal",
              })
          )
        );
      }
    });
  }, [visitorId]);

  if (errorMessage !== "") {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          padding: "20px",
        }}
      >
        <Alert severity="error" sx={{ width: 500 }}>
          This is an error! — <strong>{errorMessage}</strong>
        </Alert>
      </Box>
    );
  } else {
    return (
      <Container sx={{ py: 5, width: "800px" }}>
        <Grid container spacing={2}>
          {validPasses.map((ticket) => ValidTicket({ ticket }))}
          {expiredPasses.map((ticket) => ExpiredTicket({ ticket }))}
        </Grid>
      </Container>
    );
  }
}

/**
 * Function to generate a valid ticket pass
 *
 * @param ticket - information about the ticket
 * @returns a card with the ticket information
 * @author Siger
 */
function ValidTicket({ ticket }) {
  return (
    <Grid item xs={12} key={ticket.ticketId}>
      <Card sx={{ width: "800px" }}>
        <CardContent className="pass-card-content">
          <Typography
            variant="h6"
            className="pass-visitor-information"
            sx={{ fontStyle: "italic" }}
          >
            Pass issued to
          </Typography>
          <Typography
            variant="h5"
            className="pass-visitor-information"
            sx={{ fontWeight: "bolder" }}
          >
            {ticket.visitor.name}
          </Typography>
          <Typography
            variant="h6"
            color="text.secondary"
            className="pass-visitor-information"
          >
            {ticket.visitor.email}
          </Typography>
          <Typography
            variant="h6"
            className="pass-ticket-information"
            sx={{ fontStyle: "italic" }}
          >
            Pass valid on
          </Typography>
          <Typography
            variant="h5"
            className="pass-ticket-information"
            sx={{ fontWeight: "bolder" }}
          >
            {ticket.visitDate}
          </Typography>
          <Typography variant="secondary" className="pass-ticket-id">
            {"Ticket ID: " + ticket.ticketId}
          </Typography>
        </CardContent>
      </Card>
    </Grid>
  );
}

/**
 * Function to generate an expired ticket pass
 *
 * @param ticket - information about the ticket
 * @returns a card with the ticket information
 * @author Siger
 */
function ExpiredTicket({ ticket }) {
  return (
    <Grid item xs={12} key={ticket.ticketId}>
      <Card sx={{ width: "800px" }}>
        <CardContent className="pass-expired">
          <Typography
            variant="h6"
            className="pass-visitor-information"
            sx={{ fontStyle: "italic" }}
          >
            Pass issued to
          </Typography>
          <Typography
            variant="h5"
            className="pass-visitor-information"
            sx={{ fontWeight: "bolder" }}
          >
            {ticket.visitor.name}
          </Typography>
          <Typography
            variant="h6"
            color="text.secondary"
            className="pass-visitor-information"
          >
            {ticket.visitor.email}
          </Typography>
          <Typography
            variant="h6"
            className="pass-ticket-information"
            sx={{ fontStyle: "italic" }}
          >
            Pass valid on
          </Typography>
          <Typography
            variant="h5"
            className="pass-ticket-information"
            sx={{ fontWeight: "bolder" }}
          >
            {ticket.visitDate}
          </Typography>
          <Typography variant="secondary" className="pass-ticket-id">
            {"Ticket ID: " + ticket.ticketId}
          </Typography>
          <Typography variant="h2" className="expire-message">
            This pass has expired
          </Typography>
        </CardContent>
      </Card>
    </Grid>
  );
}
