import React, { useState, useEffect } from "react";
import { MuseumSchedule } from "./ViewMuseumOpeningHours";
import { useParams } from "react-router-dom";

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
  styled,
  Button,
  TextField,
} from "@mui/material/";
import { Link } from "react-router-dom";

function EditMuseumInfo({ id, setMuseums }) {
  const [name, setName] = useState("");
  const [visitFee, setVisitFee] = useState(0);

  const handleChange = (event) => {
    event.preventDefault();
    axios
      .post(
        `/api/museum/app/edit/${id}/?name=` + name + "&visitFee=" + visitFee
      )
      .then(function (response) {
        const editedMuseum = response.data;
        console.log(response.data);
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  };

  return (
    <>
      <Typography variant="h4" component="h1" marginTop={5} marginBottom={2}>
        Search for tickets
      </Typography>
      {/* <form onSubmit={handleChange}>
        <TextField
          id="search"
          label="Search for visitor by ID"
          variant="outlined"
          size="small"
          sx={{ mt: 1, mb: 1 }}
          onChange={handleChange}
        />
      </form> */}
    </>
  );
}

export function ManagerViewMuseumInfo({ setMuseums, handleChange }) {
  const { id } = useParams();
  return (
    <>
      <form onSubmit={handleChange}>
        <TextField
          id="changeName"
          label="Enter new name"
          variant="outlined"
          size="small"
          sx={{ mt: 1, mb: 1 }}
        />
        <TextField
          id="changeName"
          label="Enter new visit fee"
          variant="outlined"
          size="small"
          sx={{ mt: 1, mb: 1 }}
        />
      </form>

      <p> {id} </p>
    </>
  );
}

/**
 * Main function for viewing museum information, including name, visit fee and link to schedule
 * @author VZ
 * @returns RQ Museum information in a table
 */
export function ViewMuseumInfo() {
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
        console.log(response.data);
        setMuseums(response.data);
      })
      .catch(function (error) {
        console.log(error.response.data);
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
                    <a
                      href={`/museum/schedule/${museum.museumId}`}
                      className="hover-underline-animation"
                    >
                      View&nbsp;{grammarCheck(museum.name)}&nbsp;Opening Hours
                    </a>
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
