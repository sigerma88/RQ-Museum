import React, { useState, useEffect } from "react";
import { MuseumSchedule } from "./ViewMuseumOpeningHours";
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
} from "@mui/material/";

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
          {/* <Button
            variant="contained"
            sx={{ position: "relative", right: -300, top: -55 }}
            onClick={() => {
              window.location.href = "/museum/create";
            }}
          >
            Add Museum
          </Button> */}
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
          {/* <Button
            variant="contained"
            sx={{ position: "relative", right: -300, top: -55 }}
            onClick={() => {
              window.location.href = "/employee/create";
            }}
          >
            Add Employee
          </Button> */}
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
