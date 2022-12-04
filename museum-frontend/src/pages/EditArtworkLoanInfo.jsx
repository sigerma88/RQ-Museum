import React, { useState } from "react";
import axios from "axios";
import {
  Typography,
  TextField,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import LoadingButton from "@mui/lab/LoadingButton";

/**
 * Function to check if the input is in currency format
 *
 * @param value - The value to check
 * @returns  True if the value is in currency format, false otherwise
 * @author Siger
 */
function isCurrency(value) {
  return /^\d+(\.\d{1,2})?$/.test(value);
}

/**
 * Function for the dialog to edit an artwork's loan info
 *
 * @returns  A form to edit the artwork loan info
 * @author kieyanmamiche
 * @author Siger
 */

export function EditArtworkLoanInfo({
  open,
  handleClose,
  artwork,
  setArtwork,
}) {
  const artworkId = artwork.artworkId;
  const [isAvailableForLoan, setIsAvailableForLoan] = useState(
    artwork.isAvailableForLoan
  );
  const [loanFee, setLoanFee] = useState(artwork.loanFee);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [isFormInvalid, setIsFormInvalid] = useState(false);

  // Function which is called when we submit the form
  const handleSubmit = async (event) => {
    event.preventDefault();

    // Check if loan fee is in currency format
    if (!isCurrency(loanFee)) {
      setErrorMessage("Loan fee must be in currency format");
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
            artwork.isAvailableForLoan = isAvailableForLoan;
            artwork.loanFee = loanFee;
            setArtwork(artwork);
            handleClose();
          } else {
            setErrorMessage("Something went wrong");
            setIsFormInvalid(true);
          }
        })
        .catch(function (error) {
          console.log(error);
          setErrorMessage(error.response.data);
          setIsFormInvalid(true);
        });
    }

    setLoading(false);
  };

  return (
    <>
      <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
        <DialogTitle>Edit Artwork Loan Information</DialogTitle>

        <DialogContent
          style={{
            display: "flex",
            justifyContent: "space-evenly",
            alignContent: "center",
            marginTop: "3%",
          }}
        >
          <form
            style={{ width: "100%", alignContent: "flex-end" }}
            onSubmit={(event) => {
              setLoading(true);
              handleSubmit(event);
            }}
          >
            <Box
              sx={{
                marginTop: 3,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
              }}
            >
              <Typography>Artwork ID: {artworkId}</Typography>
              <FormControl sx={{ width: "60%", marginBottom: 3, marginTop: 3 }}>
                <InputLabel id="demo-simple-select-label">
                  Available for loan?
                </InputLabel>
                <Select
                  labelId="isAvailableForLoan"
                  id="isAvailableForLoan"
                  label="Available for loan?"
                  value={isAvailableForLoan}
                  onChange={(e) => setIsAvailableForLoan(e.target.value)}
                  error={isFormInvalid}
                >
                  <MenuItem value={"true"}>Available for loan</MenuItem>
                  <MenuItem value={"false"}>Not available for loan</MenuItem>
                </Select>
              </FormControl>
              <TextField
                id="loanFee"
                label="Loan Fee"
                name="Loan Fee"
                margin="normal"
                autoFocus
                sx={{ width: "60%" }}
                value={loanFee}
                onChange={(e) => setLoanFee(e.target.value)}
                error={isFormInvalid}
              />
              <Typography variant="p" component="p" color="red">
                {isFormInvalid && errorMessage}
              </Typography>
            </Box>

            <DialogActions>
              <LoadingButton
                variant="contained"
                loading={loading}
                loadingPosition="end"
                sx={{ mt: 3, mb: 2, width: "50%" }}
                type="submit"
              >
                Submit
              </LoadingButton>
            </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </>
  );
}
