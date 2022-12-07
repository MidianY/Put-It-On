import React, { useEffect, useState } from "react";
import './Components.css';
import ClothingItem from "./clothingItem";
import closetData from "../mocks/mockCloset.json"

export default function UserCloset({closetItems}: {closetItems: Map<string, string[]>}) {
    // const removeItem = (key: string, color: string) => {
    //     const keyColors: string[] | undefined = closetClothes.get(key);
    //     if (keyColors !== undefined) {
    //         console.log(keyColors.splice(keyColors.indexOf(color), 1));
    //         keyColors.splice(keyColors.indexOf(color), 1);
    //         closetClothes.set(key, keyColors);
    //     }
    // }
    // const [value, setValue] = useState(0);
    // function updateState() {
    //     return () => setValue(value => value + 1);
    // }
    const closet: any = closetData.closet;
    return (
    <div >
        {/* {Array.from(closetItems.keys()).map((key) => {
        return closetItems.get(key)?.map((color) => {
            return (
            <ClothingItem
                color={color}
                clothingType={key}
                itemClicked={false}
                onSelect={() => null}
                inCloset={true}
                chooseClothingItem={() => null}/>
            )
        })
    })} */}
            {/* {closet.map((item: any) => {
            return (
            <ClothingItem
                color={item.color}
                clothingType={item.item}
                itemClicked={false}
                onSelect={() => null}
                inCloset={true}
                chooseClothingItem={() => null}/>
            )
        })
    } */}
    </div>
    );
    
}