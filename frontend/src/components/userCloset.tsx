import React, { MouseEventHandler, useEffect, useState } from "react";
import './Components.css';
import ClothingItem from "./clothingItem";
import closetData from "../mocks/mockCloset.json"

export default function UserCloset({closetItems, pickColor}: {closetItems: Map<string, string>[], pickColor: MouseEventHandler}) {
    return(
        <div className="closet-options">
            {closetItems.map((item: any) => {
                return(
                    <ClothingItem 
                    clothingType={item.item} 
                    color={item.color}
                    itemClicked={false}
                    onSelect={() => null}
                    inCloset={true}
                    pickColor={pickColor}
                    recommended={[false, false]}/>
                )
            })}
        </div>
    )
}
