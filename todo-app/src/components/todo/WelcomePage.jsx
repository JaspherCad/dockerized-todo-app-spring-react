import { Link, useNavigate, useParams } from "react-router-dom"
import { useAuth } from "./Security/AutoContext";
import ContainersOfTodo from "./ContainersOfTodo"
import { useState } from "react";
export default function WelcomePage(){
    const authContext = useAuth();
    const userParam = useParams();
    const [search, setSearch] = useState("");
    const navigate = useNavigate();

    //console.log(params.username) //must print TITE or whatevers on localhost/welcome/TITE

    return(
        <>
            <h1>WELCOME, {authContext.loggedUser}! dockerized</h1>
            <div className="form">
                <div>
                    <label>PASTE A SHARED TODO HERE: </label>
                    <input type="text" name="sharedTodo" onChange={(event) => setSearch(event.target.value)} value={search} placeholder="55711c78-fccc-4cb5-970e-cd446b6f44e3"/>
                </div>


                <div>
                    <button name="search" onClick={handelSubmit}>SEARCH!</button>
                </div>

                <h6>example: link/share<span style={{ color: 'red' }}>/55711c78-fccc-4cb5-970e-cd446b6f44e3</span></h6>


                <h6><span style={{ color: 'red' }}>copy </span> the hash or the <span style={{ color: 'red' }}> <span style={{ color: 'red' }}></span>55711c78-fccc-4cb5-970e-cd446b6f44e3 </span> then paste here</h6>
                <li></li>
                <h6><span style={{ color: 'red' }}>HOW TO SEND ENCRYPTED TODO THRU LINK? </span> </h6>
                <h6><span style={{ color: 'red' }}>1: </span> Create todo container</h6>
                <h6><span style={{ color: 'red' }}>1.1: </span>WHY? todo container contains list of todos</h6>
                <h6><span style={{ color: 'red' }}>1.2: </span>todo container is what you share to public</h6>

                <li></li>
                <h6><span style={{ color: 'red' }}>2: </span> Inside todo: CLICK SEE TODOS</h6>
                <h6><span style={{ color: 'red' }}>2.1: </span>Just play with my app. SHARE IT NOW!</h6>




            </div>
            
            <ContainersOfTodo></ContainersOfTodo>
        </>
    )



    function handelSubmit(){
        console.log(1)
        navigate(`/share/${search}`)
    }

}