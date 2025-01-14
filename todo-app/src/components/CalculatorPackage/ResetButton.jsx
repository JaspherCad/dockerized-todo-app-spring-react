import { useState } from "react"

export default function ResetButton({resetProp}){

    return(
        <button className="resetButton" onClick={() => {
            resetProp()
        }}>+RESET{resetProp}</button>
    )

}