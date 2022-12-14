import React from "react";
import './Components.css';
import mockOutfit from '../mocks/mockOutfit.json';
import ClothingItem from "./clothingItem";

export default function RecommendedOutfit() {
    const outfitResponse = mockOutfit;
    const outfit  = outfitResponse.outfit;
    return(
        <div className="outfit">
        {outfit.map((item: any, index: number) => {
            return (
            <div key={index}>
            
            <ClothingItem 
            clothingType={item.item} 
            color={item.color}
            itemClicked={false}
            onSelect={() => null}
            inCloset={false}
            pickColor={() => null}
            recommended={true}/>
            </div>
            )
        })}
        </div>

    )
}