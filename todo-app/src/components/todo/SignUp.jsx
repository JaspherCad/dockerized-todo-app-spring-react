import { useNavigate } from "react-router-dom";
import { useAuth } from "./Security/AutoContext";
import { useState } from "react";

export default function SignUp(){


    const authContext = useAuth();

    const [email, setEmail] = useState("email");
    const [username, setUsername] = useState("username");
    const [password, setPassword] = useState("password");


    const navigate = useNavigate();
    return(
        <>
            <h1>SIGNUP HERE</h1>

           {/* <ShowErrorMsg showErrorMessageProps={showErrorMessage}></ShowErrorMsg>  */}
           {/* THIS ^^^^ is very similar to useContext.. but use Context IS HIGHER parent so it can share 
           to ALL COMPONENT ACROSS. In use context, we are using a similar context. SIMILAR SA SPRING BOOT */}


            <div className="form">
                <div>
                    <label>email: </label>
                    <input type="email" name="email" onChange={(event) => setEmail(event.target.value)} value={email} />
                </div>

                <div>
                    <label>username: </label>
                    <input type="text" name="username" onChange={(event) => setUsername(event.target.value)} value={username} />
                </div>

                <div>
                    <label>Password: </label>
                    <input type="password" name="password" value={password} onChange={(event) => setPassword(event.target.value)} />
                </div>

                <div>
                    <button name="signup" onClick={handelSubmit}>Signup</button>
                </div>

            </div>

            <h5>password: {password}</h5>
        </>
    )

    async function handelSubmit(){
        console.log(email)
         if (await authContext.signup(email, username, password) == true){
             console.log('SIGNED UP KANA')
             navigate(`/login`)
         }else{
             console.log('error')
         }
    }
    

}