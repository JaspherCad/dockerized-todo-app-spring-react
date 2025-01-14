import Login from "./Login"
import WelcomePage from "./WelcomePage"
import TodoPage from "./TodosPage"
import HeaderComponent from "./HeaderComponent"
import {BrowserRouter, Routes, Route} from 'react-router-dom'
import AuthProvider from "./Security/AutoContext"
import TodoComponent from "./TodoComponent"
import SignUp from "./SignUp"
import SharedComponent from "./SharedComponent"
export default function TodoApp(){


    return(
        <>
        <AuthProvider> {/* WANT TO SHARE CONTEXT ACCROSS?? USE THIS */}
            <BrowserRouter>
                <HeaderComponent /> {/* inside BrowserRouter because of <Link> .*/}

                <Routes>
                    <Route path="/welcome" element={<Login />}></Route>

                    <Route path="/" element={<Login />}></Route>
                    <Route path="/signup" element={<SignUp />}></Route>
                    <Route path="/login" element={<Login />}></Route>
                    <Route path="/welcome/:username" element={<WelcomePage />}></Route>
                    <Route path="/welcome/:username/todo-page/containers/:containerId/:containerTitle/todos" element={<TodoPage />}></Route>{/* /users/{name}/containers/{containerId}/todos/{id} */}
                    
                    <Route path="/welcome/:username/todo-page/containers/:containerId/:containerTitle/todos/:todoid" element={<TodoComponent />}></Route>

                    

                    <Route path="/share/:sharelink" element={<SharedComponent />}></Route>
                    {/* after clicking edit button (from todopage) navigate(current user, todo.id) */}

                    <Route path="/welcome/:username/share/:sharelink/todos/:todoid" element={<TodoComponent />}></Route>
                    {/* after clicking edit button (from todopage) navigate(current user, todo.id) */}

                


                </Routes>
            </BrowserRouter>
        </AuthProvider>
        </>
    )

}