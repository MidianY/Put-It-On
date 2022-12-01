import React, { MouseEventHandler, useState } from "react";
import './Components.css';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import ClothingItem from "./clothingItem";

export default function ClothingOptions({chooseClothingItem}: {chooseClothingItem: (color: string, clothingType: string) => void}) {
    const [clickedItemIndex, setClickedItemIndex] = useState(0);
    return(
        <Tabs fill justify>
            <Tab eventKey="shirts" title="Shirts">
                <div className="clothing-options">
                    <ClothingItem 
                        clothingType="tShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="longSleeveShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="tShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===2}
                        onSelect={() => setClickedItemIndex(2)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="longSleeveShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===3}
                        onSelect={() => setClickedItemIndex(3)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="tShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===4}
                        onSelect={() => setClickedItemIndex(4)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="longSleeveShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===5}
                        onSelect={() => setClickedItemIndex(5)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                </div> 
            </Tab>
            <Tab eventKey="pants" title="Pants">
            <div className="clothing-options">
                    <ClothingItem 
                        clothingType="tShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="longSleeveShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="tShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===2}
                        onSelect={() => setClickedItemIndex(2)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                </div> 
            </Tab>
            <Tab eventKey="shoes" title="Shoes">
            <div className="clothing-options">
                    <ClothingItem 
                        clothingType="tShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="longSleeveShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="tShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===2}
                        onSelect={() => setClickedItemIndex(2)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                </div> 
            </Tab>
            <Tab eventKey="outer" title="Outer">
            <div className="clothing-options">
                    <ClothingItem 
                        clothingType="tShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===0}
                        onSelect={() => setClickedItemIndex(0)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="longSleeveShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===1}
                        onSelect={() => setClickedItemIndex(1)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                    <ClothingItem 
                        clothingType="tShirt" 
                        color="white" 
                        itemClicked={clickedItemIndex===2}
                        onSelect={() => setClickedItemIndex(2)}
                        inCloset={false}
                        chooseClothingItem={chooseClothingItem}/>
                </div> 
            </Tab>
        </Tabs>
    )
}
