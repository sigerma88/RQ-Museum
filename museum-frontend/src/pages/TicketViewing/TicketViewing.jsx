import React, { useState, useContext } from "react";
import {
  Button,
  Divider,
  IconButton,
  TextField,
  Typography,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import { LoginContext } from "../../Contexts/LoginContext";
import { Login } from "../Profile/Login/Login";
import { GenerateTicketPasses } from "./Tickets/Ticket";
import { TicketBuying } from "./TicketModals/TicketBuying";

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
      <Typography variant="h4" component="h1" marginTop={5} marginBottom={2}>
        My Tickets
      </Typography>
      <Button
        onClick={handleOpen}
        variant="contained"
        size="large"
        sx={{ position: "relative", top: "-58px", right: "-40%" }}
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

function StaffTicket() {
  const [search, setSearch] = useState("");
  const [visitorId, setVisitorId] = useState("");

  const handleSearch = (event) => {
    setSearch(event.target.value);
  };

  const handleSearchSubmit = (event) => {
    event.preventDefault();
    setVisitorId(search);
  };

  return (
    <>
      <Typography variant="h4" component="h1" marginTop={5} marginBottom={2}>
        Search for tickets
      </Typography>
      <form onSubmit={handleSearchSubmit}>
        <TextField
          id="search"
          label="Search for visitor by ID"
          variant="outlined"
          size="small"
          sx={{ mt: 1, mb: 1 }}
          value={search}
          onChange={handleSearch}
        />
        <IconButton aria-label="search" type="submit">
          <SearchIcon
            color="secondary"
            fontSize="large"
            sx={{ ml: 1, mb: 1 }}
          />
        </IconButton>
      </form>
      <Divider variant="middle" />
      {visitorId !== "" && <GenerateTicketPasses visitorId={visitorId} />}
    </>
  );
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
  } else if (loggedIn && (userRole === "manager" || userRole === "employee")) {
    return <StaffTicket />;
  } else {
    return <Login />;
  }
}

export default TicketViewing;
