import axios from "axios";
import { useAuth } from "../Security/AutoContext";
export const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json'
    }
});

//after login, after using INTERCEPTOR we can now put the token to all REQUEST API CALL that we are callin ;)



// Add an interceptor to catch 401 Unauthorized errors
apiClient.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            // Token expired or invalid, logout the user
            const authContext = useAuth();

            // Show alert to the user
            window.alert("Your session has expired. Please log in again.");


            authContext.logout(); // Your logout logic
            window.location.href = "/login"; // Redirect to login page
        }
        return Promise.reject(error);
    }
);