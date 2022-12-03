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

/**
 * Helper function to get the day of the week from a date
 *
 * @param date
 * @returns day of the week
 * @author Victor
 */
function getDayOfWeek(date) {
  const dayOfWeek = new Date(date).getUTCDay();
  return isNaN(dayOfWeek)
    ? null
    : [
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
      ][dayOfWeek];
}

/**
 * Helper function to get the year from a date
 *
 * @param date
 * @returns year of the date
 * @author Victor
 */
function getYear(date) {
  const year = new Date(date).getUTCFullYear();
  return isNaN(year) ? null : year;
}

/**
 * Helper function to get the month from a date
 *
 * @param date
 * @returns month of the date
 * @author Victor
 */
function getMonth(date) {
  const month = new Date(date).getUTCMonth();
  return isNaN(month)
    ? null
    : [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
      ][month];
}
/**
 * Helper function that returns the numerical month of a date
 *
 * @param date
 * @returns numerical month of the date
 * @author Victor
 */
function getMonthNum(date) {
  const month = new Date(date).getMonth();
  return isNaN(month)
    ? null
    : ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"][month];
}

/**
 * Helper function to get the day from a date
 *
 * @param date
 * @returns day of the date
 * @author Victor
 */
function getDay(date) {
  let day = new Date(date).getUTCDate();
  return isNaN(day) ? null : day;
}

/**
 * Helper function that returns a formatted date YYYY-MM-DD to feed the JSON object in the POST request
 *
 * @param date
 * @returns formatted date YYYY-MM-DD
 * @author Victor
 */
function getDate(date) {
  return getYear(date) + "-" + getMonthNum(date) + "-" + getDay(date);
}

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
  const [errorMessage, setErrorMessage] = useState("");
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
          console.log(response.data);
          const tp = response.data;
          setErrorMessage("");
          setIsFormInvalid(false);
          //ADD THE SHIFT TO THE EMPLOYEE'S SCHEDULE
          axios
            .post(
              `/api/scheduling/employee/${id}/add/shift/${response.data.timePeriodId}`
            )
            .then(function (response) {
              // if the request is successful
              console.log(response.data);
              setTimePeriods([...timePeriods, tp]); // set the state to the data returned from the API
            })
            .catch(function (error) {
              console.log(error.response.data);
            });
        }
      })
      .catch(function (error) {
        console.log(error.response.data);
        setErrorMessage(error.response.data);
        setIsFormInvalid(true);
      });
  };

  return (
    <>
      <h2 style={{ marginTop: 30 }}>Add Shift</h2>
      <form onSubmit={handleSubmit}>
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
                    helperText={isFormInvalid && errorMessage}
                    error={isFormInvalid && errorMessage}
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
                    helperText={isFormInvalid && errorMessage}
                    error={isFormInvalid && errorMessage}
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
                    helperText={isFormInvalid && errorMessage}
                    error={isFormInvalid && errorMessage}
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
  // const
  const [timePeriods, setTimePeriods] = useState([]); // initial state set to empty array
  const { id } = useParams(); //get the employee id from the url

  //GET request to get the employee's schedule by id
  useEffect(() => {
    axios
      .get(`/api/scheduling/employee/shifts/${id}`)
      .then(function (response) {
        // if the requepst is successful
        console.log(response.data);
        setTimePeriods(response.data); // set the state to the data returned from the API
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  }, []);

  //DELETE request to remove the timeperiod from the employee's schedule
  const handleRemove = async (event, tpId) => {
    event.preventDefault();

    axios
      .delete(`/api/scheduling/employee/${id}/remove/shift/${tpId}`)
      .then(function (response) {
        // if the request is successful
        console.log(response.data);
        setTimePeriods(
          timePeriods.filter((timePeriod) => timePeriod.timePeriodId !== tpId)
        );
        console.log(timePeriods);
      })
      .catch(function (error) {
        console.log(error.response.data);
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

  const [timePeriods, setTimePeriods] = useState([]); // initial state set to empty array

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
        console.log(error.response.data);
      });
  }, [userId]);

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
    </>
  );
}
