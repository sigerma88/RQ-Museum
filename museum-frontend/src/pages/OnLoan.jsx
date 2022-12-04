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
  IconButton,
  Tooltip,
} from "@mui/material/";
import DeleteIcon from "@mui/icons-material/Delete";

/**
 * Function to show cards for artworks on loan
 * @returns A card for each artwork on loan
 * @author Eric
 */
export function OnLoan({ loanAccepted }) {
  const [loans, setLoans] = useState([]);
  const { userId, userRole } = useContext(LoginContext);

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
        setLoans(loan.filter((aLoan) => aLoan.requestAccepted === true));
      })
      .catch(function (error) {
        console.log(error);
      });
  }, [userId, userRole, loanAccepted]);

  function HandleDeleteBtnClick(loanId) {
    axios
      .delete(`/api/loan/delete/${loanId}`)
      .then(function (response) {
        console.log(response.data);
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
          "There are artworks on loan"
        ) : (
          <Container>
            <Grid container spacing={3} justifyContent="center">
              {loans.map((loan) => (
                <Grid item key={loan.loanId} xs={4}>
                  <Card
                    sx={{
                      display: "flex",
                      flexDirection: "column",
                      height: 340,
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
                      <Typography
                        variant="subtitle1"
                        component="div"
                        sx={{ fontSize: 15 }}
                      >
                        {"Loaned to: " + loan.visitorDto.name}
                      </Typography>
                      <Tooltip title="Delete" placement="bottom" arrow>
                        <IconButton
                          aria-label="delete"
                          size="medium"
                          onClick={(e) => HandleDeleteBtnClick(loan.loanId)}
                        >
                          <DeleteIcon fontSize="inherit" />
                        </IconButton>
                      </Tooltip>
                    </CardContent>
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
