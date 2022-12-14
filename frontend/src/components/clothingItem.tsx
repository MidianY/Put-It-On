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
import OverlayTrigger from 'react-bootstrap/OverlayTrigger';
import Tooltip from 'react-bootstrap/Tooltip';
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
clothingTypeMap.set("sneakers", SneakerIcon);
clothingTypeMap.set("boots", BootIcon);

function returnClothingIcon(clothingType : string, color: string, inCloset: boolean, recommended: boolean) {
    const ClothingIcon = clothingTypeMap.get(clothingType);
    return(
        <ClothingIcon className={inCloset ? "closet-item" : recommended ? "outfit-item" :"clothing-item"} fill={color} />
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
            <BsCheck onClick={chooseColor} className="color-check" fill={["yellow", "white", "khaki"].includes(color) ? "black" : "white"}/>
        )}
        </div>
        </div>
    )
}

function ColorSelection({clothingType, pickColor}: {clothingType: string, pickColor: MouseEventHandler}) {
    const colors: string[] = ["white", "black", "blue", "khaki","red", "green", "yellow", "purple", "pink", "orange", "saddlebrown","grey"];
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

export default function ClothingItem({clothingType, color, itemClicked, onSelect, inCloset, pickColor, recommended}: 
    {clothingType: string, color: string, itemClicked: boolean, onSelect: MouseEventHandler, inCloset: boolean, pickColor: MouseEventHandler, recommended: boolean}) {
    const [clothingClicked, setClothingClicked] = useState(false);
    const handleClothesClick = () => {
        setClothingClicked(!clothingClicked);
    }
    const removeItem = async(event: React.MouseEvent<Element, MouseEvent>) => {
        await fetch(`http://localhost:3230/editCloset?color=${color}&item=${clothingType}&action=remove`)
        .then(response => response.json())
        .catch(err => console.log('error', err));
    }   

    return(
    <OverlayTrigger
        placement={inCloset ? "bottom" : recommended ? "right" : "top"}
        overlay={
            <Tooltip>{inCloset || recommended ? `${color.charAt(0).toUpperCase() + color.slice(1)} ${clothingType.charAt(0).toUpperCase() + clothingType.slice(1)}`: clothingType.charAt(0).toUpperCase() + clothingType.slice(1) }</Tooltip>
        }>
    <div className="clothing-with-colors" onClick={onSelect}>
        <div className={inCloset ? "closet-container" : !recommended? "clothing-container": ""} onClick={handleClothesClick}>
            <div onClick={pickColor}>
            {inCloset && (
                <TiDelete onClick={(event) => {removeItem(event); pickColor(event);}} className="delete-clothing" color="red"/>
            )}
            </div>
            {!recommended && <RxBox color={inCloset ? "black" : "white"} className={inCloset ? "closet-box" :"clothing-box"}/>}
            {returnClothingIcon(clothingType, color, inCloset, recommended)}
        </div>
        {!inCloset && itemClicked && clothingClicked && (
            <ColorSelection clothingType={clothingType} pickColor={pickColor}/>
        )}
    </div>
    </OverlayTrigger>
    )
}