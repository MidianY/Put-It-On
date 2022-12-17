import React from 'react';
import { render, screen } from '@testing-library/react';
import {BrowserRouter} from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import App from '../App'

test('Home page properly rendered initially', () => {
    render(<BrowserRouter><App /></BrowserRouter>)
    const cityInput = screen.getByRole("textbox" ,{name: "enter city name here"});
    const header = screen.getByRole(/.*/, {name: "put it on page header"});
    const closetButton = screen.getByRole(/.*/, {name: "button to go to closet page"});
    expect(cityInput).toBeInTheDocument();
    expect(header).toBeInTheDocument();
    expect(closetButton).toBeInTheDocument();
})

test('Test closet stats initializes correctly', async () => {
    render(<BrowserRouter><App /></BrowserRouter>)
    const closetStats = screen.getByRole(/.*/, {name: "closet stats"})
    expect(closetStats).toBeInTheDocument();
    const tops = screen.getByRole(/.*/, {name: "0 tops"})
    const bottoms = screen.getByRole(/.*/, {name: "0 bottoms"})
    const outers = screen.getByRole(/.*/, {name: "0 outerwear"})
    const shoes = screen.getByRole(/.*/, {name: "0 shoes"})
    expect(tops).toBeInTheDocument();
    expect(bottoms).toBeInTheDocument();
    expect(outers).toBeInTheDocument();
    expect(shoes).toBeInTheDocument();
})

// test('', async() => {
//     render(<BrowserRouter><App /></BrowserRouter>)
//     const cityInput = screen.getByRole("textbox" ,{name: "enter city name here"});
//     userEvent.type(cityInput, "providence");
//     const searchResult = await screen.findByText('Providence, RI')
// })

