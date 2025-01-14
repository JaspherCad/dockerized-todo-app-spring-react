import { useState } from "react"

export default function ButtonCounter({byProps, incrementProps}){
    
    //INDIVIDUAL COUNT, DISREGARD
    const [count, setCount] = useState(0);
    
    function changeIndividualCount(){
        setCount(count+byProps)
    }
    //INDIVIDUAL COUNT, DISREGARD
    





    return(
     <>
        <h1>INDIVIDUAL COUNT: {count}</h1>
        
        <button className="counterButton" onClick={() => {
            incrementProps(byProps);
            changeIndividualCount();
        }}>+{byProps}</button>

     </>   
    )
}