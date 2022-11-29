import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import { styled } from "@mui/material/styles";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { TimePicker } from "@mui/x-date-pickers/TimePicker";
import Stack from "@mui/material/Stack";
import {
  Button,
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
  const [date, setDate] = useState(null);
  //console.log({ date: getDate(date) });
  const [startTime, setStartTime] = useState(null);
  //console.log({ startTime: startTime && dayjs(startTime).format("HH:mm:ss") });
  const [endTime, setEndTime] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

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
  function handleRemove(event, tpId) {
    event.preventDefault();

    axios
      .delete(`/api/scheduling/employee/${id}/remove/shift/${tpId}`)
      .then(function (response) {
        // if the request is successful
        console.log(response.data);
        // for (let timePeriod of timePeriods) {
        //   if (timePeriod.timePeriodId === tpId) {
        setTimePeriods(
          timePeriods.filter((timePeriod) => timePeriod.timePeriodId !== tpId)
        );
        console.log(timePeriods);
        //   }
        // }
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  }

  //POST request to add a shift to the employee's schedulem
  //which entails first creating a time period and
  //then adding it to the employee's schedule
  function handleSubmit(event) {
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
  }

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
  function getYear(date) {
    const year = new Date(date).getUTCFullYear();
    return isNaN(year) ? null : year;
  }
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

  function getMonthNum(date) {
    const month = new Date(date).getMonth();
    return isNaN(month)
      ? null
      : ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"][month];
  }
  function getDay(date) {
    let day = new Date(date).getUTCDate();
    return isNaN(day) ? null : day;
  }

  function getDate(date) {
    return getYear(date) + "-" + getMonthNum(date) + "-" + getDay(date);
  }

  // const numOfTimePeriods = timePeriods.length;

  // Date formatted as YYYY, MONTH DAY
  //We only need the start date, because we are assuming that a shift spans one day at most
  // if the manager wants to schedule a shift that spans multiple days,
  //they can schedule multiple shifts

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

  const dates = timePeriods.map((timePeriod) => (
    <TableRow key={timePeriod.timePeriodId}>
      <TableCell>
        {getDay(timePeriod.startDate.split(" ")[0])}{" "}
        {getMonth(timePeriod.startDate.split(" ")[0])}{" "}
        {getYear(timePeriod.startDate.split(" ")[0])}
      </TableCell>
    </TableRow>
  ));
  // day of the week
  const dayOfStartDates = timePeriods.map((timePeriod) => (
    <TableRow key={timePeriod.timePeriodId}>
      <td> {getDayOfWeek(timePeriod.startDate.split(" ")[0])}</td>
    </TableRow>
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

      <h2 style={{ marginTop: 30 }}>Add Shift</h2>
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
      <form onSubmit={handleSubmit}>
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
