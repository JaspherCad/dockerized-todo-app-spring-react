package com.jaspher.rest.webservices.restfulwebservices.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jaspher.rest.webservices.restfulwebservices.accesslog.model.TodoAccessLog;
import com.jaspher.rest.webservices.restfulwebservices.comment.model.Comment;
import com.jaspher.rest.webservices.restfulwebservices.comment.model.CommentDTOoutput;
import com.jaspher.rest.webservices.restfulwebservices.sharablelink.model.SharableLink;
import com.jaspher.rest.webservices.restfulwebservices.todo.model.Todo;
import com.jaspher.rest.webservices.restfulwebservices.todo.service.TodoService;
import com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy.TodoContainer;
import com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy.TodoContainerDTO;
import com.jaspher.rest.webservices.restfulwebservices.users.User;

import java.util.List;
import java.util.Map;


@RestController
public class TodoControllerJpa {
	
	TodoService serviceDao;
	
	 @Autowired
	 private SimpMessagingTemplate messagingTemplate;
	
	public TodoControllerJpa(TodoService serviceDao) { //dependency injection
		super();
		this.serviceDao = serviceDao;
	}

	@GetMapping("/users")
	public List<Todo> retrieveAllTodo(){
		return serviceDao.returnAll();
	}
//	@PostMapping("/users")
//	public void addUser(@RequestBody Todo todo){
//		serviceDao.addTodo(todo.getUsername(), todo.getDescription(), todo.getTargetDate(), false);
//		//serviceDao.addTodoButReturnClass(todo);
//		System.out.println(todo);
//		System.out.println(todo.getDescription());
//		
//
//	}
	
	
	
	
	
	//THERE WAS NO ERROR HERE, the error is in SPRING SECURITY
	@GetMapping("/users/{username}/todos")
    public List<Todo>  retrieveByUser(@PathVariable String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
		
		if(!loggedUser.equals(username)) {
	        return null;
		}
        return serviceDao.getTodosByUsername(username); //SERVICEDAO IS TODO SERVICE! HAHA
    }
	
	
	//TODO TODO TODO TODO!! where can i retreve? new method below (getTodoContainer chuchu)
	
	//TODO: ERROR MAY OCCURE HERE AFTER IMPLEMENTING !loggedUser.equals(username)
	//@PostMapping(path = "/users/{username}/todos")
	public Todo createTodo(@PathVariable String username, @RequestBody Todo todo, @RequestBody Long containerId) {
		//get the username or fullname from JWT! DO IT INSDE SERVICE of TODO
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
		
		if(!loggedUser.equals(username)) {
	        return null;
		}
	
		return serviceDao.addTodo(username, containerId, todo.getDescription(), todo.getTargetDate(), false);
	}
	
				//	{
				//	ID
				//	USERNAME
				//	DESCRIPTION
				//	TARGETDATE
				//	DONE
				//}

	
	//TODO: IF SUCCESSFUL, CHANGE ALL LINKS TO THIS PATTERN!!!
	//HERES THE get method btw  @GetMapping("/users/{username}/containers/{containerId}/todos")
	@PostMapping(path = "/users/{username}/containers/{containerId}/todos")
	public Todo createTodov2(@PathVariable String username,  @RequestBody Todo todo, @PathVariable Long containerId) {
		//get the username or fullname from JWT! DO IT INSDE SERVICE of TODO
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
		
		if(!loggedUser.equals(username)) {
	        return null;
		}
	
		return serviceDao.addTodo(username, containerId, todo.getDescription(), todo.getTargetDate(), false);
	}
	
	
	
