import React from "react";
import './Page.css';
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';

export default function Home() {
    const navigate = useNavigate();
    return(
        <div>
            <div className="Page-header">Put It On!</div>
            <Button onClick={() => {navigate("/closet")}}>Go to closet</Button>
        </div>
    )
}