import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Typography,
  TextField,
  Button,
  Box,
  Paper,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import "./Common.css";

/**
 * Edit EditArtworkLoanInfo
 * @returns  A form to edit the artwork loan info
 * @author kieyanmamiche
 */

export function EditArtworkLoanInfo() {
  const [artworkId, setArtworkId] = useState(null);
  const [isAvailableForLoan, setIsAvailableForLoan] = useState(null);
  const [loanFee, setLoanFee] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  /**
   * To set EditArtworkLoanInfo parameters to null when text input is just whitespace
   * @author kieyanmamiche
   */
  useEffect(() => {
    if (artworkId != null && artworkId.trim().length === 0) {
      setArtworkId(null);
    }

    if (isAvailableForLoan != null && isAvailableForLoan.trim().length === 0) {
      setIsAvailableForLoan(null);
    }

    if (loanFee != null && loanFee.trim().length === 0) {
      setLoanFee(null);
    }
  }, [artworkId, isAvailableForLoan, loanFee]);

  /**
   * Function which is called when we submit the form
   * @author kieyanmamiche
   */
  const handleSubmit = async (event) => {
    event.preventDefault();

    if (artworkId == null) {
      setErrorMessage("No artwork id entered");
      setIsFormInvalid(true);
    } else {
      axios
        .put(`/api/artwork/loanInfo/${artworkId}`, {
          isAvailableForLoan: isAvailableForLoan,
          loanFee: loanFee,
        })
        .then(function (response) {
          if (response.status === 200) {
            setErrorMessage(null);
            setIsFormInvalid(false);

            if (isAvailableForLoan !== null) {
              setIsAvailableForLoan(isAvailableForLoan);
              localStorage.setItem("isAvailableForLoan", isAvailableForLoan);
            }

            if (loanFee !== null) {
              setLoanFee(loanFee);
              localStorage.setItem("loanFee", loanFee);
            }
          }
        })
        .catch(function (error) {
          setErrorMessage(error.response.data);
          setIsFormInvalid(true);
        });
    }
  };

  /**
   * The component that we return which represents the EditArtworkLoanInfo Form
   * @author kieyanmamiche
   */
  return (
    <>
      <div className="EditArtworkLoanInfo" style={{ marginTop: "3%" }}>
        <Typography style={{ fontSize: "36px" }} gutterBottom>
          Edit Artwork Loan Info
        </Typography>

        <div
          style={{
            display: "flex",
            justifyContent: "space-evenly",
            alignContent: "center",
            marginTop: "3%",
          }}
        >
          <Paper
            elevation={3}
            style={{
              width: "60%",
              padding: "50px 50px",
            }}
          >
            <form
              style={{ width: "100%", alignContent: "flex-end" }}
              onSubmit={handleSubmit}
            >
              <Box
                sx={{
                  marginTop: 3,
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                }}
              >
                <TextField
                  margin="normal"
                  id="artworkId"
                  label="ArtworkId"
                  name="artworkId"
                  autoComplete="artworkId"
                  autoFocus
                  onChange={(e) => setArtworkId(e.target.value)}
                  defaultValue={artworkId}
                  className="login-field"
                />
                <FormControl className="login-field">
                  <InputLabel id="demo-simple-select-label">
                    Is artwork available for loan?
                  </InputLabel>
                  <Select
                    labelId="isAvailableForLoan"
                    id="isAvailableForLoan"
                    label="Is artwork available for loan"
                    onChange={(e) => setIsAvailableForLoan(e.target.value)}
                  >
                    <MenuItem value={"true"}>Available for loan</MenuItem>
                    <MenuItem value={"false"}>Not available for loan</MenuItem>
                  </Select>
                </FormControl>
                <TextField
                  margin="normal"
                  id="loanFee"
                  label="Loan Fee"
                  name="Loan Fee"
                  autoComplete="Loan Fee"
                  autoFocus
                  onChange={(e) => setLoanFee(e.target.value)}
                  defaultValue={loanFee}
                  className="login-field"
                />
                <div
                  style={{
                    height: 20,
                  }}
                >
                  {" "}
                </div>
                <Typography variant="p" component="p" color="red">
                  {isFormInvalid && errorMessage}
                </Typography>
              </Box>
              <Button
                type="submit"
                variant="contained"
                sx={{ mt: 3, mb: 2, width: "40%" }}
              >
                Edit Artwork Loan Info
              </Button>
            </form>
          </Paper>
        </div>
      </div>
    </>
  );
}
