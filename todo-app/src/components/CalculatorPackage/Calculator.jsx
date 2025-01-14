import { useState } from "react"
import ButtonCounter from "./ButtonCounter"
import ResetButton from "./ResetButton";
export default function Calculator(){

    const [count, setCount] = useState(0);

    function incrementButton(by){ //because BY will have its own value
        setCount(count + by)
    }

    function resetButton(){
        setCount(count-count)
    }

    return(
     <>
        <h1>{count}</h1>
        
        <div>
            <ButtonCounter byProps={1} incrementProps={incrementButton}     />
        </div>

        <div>
            <ButtonCounter byProps={2} incrementProps={incrementButton}     />
        </div>

        <div>
            <ButtonCounter byProps={3} incrementProps={incrementButton}     />
        </div>

        <div>
            <ResetButton resetProp={resetButton}/>
        </div>
        
        
     </>   
    )
    
}

