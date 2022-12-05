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
  Checkbox,
  Snackbar,
  Alert,
  Dialog,
  DialogTitle,
  DialogActions,
  List,
  ListItem,
  ListItemText,
  ListItemAvatar,
} from "@mui/material/";
import "./ViewEmployees.css";
import LoadingButton from "@mui/lab/LoadingButton";

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

// Helper function to check if the name ends with an s and add an apostrophe depending on the case
function grammarCheck(name) {
  return name.charAt(name.length - 1) !== "s" ? name + "'s" : name + "'";
}

/**
 * Function for the confirmation dialog when deleting an employee
 *
 * @param props - The props for the dialog
 * @returns The confirmation dialog
 * @author Siger
 */
function ConfirmationDialog(props) {
  const {
    open,
    close,
    employees,
    setEmployees,
    selectedEmployees,
    handleSelect,
  } = props;
  const [loading, setLoading] = useState(false);

  // Function to delete the selected employees
  const handleDelete = async (event) => {
    event.preventDefault();
    await setLoading(true);
    for (let i = 0; i < selectedEmployees.length; i++) {
      await axios
        .delete(`/api/employee/${selectedEmployees[i].museumUserId}`)
        .then((response) => {
          if (response.status === 200) {
            setEmployees(
              employees.filter(
                (employee) =>
                  employee.museumUserId !== selectedEmployees[i].museumUserId
              )
            );
            handleSelect(selectedEmployees[i]);
          } else {
            console.log("Something went wrong");
          }
        })
        .catch((error) => {
          console.log(error);
        });
    }

    await setLoading(false);
    await close();
  };

  return (
    <Dialog open={open} onClose={close}>
      <DialogTitle>
        Are you sure you want to delete the selected employees?
      </DialogTitle>
      <List>
        {selectedEmployees.map((employee) => (
          // List the employees to be deleted
          <ListItem key={employee.museumUserId}>
            <ListItemAvatar>{employee.museumUserId}</ListItemAvatar>
            <ListItemText primary={employee.name} secondary={employee.email} />
          </ListItem>
        ))}
      </List>
      <DialogActions>
        <Button sx={{ width: 150 }} onClick={close}>
          Cancel
        </Button>
        <LoadingButton
          loading={loading}
          loadingPosition="end"
          color="error"
          sx={{ width: 150 }}
          onClick={handleDelete}
        >
          Confirm
        </LoadingButton>
      </DialogActions>
    </Dialog>
  );
}

/**
 * Main function for the ViewEmployees page
 * The page displays a list of all employees and allows the manager to delete them (fire) or add a new one (hire)
 *
 * @author VZ
 * @author Kevin
 * @author Siger
 * @returns table of employees with name, email and schedule
 */
export default function ViewEmployees() {
  const [employees, setEmployees] = useState([]);
  const [selectedEmployees, setSelectedEmployees] = useState([]);
  const [alertErrorOpen, setAlertErrorOpen] = useState(false);
  const [deleteConfirmOpen, setDeleteConfirmOpen] = useState(false);

  useEffect(() => {
    axios
      .get("/api/employee")
      .then(function (response) {
        // set the state to the data returned from the API
        setEmployees(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [employees]);

  const handleSelect = (employee) => {
    if (selectedEmployees.includes(employee)) {
      setSelectedEmployees(selectedEmployees.filter((e) => e !== employee));
    } else {
      setSelectedEmployees([...selectedEmployees, employee]);
    }
  };

  // Handle the opening of the alert for bad selection for deletion
  const handleAlertErrorOpen = () => {
    setAlertErrorOpen(true);
  };
  const handleAlertErrorClose = () => {
    setAlertErrorOpen(false);
  };

  // Handle the opening of the confirmation dialog for deletion
  const handleDeleteConfirmOpen = () => {
    setDeleteConfirmOpen(true);
  };
  const handleDeleteConfirmClose = () => {
    setDeleteConfirmOpen(false);
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
                if (selectedEmployees.length === 0) {
                  handleAlertErrorOpen();
                } else {
                  handleDeleteConfirmOpen();
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
                          employees.map((employee) => employee)
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
                      checked={selectedEmployees
                        .map((e) => e.museumUserId)
                        .includes(employee.museumUserId)}
                      onChange={() => {
                        handleSelect(employee);
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

        {employees[0].museumUserId !== null && (
          <ConfirmationDialog
            open={deleteConfirmOpen}
            close={handleDeleteConfirmClose}
            selectedEmployees={selectedEmployees}
            handleSelect={handleSelect}
            employees={employees}
            setEmployees={setEmployees}
          />
        )}
      </>
    );
  }
}
