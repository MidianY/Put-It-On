import React, { useEffect, useState } from "react";
import './Page.css';
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import ClothingOptions from "../components/clothingOptions";
import UserCloset from "../components/userCloset";

// export default function Closet() {
//     const navigate = useNavigate();
//     const [viewCloset, setViewCloset] = useState(false)
//     const clickCloset = () => {
//         setViewCloset(!viewCloset)
//     }
//     return(
//         <div>
//         <div className="Page-header">Closet</div>
//         {!viewCloset &&
//         <ClothingOptions />}
//         {viewCloset &&
//         <UserCloset/>}
//         <Button onClick={clickCloset}>{viewCloset ? "Hide Closet" : "View Closet"}</Button>
//         <Button className="page-button" onClick={() => {navigate("/")}}>Return home</Button>
//         </div> 
//     )
// }

export default function Closet() {
    const navigate = useNavigate();
    // const [viewCloset, setViewCloset] = useState(false)
    // const clickCloset = () => {
    //     setViewCloset(!viewCloset)
    // }
    const [closetItems, setClosetItems] = useState(new Map<string, string[]>());
    return(
        <div>
        <div className="Page-header">Closet</div>
        <ClothingOptions />
        <UserCloset closetItems={closetItems}/>
        {/* {!viewCloset &&
        <ClothingOptions />}
        {viewCloset &&
        <UserCloset/>}
        <Button onClick={clickCloset}>{viewCloset ? "Hide Closet" : "View Closet"}</Button> */}
        <Button className="page-button" onClick={() => {navigate("/")}}>Return home</Button>
        </div> 
    )
}