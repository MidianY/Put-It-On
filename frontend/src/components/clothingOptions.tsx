import React from "react";
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import ClothingItem from "./clothingItem";

export default function ClothingOptions() {
    return(
        <Tabs>
        <Tab eventKey="shirts" title="Shirts">
            Shirts
            <ClothingItem />
        </Tab>
        <Tab eventKey="pants" title="Pants">
            Pants
        </Tab>
        <Tab eventKey="shoes" title="Shoes">
            Shoes
        </Tab>
      </Tabs>
    )
}