	//change pattern to this
	//	@PostMapping(path = "/users/{username}/containers/{containerId}/todos")

//	@GetMapping(path = "/users/{name}/todos/{id}")
//	public Todo retrieveTodoById(@PathVariable String name, @PathVariable int id) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
//        
//		String loggedUser;
//		loggedUser = currentUser.getFullName();
//		
//		if(!loggedUser.equals(name)) {
//	        return null;
//		}
//		return serviceDao.findById(id, name);
//	}
	//change pattern to this MINATO
		//	@PostMapping(path = "/users/{username}/containers/{containerId}/todos")
	@GetMapping(path = "/users/{name}/containers/{containerId}/todos/{id}")
	public Todo retrieveTodoByIdv2(@PathVariable String name,  @PathVariable Long containerId, @PathVariable int id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
		
		if(!loggedUser.equals(name)) {
	        return null;
		}
		return serviceDao.findById(id, name, containerId);
	}
	
	
	
	
	
//	@PutMapping(path = "/users/{name}/todos/{id}")
//	public ResponseEntity<Todo>  updateTodoById(@PathVariable String name, @PathVariable int id,
//			@RequestBody Todo todo) {
//				//here the todo request body is whatever we SENT FROM frontend. meaning, populated na yung content.
//				//meaning: Whatever we are sending, will be mapped to Todo todo @RequestBody.
//		
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
//        
//		String loggedUser;
//		loggedUser = currentUser.getFullName();
//		
//		if(!loggedUser.equals(name)) {
//	        return null;
//		}else {
//			Todo updatedTodo = serviceDao.updateTodo(name, id, todo);
//			
//	        return ResponseEntity.ok(updatedTodo);
//
////	        TodoDto {
////	            private String description;
////	            private LocalDate targetDate;
////	            private boolean done;
////	        } //check data.sql for dummy data for targetDate
//		}
//		
//		
//	}
	
	
	
	@DeleteMapping(path = "/users/{name}/containers/{containerId}/todos/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable String name, @PathVariable int id, @PathVariable Long containerId){
		 
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
		
		if(!loggedUser.equals(name)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}else {
			serviceDao.deleteById(name, id, containerId);
			return ResponseEntity.noContent().build();
		}
		
		
		
	}
	
	
	
	
	
	
	//	@PostMapping(path = "/users/{username}/containers/{containerId}/todos")

