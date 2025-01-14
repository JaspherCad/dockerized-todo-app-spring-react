import { useState } from "react"
import ShowErrorMsg from "./ShowErrorMsg";
import { useNavigate } from "react-router-dom";
import { AuthContext, useAuth } from "./Security/AutoContext";
import { testSecurityApi } from "./api/TodoApiServices";
export default function Login(){

    const [username, setUsername] = useState("mapped here");
    const [password, setPassword] = useState("")
    const [showErrorMessage, setshowErrorMessage] = useState(false)

    const authContext = useAuth();

    const navigate = useNavigate();
    return(
        <>
            <h1>Welcome {username}</h1>

           <ShowErrorMsg showErrorMessageProps={showErrorMessage}></ShowErrorMsg> 
           {/* THIS ^^^^ is very similar to useContext.. but use Context IS HIGHER parent so it can share 
           to ALL COMPONENT ACROSS. In use context, we are using a similar context. SIMILAR SA SPRING BOOT */}


            <div className="form">
                <div>
                    <label>Email: </label>
                    <input type="text" name="username" onChange={handleUsername}value={username} />
                </div>

                <div>
                    <label>Password: </label>
                    <input type="password" name="password" value={password} onChange={(event) => setPassword(event.target.value)} />
                </div>

            

                <div>
                    <button name="login" onClick={handelSubmit}>Login</button>
                </div>

            </div>

            <h5>password: {password}</h5>

            <div>
                    <p>Information and Security Assurance: group 3</p>
                    <p>1: Signup first</p>
                    <p>2: Then Login</p>

                </div>
        </>
    )

    function handleUsername(event){
        setUsername(event.target.value)
    }

    async function  handelSubmit(){
        if (await authContext.login(username, password) == true){ //since we used await on auth.login, so do here.
            console.log('success')
            setshowErrorMessage(false)
            const urlIdk = "/welcome/" + authContext.loggedUser
            navigate(urlIdk) //FROM TodoApp, this is the :username

            
        }else{
            setshowErrorMessage(true)
        }
        



        // if(username==="jaspher" && password==="password"){ //authenticate('string', 'string')
        //     console.log('success')
        //     setshowErrorMessage(false)
        //     authContext.handleSetLoggedUser(username)
        //     const urlIdk = "/welcome/" + username
            
        //     navigate(urlIdk) //FROM TodoApp, this is the :username
        // }else{
        //     console.log('failed')
        //     setshowErrorMessage(true)

        // }
    }

    

}