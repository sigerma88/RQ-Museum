import React, { useEffect, useContext, useState } from "react";
import { LoginContext } from "../Contexts/LoginContext";
import axios from "axios";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {
  Grid,
  Card,
  CardContent,
  Box,
  Typography,
  CardMedia,
  Paper,
  Button,
  ButtonGroup,
} from "@mui/material/";

function StatusOrLoanee(props) {
  if (props.userRole === "visitor") {
    return (
      <Typography variant="subtitle1" component="div" sx={{ fontSize: 15 }}>
        {"Loan fee: " + props.artworkLoanFee + "$"}
      </Typography>
    );
  } else if (props.userRole === "manager" || props.userRole === "employee") {
    return (
      <Typography variant="subtitle1" component="div" sx={{ fontSize: 15 }}>
        {"Loan to: " + props.userName}
      </Typography>
    );
  }
}

function DisplayRoom(props) {
  if (props.userRole === "visitor") {
    return null;
  } else {
    return (
      <Typography variant="subtitle1" component="div" sx={{ fontSize: 15 }}>
        {"Artwork in room: " + props.artworkRoom}
      </Typography>
    );
  }
}

function DisplayButton(props) {
  //state variables for the Modal State

  function HandleAcceptBtnClick() {
    // POST request using fetch inside useEffect React hook
    axios.put("/api/loan/edit", {
      loanId: props.loanElement.loanId,
      requestAccepted: true,
      artworkDto: props.loanElement.artworkDto,
      visitorDto: props.loanElement.visitorDto,
    });
    alert("Loan accepted");
  }

  function HandleDeclineBtnClick() {
    // POST request using fetch inside useEffect React hook
    {
      axios.put("/api/loan/edit", {
        loanId: props.loanElement.loanId,
        requestAccepted: false,
        artworkDto: props.loanElement.artworkDto,
        visitorDto: props.loanElement.visitorDto,
      });
    }
  }

  if (props.userRole === "visitor") {
    return null;
  } else if (props.userRole === "manager" || props.userRole === "employee") {
    return (
      <div>
        <Box
          sx={{
            "& button": { m: 1 },
          }}
        >
          <Button
            variant="contained"
            color="success"
            onClick={(e) => HandleAcceptBtnClick(props.loanElement)}
          >
            Accept
          </Button>
          <Button
            variant="contained"
            color="error"
            onClick={(e) => HandleDeclineBtnClick(props.loanElement)}
          >
            Decline
          </Button>
        </Box>
        {/* {acceptBtnState && (
          <Container>{props.loanElement.artworkDto.name}</Container>
        )} */}
        {/* <PopUp
          isAcceptBtnClicked={acceptBtnState}
          isDeclineBtnClicked={declineBtnState}
          //loanElement={loan}
          sx={{
            position: "relative",
          }}
        /> */}
      </div>
    );
  }
}
// function PopUp(props) {
//   if ((props.isAcceptBtnClicked = true)) {
//     return <Container>Hello World</Container>;
//   }
// }

export function Loan() {
  const [loans, setLoans] = useState([]); // initial state set to empty array
  const { userName, userEmail, userId, userRole } = useContext(LoginContext);

  useEffect(() => {
    let url = "";
    if (userRole === "visitor") {
      url = `/api/loan/view/${userId}`;
    } else if (userRole === "manager" || userRole === "employee") {
      url = "/api/loan";
    }
    axios
      .get(url)
      .then((response) => {
        // if the request is successfull
        const loan = response.data;
        setLoans(
          response.data.filter((aLoan) => aLoan.requestAccepted === null)
        );
        console.log(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }, []);

  function HandleAcceptBtnClick(loan) {
    // POST request using fetch inside useEffect React hook
    axios
      .put("/api/loan/edit", {
        loanId: loan.loanId,
        requestAccepted: true,
        artworkDto: loan.artworkDto,
        visitorDto: loan.visitorDto,
      })
      .then(function (response) {
        setLoans(loans.filter((aLoan) => aLoan.loanId !== loan.loanId));
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  }

  function HandleDeclineBtnClick(loan) {
    // POST request using fetch inside useEffect React hook
    {
      axios
        .put("/api/loan/edit", {
          loanId: loan.loanId,
          requestAccepted: false,
          artworkDto: loan.artworkDto,
          visitorDto: loan.visitorDto,
        })
        .then(function (response) {
          setLoans(loans.filter((aLoan) => aLoan.loanId !== loan.loanId));
        })
        .catch(function (error) {
          console.log(error.response.data);
        });
    }
  }

  return (
    <>
      <Box
        sx={{
          marginTop: 5,
          marginLeft: 5,
          marginRight: 5,
          marginBottom: 5,
        }}
      >
        {loans.length == 0 ? (
          "There are no loan requests"
        ) : (
          <Container>
            <Grid container spacing={3} justifyContent="center">
              {loans.map((loan) => (
                <Grid item key={loan.loanID} xs={4}>
                  <Card
                    sx={{
                      display: "flex",
                      flexDirection: "column",
                      height: 400,
                    }}
                  >
                    <CardContent>
                      <CardMedia
                        component="img"
                        image={loan.artworkDto.image}
                        alt="Artwork image"
                        sx={{
                          display: "box",
                          marginLeft: "auto",
                          marginRight: "auto",
                          height: 200,
                          objectFit: "cover",
                        }}
                      />
                      <Typography
                        variant="h8"
                        component="div"
                        sx={{
                          fontSize: 20,
                          overflow: "hidden",
                          whiteSpace: "nowrap",
                          textOverflow: "ellipsis",
                        }}
                      >
                        {loan.artworkDto.name}
                      </Typography>
                      <Typography
                        variant="subtitle1"
                        component="div"
                        sx={{
                          fontSize: 15,
                        }}
                      >
                        {"by " + loan.artworkDto.artist}
                      </Typography>
                      <DisplayRoom
                        userRole={userRole}
                        userName={loan.visitorDto.name}
                        loanStatus={loan.requestAccepted}
                        artworkRoom={loan.artworkDto.room}
                      />
                      <StatusOrLoanee
                        userRole={userRole}
                        userName={loan.visitorDto.name}
                        artworkLoanFee={loan.artworkDto.loanFee}
                      />
                    </CardContent>
                    {userRole === "manager" || userRole === "employee" ? (
                      <div>
                        <Box
                          sx={{
                            "& button": { m: 1 },
                          }}
                        >
                          <Button
                            variant="contained"
                            color="success"
                            onClick={(e) => HandleAcceptBtnClick(loan)}
                          >
                            Accept
                          </Button>
                          <Button
                            variant="contained"
                            color="error"
                            onClick={(e) => HandleDeclineBtnClick(loan)}
                          >
                            Decline
                          </Button>
                        </Box>
                        {/* {acceptBtnState && (
          <Container>{props.loanElement.artworkDto.name}</Container>
        )} */}
                        {/* <PopUp
          isAcceptBtnClicked={acceptBtnState}
          isDeclineBtnClicked={declineBtnState}
          //loanElement={loan}
          sx={{
            position: "relative",
          }}
        /> */}
                      </div>
                    ) : (
                      ""
                    )}
                  </Card>
                </Grid>
              ))}
            </Grid>
          </Container>
        )}
      </Box>
    </>
  );
}
