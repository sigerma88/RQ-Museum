import React, { useState, useEffect } from "react";
import axios from "axios";

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
      <div>
        <h1>Employees</h1>
        <table style={{ width: "100%" }}>
          <thead>
            <tr>
              <th> Name </th>
              <th> Email </th>
              <th> View Schedule </th>
            </tr>
          </thead>
          <tbody>
            {employees.map((employee) => (
              <tr key={employee.museumUserId}>
                <td> {employee.name} </td>
                <td> {employee.email} </td>
                <td>
                  {" "}
                  <a href={`/schedule/${employee.museumUserId}`}>
                    View {grammarCheck(employee.name)} schedule
                  </a>{" "}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }
}

function grammarCheck(name) {
  return name.charAt(name.length - 1) !== "s" ? name + "'s" : name + "'";
}
