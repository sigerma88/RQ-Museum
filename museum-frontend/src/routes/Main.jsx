import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "../pages/Home";
import { Navigation } from "../layouts/Navigation";
export function Main() {
  return (
    <BrowserRouter>
      <Navigation />
      <Routes>
        <Route path="/" index element={<Home />} />
      </Routes>
    </BrowserRouter>
  );
}
