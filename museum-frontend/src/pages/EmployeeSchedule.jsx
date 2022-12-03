import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import {
  DatePicker,
  LocalizationProvider,
  TimePicker,
} from "@mui/x-date-pickers/";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import {
  Button,
  Table,
  styled,
  Stack,
  TableBody,
  TableCell,
  tableCellClasses,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Typography,
  TextField,
} from "@mui/material";
import dayjs from "dayjs";
import { LoginContext } from "../Contexts/LoginContext";
import {
  getDate,
  getDay,
  getDayOfWeek,
  getMonth,
  getYear,
} from "./utils/DateUtils";

/**
 * Function to add shift to an employee
 *
 * @author VZ
 * @param {id} - id of the employee
 * @param {timePeriods} - array of time periods
 * @param {setTimePeriods} - function to set the time periods
 * @returns a form to add a shift
 */
function AddShift({ id, timePeriods, setTimePeriods }) {
  const [date, setDate] = useState(null);
  const [startTime, setStartTime] = useState(null);
  const [endTime, setEndTime] = useState(null);
  const [errorMessage, setErrorMessage] = useState(false);
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  //POST request to add a shift to the employee's schedule
  //which entails first creating a time period and
  //then adding it to the employee's schedule
  const handleSubmit = async (event) => {
    event.preventDefault();

    axios
      .post(`/api/scheduling/shift/create`, {
        startDate: getDate(date) + " " + dayjs(startTime).format("HH:mm:ss"),
        endDate: getDate(date) + " " + dayjs(endTime).format("HH:mm:ss"),
      })
      .then(function (response) {
        //CREATE THE SHIFT
        if (response.status === 200) {
          const tp = response.data;
          //ADD THE SHIFT TO THE EMPLOYEE'S SCHEDULE
          axios
            .post(
              `/api/scheduling/employee/${id}/add/shift/${response.data.timePeriodId}`
            )
            .then(function (response) {
              // if the request is successful
              setErrorMessage("");
              setIsFormInvalid(false);
              setTimePeriods([...timePeriods, tp]); // set the state to the data returned from the API
            })
            .catch(function (error) {
              console.log(error);
            });
        }
      })
      .catch(function (error) {
        console.log(error);
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
      });
  };

  return (
    <>
      <h2 style={{ marginTop: 30 }}>Add Shift</h2>
      <form
        onSubmit={(event) => {
          if (date && startTime && endTime) {
            handleSubmit(event);
          } else {
            event.preventDefault();
            setErrorMessage("Please fill out all fields");
            setIsFormInvalid(true);
          }
        }}
      >
        <div style={{ marginTop: 30 }}>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <Stack justifyContent="center" direction="row" spacing={3}>
              <DatePicker
                label="Select Date"
                value={date}
                onChange={(newValue) => {
                  setDate(newValue);
                }}
                renderInput={(params) => (
                  <TextField
                    {...params}
                    helperText={errorMessage}
                    error={isFormInvalid}
                  />
                )}
              />
              <TimePicker
                label="Select Start Time"
                value={startTime}
                onChange={(newValue) => {
                  setStartTime(newValue);
                }}
                renderInput={(params) => (
                  <TextField
                    {...params}
                    helperText={errorMessage}
                    error={isFormInvalid}
                  />
                )}
              />
              <TimePicker
                label="Select End Time"
                value={endTime}
                onChange={(newValue) => {
                  setEndTime(newValue);
                }}
                renderInput={(params) => (
                  <TextField
                    {...params}
                    helperText={errorMessage}
                    error={isFormInvalid}
                  />
                )}
              />
            </Stack>
          </LocalizationProvider>
        </div>
        <Button
          variant="contained"
          type="submit"
          style={{ width: 120, marginTop: 30, padding: 10 }}
        >
          Add Shift
        </Button>
      </form>
    </>
  );
}

/**
 * Main function that returns the schedule of an employee as viewed by the manager so he can edit them
 *
 * @author VZ and Kevin
 * @returns table that contains the shifts of an employee by id
 */
