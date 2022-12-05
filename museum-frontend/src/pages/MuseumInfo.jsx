import React, { useState, useEffect, useContext } from "react";

import axios from "axios";
import {
  Table,
  TableBody,
  TableHead,
  TableRow,
  TableContainer,
  Paper,
  Typography,
  TableCell,
  tableCellClasses,
  Box,
  styled,
  Button,
  TextField,
} from "@mui/material/";
import { Link } from "react-router-dom";
import { LoginContext } from "../Contexts/LoginContext";

/**
 * Function for manager to view and edit museum information, i.e.  name and visit fee
 *
 * @author VZ
 * @returns Museum information in a table
 */
function ManagerViewMuseumInfo() {
  const [museumFromLocalStorage, setMuseumFromLocalStorage] = useState(null);
  const [name, setName] = useState("");
  const [visitFee, setVisitFee] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  const StyledTableCell = styled(TableCell)(() => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: "#ababab",
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  }));

  const StyledTableRow = styled(TableRow)(({ theme }) => ({
    "&:nth-of-type(odd)": {
      backgroundColor: theme.palette.action.hover,
    },
    // hide last border
    "&:last-child td, &:last-child th": {
      border: 0,
    },
  }));

  useEffect(() => {
    setMuseumFromLocalStorage(JSON.parse(localStorage.getItem("museum")));
  }, []);

  const handleChange = (event) => {
    event.preventDefault();
    const museumId = museumFromLocalStorage.museumId;
    axios
      .put(
        `/api/museum/app/edit/${museumId}/?name=` +
          name +
          "&visitFee=" +
          visitFee
      )
      .then(function (response) {
        localStorage.setItem("museum", JSON.stringify(response.data));
        setMuseumFromLocalStorage(response.data);
        setErrorMessage("");
        setIsFormInvalid(false);
      })
      .catch(function (error) {
        console.log(error);
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
      });
  };

  if (!museumFromLocalStorage) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <div>
        <h1 style={{ marginTop: 20, marginBottom: 20 }}>Museum Information</h1>
      </div>
      <TableContainer
        component={Paper}
        sx={{
          display: "flex",
          justifyContent: "center",
          maxHeight: "500px",
          maxWidth: "1000px",
          boxShadow: 4,
          borderRadius: 1,
          my: 2,
          mx: "auto",
        }}
      >
        <Table stickyHeader aria-label="simple table">
          <TableHead>
            <TableRow>
              <StyledTableCell>
                <Typography
                  sx={{
                    fontWeight: "bold",
                    fontSize: 18,
                  }}
                >
                  Name
                </Typography>
              </StyledTableCell>
              <StyledTableCell>
                <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                  Visit Fee ($)
                </Typography>
              </StyledTableCell>
              <StyledTableCell align="right">
                <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                  Opening Hours
                </Typography>
              </StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            <StyledTableRow
              key={museumFromLocalStorage.museumId}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <StyledTableCell>{museumFromLocalStorage.name}</StyledTableCell>
              <StyledTableCell>
                {museumFromLocalStorage.visitFee}
              </StyledTableCell>
              <StyledTableCell align="right">
                <Link
                  to={`/museum/info/schedule/${museumFromLocalStorage.museumId}`}
                  className="hover-underline-animation"
                >
                  View&nbsp;{grammarCheck(museumFromLocalStorage.name)}
                  &nbsp;Opening Hours
                </Link>
              </StyledTableCell>
            </StyledTableRow>
          </TableBody>
        </Table>
      </TableContainer>

      <form
        style={{ width: "100%", alignContent: "flex-end" }}
        onSubmit={(event) => {
          if (name || visitFee) {
            handleChange(event);
          } else {
            event.preventDefault();
            setErrorMessage("Please fill out at least one field");
            setIsFormInvalid(true);
          }
        }}
      >
        <Box
          sx={{
            marginTop: 3,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <TextField
            margin="normal"
            id="name"
            label="New Name"
            name="New Name"
            autoComplete="New Name"
            autoFocus
            onChange={(e) => setName(e.target.value)}
            helperText={errorMessage}
            error={isFormInvalid}
          />
          <TextField
            margin="normal"
            id="email"
            label="New Visit Fee"
            name="New Visit Fee"
            autoComplete="New Visit Fee"
            autoFocus
            onChange={(e) => setVisitFee(e.target.value)}
            helperText={errorMessage}
            error={isFormInvalid}
          />
        </Box>
        <Button
          type="submit"
          variant="contained"
          sx={{ mt: 3, mb: 2, width: "150px" }}
        >
          Edit Museum
        </Button>
      </form>
    </>
  );
}

/**
 * Function for viewing museum information, including name, visit fee and link to schedule
 *
 * @author VZ
 * @returns RQ Museum information in a table
 */
function ViewMuseumInfo() {
  const [museums, setMuseums] = useState([]);

  const StyledTableCell = styled(TableCell)(() => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: "#ababab",
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  }));

  const StyledTableRow = styled(TableRow)(({ theme }) => ({
    "&:nth-of-type(odd)": {
      backgroundColor: theme.palette.action.hover,
    },
    // hide last border
    "&:last-child td, &:last-child th": {
      border: 0,
    },
  }));

  useEffect(() => {
    axios
      .get("/api/museum")
      .then(function (response) {
        setMuseums(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }, []);

  if (museums.length === 0) {
    return (
      <>
        <div>
          <h1 style={{ marginTop: 20, marginBottom: 20 }}>
            Museum Information
          </h1>
        </div>
        <Typography>
          Information on the Museum is currently unavailable
        </Typography>
      </>
    );
  } else {
    return (
      <>
        <div>
          <h1 style={{ marginTop: 20, marginBottom: 20 }}>
            Museum Information
          </h1>
        </div>
        <TableContainer
          component={Paper}
          sx={{
            display: "flex",
            justifyContent: "center",
            maxHeight: "500px",
            maxWidth: "1000px",
            boxShadow: 4,
            borderRadius: 1,
            my: 2,
            mx: "auto",
          }}
        >
          <Table stickyHeader aria-label="simple table">
            <TableHead>
              <TableRow>
                <StyledTableCell>
                  <Typography
                    sx={{
                      fontWeight: "bold",
                      fontSize: 18,
                    }}
                  >
                    Name
                  </Typography>
                </StyledTableCell>
                <StyledTableCell>
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    Visit Fee ($)
                  </Typography>
                </StyledTableCell>
                <StyledTableCell align="right">
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    Opening Hours
                  </Typography>
                </StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {museums.map((museum) => (
                <StyledTableRow
                  key={museum.museumId}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <StyledTableCell>{museum.name}</StyledTableCell>
                  <StyledTableCell>{museum.visitFee}</StyledTableCell>
                  <StyledTableCell align="right">
                    <Link
                      to={`/museum/info/schedule/${museum.museumId}`}
                      className="hover-underline-animation"
                    >
                      View&nbsp;{grammarCheck(museum.name)}&nbsp;Opening Hours
                    </Link>
                  </StyledTableCell>
                </StyledTableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </>
    );
  }
}

// Helper function to check if the name ends with an s and add an apostrophe depending on the case
function grammarCheck(name) {
  return name.charAt(name.length - 1) !== "s" ? name + "'s" : name + "'";
}

/**
 * Main function that returns component depending on if it's the manager or not
 * @author VZ
 * @returns Museum information in a table
 */
export default function MuseumInfo() {
  const { loggedIn, userRole } = useContext(LoginContext);

  if (loggedIn && userRole === "manager") {
    return <ManagerViewMuseumInfo />;
  } else {
    return <ViewMuseumInfo />;
  }
}
