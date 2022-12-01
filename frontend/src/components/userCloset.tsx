import React, { useEffect, useState } from "react";
import './Components.css';
import ClothingItem from "./clothingItem";

// export default function UserCloset() {
//     const removeItem = (key: string, color: string) => {
//         const keyColors: string[] | undefined = closetClothes.get(key);
//         if (keyColors !== undefined) {
//             console.log(keyColors.splice(keyColors.indexOf(color), 1));
//             keyColors.splice(keyColors.indexOf(color), 1);
//             closetClothes.set(key, keyColors);
//         }
//     }
//     return (
//     <div className="clothing-options">
//         {/* {Array.from(closetClothes.keys()).map((key) => {
//         return closetClothes.get(key)?.map((color) => {
//             return (
//             <ClothingItem
//                 color={color}
//                 clothingType={key}
//                 itemClicked={false}
//                 onSelect={() => removeItem(key, color)}
//                 inCloset={true}/>
//             )
//         })
//     })} */}
//     </div>
//     );
    
// }

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
    return (
    <div >
        {Array.from(closetItems.keys()).map((key) => {
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
    })}
    </div>
    );
    
}