export function ManagerViewEmployeeSchedule() {
  const [timePeriods, setTimePeriods] = useState([]);
  const { id } = useParams(); //get the employee id from the url

  //GET request to get the employee's schedule by id
  useEffect(() => {
    axios
      .get(`/api/scheduling/employee/shifts/${id}`)
      .then(function (response) {
        // if the request is successful
        setTimePeriods(response.data); // set the state to the data returned from the API
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [id]);

  //DELETE request to remove the timeperiod from the employee's schedule
  const handleRemove = async (event, tpId) => {
    event.preventDefault();

    axios
      .delete(`/api/scheduling/employee/${id}/remove/shift/${tpId}`)
      .then(function (response) {
        // if the request is successful
        setTimePeriods(
          timePeriods.filter((timePeriod) => timePeriod.timePeriodId !== tpId)
        );
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  // Date formatted as DD MM YYYY
  // We only need the start date, because we are assuming that a shift spans one day at most
  // If the manager wants to schedule a shift that spans multiple days,
  // they can schedule multiple shifts
  const row = timePeriods.map((timePeriod) => (
    <TableRow key={timePeriod.timePeriodId}>
      <TableCell>
        {getDay(timePeriod.startDate.split(" ")[0])}{" "}
        {getMonth(timePeriod.startDate.split(" ")[0])}{" "}
        {getYear(timePeriod.startDate.split(" ")[0])}
      </TableCell>
      <TableCell>{getDayOfWeek(timePeriod.startDate.split(" ")[0])}</TableCell>
      <TableCell>
        {timePeriod.startDate
          .split(" ")[1]
          .substring(0, timePeriod.startDate.split(" ")[1].length - 3)}
      </TableCell>
      <TableCell>
        {timePeriod.endDate
          .split(" ")[1]
          .substring(0, timePeriod.startDate.split(" ")[1].length - 3)}
      </TableCell>
      <TableCell>
        <Button
          onClick={(event) => handleRemove(event, timePeriod.timePeriodId)}
        >
          Remove Shift
        </Button>
      </TableCell>
    </TableRow>
  ));

  const StyledTableCell = styled(TableCell)(() => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: "#ababab",
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  }));

  if (timePeriods.length === 0) {
    return (
      <>
        <div>
          <h1 style={{ marginTop: 20, marginBottom: 20 }}>
            Employee's Schedule
          </h1>
        </div>

        <TableContainer
          component={Paper}
          sx={{
            maxWidth: 1000,
            display: "flex",
            justifyContent: "center",
            maxHeight: "500px",
            boxShadow: 4,
            borderRadius: 1,
            my: 2,
            mx: "auto",
          }}
        >
          <Table stickyHeader aria-label="dense table">
            <TableHead>
              <TableRow>
                <StyledTableCell>
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    Date
                  </Typography>
                </StyledTableCell>
                <StyledTableCell>
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    Day of the Week
                  </Typography>
                </StyledTableCell>
                <StyledTableCell>
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    Start Time
                  </Typography>
                </StyledTableCell>
                <StyledTableCell>
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    End Time
                  </Typography>
                </StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              <TableRow>
                <StyledTableCell
                  sx={{
                    display: "flex",
                  }}
                >
                  This employee has no shift at the moment.
                </StyledTableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
        <AddShift
          id={id}
          timePeriods={timePeriods}
          setTimePeriods={setTimePeriods}
        />
      </>
    );
  }
  return (
    <>
      <div>
        <h1 style={{ marginTop: 20, marginBottom: 20 }}>Employee's Schedule</h1>
      </div>
      <TableContainer
        component={Paper}
        sx={{
          maxWidth: 1000,
          display: "flex",
          justifyContent: "center",
          maxHeight: "500px",
          boxShadow: 4,
          borderRadius: 1,
          my: 2,
          mx: "auto",
        }}
      >
        <Table stickyHeader aria-label="dense table">
          <TableHead>
            <TableRow>
              <StyledTableCell>
                <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                  Date
                </Typography>
              </StyledTableCell>
              <StyledTableCell>
                <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                  Day of the Week
                </Typography>
              </StyledTableCell>
              <StyledTableCell>
                <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                  Start Time
                </Typography>
              </StyledTableCell>
              <StyledTableCell>
                <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                  End Time
                </Typography>
              </StyledTableCell>
              <StyledTableCell> </StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>{row}</TableBody>
        </Table>
      </TableContainer>
      <AddShift
        id={id}
        timePeriods={timePeriods}
        setTimePeriods={setTimePeriods}
      />
    </>
  );
}

/**
 * Main function that returns the schedule of an employee as viewed by the employee for consultation
 *
 * @returns the ViewSchedule page
 * @author Victor
 */
export function EmployeeViewEmployeeSchedule() {
  const { userId } = useContext(LoginContext);

  const [timePeriods, setTimePeriods] = useState([]);

  const StyledTableCell = styled(TableCell)(() => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: "#ababab",
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  }));

  const row = timePeriods.map((timePeriod) => (
    <TableRow key={timePeriod.timePeriodId}>
      <TableCell>
        {getDay(timePeriod.startDate.split(" ")[0])}{" "}
        {getMonth(timePeriod.startDate.split(" ")[0])}{" "}
        {getYear(timePeriod.startDate.split(" ")[0])}
      </TableCell>
      <TableCell>{getDayOfWeek(timePeriod.startDate.split(" ")[0])}</TableCell>

      <TableCell>
        {timePeriod.startDate
          .split(" ")[1]
          .substring(0, timePeriod.startDate.split(" ")[1].length - 3)}
      </TableCell>
      <TableCell>
        {timePeriod.endDate
          .split(" ")[1]
          .substring(0, timePeriod.startDate.split(" ")[1].length - 3)}
      </TableCell>
    </TableRow>
  ));

  useEffect(() => {
    axios
      .get(`/api/scheduling/employee/shifts/${userId}`)
      .then(function (response) {
        setTimePeriods(response.data); // set the state to the data returned from the API
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [userId]);
  if (timePeriods.length === 0) {
    return (
      <>
        <div>
          <h1 style={{ marginTop: 20, marginBottom: 20 }}>
            Employee's Schedule
          </h1>
        </div>
        <TableContainer
          component={Paper}
          sx={{
            maxWidth: 1000,
            display: "flex",
            justifyContent: "center",
            maxHeight: "500px",
            boxShadow: 4,
            borderRadius: 1,
            my: 2,
            mx: "auto",
          }}
        >
          <Table stickyHeader aria-label="dense table">
            <TableHead>
              <TableRow>
                <StyledTableCell>
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    Date
                  </Typography>
                </StyledTableCell>
                <StyledTableCell>
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    Day of the Week
                  </Typography>
                </StyledTableCell>
                <StyledTableCell>
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    Start Time
                  </Typography>
                </StyledTableCell>
                <StyledTableCell>
                  <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                    End Time
                  </Typography>
                </StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              <TableRow>
                <StyledTableCell
                  sx={{
                    display: "flex",
                  }}
                >
                  This employee has no shift at the moment.
                </StyledTableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
      </>
    );
  }
  return (
    <>
      <div>
        <h1 style={{ marginTop: 20, marginBottom: 20 }}>Employee's Schedule</h1>
      </div>
      <TableContainer
        component={Paper}
        sx={{
          maxWidth: 1000,
          display: "flex",
          justifyContent: "center",
          maxHeight: "500px",
          boxShadow: 4,
          borderRadius: 1,
          my: 2,
          mx: "auto",
        }}
      >
        <Table stickyHeader aria-label="dense table">
          <TableHead>
            <TableRow>
              <StyledTableCell>
                <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                  Date
                </Typography>
              </StyledTableCell>
              <StyledTableCell>
                <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                  Day of the Week
                </Typography>
              </StyledTableCell>
              <StyledTableCell>
                <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                  Start Time
                </Typography>
              </StyledTableCell>
              <StyledTableCell>
                <Typography sx={{ fontWeight: "bold", fontSize: 18 }}>
                  End Time
                </Typography>
              </StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>{row}</TableBody>
        </Table>
      </TableContainer>
    </>
  );
}