	@PutMapping(path = "/users/{name}/containers/{containerId}/todos/{id}")
	public ResponseEntity<Todo>  updateTodoByIdv2(@PathVariable String name, @PathVariable int id, @PathVariable Long containerId,
			@RequestBody Todo todo) {
				//here the todo request body is whatever we SENT FROM frontend. meaning, populated na yung content.
				//meaning: Whatever we are sending, will be mapped to Todo todo @RequestBody.
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
		
		if(!loggedUser.equals(name)) {
	        return null;
		}else {
			Todo updatedTodo = serviceDao.updateTodo(name, id, todo, containerId);
			
	        return ResponseEntity.ok(updatedTodo);

//	        TodoDto {
//	            private String description;
//	            private LocalDate targetDate;
//	            private boolean done;
//	        } //check data.sql for dummy data for targetDate
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	@PutMapping(path = "/users/toggleTodo/{id}")
	public ResponseEntity<Todo>  toggleTodoIsDone(@PathVariable int id, @RequestBody Todo todoState) { //meaning the name and id is from URL, so we use @PathVariable para masabi sa IDE na galing URL yung VALUES!!!
		
		Todo todo = serviceDao.toggleDoneToTrue(id, todoState);
		return ResponseEntity.ok(todo);
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//SHARING
	//SHARING
	//SHARING
	//SHARING
	//SHARING
	//SHARING
	
	//logic: instaed of access todo's of user using their logged creditentials, just use the SHARABLE LINK (generaetd).. todo tables have it 
	@PostMapping("/share/container/{containerId}/create")
	public ResponseEntity<String> createShareableLink(@RequestBody Map<String, String> requestBody, @PathVariable Long containerId) {
	    
//	reqBody {
//			  "username": "jaspher"
//			}

		
		String username = requestBody.get("username");
	    
	    if (username == null || username.isEmpty()) {
	        return ResponseEntity.badRequest().body("Username is required");
	    }

	    String token = serviceDao.createShareableLink(username, containerId);
	    return ResponseEntity.ok(token);
	}
	
	@DeleteMapping("/share/container/{containerId}/delete")
	public void deleteShareableLink(@PathVariable Long containerId){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
		serviceDao.removeSharableToken(loggedUser, containerId);
	    
	}

    @GetMapping("/share/{token}/containers/todos")
    public ResponseEntity<TodoContainerDTO> getTodosByShareableLink(@PathVariable String token) {
    	// Authentication not required for viewing
        String loggedUser = null; // Set to null for public access
//    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
//        
        
        TodoContainer container = serviceDao.getTodoContainerByShareableLink(token);
        if (container == null) {
            return ResponseEntity.notFound().build();  // Handle not found case
        }
        
        String containerTitle = container.getTitle();
        Long containerId = container.getId();
//		String loggedUser;
		loggedUser = null;
        List<Todo> todos = serviceDao.getTodosByShareableLink(token, loggedUser);
        String ownerOfTodo = serviceDao.getOwnerFromToken(token);
        
        TodoContainerDTO containerDTO = new TodoContainerDTO(containerTitle, todos, ownerOfTodo);
        containerDTO.setContainerId(containerId);
        return ResponseEntity.ok(containerDTO);
    }
    
    
    
    
    @PostMapping("/share/{token}/containers/todos")
    public ResponseEntity<Todo> addTodosBySharableLink(@PathVariable String token, @RequestBody Todo updatedTodo) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
        Todo todo = serviceDao.addTodosBySharableLink(currentUser, token, loggedUser, updatedTodo);
        return ResponseEntity.ok(todo);
    }
    
    
    

    @GetMapping("/share/{token}/container/todos/{id}")
    public ResponseEntity<Todo> getSpecificTodoBySharableLink(@PathVariable String token, @PathVariable int id){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
    	Todo todo = serviceDao.getTodoByShareableLinkAndId(token, id, loggedUser);
    	
        return ResponseEntity.ok(todo);
    }
    
    //we are editin the smae entity but through different ways
    //in original, we access thru url using USERNAME (getFullName)
    //but in sharing, we access thru url using TOKEN (which are gneraed thru service.
    @PutMapping("/share/{token}/container/todos/{id}")
    public ResponseEntity<Todo> updateTodoByShareableLink(@PathVariable String token, @PathVariable int id,
            @RequestBody Todo updatedTodo) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
        Todo todo = serviceDao.updateTodoByShareableLink(token, id, updatedTodo, loggedUser);
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/share/{token}/container/todos/{id}")
    public ResponseEntity<Void> deleteTodoByShareableLink(@PathVariable String token, @PathVariable int id) {
        //im not going to delete the container
    	//rather going to delete a todo by id inside the cotainer thru SHareLink
    	//inside sharelink we have userID and containerI
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
    	serviceDao.deleteTodoByShareableLink(token, id, loggedUser);
        return ResponseEntity.noContent().build();
    }
	
	
	
	
	//SHARING
	//SHARING
	//SHARING
	//SHARING
	//SHARING
	//SHARING
	
	
	//MESAGES COMMENTS
  //MESAGES COMMENTS
  //MESAGES COMMENTS
  //MESAGES COMMENTS
  //MESAGES COMMENTS
  //MESAGES COMMENTS
    
    
    @PostMapping("/share/{token}/container/todos/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable String token, @PathVariable Integer id, @RequestBody CommentDTOoutput commentDTOoutput){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Comment comment = serviceDao.addCommentsToSharedTodo(token, currentUser, commentDTOoutput.getContent(), id);
        
        // Notify subscribers about the new comment
        messagingTemplate.convertAndSend("/topic/comments/" + token + "/" + id, comment);
        //^^^ is this like database for msgs???? maybe where we can send/STORE all msgs
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
    
    @GetMapping("/share/{token}/container/todos/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String token, @PathVariable Integer id){
        List<Comment> comments = serviceDao.getCommentsFromSharedTodo(token, id);
        return ResponseEntity.ok(comments);
    }
    
    
//    SimpMessagingTemplate: This is used to send messages to clients subscribed to specific topics.
//    /topic/comments/{token}/{id}: This is the topic to which clients will subscribe to receive real-time updates.
    
    
    
  //MESAGES COMMENTS
    //MESAGES COMMENTS
    //MESAGES COMMENTS
    //MESAGES COMMENTS
    //MESAGES COMMENTS
    //MESAGES COMMENTS
	
	
	//ACCESSING LOGS
	//ACCESSING LOGS
	//ACCESSING LOGS
	//ACCESSING LOGS
	//ACCESSING LOGS
	//ACCESSING LOGS

    
//    @GetMapping("/users/{username}/todos/access-logs")
//    public ResponseEntity<List<TodoAccessLog>> getLogsByOwner(@PathVariable String username) {
//    	
//    	List<TodoAccessLog> logs = serviceDao.getLogsByOwner(username);
//        return ResponseEntity.ok(logs);
//    }
//    
//    @GetMapping("/users/access-logs/accessed-by/{username}")
//    public ResponseEntity<List<TodoAccessLog>> getLogsByAccessedBy(@PathVariable String username) {
//        List<TodoAccessLog> logs = serviceDao.getLogsByAccessedBy(username);
//        return ResponseEntity.ok(logs);
//    }
//
//    @GetMapping("/users/{username}/todos/access-logs/{actionType}")
//    public ResponseEntity<List<TodoAccessLog>> getLogsByOwnerAndActionType(
//            @PathVariable String username, 
//            @PathVariable String actionType 
//            ) {
//      
//
//        List<TodoAccessLog> logs = serviceDao.getLogsByfindByActionType(actionType);
//        return ResponseEntity.ok(logs);
//    }
//    
    
    
    
    
    //new access logging style
    @GetMapping("/users/alllogsincontainer/{sharelink}")
    public ResponseEntity<List<TodoAccessLog>> getAllLogsByContainer(
								            @PathVariable String sharelink){
    	
    	
    	
        List<TodoAccessLog> logs = serviceDao.getAllLogsByContainer(sharelink);
        return ResponseEntity.ok(logs);
    }

    
    
    
    
    
    
    
    
	//ACCESSING LOGS
	//ACCESSING LOGS
	//ACCESSING LOGS
	//ACCESSING LOGS
	//ACCESSING LOGS
	//ACCESSING LOGS
	//ACCESSING LOGS
    
    
    
    
    
    
    
    
    
    
  //TodoContainerImplementation
    //TodoContainerImplementation
    //TodoContainerImplementation
    //TodoContainerImplementation
    
    
    //if error exist, implement @GET
    @PostMapping("/users/{username}/containers")
    public ResponseEntity<TodoContainer> createContaenr(@PathVariable String username, @RequestBody Map<String, String> request){
    	String title = request.get("title");
    	//for security, i like to use JWT rather than link...
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
    	TodoContainer todoContainer = serviceDao.createContainer(title, loggedUser);
    	return ResponseEntity.ok(todoContainer);
    }
    
    @DeleteMapping("/users/{username}/containers/{containerId}")
    public ResponseEntity<Void> deleteContainer(@PathVariable String username, @PathVariable Long containerId){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
		serviceDao.deleteContainer(loggedUser, containerId);
    	
    	return ResponseEntity.noContent().build(); 
    }
    
    
    @PutMapping("/users/{username}/containers/{containerId}")
    public ResponseEntity<TodoContainer> updateCOntainerTitle(@PathVariable String username, @PathVariable Long containerId, 
    		@RequestBody Map<String, String> title){
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
		
		String titleUpdate = title.get("title");
		
		TodoContainer container =  serviceDao.updateContainer(loggedUser, containerId, titleUpdate);
		return ResponseEntity.ok(container);
    	
    }
    
    
    
    @GetMapping("/users/{username}/containers")
    public ResponseEntity<List<TodoContainer>> getAllContainersWithTodos(@PathVariable String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        String loggedUser = currentUser.getFullName();

        if (!loggedUser.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<TodoContainer> containers = serviceDao.getContainersWithTodosByUsername(username);
        return ResponseEntity.ok(containers);
    }
    
    
    
    
    //This method will allow you to retrieve all todos that belong to a specific container.
    @GetMapping("/users/{username}/containers/{containerId}/todos")
    public ResponseEntity<List<Todo>> getTodosInContainer(@PathVariable String username, @PathVariable Long containerId) {
    	//for security, i like to use JWT rather than link...

    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
		String loggedUser;
		loggedUser = currentUser.getFullName();
		
		if (!loggedUser.equals(username)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	    }
		
		List<Todo> todos = serviceDao.getTodosInContainer(username, containerId);
	    return ResponseEntity.ok(todos);
    }
    
    //TODO HOW TO ACCESS SPECIFIC TODO??? WHAT IF I USED THE EXISTING GETBYSPECIFIC TODO MUNA??
    //TODO HOW TO ACCESS SPECIFIC TODO??? WHAT IF I USED THE EXISTING GETBYSPECIFIC TODO MUNA??
    //TODO HOW TO ACCESS SPECIFIC TODO??? WHAT IF I USED THE EXISTING GETBYSPECIFIC TODO MUNA??
    //TODO HOW TO ACCESS SPECIFIC TODO??? WHAT IF I USED THE EXISTING GETBYSPECIFIC TODO MUNA??
    //TODO HOW TO ACCESS SPECIFIC TODO??? WHAT IF I USED THE EXISTING GETBYSPECIFIC TODO MUNA??
    //TODO HOW TO ACCESS SPECIFIC TODO??? WHAT IF I USED THE EXISTING GETBYSPECIFIC TODO MUNA??
    
    //This method will retrieve all containers owned by a user, each with the todos inside them.


    
    
    
    
    
    
  //TodoContainerImplementation
    //TodoContainerImplementation
    //TodoContainerImplementation
    //TodoContainerImplementation
    
    
    
    
    
    
    

	
	
	
}
