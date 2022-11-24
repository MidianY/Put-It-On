import React from "react";
import './Page.css';
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import ClothingOptions from "../components/clothingOptions";


export default function Closet() {
    const navigate = useNavigate();
    return(
        <div>
        <div className="Page-header">Closet</div>
        <ClothingOptions></ClothingOptions>
        <Button onClick={() => {navigate("/")}}>Return home</Button>
        </div> 
    )
}