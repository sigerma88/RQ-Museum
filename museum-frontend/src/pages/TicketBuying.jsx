import React, { useState, useEffect, useContext } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TextField,
} from "@mui/material";
import { Navigation } from "../layouts/Navigation";

/**
 * Dialog component for visitors to specify what tickets they want to buy
 *
 * @returns The ticket buying dialog
 * @author Siger
 */
export function TicketBuying({ open, handleClose, visitorId }) {
  const [numTickets, setNumTickets] = useState(1);
  const [ticketDate, setTicketDate] = useState(
    new Date().toISOString().split("T")[0]
  );

  const handleSubmit = async (event) => {
    event.preventDefault();
    console.log(visitorId);
    console.log(numTickets);
    console.log(ticketDate);
    axios
      .post("/api/ticket/purchase?number=" + numTickets, {
        visitDate: ticketDate,
        visitorId: visitorId,
      })
      .then((response) => {
        handleClose();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <Dialog open={open} onClose={handleClose}>
      <form onSubmit={handleSubmit}>
        <DialogTitle>Buy Tickets</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please specify the number of tickets you want to buy and their date.
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Number of Tickets"
            type="number"
            fullWidth
            variant="standard"
            required
            InputLabelProps={{ shrink: true }} // This is to prevent the label from overlapping the text field
            defaultValue={numTickets}
            onChange={(event) => setNumTickets(event.target.value)}
          />
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Date"
            type="date"
            fullWidth
            variant="standard"
            required
            InputLabelProps={{ shrink: true }} // This is needed to make the label shrink
            defaultValue={ticketDate}
            onChange={(event) => setTicketDate(event.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button type="submit">Buy</Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}
