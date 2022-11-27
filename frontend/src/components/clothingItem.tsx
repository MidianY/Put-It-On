import React from "react";
import './Components.css';
import {ReactComponent as TShirtIcon} from '../icons/tshirt.svg'
import {ReactComponent as LongSleeveShirtIcon} from '../icons/longsleeve.svg'

export default function ClothingItem() {
    return(
    <div>
        <TShirtIcon className="clothing-item" fill="white" />
        <LongSleeveShirtIcon className="clothing-item" fill="white" />
    </div>
    )
}