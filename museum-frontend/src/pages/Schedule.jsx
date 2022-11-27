import React, {useState, useEffect}from 'react';
import axios from 'axios';
import {useParams} from 'react-router-dom';

/**
 * 
 * @author VZ and Kevin
 * @returns table that contains the shifts of an employee by id
 */
export function Schedule() {
  // const 
  const [timePeriods, setTimePeriods] = useState([]); // initial state set to empty array
  const [employee, setEmployee] = useState({}); // initial state set to empty array
  const {id} = useParams(); //get the employee id from the url

  useEffect(() => {
    axios 
    .get(`/api/scheduling/employee/shifts/${id}`)
    .then(function (response) {
      // if the request is successful
      console.log(response.data);
      setTimePeriods(response.data); // set the state to the data returned from the API
    })
    .catch(function (error) {
      console.log(error.response.data)

    })
    },  []);

  if (timePeriods.length === 0) {
    return <p>This employee has no shifts.</p>;
  }
  
  return (
    <div>
    <h1>Employee Schedule</h1>
    <table>
    <tbody>
    {timePeriods.map((timePeriod) => (
      <tr key={timePeriod.timePeriodId}>
        <td> {timePeriod.timePeriodId} </td>
        <td> {timePeriod.startDate} </td>
        <td> {timePeriod.endDate} </td>
      </tr>
      
    ))}
    </tbody>
    </table>
    </div>
  )
}
