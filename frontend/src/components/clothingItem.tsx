import React, { MouseEventHandler, useEffect, useState } from "react";
import './Components.css';
import {ReactComponent as TShirtIcon} from '../icons/tshirt.svg'
import {ReactComponent as LongSleeveShirtIcon} from '../icons/longsleeve.svg'
import { RxBox } from "react-icons/rx";
import { BsSquareFill, BsCheck } from "react-icons/bs";
import { FaSquareFull } from "react-icons/fa";


const clothingTypeMap : Map<String, any> = new Map();
clothingTypeMap.set("tShirt", TShirtIcon);
clothingTypeMap.set("longSleeveShirt", LongSleeveShirtIcon);

function returnClothingIcon(clothingType : string, color: string) {
    const ClothingIcon = clothingTypeMap.get(clothingType);
    return(
        <ClothingIcon className="clothing-item" fill={color}/>
    )
}

function ColorBox({color, clothingType, onCheck}: {color: string, clothingType: string, onCheck: (color: string, clothingType: string) => void}) {
    const [isColorClicked, setIsColorClicked] = useState(false);
    function chooseColor() {
        setIsColorClicked(!isColorClicked);
        onCheck(color, clothingType);
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

function ColorSelection({clothingType, chooseClothingItem}: {clothingType: string, chooseClothingItem: (color: string, clothingType: string) => void}) {
    const colors: string[] = ["white", "black", "blue", "#add8e6","red", "green", "yellow", "purple", "pink", "orange", "#8b4513","grey"];
    return(
        <div>
        <div className="color-options">
            {colors.map((color, index) => {
                return (
                    <ColorBox color={color} clothingType={clothingType} onCheck={chooseClothingItem}/>
                )
            })}
        </div>
        </div>

    )
}

export default function ClothingItem({clothingType, color, itemClicked, onSelect, inCloset, chooseClothingItem}: 
    {clothingType: string, color: string, itemClicked: boolean, onSelect: MouseEventHandler, inCloset: boolean, chooseClothingItem: (color: string, clothingType: string) => void}) {
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
            <ColorSelection clothingType={clothingType} chooseClothingItem={chooseClothingItem}/>
        )}
    </div>
    )
}