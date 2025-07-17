// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './HomePage';
import ProfessorPage from './ProfessorPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/professor/:username" element={<ProfessorPage />} />
      </Routes>
    </Router>
  );
}

export default App;
