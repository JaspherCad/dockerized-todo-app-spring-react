import 'bootstrap/dist/css/bootstrap.min.css'; // Ensure Bootstrap CSS is imported
import { useAuth } from "./Security/AutoContext";
import { deleteByIdApi, getAllTODOSInsideContainersWithTodos, retrieveByUserApi, updateTodoApi, updateTodoDone } from './api/TodoApiServices';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

export default function TodoPage() {


    const authContext = useAuth();
    const navigate = useNavigate();
    const { username, containerId, containerTitle } = useParams();

    const [title, setTitle] = useState("titles")
    const [todos, setTodo] = useState([]) //BEST FOR FREQUENT UPDATES

    useEffect( //BEST FOR INITIAL UPDATES "when" the component is ready...
        () => reloadTodos(), [] //call this function imidieatly. 
    )



    // const todos = [ //temporarily static, we will use Spring boot api next...
    
    //     { id: 1, description: "learn this", targetDate: targetDate, isDone: false },
    //     { id: 2, description: "learn this", targetDate: targetDate, isDone: false },
    //     { id: 3, description: "learn this", targetDate: targetDate, isDone: false },
    //     { id: 4, description: "learn this", targetDate: targetDate, isDone: false }

    // ];






    

    return (
        <>
            <div className="container mt-5">
                <h1 className="mb-4">{title} {authContext.loggedUser}</h1> 

                <div className="listTodoComponents">
                    <table className="table table-striped table-hover">
                        <thead className="thead-dark">
                            <tr>
                                <th>ID</th>
                                <th>Description</th>
                                <th>Target Date</th>
                                <th>Done?</th>
                                <th>Delete</th>
                                <th>Update</th>


                            </tr>
                        </thead>

                        <tbody>
                            {
                                todos.map(todo => (
                                    <tr key={todo.id}>
                                        <td>{todo.id}</td>
                                        <td>{todo.description}</td>
                                        <td>{todo.targetDate ? todo.targetDate.toString() : 'No Date'}</td>


                                        <td>
                                            <button className='btn btn-secondary' onClick={() => handleSetDone(todo, todo.id)}>
                                                {todo.done ? 'Yes' : 'No'}
                                            </button>
                                        </td>


                                        <td><button className='deleteBtn' onClick={() => handleDelete(authContext.loggedUser, todo.id, containerId)}>Delete</button></td>
                                        <td><button className='updateBtn' onClick={() => handleUpdate(authContext.loggedUser, todo.id, containerId)}>Update</button></td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>

                    <button className='btn btn-success' onClick={createTodoHandler}>Create Todo</button>
                </div>
            </div>
        </>
    );


    //once i deleted a data, reload the TODOS...
    function handleDelete(name, id){
        console.log(`deleted ${name} with id of ${id}`)
        deleteByIdApi(name,containerId ,id)
            .then( (response) =>{
                reloadTodos()
                //DO THIS: show to page that a Todo has been deleted. USE useState.
            }) 
            .catch( (error) => console.log(error))
            .finally(() => console.log('cleanup'))
    }





    function handleUpdate(name, id, containerId){
    

        navigate(`/welcome/${name}/todo-page/containers/${containerId}/${containerTitle}/todos/${id}`) 
        //after navigating, useParams to access ID, then use  getByIdApi(name, id)
        //not here, but on "(navigate(`../todo/${name}/${id}`) )"

    }






    function reloadTodos(){ //where to call this? everytime the page reloads... useEffect!!
        getAllTODOSInsideContainersWithTodos(authContext.loggedUser, containerId)
        .then((response) => {
            setTodo(response.data);
            setTitle(containerTitle)
            console.log(response.data)
        })
        .catch((error) => console.log(error))
        .finally(() => console.log('cleanup'));
    }


    function createTodoHandler(){
        navigate(`/welcome/${authContext.loggedUser}/todo-page/containers/${containerId}/${containerTitle}/todos/-1`) 
    }

    


    //idea, get the todo from todo(frontend) then create new obj todo to send into backend
    //just use the update.. DUH!
    function handleSetDone(todo, todoid){

        console.log(todo.targetDate)
        const updatedTodo = { //nag de-decrement eh, so include targetDate
            done: !todo.done,  
            targetDate: todo.targetDate ? todo.targetDate : null,  
            description: todo.description, // Keep other fields unchanged
        };

        updateTodoDone(todoid, updatedTodo)
                    .then((response) => {
                        reloadTodos()
                    }).catch((error) => console.log(error))

    }


}
