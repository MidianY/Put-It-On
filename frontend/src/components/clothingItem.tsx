import React, { MouseEventHandler, useEffect, useState } from "react";
import './Components.css';
import {ReactComponent as TShirtIcon} from '../icons/tshirt.svg'
import {ReactComponent as LongSleeveShirtIcon} from '../icons/longsleeve.svg'
import { RxBox } from "react-icons/rx";
import { BsCheck } from "react-icons/bs";
import { FaSquareFull } from "react-icons/fa";
import { GiTankTop } from "react-icons/gi";


const clothingTypeMap : Map<String, any> = new Map();
clothingTypeMap.set("tShirt", TShirtIcon);
clothingTypeMap.set("longSleeveShirt", LongSleeveShirtIcon);
clothingTypeMap.set("tankTop", GiTankTop);


function returnClothingIcon(clothingType : string, color: string) {
    const ClothingIcon = clothingTypeMap.get(clothingType);
    return(
        <ClothingIcon className="clothing-item" fill={color}/>
    )
}

function ColorBox({color, clothingType}: {color: string, clothingType: string}) {
    const [isColorClicked, setIsColorClicked] = useState(false);
    function chooseColor() {
        setIsColorClicked(!isColorClicked);
        // addToCloset endpoint and removeFromCloset endpoint
    }
    return (
        <div className="color-box-container" onClick={chooseColor}>
        <FaSquareFull className="color-box" size={35} fill={color}/>
        {isColorClicked && (
            <BsCheck onClick={chooseColor} className="color-check" fill={["yellow", "white", "#add8e6"].includes(color) ? "black" : "white"}/>
        )}
        </div>
    )
}

function ColorSelection({clothingType}: {clothingType: string}) {
    const colors: string[] = ["white", "black", "blue", "#add8e6","red", "green", "yellow", "purple", "pink", "orange", "#8b4513","grey"];
    return(
        <div>
        <div className="color-options">
            {colors.map((color, index) => {
                return (
                    <ColorBox key={color + "" + clothingType} color={color} clothingType={clothingType} />
                )
            })}
        </div>
        </div>

    )
}

export default function ClothingItem({clothingType, color, itemClicked, onSelect, inCloset}: 
    {clothingType: string, color: string, itemClicked: boolean, onSelect: MouseEventHandler, inCloset: boolean}) {
    const [clothingClicked, setClothingClicked] = useState(false);
    const handleClothesClick = () => {
        setClothingClicked(!clothingClicked);
    }   
    return(
    <div className="clothing-with-colors" onClick={onSelect}>
        <div className="clothing-container" onClick={handleClothesClick}>
            <RxBox className="clothing-box"/>
            {returnClothingIcon(clothingType, color)}
        </div>
        {!inCloset && itemClicked && clothingClicked && (
            <ColorSelection clothingType={clothingType} />
        )}
    </div>
    )
}