import React, { useEffect, useState } from "react";
import './Page.css';
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import ClothingOptions from "../components/clothingOptions";
import UserCloset from "../components/userCloset";
import ClothingItem from "../components/clothingItem";

export default function Closet() {
    const navigate = useNavigate();
    const [closetItems, setClosetItems] = useState<Map<string, string>[]>([]);
    const fetchCloset = async(): Promise<void> => {
        let closet = await fetch("http://localhost:3230/getCloset")
        .then(response => response.json()).then(json => {return json})
        .catch(err => console.log('error', err))
        if (closet.result === "success") {
            let closetItems = closet.closet;
            let clothesList: Map<string, string>[] = [];
            closetItems.map((item: Map<string, string>) => {
                clothesList.push(item);
            })
            setClosetItems(clothesList);
        }
    }
    
    useEffect(() => {
        fetchCloset();
    }, [])

    return(
        <div>
        <div aria-label="closet page header" className="Page-header">Closet</div>
        <div aria-label="closet instructions" className="closet-instructions">Click on a clothing item and color to add it to your closet!</div>
        <ClothingOptions pickColor={fetchCloset}/>
        <UserCloset closetItems={closetItems} pickColor={fetchCloset}/>
        <Button aria-label="button to go to home page" className="page-button" onClick={() => {navigate("/")}}>Return home</Button>
        </div> 
    )
}