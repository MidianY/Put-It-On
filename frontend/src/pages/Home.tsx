import React from "react";
import './Page.css';
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import CitySearch from "../components/citySearch";

export default function Home() {
    const navigate = useNavigate();
    return(
        <div>
            <div className="Page-header">Put It On!</div>
            <CitySearch />
            <Button onClick={() => {navigate("/closet")}}>Go to closet</Button>
        </div>
    )
}