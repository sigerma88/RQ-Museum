import React, { useState, useEffect } from "react";
import axios from "axios";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import { styled } from "@mui/material/styles";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { createTheme, padding } from "@mui/system";
import { Typography } from "@mui/material";

function createData(name, email, view_schedule) {
  return { name, email, view_schedule };
}
const StyledTableCell = styled(TableCell)(({ theme }) => ({
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

const header = createTheme({
  typography: {
    fontWeight: "bold",
    fontSize: 18,
  },
});

/**
 * @author VZ and Kevin
 * @returns table of employees
 */
export function ViewEmployees() {
  const [employees, setEmployees] = useState([]); // initial state set to empty array

  useEffect(() => {
    // function getAllEmployees() {
    axios // axios is a library that allows us to make HTTP requests
      .get("/api/employee")
      .then(function (response) {
        // if the request is successful
        console.log(response.data);
        setEmployees(response.data); // set the state to the data returned from the API
      })
      .catch(function (error) {
        // if the request fails
        console.log(error.response.data);
      });
    // }
    // getAllEmployees()
  }, []);

  if (employees.length === 0) {
    return <p>There is no employee.</p>;
  } else {
    return (
      <>
        <div>
          <h1 style={{ marginTop: 20, marginBottom: 20 }}>
            List of all Employees
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
            borderRadius: 2,
            my: 2,
            mx: "auto",
          }}
        >
          <Table aria-label="simple table">
            <TableHead>
              <TableRow>
                <StyledTableCell>
                  <Typography sx={header}>Name</Typography>
                </StyledTableCell>
                <StyledTableCell>
                  <Typography sx={header}>Email</Typography>
                </StyledTableCell>
                <StyledTableCell align="right">
                  <Typography sx={header}>View&nbsp;Schedule</Typography>
                </StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {employees.map((employee) => (
                <StyledTableRow
                  key={employee.museumUserId}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <StyledTableCell>{employee.name}</StyledTableCell>
                  <StyledTableCell>{employee.email}</StyledTableCell>
                  <StyledTableCell align="right">
                    <a href={`/schedule/${employee.museumUserId}`}>
                      View {grammarCheck(employee.name)} schedule
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

function grammarCheck(name) {
  return name.charAt(name.length - 1) !== "s" ? name + "'s" : name + "'";
}
