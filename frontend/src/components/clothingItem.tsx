import React, { MouseEventHandler, useEffect, useState } from "react";
import './Components.css';
import {ReactComponent as TShirtIcon} from '../icons/tshirt.svg'
import {ReactComponent as LongSleeveShirtIcon} from '../icons/longsleeve.svg'
import {ReactComponent as TankIcon} from '../icons/tank.svg'
import {ReactComponent as SweatshirtIcon} from '../icons/sweatshirt.svg'
import {ReactComponent as PantsIcon} from '../icons/pants.svg'
import {ReactComponent as ShortsIcon} from '../icons/shorts.svg'
import {ReactComponent as SkirtIcon} from '../icons/skirt.svg'
import {ReactComponent as HoodieIcon} from '../icons/hoodie.svg'
import {ReactComponent as JacketIcon} from '../icons/jacket.svg'
import {ReactComponent as CoatIcon} from '../icons/coat.svg'
import {ReactComponent as SneakerIcon} from '../icons/sneaker.svg'
import {ReactComponent as BootIcon} from '../icons/boot.svg'

import { RxBox } from "react-icons/rx";
import { BsCheck } from "react-icons/bs";
import { FaSquareFull } from "react-icons/fa";
import { TiDelete } from "react-icons/ti";


const clothingTypeMap : Map<String, any> = new Map();
clothingTypeMap.set("short-sleeve", TShirtIcon);
clothingTypeMap.set("long-sleeve", LongSleeveShirtIcon);
clothingTypeMap.set("tank", TankIcon);
clothingTypeMap.set("sweatshirt", SweatshirtIcon);
clothingTypeMap.set("pants", PantsIcon);
clothingTypeMap.set("shorts", ShortsIcon);
clothingTypeMap.set("skirt", SkirtIcon);
clothingTypeMap.set("hoodie", HoodieIcon);
clothingTypeMap.set("jacket", JacketIcon);
clothingTypeMap.set("coat", CoatIcon);
clothingTypeMap.set("sneaker", SneakerIcon);
clothingTypeMap.set("boot", BootIcon);

function returnClothingIcon(clothingType : string, color: string, inCloset: boolean) {
    const ClothingIcon = clothingTypeMap.get(clothingType);
    return(
        <ClothingIcon className={inCloset ? "closet-item" : "clothing-item"} fill={color}/>
    )
}

function ColorBox({color, clothingType, pickColor}: {color: string, clothingType: string, pickColor: MouseEventHandler}) {
    const [isColorClicked, setIsColorClicked] = useState(false);
    const editCloset = async(add: boolean) => {
        await fetch(`http://localhost:3230/editCloset?color=${color}&item=${clothingType}&action=${add ? "add" : "remove"}`)
        .then(response => response.json())
        .catch(err => console.log('error', err))
    }

    function chooseColor() {
        setIsColorClicked(!isColorClicked);
        editCloset(!isColorClicked);
    }
    return (
        <div onClick={pickColor}>
        <div className="color-box-container" onClick={chooseColor}>
        <FaSquareFull className="color-box" size={35} fill={color}/>
        {isColorClicked && (
            <BsCheck onClick={chooseColor} className="color-check" fill={["yellow", "white", "#add8e6", "khaki"].includes(color) ? "black" : "white"}/>
        )}
        </div>
        </div>
    )
}

function ColorSelection({clothingType, pickColor}: {clothingType: string, pickColor: MouseEventHandler}) {
    const colors: string[] = ["white", "black", "blue", "khaki","red", "green", "yellow", "purple", "pink", "orange", "#8b4513","grey"];
    return(
        <div>
        <div className="color-options">
            {colors.map((color, index) => {
                return (
                    <div onClick={pickColor}>
                    <ColorBox key={color + "" + clothingType} color={color} clothingType={clothingType} pickColor={pickColor}/>
                    </div>
                )
            })}
        </div>
        </div>

    )
}

export default function ClothingItem({clothingType, color, itemClicked, onSelect, inCloset, pickColor}: 
    {clothingType: string, color: string, itemClicked: boolean, onSelect: MouseEventHandler, inCloset: boolean, pickColor: MouseEventHandler}) {
    const [clothingClicked, setClothingClicked] = useState(false);
    const handleClothesClick = () => {
        setClothingClicked(!clothingClicked);
    }
    const removeItem = async() => {
        await fetch(`http://localhost:3230/editCloset?color=${color}&item=${clothingType}&action=remove`)
        .then(response => response.json())
        .catch(err => console.log('error', err));
    }   

    return(
    <div>
    <div className="clothing-with-colors" onClick={onSelect}>
        <div className={inCloset ? "closet-container grid grid-cols-4" :"clothing-container"} onClick={handleClothesClick}>
            {inCloset && (
            <div onClick={pickColor}>
                <TiDelete onClick={removeItem} className="delete-clothing" color="red"/>
            </div>)}
            <RxBox color={inCloset ? "black" : "white"} className={inCloset ? "closet-box" :"clothing-box"}/>
            {returnClothingIcon(clothingType, color, inCloset)}
        </div>
        {!inCloset && itemClicked && clothingClicked && (
            <ColorSelection clothingType={clothingType} pickColor={pickColor}/>
        )}
    </div>
    </div>
    )
}