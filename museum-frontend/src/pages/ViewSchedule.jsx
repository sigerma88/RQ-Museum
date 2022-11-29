import React, { useState, useEffect, useContext } from "react";
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
import { LoginContext } from "../Contexts/LoginContext";

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

function ViewSchedule() {
  const { userId } = useContext(LoginContext);

  const [timePeriods, setTimePeriods] = useState([]); // initial state set to empty array

  const StyledTableCell = styled(TableCell)(({ theme }) => ({
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
        // if the request is successful
        console.log(response.data);
        //indow.location.reload();
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
    </>
  );
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

export default ViewSchedule;
