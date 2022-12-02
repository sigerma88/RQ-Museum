import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Grid,
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
  Tooltip,
  Checkbox,
  Snackbar,
  Alert,
} from "@mui/material/";
import "./ViewEmployees.css";

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

/**
 * Main function for the ViewEmployees page that displays all employees in the database
 * @author VZ
 * @author Kevin
 * @author Siger
 * @returns table of employees with name, email and schedule
 */
export function ViewEmployees() {
  const [employees, setEmployees] = useState([]);
  const [selectedEmployees, setSelectedEmployees] = useState([]);
  const [alertSuccessOpen, setAlertSuccessOpen] = useState(false);
  const [alertErrorOpen, setAlertErrorOpen] = useState(false);

  useEffect(() => {
    axios
      .get("/api/employee")
      .then(function (response) {
        // set the state to the data returned from the API
        setEmployees(response.data);
      })
      .catch(function (error) {
        // if the request fails
        console.log(error.response.data);
      });
  }, []);

  const handleSelect = (employeeId) => {
    if (selectedEmployees.includes(employeeId)) {
      setSelectedEmployees(selectedEmployees.filter((id) => id !== employeeId));
    } else {
      setSelectedEmployees([...selectedEmployees, employeeId]);
    }
  };

  const handleAlertSuccessOpen = () => {
    setAlertSuccessOpen(true);
  };

  const handleAlertSuccessClose = () => {
    setAlertSuccessOpen(false);
  };

  const handleAlertErrorOpen = () => {
    setAlertErrorOpen(true);
  };

  const handleAlertErrorClose = () => {
    setAlertErrorOpen(false);
  };

  if (employees.length === 0) {
    return (
      <>
        <div>
          <h1 style={{ marginTop: 20, marginBottom: 20 }}>
            List&nbsp;of&nbsp;all&nbsp;Employees
          </h1>
          <Button
            variant="contained"
            sx={{ position: "relative", right: -300, top: -55 }}
            onClick={() => {
              window.location.href = "/employee/create";
            }}
          >
            Add Employee
          </Button>
        </div>
        <Typography>There are no employees at the moment.</Typography>
      </>
    );
  } else {
    return (
      <>
        <Grid container spacing={2}>
          <Grid item xs={4}></Grid>
          <Grid item xs={4}>
            <h1 style={{ marginTop: 20, marginBottom: 20 }}>
              List of all Employees
            </h1>
          </Grid>
          <Grid item xs={2}>
            <Button
              variant="contained"
              color="success"
              sx={{ marginTop: 3.5 }}
              onClick={() => {
                window.location.href = "/employee/create";
              }}
            >
              Hire Employee
            </Button>
          </Grid>
          <Grid item xs={2}>
            <Button
              variant="contained"
              color="error"
              sx={{ marginTop: 3.5 }}
              onClick={() => {
                // TODO: Implement delete employee
                if (selectedEmployees.length === 0) {
                  handleAlertErrorOpen();
                } else {
                  handleAlertSuccessOpen();
                }
              }}
            >
              Fire Employee
            </Button>
            <Snackbar open={alertErrorOpen} onClose={handleAlertErrorClose}>
              <Alert
                onClose={handleAlertErrorClose}
                severity="error"
                sx={{ width: "100%" }}
              >
                Error: No employee selected.
              </Alert>
            </Snackbar>
            <Snackbar
              open={alertSuccessOpen}
              autoHideDuration={6000}
              onClose={handleAlertSuccessClose}
            >
              <Alert
                onClose={handleAlertSuccessClose}
                severity="success"
                sx={{ width: "100%" }}
              >
                Success: Employee(s) fired.
              </Alert>
            </Snackbar>
          </Grid>
        </Grid>
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
                  <Checkbox
                    checked={selectedEmployees.length === employees.length}
                    indeterminate={
                      selectedEmployees.length > 0 &&
                      selectedEmployees.length < employees.length
                    }
                    onChange={() => {
                      if (selectedEmployees.length < employees.length) {
                        setSelectedEmployees(
                          employees.map((employee) => employee.museumUserId)
                        );
                      } else {
                        setSelectedEmployees([]);
                      }
                    }}
                  />
                </StyledTableCell>
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
                    Email
                  </Typography>
                </StyledTableCell>
                <StyledTableCell align="right">
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    View&nbsp;Schedule
                  </Typography>
                </StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {employees.map((employee) => (
                <StyledTableRow
                  key={employee.museumUserId}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <StyledTableCell>
                    <Checkbox
                      checked={selectedEmployees.includes(
                        employee.museumUserId
                      )}
                      onChange={() => {
                        handleSelect(employee.museumUserId);
                      }}
                    />
                  </StyledTableCell>
                  <StyledTableCell>{employee.name}</StyledTableCell>
                  <StyledTableCell>{employee.email}</StyledTableCell>
                  <StyledTableCell align="right">
                    <a
                      href={`/employee/schedule/${employee.museumUserId}`}
                      className="hover-underline-animation"
                    >
                      View&nbsp;{grammarCheck(employee.name)}&nbsp;schedule
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
