import { createContext, useContext, useEffect, useState } from "react";
import { jwtImplementationApi, signupUser, testSecurityApi } from "../api/TodoApiServices";
import { apiClient } from "../api/ApiClient";


 //1: Crete context ?why not inside? coz they are function. sinunod ko lang nasa handout

 export const AuthContext = createContext();
 export const useAuth = () => useContext(AuthContext);
 
 export default function AuthProvider({ children }) {
     const [number, setNumber] = useState(0);
     const [loggedUser, setLoggedUser] = useState(localStorage.getItem('username') || "default");
     const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('token'));
     const [token, setToken] = useState(localStorage.getItem('token') || '');
 
     useEffect(() => {
         if (token) {
             apiClient.interceptors.request.use((config) => {
                 config.headers.Authorization = token;
                 return config;
             });
         }
     }, [token]);
 
     useEffect(() => {
         // Ensure authentication state is consistent with localStorage on component mount
         const storedToken = localStorage.getItem('token');
         const storedUsername = localStorage.getItem('username');
         if (storedToken && storedUsername) {
             setToken(storedToken);
             setLoggedUser(storedUsername);
             setIsAuthenticated(true);
             apiClient.interceptors.request.use((config) => {
                 config.headers.Authorization = storedToken;
                 return config;
             });
         }
     }, []);
 
     function handleSetLoggedUser(user) {
         setLoggedUser(user);
     }
 
     async function signup(email, username, password) {
         try {
             const response = await signupUser(email, username, password);
             if (response.status === 200) {
                 console.log(response.data);
                 return true;
             } else {
                 return false;
             }
         } catch (error) {
            if (error.response && error.response.status === 409) {
                alert("An account with this email already exists");
            } else {
                alert("An error occurred during registration");
            }
             console.error("Signup failed:", error);
             return false;
         }
     }
 
     async function login(username, password) {
         try {
             const response = await jwtImplementationApi(username, password);
             const token = 'Bearer ' + response.data.token;
 //M
             if (response.status === 200) {
                 handleSetLoggedUser(response.data.username);
                 setIsAuthenticated(true);
                 setToken(token);
                 localStorage.setItem('token', token);
                 localStorage.setItem('username', response.data.username);
 
                 apiClient.interceptors.request.use((config) => {
                     config.headers.Authorization = token;
                     return config;
                 });
                 return true;
             } else {
                 handleSetLoggedUser(null);
                 setIsAuthenticated(false);
                 return false;
             }
         } catch (error) {
             handleSetLoggedUser(null);
             setIsAuthenticated(false);
             return false;
         }
     }
 
     function logout() {
         localStorage.removeItem('token');
         localStorage.removeItem('username');
         setToken('');
         setIsAuthenticated(false);
         setLoggedUser("default");
     }
 
     return (
         <AuthContext.Provider value={{ number, token, setNumber, login, loggedUser, signup, isAuthenticated, logout }}>
             {children}
         </AuthContext.Provider>
     );
 }