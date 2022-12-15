import React from "react";
import './Components.css';
import mockOutfit from '../mocks/mockOutfit.json';
import ClothingItem from "./clothingItem";
import {BsQuestionCircleFill} from "react-icons/bs"
import { OverlayTrigger } from "react-bootstrap";
import { Tooltip } from "react-bootstrap";

export default function RecommendedOutfit() {
    const outfitResponse = mockOutfit;
    const outfit  = outfitResponse.outfit;
    return(
        <div className="outfit">
        Recommended Outfit:
        {outfit.map((item: any, index: number) => {
            return (
            <div key={index} className="outfit-row">
            <ClothingItem 
            clothingType={item.item} 
            color={item.color}
            itemClicked={false}
            onSelect={() => null}
            inCloset={false}
            pickColor={() => null}
            recommended={[true, item.inCloset]}/>
            </div>
            )
        })}
        </div>

    )
}