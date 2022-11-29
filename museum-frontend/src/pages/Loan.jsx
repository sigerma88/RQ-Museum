import React, { useEffect, useContext, useState } from "react";
import { LoginContext } from "../Contexts/LoginContext";
import axios from "axios";

export function Loan() {
  const { userName, userEmail, userId, userRole } = useContext(LoginContext);
  const [loans, setLoans] = useState([]); // initial state set to empty array

  //   useEffect(() => {
  // let url = "";
  // if (userRole === "visitor") {
  //   url = `/api/loan/view/${userId}`;
  // } else if (userRole === "manager" || userRole === "employee") {
  //   url = "/api/loan";
  // }
  //     axios
  //       .get("/api/loan")
  //       .then((response) => {
  //         // if the request is successfull
  //         const loan = response.data;
  //         setLoans(loan);
  //         console.log(response.data);
  //       })
  //       .catch(function (error) {
  //         const loan = error.response.data;
  //         console.log(error.response.data);
  //         setLoans(error.response.data);
  //         console.log(loans);
  //       });
  //   }, []);

  useEffect(() => {
    axios
      .get("/api/loan")
      .then((response) => {
        // if the request is successfull
        const loan = response.data;
        setLoans(loan);
        console.log(loans);
        console.log(response.data);
      })
      .catch(function (error) {
        const loan = error.response.data;
        console.log(error.response.data);
        setLoans(error.response.data);
        console.log(loans);
      });
  }, []);

  return (
    <>
      {loans.map((loan) => {
        <p>{loan.loanId}</p>;
      })}
    </>
  );
}
