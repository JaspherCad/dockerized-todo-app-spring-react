import { apiClient } from './ApiClient'


export function retrieveByUserApi(name){
    return apiClient.get(`http://localhost:8080/users/${name}/todos`)
}

export function deleteByIdApi(name, containerId, id){
    return apiClient.delete(`http://localhost:8080/users/${name}/containers/${containerId}/todos/${id}`) //will return FILLED todo 
} 
//  http://localhost:8080/users/jaspher/5




///users/{name}/containers/{containerId}/todos/{id}
export function getByIdApi(name, containerId, id){ //retrieveBackendAPI
    return apiClient.get(`http://localhost:8080/users/${name}/containers/${containerId}/todos/${id}`) //will return FILLED todo 
} 










//  http://localhost:8080/users/jaspher/containers/1/todos/6

export function updateTodoDone( id, todoState){ //todo is @RequestBody
    return apiClient.put(`http://localhost:8080/users/toggleTodo/${id}`, todoState) //will return UPDATED todo
}


export function updateTodoApi(username, containerId, todoid, todo){ //todo is @RequestBody
    return apiClient.put(`/users/${username}/containers/${containerId}/todos/${todoid}`, todo) //will return UPDATED todo
}







export function createTodoApi(name, containerId, todo){
    return apiClient.post(`http://localhost:8080/users/${name}/containers/${containerId}/todos`, todo )
}



export function getAllContainersWithTodos(name){
    return apiClient.get(`http://localhost:8080/users/${name}/containers`)
}

export function getAllTODOSInsideContainersWithTodos(name, containerId){
    return apiClient.get(`http://localhost:8080/users/${name}/containers/${containerId}/todos`)
}

export function updateCOntainerTitleApi(username, containerId, title){
    return apiClient.put(`http://localhost:8080/users/${username}/containers/${containerId}`, title)

}

export function deleteContainerApi(username, containerId){
    return apiClient.delete(`http://localhost:8080/users/${username}/containers/${containerId}`)

}












export function shareTodoContainerApi(containerId, username){ //creates sharable link
    return apiClient.post(`http://localhost:8080/share/container/${containerId}/create`, username )
}

export function removeSharableLinksFromContainer(containerId){
    return apiClient.delete(`http://localhost:8080/share/container/${containerId}/delete`)
}


export function addTodosBySharableLink(sharabeLink, todo){ //ADD to todos's sharable link
    return apiClient.post(`http://localhost:8080/share/${sharabeLink}/containers/todos`, todo)
}

export function getTodosByShareableLinkApi(sharabeLink){ //get todos's sharable link
    return apiClient.get(`http://localhost:8080/share/${sharabeLink}/containers/todos` )
}



//http://localhost:8080/share/681387c5-554b-4206-896c-500c696a5894/container/todos/6
export function getSpecificTodoBySharableLinkApi(sharabeLink, todoId){ //get SPECIFIC todos's sharable link
    return apiClient.get(`http://localhost:8080/share/${sharabeLink}/container/todos/${todoId}`)
}





export function deleteSpecificTodoBySharableLinkApi(sharabeLink, todoId){ //get SPECIFIC todos's sharable link
    return apiClient.delete(`http://localhost:8080/share/${sharabeLink}/container/todos/${todoId}`)
}

export function updateSpecificTodoBySharableLinkApi(sharabeLink, todoId, todo){ //get SPECIFIC todos's sharable link
    return apiClient.put(`http://localhost:8080/share/${sharabeLink}/container/todos/${todoId}`,todo
        
            
        
    )
}






//MESSAGING COMMENTING
//MESSAGING COMMENTING
export function addCommentsApi(token, todoId, content){
    return apiClient.post(`http://localhost:8080/share/${token}/container/todos/${todoId}/comments`, content)
}
export function getCommentsApi(token, todoId){
    return apiClient.get(`http://localhost:8080/share/${token}/container/todos/${todoId}/comments`)
}
//MESSAGING COMMENTING
//MESSAGING COMMENTING




//logging
export function logInsideCOntainers(sharelink){ //get SPECIFIC todos's sharable link
    return apiClient.get(`/users/alllogsincontainer/${sharelink}`)
}


// export function getSpecificTodoBySharableLinkApi(sharabeLink, todoId){ //get SPECIFIC todos's sharable link
//     return apiClient.get(`http://localhost:8080/share/${sharabeLink}/container/todos/${todoId}`)
// }


export function createTodoContainerAPI(username, title){
    //post method and 
    // {
    //     "title": " 1 Create BABY with you"
    // }

    return apiClient.post(`http://localhost:8080/users/${username}}/containers`,
        {
            title
        }
    )

}
















//after clicking login, call this api.

//Naturally, AFTER DISABLING CSRF, this will result error: Response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource.
//what?? well, OPTION method is sent first to check if GET/POST req is safe.

//to fix this, allow OPTION request in Spring boot security
//after allowing OPTION REQUEST as Preflight error, new issue is not hard coding the TOKEN.



export function testSecurityApi(token){
    return apiClient.get(`http://localhost:8080/test-security`,
        {
            headers: {
                //Authorization: "Basic amFzcGhlcjpwYXNzd29yZA==" --dont hardcore, use the login power ;)
                Authorization: token ,
            
                //where to get?? craete token on login using the pattern of USERNAME and PASSWORD

            }
        }
    )
}



// export function jwtImplementationApi(username, password){
//     return apiClient.post(`http://localhost:8080/authenticate`,
//         {
//            username, password //we are submitting an objects
//         }
//     )
// }


export function jwtImplementationApi(email, password){ //this will return token
    return apiClient.post(`http://localhost:8080/auth/login`,
        {
            email, password //we are submitting an objects
        }
    )
}

export function signupUser(email, fullName, password) {
    return apiClient.post('/auth/signup', {
        email,
        password,
        fullName
    });
}