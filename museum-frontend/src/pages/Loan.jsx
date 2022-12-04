import React, { useEffect, useContext, useState } from "react";
import { LoginContext } from "../Contexts/LoginContext";
import axios from "axios";
import Container from "react-bootstrap/Container";
import {
  Grid,
  Card,
  CardContent,
  Box,
  Typography,
  CardMedia,
  Button,
  IconButton,
  Tooltip,
} from "@mui/material/";
import CircleIcon from "@mui/icons-material/Circle";
import DeleteIcon from "@mui/icons-material/Delete";
import { OnLoan } from "./OnLoan";

/**
 * Function to show loan fee if visitor or show loanee if staff
 * @param {*} props - artworkLoanFee, userRole and userName
 * @returns A Typography with text corresponding to view for visitor or staff
 * @author Eric
 */
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

/**
 * Function to show the status of a visitor's loan request
 * @param {*} props - loanElement, userRole
 * @returns A Typography with text corresponding to status of loan request
 * @author Eric
 */
function StatusOfVisitorLoanRequest(props) {
  if (props.userRole === "visitor") {
    if (props.loanElement.requestAccepted) {
      return (
        <div>
          <Typography
            variant="subtitle1"
            component="div"
            sx={{
              fontSize: 15,
            }}
          >
            Status of request: Accepted
          </Typography>
          <CircleIcon color="success" />
        </div>
      );
    } else if (props.loanElement.requestAccepted === false) {
      return (
        <div>
          <Typography
            variant="subtitle1"
            component="div"
            sx={{
              fontSize: 15,
            }}
          >
            Status of request: refused
          </Typography>
          <CircleIcon color="error" />
        </div>
      );
    } else {
      return (
        <div>
          <Typography
            variant="subtitle1"
            component="div"
            sx={{
              fontSize: 15,
            }}
          >
            Status of request: Waiting
          </Typography>
          <CircleIcon color="warning" />
        </div>
      );
    }
  } else {
    return "";
  }
}

/**
 * Function to show the room of the artwork that is requested for loan
 * @param {*} props - userRole, userName, loanStatus, artworkRoom
 * @returns A Typography with text corresponding to room of artwork
 * @author Eric
 */
function DisplayRoom(props) {
  if (props.userRole === "visitor") {
    return null;
  } else {
    return (
      <Typography variant="subtitle1" component="div" sx={{ fontSize: 15 }}>
        {"Artwork in room: " + props.artworkRoom.roomName}
      </Typography>
    );
  }
}

/**
 * Function to show cards for each loan request
 * @returns A card for each loan request
 * @author Eric
 */
function LoanRequests({ loanAccepted, setLoanAccepted }) {
  const { userId, userRole } = useContext(LoginContext);
  const [loans, setLoans] = useState([]);

  useEffect(() => {
    let url = "";
    if (userRole === "visitor") {
      url = `/api/loan/user/${userId}`;
    } else if (userRole === "manager" || userRole === "employee") {
      url = "/api/loan";
    }
    axios
      .get(url)
      .then((response) => {
        // if the request is successfull
        const loan = response.data;
        if (userRole === "visitor") {
          setLoans(loan);
        } else if (userRole === "manager" || userRole === "employee") {
          setLoans(loan.filter((aLoan) => aLoan.requestAccepted === null));
        }
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [userId, userRole]);

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
        setLoanAccepted(!loanAccepted);
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  }

  function HandleDeclineBtnClick(loan) {
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

  function HandleDeleteBtnClick(loanId) {
    axios
      .delete(`/api/loan/delete/${loanId}`)
      .then(function (response) {
        setLoans(loans.filter((aLoan) => aLoan.loanId !== loanId));
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
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
        {loans.length === 0 ? (
          "There are no loan requests"
        ) : (
          <Container>
            <Grid container spacing={3} justifyContent="center">
              {loans.map((loan) => (
                <Grid item key={loan.loanId} xs={4}>
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
                      <StatusOfVisitorLoanRequest
                        loanElement={loan}
                        userRole={userRole}
                      />
                      {userRole === "visitor" &&
                      (loan.requestAccepted === null ||
                        loan.requestAccepted === false) ? (
                        <Tooltip title="Delete" placement="bottom" arrow>
                          <IconButton
                            aria-label="delete"
                            size="medium"
                            onClick={(e) => HandleDeleteBtnClick(loan.loanId)}
                          >
                            <DeleteIcon fontSize="inherit" />
                          </IconButton>
                        </Tooltip>
                      ) : (
                        ""
                      )}
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

/**
 * Function to show onLoan page if user is staff member
 * @returns Loan page with onLoan page if staff member
 * @author Eric
 */
export function Loan() {
  const { userRole } = useContext(LoginContext);
  const [loanAccepted, setLoanAccepted] = useState(false);

  return (
    <>
      <Typography variant="h4" component="h1" marginTop={5} marginBottom={2}>
        Loan Requests
      </Typography>
      <LoanRequests
        loanAccepted={loanAccepted}
        setLoanAccepted={setLoanAccepted}
      />
      {userRole === "manager" || userRole === "employee" ? (
        <div>
          <Typography
            variant="h4"
            component="h1"
            marginTop={5}
            marginBottom={2}
          >
            Artworks currently on loan
          </Typography>
          <OnLoan loanAccepted={loanAccepted} />
        </div>
      ) : null}
    </>
  );
}
