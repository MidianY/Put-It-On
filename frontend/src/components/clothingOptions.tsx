import React, { MouseEventHandler, useState } from "react";
import './Components.css';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import ClothingItem from "./clothingItem";

export default function ClothingOptions({pickColor}: {pickColor: MouseEventHandler}) {
    const [clickedItemIndex, setClickedItemIndex] = useState(0);
    return(
        <Tabs fill justify>
            <Tab eventKey="shirts" title="Shirts">
                <div className="clothing-options">
                    <ClothingItem 
                        clothingType="short-sleeve" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                    <ClothingItem 
                        clothingType="long-sleeve" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                    <ClothingItem 
                        clothingType="tank" 
                        color="white" 
                        itemClicked={clickedItemIndex===2}
                        onSelect={() => setClickedItemIndex(2)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                    <ClothingItem 
                        clothingType="sweatshirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===3}
                        onSelect={() => setClickedItemIndex(3)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                </div> 
            </Tab>
            <Tab eventKey="pants" title="Pants">
            <div className="clothing-options">
                    <ClothingItem 
                        clothingType="pants" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                    <ClothingItem 
                        clothingType="shorts" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                    <ClothingItem 
                        clothingType="skirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===2}
                        onSelect={() => setClickedItemIndex(2)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                </div> 
            </Tab>
            <Tab eventKey="outer" title="Outer">
            <div className="clothing-options">
                    <ClothingItem 
                        clothingType="hoodie" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                    <ClothingItem 
                        clothingType="jacket" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                    <ClothingItem 
                        clothingType="coat" 
                        color="white" 
                        itemClicked={clickedItemIndex===2}
                        onSelect={() => setClickedItemIndex(2)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                </div> 
            </Tab>
            <Tab eventKey="shoes" title="Shoes">
            <div className="clothing-options">
                    <ClothingItem 
                        clothingType="sneaker" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                    <ClothingItem 
                        clothingType="boot" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        pickColor={pickColor}
                        />
                </div> 
            </Tab>
        </Tabs>
    )
}
