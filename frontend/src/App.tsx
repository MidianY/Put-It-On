import React from 'react';
import './App.css';
import Home from './pages/Home';
import Closet from './pages/Closet';
import {Routes, Route } from 'react-router-dom';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/closet" element={<Closet />} />
      </Routes>
    </div>
  );
}

export default App;
