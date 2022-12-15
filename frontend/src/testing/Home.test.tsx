import React from 'react';
import { render, screen } from '@testing-library/react';
import {Routes, Route, BrowserRouter as Router, BrowserRouter } from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import App from '../App'
import Home from '../pages/Home'

test('Home page properly rendered initially', () => {
    render(<BrowserRouter><App /></BrowserRouter>)
    const cityInput = screen.getByRole("textbox" ,{name: "enter city name here"});
    const header = screen.getByRole(/.*/, {name: "put it on page header"});
    const closetButton = screen.getByRole(/.*/, {name: "button to go to closet page"});
    expect(cityInput).toBeInTheDocument();
    expect(header).toBeInTheDocument();
    expect(closetButton).toBeInTheDocument();
})

test('Entering city name functionality', () => {
    render(<BrowserRouter><Home /></BrowserRouter>)
    const cityInput = screen.getByRole("textbox" ,{name: "enter city name here"});
    userEvent.type(cityInput, "providenc");
    const searchOptions = screen.getByRole(/.*/, {name: "city option: Providence, KY "});

})

