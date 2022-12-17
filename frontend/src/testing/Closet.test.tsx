import React from 'react';
import { render, screen } from '@testing-library/react';
import {BrowserRouter} from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import App from '../App'

test('Clicking on home page button takes user to closet page, which renders correctly', () => {
    render(<BrowserRouter><App /></BrowserRouter>)
    const closetButton = screen.getByRole(/.*/, {name: "button to go to closet page"});
    userEvent.click(closetButton);
    const header = screen.getByRole(/.*/, {name: "closet page header"});
    const homeButton = screen.getByRole(/.*/, {name: "button to go to home page"});
    expect(header).toBeInTheDocument();
    expect(homeButton).toBeInTheDocument();
})

test('Closet options render correctly', async() => {
    render(<BrowserRouter><App /></BrowserRouter>)
    const closetOptions = screen.getByRole(/.*/, {name: "closet options"})
    const tops = await screen.findByText("Tops");
    const bottoms = await screen.findByText("Bottoms");
    const outer = await screen.findByText("Outer");
    const shoes = await screen.findByText("Shoes");
    const topItems = screen.getByRole(/.*/, {name: "tops tab"})
    expect(closetOptions).toBeInTheDocument();
    expect(tops).toBeInTheDocument();
    expect(bottoms).toBeInTheDocument();
    expect(outer).toBeInTheDocument();
    expect(shoes).toBeInTheDocument();
    expect(topItems).toBeInTheDocument();
    userEvent.click(bottoms);
    const bottomItems = screen.getByRole(/.*/, {name: "bottoms tab"})
    expect(bottomItems).toBeInTheDocument();
    userEvent.click(outer);
    const outerItems = screen.getByRole(/.*/, {name: "outerwear tab"})
    expect(outerItems).toBeInTheDocument();
    userEvent.click(shoes);
    const shoesItems = screen.getByRole(/.*/, {name: "shoes tab"})
    expect(shoesItems).toBeInTheDocument();
})

test('Button sends user back to home page', () => {
    render(<BrowserRouter><App /></BrowserRouter>)
    const homeButton = screen.getByRole(/.*/, {name: "button to go to home page"});
    userEvent.click(homeButton);
    const closetButton = screen.getByRole(/.*/, {name: "button to go to closet page"});
    expect(closetButton).toBeInTheDocument();
})