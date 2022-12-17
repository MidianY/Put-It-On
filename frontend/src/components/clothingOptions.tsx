import React, { MouseEventHandler, useState } from "react";
import './Components.css';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import ClothingItem from "./clothingItem";

export default function ClothingOptions({pickColor}: {pickColor: MouseEventHandler}) {
    const [clickedItemIndex, setClickedItemIndex] = useState(0);
    return(
        <Tabs aria-label="closet options" fill justify>
            <Tab eventKey="tops" title="Tops">
                <div aria-label="tops tab" className="clothing-options">
                    <ClothingItem 
                        clothingType="short-sleeve" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                    <ClothingItem 
                        clothingType="long-sleeve" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                    <ClothingItem 
                        clothingType="tank" 
                        color="white" 
                        itemClicked={clickedItemIndex===2}
                        onSelect={() => setClickedItemIndex(2)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                    <ClothingItem 
                        clothingType="sweatshirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===3}
                        onSelect={() => setClickedItemIndex(3)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                </div> 
            </Tab>
            <Tab eventKey="bottoms" title="Bottoms">
            <div aria-label="bottoms tab" className="clothing-options">
                    <ClothingItem 
                        clothingType="pants" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                    <ClothingItem 
                        clothingType="shorts" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                    <ClothingItem 
                        clothingType="skirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===2}
                        onSelect={() => setClickedItemIndex(2)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                </div> 
            </Tab>
            <Tab eventKey="outer" title="Outer">
            <div aria-label="outerwear tab" className="clothing-options">
                    <ClothingItem 
                        clothingType="hoodie" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                    <ClothingItem 
                        clothingType="jacket" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                    <ClothingItem 
                        clothingType="coat" 
                        color="white" 
                        itemClicked={clickedItemIndex===2}
                        onSelect={() => setClickedItemIndex(2)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                </div> 
            </Tab>
            <Tab eventKey="shoes" title="Shoes">
            <div aria-label="shoes tab"  className="clothing-options">
                    <ClothingItem 
                        clothingType="sneakers" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                    <ClothingItem 
                        clothingType="boots" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        pickColor={pickColor}
                        recommended={[false, false]}
                        />
                </div> 
            </Tab>
        </Tabs>
    )
}
