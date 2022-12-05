import React, { useContext, useEffect, useState } from "react";
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
import { LoginContext } from "../Contexts/LoginContext";

/**
 * Dialog component for visitors to specify what tickets they want to buy
 *
 * @returns The ticket buying dialog
 * @author Siger
 */
export function TicketBuying({ open, handleClose, visitorId }) {
  const [numTickets, setNumTickets] = useState(1);
  const [ticketDate, setTicketDate] = useState(
    new Date().toLocaleDateString("en-CA", { timeZone: "America/Montreal" })
  );
  const { museum } = useContext(LoginContext);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  const handleSubmit = async (event) => {
    // TODO: go to payment page handled by external service
    event.preventDefault();
    axios
      .post("/api/ticket/purchase?number=" + numTickets, {
        visitDate: ticketDate,
        visitorId: visitorId,
      })
      .then((response) => {
        if (response.status === 200) {
          handleClose();
        } else {
          setErrorMessage("Something went wrong");
          setIsFormInvalid(true);
        }
      })
      .then(() => {
        // Reload the page to show the new tickets
        window.location.reload();
      })
      .catch((error) => {
        console.log(error);
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
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
          <DialogContentText style={{ fontWeight: 700 }}>
            You'll pay C${museum.visitFee} per ticket.
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
            helperText={
              isFormInvalid &&
              errorMessage.toLowerCase().includes("number") &&
              errorMessage
            }
            error={
              isFormInvalid && errorMessage.toLowerCase().includes("number")
            }
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
            helperText={
              isFormInvalid &&
              errorMessage.toLowerCase().includes("date") &&
              errorMessage
            }
            error={isFormInvalid && errorMessage.toLowerCase().includes("date")}
          />
          <DialogContentText style={{ fontWeight: 700 }}>
            Total: C${(museum.visitFee * numTickets).toFixed(2)}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button type="submit">Buy</Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}
