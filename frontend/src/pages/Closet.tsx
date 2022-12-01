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
    const chooseClothingItem = (color: string, clothingType: string) => {
        if (Array.from(closetItems.keys()).includes(clothingType)) {
            let clothingList: string[] | undefined = closetItems.get(clothingType);
            if (clothingList !== undefined) {
                if (!clothingList.includes(color)) {
                    clothingList.push(color);
                    let newClosetItems = new Map(closetItems);
                    newClosetItems.set(clothingType, clothingList);
                    setClosetItems(newClosetItems);
                }
            }
        }
        else {
            let newClosetItems = new Map(closetItems);
            newClosetItems.set(clothingType, [color]);
            setClosetItems(newClosetItems);
        }
    }
    return(
        <div>
        <div className="Page-header">Closet</div>
        <ClothingOptions chooseClothingItem={chooseClothingItem}/>
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