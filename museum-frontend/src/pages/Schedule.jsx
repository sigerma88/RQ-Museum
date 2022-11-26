import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
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
import TextField from "@mui/material/TextField";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { TimePicker } from "@mui/x-date-pickers/TimePicker";
import Stack from "@mui/material/Stack";

/**
 *
 * @author VZ and Kevin
 * @returns table that contains the shifts of an employee by id
 */
export function Schedule() {
  // const
  const [timePeriods, setTimePeriods] = useState([]); // initial state set to empty array
  const [employee, setEmployee] = useState({}); // initial state set to empty array
  const { id } = useParams(); //get the employee id from the url
  const [value, setValue] = useState(null);

  useEffect(() => {
    axios
      .get(`/api/scheduling/employee/shifts/${id}`)
      .then(function (response) {
        // if the request is successful
        console.log(response.data);
        setTimePeriods(response.data); // set the state to the data returned from the API
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  }, []);

  function handleSubmit(event) {
    event.preventDefault();

    axios
      .post(`/api/shift/create`, {
        startDate: "2021-10-01",
        endDate: "2021-10-01",
      })
      .then(function (response) {
        console.log(response.data);
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  }

  function getDayOfWeek(date) {
    const dayOfWeek = new Date(date).getDay();
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
  function getYear(date) {
    const year = new Date(date).getFullYear();
    return isNaN(year) ? null : year;
  }
  function getMonth(date) {
    const month = new Date(date).getMonth();
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
  function getDay(date) {
    const day = new Date(date).getDate();
    return isNaN(day) ? null : day;
  }

  const numOfTimePeriods = timePeriods.length;

  // Date formatted as YYYY, MONTH DAY
  //We only need the start date, because we are assuming that a shift spans one day at most
  // if the manager wants to schedule a shift that spans multiple days,
  //they can schedule multiple shifts
  const dates = timePeriods.map((timePeriod) => (
    <tr key={timePeriod.timePeriodId}>
      <td>
        {getDay(timePeriod.startDate.split(" ")[0])}{" "}
        {getMonth(timePeriod.startDate.split(" ")[0])}{" "}
        {getYear(timePeriod.startDate.split(" ")[0])}
      </td>
    </tr>
  ));
  // day of the week
  const dayOfStartDates = timePeriods.map((timePeriod) => (
    <tr key={timePeriod.timePeriodId}>
      <td> {getDayOfWeek(timePeriod.startDate.split(" ")[0])}</td>
    </tr>
  ));

  //start times of shifts for each day, formatted to display HH:mm
  const startTimes = timePeriods.map((timePeriod) => (
    <tr key={timePeriod.timePeriodId}>
      <td>
        {" "}
        {timePeriod.startDate
          .split(" ")[1]
          .substring(0, timePeriod.startDate.split(" ")[1].length - 3)}
      </td>
    </tr>
  ));

  const endTimes = timePeriods.map((timePeriod) => (
    <tr key={timePeriod.timePeriodId}>
      <td>
        {" "}
        {timePeriod.endDate
          .split(" ")[1]
          .substring(0, timePeriod.startDate.split(" ")[1].length - 3)}
      </td>
    </tr>
  ));

  const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: "#ababab",
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  }));

  const header = createTheme({
    typography: {
      fontWeight: "bold",
      fontSize: 18,
    },
  });

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
            maxWidth: "1000px",
            boxShadow: 4,
            borderRadius: 1,
            my: 2,
            mx: "auto",
          }}
        >
          <Table stickyHeader aria-label="dense table">
            <TableHead>
              <StyledTableCell>
                <Typography sx={header}>Date</Typography>
              </StyledTableCell>
              <StyledTableCell>
                <Typography sx={header}>Day of the Week</Typography>
              </StyledTableCell>
              <StyledTableCell>
                <Typography sx={header}>Start Time</Typography>
              </StyledTableCell>
              <StyledTableCell>
                <Typography sx={header}>End Time</Typography>
              </StyledTableCell>
            </TableHead>
            <TableBody>
              <TableRow>
                <StyledTableCell
                  sx={{
                    display: "flex",
                  }}
                >
                  This employee currently has no shifts.
                </StyledTableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>
        <h2 style={{ marginTop: 30 }}>Add Shift</h2>
        <div style={{ marginTop: 30 }}>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <Stack justifyContent="center" direction="row" spacing={3}>
              <DatePicker
                label="Select Date"
                value={value}
                onChange={(newValue) => {
                  setValue(newValue);
                }}
                renderInput={(params) => <TextField {...params} />}
              />
              <TimePicker
                label="Select Start Time"
                value={value}
                onChange={(newValue) => {
                  setValue(newValue);
                }}
                renderInput={(params) => <TextField {...params} />}
              />
              <TimePicker
                label="Select End Time"
                value={value}
                onChange={(newValue) => {
                  setValue(newValue);
                }}
                renderInput={(params) => <TextField {...params} />}
              />
            </Stack>
          </LocalizationProvider>
        </div>
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
          maxWidth: "1000px",
          boxShadow: 4,
          borderRadius: 1,
          my: 2,
          mx: "auto",
        }}
      >
        <Table stickyHeader aria-label="dense table">
          <TableHead>
            <StyledTableCell>
              <Typography sx={header}>Date</Typography>
            </StyledTableCell>
            <StyledTableCell>
              <Typography sx={header}>Day of the Week</Typography>
            </StyledTableCell>
            <StyledTableCell>
              <Typography sx={header}>Start Time</Typography>
            </StyledTableCell>
            <StyledTableCell>
              <Typography sx={header}>End Time</Typography>
            </StyledTableCell>
          </TableHead>
          <TableBody>
            <TableRow>
              <StyledTableCell>{dates}</StyledTableCell>
              <StyledTableCell>{dayOfStartDates}</StyledTableCell>
              <StyledTableCell>{startTimes}</StyledTableCell>
              <StyledTableCell>{endTimes}</StyledTableCell>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
      <h2 style={{ marginTop: 30 }}>Add Shift</h2>
      <div style={{ marginTop: 30 }}>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <Stack justifyContent="center" direction="row" spacing={3}>
            <DatePicker
              label="Select Date"
              value={value}
              onChange={(newValue) => {
                setValue(newValue);
              }}
              renderInput={(params) => <TextField {...params} />}
            />
            <TimePicker
              label="Select Start Time"
              value={value}
              onChange={(newValue) => {
                setValue(newValue);
              }}
              renderInput={(params) => <TextField {...params} />}
            />
            <TimePicker
              label="Select End Time"
              value={value}
              onChange={(newValue) => {
                setValue(newValue);
              }}
              renderInput={(params) => <TextField {...params} />}
            />
          </Stack>
        </LocalizationProvider>
      </div>
    </>
  );
}
