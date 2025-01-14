package com.jaspher.rest.webservices.restfulwebservices.todo.service;
import java.lang.StackWalker.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jaspher.rest.webservices.restfulwebservices.accesslog.model.TodoAccessLog;
import com.jaspher.rest.webservices.restfulwebservices.comment.model.Comment;
import com.jaspher.rest.webservices.restfulwebservices.comment.repository.CommentRepository;
import com.jaspher.rest.webservices.restfulwebservices.sharablelink.model.SharableLink;
import com.jaspher.rest.webservices.restfulwebservices.todo.accesslog.repository.TodoAccessLogRepository;
import com.jaspher.rest.webservices.restfulwebservices.todo.model.Todo;
import com.jaspher.rest.webservices.restfulwebservices.todo.service.repository.TodoRepository;
import com.jaspher.rest.webservices.restfulwebservices.todo.sharablelink.repository.SharableLinkRepository;
import com.jaspher.rest.webservices.restfulwebservices.todo.users.repository.UserRepository;
import com.jaspher.rest.webservices.restfulwebservices.todoContainer.model.copy.TodoContainer;
import com.jaspher.rest.webservices.restfulwebservices.todoContainer.repository.TodoContainerRepository;
import com.jaspher.rest.webservices.restfulwebservices.users.User;

import jakarta.transaction.Transactional;

@Service
public class TodoService {
	
	private static List<Todo> todos = new ArrayList<>(); //for static practice only
	
	private TodoRepository repository;
	private UserService userService;
	private UserRepository userRepository;
    private SharableLinkRepository shareableLinkRepository;
    private TodoAccessLogRepository todoAccessLogRepository;
    private TodoContainerRepository containerRepository;
    private CommentRepository commentRepository;
	

	public TodoService(TodoRepository repository, UserService userService, UserRepository userRepository, SharableLinkRepository shareableLinkRepository, TodoAccessLogRepository todoAccessLogRepository, TodoContainerRepository containerRepository, CommentRepository commentRepository) {
		super();
		this.repository = repository;
		this.userService = userService;
		this.userRepository = userRepository;
		this.shareableLinkRepository = shareableLinkRepository;
		this.todoAccessLogRepository = todoAccessLogRepository;
		this.containerRepository = containerRepository;
		this.commentRepository = commentRepository;
	}

	private static int todosCount = 0;
	
//	static {
//		
//		todos.add(new Todo(++todosCount, "in28minutes","Get AWS Certified", 
//							LocalDate.now().plusYears(10), false ));
//		todos.add(new Todo(++todosCount, "in28minutes","Learn DevOps", 
//				LocalDate.now().plusYears(11), false ));
//		todos.add(new Todo(++todosCount, "in28minutes","Learn Full Stack Development", 
//				LocalDate.now().plusYears(12), false ));
//		todos.add(new Todo(++todosCount, "jaspher","Learn to become master", 
//				LocalDate.now().plusYears(12), false ));
//		todos.add(new Todo(++todosCount, "jaspher","ABET ACCREDITED", 
//				LocalDate.now().plusYears(12), false ));
//		todos.add(new Todo(++todosCount, "jaspher","FUCK TIP", 
//				LocalDate.now().plusYears(12), false ));
//		todos.add(new Todo(++todosCount, "jaspher","lorem ipsum", 
//				LocalDate.now().plusYears(12), false ));
//		todos.add(new Todo(++todosCount, "jaspher","random tots", 
//				LocalDate.now().plusYears(12), false ));
//		todos.add(new Todo(++todosCount, "jaspher","dance with lj", 
//				LocalDate.now().plusYears(12), false ));
//	}
	
	public List<Todo> findByUsername(String username){
//		Predicate<? super Todo> predicate = 
//				todo -> todo.getUsername().equalsIgnoreCase(username);
//		return todos.stream().filter(predicate).toList();
		return repository.findByUsername(username);
	}
	
	public Todo findById(int id, String username, Long containerId) {
	    Optional<User> optUser = userRepository.findByFullName(username);
	    if (optUser.isPresent()) {
	        Optional<Todo> optTodo = repository.findById(id);
	        if (optTodo.isPresent()) {
	            Todo todo = optTodo.get();

	            // Ensure the todo belongs to the user
	            if (!todo.getUser().getFullName().equals(username)) {
	                throw new IllegalArgumentException("Todo does not belong to the user");
	            }

	            // Ensure the todo belongs to the correct container
	            if (!todo.getTodoContainer().getId().equals(containerId)) {
	                throw new IllegalArgumentException("Todo does not belong to the specified container");
	            }

	            return todo;
	        } else {
	            throw new IllegalArgumentException("Todo not found");
	        }
	    } else {
	        throw new IllegalArgumentException("User not found");
	    }
	}
	
	public List<Todo> returnAll(){
		return todos;
	}
	
	
	
	
	//TODO: First, fill ALL the column of the To-do table because we are returning todo
	//next: now we can save() a todo and fill all the requirements which are
			//	{ ps: id is generated..
			//	User_ID get thru jwt
			//	USERNAME get thru jwt
			//	DESCRIPTION get thru responseBody
			//	TARGETDATE get thru responseBody
			//	DONE get thru responseBody
			//}
	
	
	//changes, 	Long containerId
	public Todo addTodo(String username, Long containerId, String description, LocalDate targetDate, boolean done) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        //System.out.println(currentUser.getFullName());
        Integer userId;
        
		username = currentUser.getFullName(); //why i used this? well pathVariable is insecure. so use JWT		
		
		userId = currentUser.getId(); //who is the user
		
		User userrr;
		
		Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userrr = optionalUser.get();
        } else {
            throw new IllegalArgumentException("User not found");
        }
        
        // Find the container by ID... id will be get thru similar clicking delete button where id is embedded to that button thru loop
        TodoContainer todoContainer = containerRepository.findById(containerId)
        		 .orElseThrow(() -> new IllegalArgumentException("Container not found"));
        
        
     // Ensure that the container belongs to the current user
        if (!todoContainer.getUsername().equals(username)) {
            throw new IllegalArgumentException("Container does not belong to the user");
        }
            
         
        // Create the new Todo and add it to the container
		Todo todo = new Todo();
		
//		todo.setUser();
		todo.setUser(userrr);
		todo.setUsername(userrr.getFullName());
		todo.setDescription(description);
		todo.setTargetDate(targetDate);
		todo.setDone(done);
		todo.setTodoContainer(todoContainer);
		
		

//		todoContainer.getTodos().add(todo);
//		
//	    containerRepository.save(todoContainer);

		return repository.save(todo); //sends to db
		//todos.add(todo);
		
	}
	
	
	
	
	
	
	
	
	
	
	public List<Todo> getTodosByUsername(String username) {
	    Optional<User> optionalUser = userRepository.findByFullName(username); // Assuming email is used as username
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        return repository.findByUserId(user.getId()); //get his ID
	    }
	    //actually pedeng username instead of user_id.
	    return Collections.emptyList();
	}


	@Transactional
	public void deleteById(String username, int todoid, long containerId) {
		
		Optional<User> optionalUser = userRepository.findByFullName(username);
	    if (optionalUser.isPresent()) { //well for just throwing error if not user
	        Optional<Todo> optionalTodo = repository.findById(todoid);
	        if (optionalTodo.isPresent()) {
	            Todo todo = optionalTodo.get();
	            
	            // Ensure that the Todo belongs to the user and the container
	            if (todo.getUser().getFullName().equals(username) && todo.getTodoContainer().getId().equals(containerId)) {
	            	// Delete associated access logs first

	            	// Manually disassociate the logs
	                todoAccessLogRepository.findByTodo_Id(todoid).forEach(log -> {
	                    log.setTodo(null); // Disassociate the log from the todo
	                    todoAccessLogRepository.save(log);
	                });
	            	
	            	
	                // Now delete the Todo
	                repository.delete(todo);
	            } else {
	                throw new IllegalArgumentException("Todo does not belong to the user or the specified container");
	            }
	        } else {
	            throw new IllegalArgumentException("Todo not found");
	        }
	    } else {
	        throw new IllegalArgumentException("User not found");
	    }
        
        
        
    }

	
	
	
	

	public Todo updateTodo(String username, int id, Todo todo, Long containerID) {
		//WE ARE UPDATEING TODO SO WE HAVE TO FILL ALL THE TABLE of Foreign Key.
		//so we check first if the user ID exist before we check if the Todo exist
		//why check if todo exist? DUMB! THIS IS UPDATE
		
		Optional<User> optionalUser = userRepository.findByFullName(username);
		if (optionalUser.isPresent()) { //now check to do, if not, user not found
			//well just for checking,, that's all

			Optional<Todo> optionalTodo = repository.findById(id);
			
			if (optionalTodo.isPresent()) {
				Todo todoToBeUpdated = optionalTodo.get();
				
				// Check if the Todo belongs to the user
	            if (todoToBeUpdated.getUser().getFullName().equals(username)) {

	                // Ensure the Todo belongs to the correct container
	                if (todoToBeUpdated.getTodoContainer().getId().equals(containerID)) {
	                    // Update the Todo details
	                    todoToBeUpdated.setDescription(todo.getDescription());
	                    todoToBeUpdated.setTargetDate(todo.getTargetDate());
	                    todoToBeUpdated.setDone(todo.isDone());

	                    // Save and return the updated Todo
	                    return repository.save(todoToBeUpdated);
	                } else {
	                    throw new IllegalArgumentException("Todo does not belong to the specified container");
	                }

	            } else {
	                throw new IllegalArgumentException("Todo does not belong to the user");
	            }
	        } else {
	            throw new IllegalArgumentException("Todo not found");
	        }
	    } else {
	        throw new IllegalArgumentException("User not found");
	    }
	}
	
	
	//SHARABLE LINK IMPLEMENTATION
	//SHARABLE LINK IMPLEMENTATION
	//SHARABLE LINK IMPLEMENTATION
	//SHARABLE LINK IMPLEMENTATION

	//generate sharable link
	public String createShareableLink(String username, Long containerId) {//username is from JWT at CONTROLLER : ContainerID is from URL LINK at controller
		Optional<User> user = userRepository.findByFullName(username);
	    if (user.isPresent()) {
	    	
	        User specificUser = user.get();
	        String token = UUID.randomUUID().toString();
	        
	        TodoContainer todoContainer = containerRepository.findById(containerId)
	        		.orElseThrow(() -> new RuntimeException("CONTAINER NOT FOUND HAHA!"));
	       
	        //check if containerID belongs to the current user
	        if (!todoContainer.getUsername().equals(username)) {
	            throw new RuntimeException("Container does not belong to the user");

	        }
	        SharableLink link = new SharableLink();
	        
	        
	        link.setToken(token);
	        link.setUser(specificUser);
	        link.setContainer(todoContainer);
	        

	        shareableLinkRepository.save(link);
	        return token;
	        
	        //FILL: user, token, createdAtTime
	    } else {
	        throw new RuntimeException("User not found");
	    }
	}
	
	
	@Transactional
	public void removeSharableToken(String username, Long containerId) {
	    TodoContainer container = containerRepository.findById(containerId)
	            .orElseThrow(() -> new RuntimeException("CONTAINER NOT FOUND HAHA!"));
	    
	    if (!container.getUsername().equals(username)) {
	        throw new RuntimeException("Container does not belong to the user");
	    }
	    
	    shareableLinkRepository.deleteByContainer(container);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//	public Todo addTodo(String username, Long containerId, String description, LocalDate targetDate, boolean done) {

	
	public Todo addTodosBySharableLink(User user, String token, String accessedBy, Todo todo) {
		SharableLink sharableLink = shareableLinkRepository.findByToken(token)
	            .orElseThrow(() -> new RuntimeException("Shareable link not found"));
		
		
		//since now i have sharableLink, i can access the integrated container and user. 
		//use it as a key to create another todo to todo.
		TodoContainer todoContainer = sharableLink.getContainer();
		String username = sharableLink.getUser().getFullName();
		
		Todo todos = new Todo();
		
		Optional<User> realOwnerOfTodoOpt = userRepository.findByFullName(username);
		
		if (realOwnerOfTodoOpt.isPresent()) {
			User realOwnerOfTodo = realOwnerOfTodoOpt.get();
			todos.setUser(realOwnerOfTodo); //thru jwt
		}else {
            throw new IllegalArgumentException("User not found");
		}
		
		
		todos.setUsername(todoContainer.getUsername());
		todos.setDescription(todo.getDescription()); //from the body request
		todos.setTargetDate(todo.getTargetDate());  //from the body request
		todos.setDone(false);
		todos.setTodoContainer(todoContainer);
		todos.setId(null); //to avoid colision.
		
		Todo todoSaved = repository.save(todos);
		logAction(todoSaved, accessedBy, "ADDED TODO"	, "ADDED new TODO:" + todoSaved.getDescription());
		return todoSaved;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Get todos by shareable link
	//INSTEAD (original way) of getting todo THRU username or getByFullName()
	//get todo list using the FindByUsername coz todo have username but 
    public List<Todo> getTodosByShareableLink(String token, String accessedBy) {
    	//TODO: First find the specific token from whole repository
    	//second, get that speicifc token
    	//third, using that token, get the user thru  (link)user_id .getUser().getFUllName()
    	//using those values, find the todo findByUserName(instead directly users, use token then users)
        SharableLink link = shareableLinkRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Shareable link not found"));
        //THRU the specific link, we can access the user.. the use for findByUser

        
        ///UPDATE: instead of using findByUserName (using link.getUser), we used the container
        //since link has associated container, get todos from that contiainer
        TodoContainer todoContainer = link.getContainer();
        
        
        //find from Todo List using username(origin of the SharableLink's USER then get its name)
        //since link has associated container, get todos from that contiainer

        List<Todo> todos = todoContainer.getTodos();
        
        
        
        
     // Log access
//        for (Todo todo : todos) {
//            logAction(todo, accessedBy, "VIEW", "Viewed the todo list.");
//        }
        
        
        return todos;
    }
    
    
    
    
    
    
    
    
    //share/b37a6a7f-c400-4a4a-83a8-6c269545df87/todos/1
    public Todo getTodoByShareableLinkAndId(String token, int todoId, String accessedBy) {
        // Step 3.1: Find the shareable link by its token
        SharableLink link = shareableLinkRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Shareable link not found"));

        // Step 3.2: Retrieve the specific todo by ID and username
			//        Todo todo = repository.findByIdAndUsername(todoId, link.getUser().getFullName())
			//            .orElseThrow(() -> new RuntimeException("Todo not found for the user"));

        Todo todo = link.getContainer().getTodos().stream()
        		.filter(todoss -> todoss.getId() == todoId).findFirst()
        		.orElseThrow(() -> new RuntimeException("Todo not found in the container"));
        
        
		//        // Step 3.3: Log the view action for this specific todo
		//        TodoAccessLog accessLog = new TodoAccessLog();
		//        accessLog.setTodo(todo);  // Associate the log with the specific todo
		//        accessLog.setAccessedBy(accessedBy);  // Log the user who accessed the todo
		//        accessLog.setAccessedAt(LocalDateTime.now());  // Log the time of access
		//        accessLog.setActionType("VIEW");  // The action type (VIEW in this case)
		//        todoAccessLogRepository.save(accessLog);  // Save the log to the database

        logAction(todo, accessedBy, "VIEW"	, "Viewed the todo id: ." + todoId);
        // Step 3.4: Return the specific todo
        return todo;
    }
    
    
    // Update a todo by shareable link
    public Todo updateTodoByShareableLink(String token, int id, Todo updatedTodo, String accessedBy) {
        SharableLink link = shareableLinkRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Shareable link not found"));

        Todo todoFromSharedLink = link.getContainer().getTodos().stream()
        		.filter(todos -> todos.getId() == id).findFirst()
        		.orElseThrow(() -> new RuntimeException("Todo not found in the container"));
        
        
        Todo todo = repository.findById(todoFromSharedLink.getId())
            .orElseThrow(() -> new RuntimeException("Todo not found"));

//        if (!todo.getUsername().equals(link.getUser().getFullName())) {
//            throw new RuntimeException("Unauthorized to update this todo");
//        }

        todo.setDescription(updatedTodo.getDescription());
        todo.setTargetDate(updatedTodo.getTargetDate());
        todo.setDone(updatedTodo.isDone());

        logAction(todo, accessedBy, "UPDATED", "Updated the todo: " + id);
        return repository.save(todo);
    }
    
    
    
   
    
    @Transactional
    public void deleteTodoByShareableLink(String token, int todoId, String accessedBy) {
        // Fetch and delete the todo
    	
    	SharableLink link = shareableLinkRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Shareable link not found"));

       String username = link.getUser().getFullName();
       long containerId = link.getContainer().getId();
       
       Todo todo = repository.findByIdAndUsername(todoId, link.getUser().getFullName())
               .orElseThrow(() -> new RuntimeException("Todo not found for the user"));
       
       logAction(todo, accessedBy, "DELETED", "deleted the todo: " + todoId);
       
       deleteById(username, todoId, containerId);
       //todo here after deleted is DIASSOCIATED BEFORE DELETING..
       //without diassociateing to log.setTodo(null), then it will be delete along with the fucntin
       
       

       
    }
	

    public TodoContainer getTodoContainerByShareableLink(String token) {
        SharableLink link = shareableLinkRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Shareable link not found"));
        return link.getContainer(); // Assuming SharableLink has a reference to TodoContainer
    }
    
    public String getOwnerFromToken(String token) {
        SharableLink link = shareableLinkRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Shareable link not found"));
        return link.getContainer().getUsername(); // Assuming SharableLink has a reference to TodoContainer
    }
  
	
	//SHARABLE LINK IMPLEMENTATION
	//SHARABLE LINK IMPLEMENTATION
	//SHARABLE LINK IMPLEMENTATION
	//SHARABLE LINK IMPLEMENTATION

	
	
	
    //ACCESS LOGS
    //ACCESS LOGS
  //ACCESS LOGS
    //ACCESS LOGS//ACCESS LOGS
    //ACCESS LOGS
    
    
    public void logAction(Todo todo, String accessedBy, String actionType, String details) {
        TodoAccessLog accessLog = new TodoAccessLog();
        accessLog.setTodo(todo);
        accessLog.setTodoIdd(todo.getId());
        accessLog.setContainerIdd(todo.getTodoContainer().getId());

        accessLog.setAccessedBy(accessedBy);
        accessLog.setAccessedAt(LocalDateTime.now());
        accessLog.setActionType(actionType);
        accessLog.setDetails(details);
        todoAccessLogRepository.save(accessLog);
    }
    
    
//    
//    
//    public List<TodoAccessLog> getLogsByOwner(String username){
//    	return todoAccessLogRepository.findByTodo_Username(username);
//    }
//    
//    public List<TodoAccessLog> getLogsByAccessBy(String username){
//    	return todoAccessLogRepository.findByAccessedBy(username);
//    }
//    
//    public List<TodoAccessLog> getLogsByAccessedBy(String accessedBy) {
//        return todoAccessLogRepository.findByAccessedBy(accessedBy);
//    }
//
//    public List<TodoAccessLog> getLogsByfindByActionType(String actionType) {
//        return todoAccessLogRepository.findByActionType(actionType);
//    }
//    
//    
    
    
    
    
    //updated logging style
    
//    public List<TodoAccessLog> getLogsByOwnerAndContainer(String username, Long containerId) {
//        return todoAccessLogRepository.findByTodo_UsernameAndTodo_TodoContainer_Id(username, containerId);
//    }
//    
//    
//    public List<TodoAccessLog> getLogsByAccessedByAndContainer(String accessedBy, Long containerId) {
//        return todoAccessLogRepository.findByAccessedByAndTodo_TodoContainer_Id(accessedBy, containerId);
//    }
//    public List<TodoAccessLog> getLogsByActionTypeAndContainer(String actionType, Long containerId) {
//        return todoAccessLogRepository.findByActionTypeAndTodo_TodoContainer_Id(actionType, containerId);
//    }
    
    
    
    public List<TodoAccessLog> getAllLogsByContainer(String token) {
    	
    	
    	//using sharelink, get the associated todoContainerId 
    	//for using the containerId variable;
    	
    	
    	SharableLink link = shareableLinkRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Shareable link not found"));

    	long containerId = link.getContainer().getId();
        return todoAccessLogRepository.findByContainerIdd(containerId);
    }
    
    
    //updated logging style
    
    
    
    
    
    
    
    
    
  //messaging-commenting
  //messaging-commenting
  //messaging-commenting
  //messaging-commenting
  //messaging-commenting
  //messaging-commenting
  //messaging-commenting
  //messaging-commenting
    
    
    public Comment addCommentsToSharedTodo (String token, User currentUser, String content, Integer todoId) {
    	
    	SharableLink link = shareableLinkRepository.findByToken(token)
    			.orElseThrow(()-> new RuntimeException("Sharable Link Not Found")) ;
    	
    	//THRU LINK WE CAN GET THE TODO ID..
    	
    	Todo todo = link.getContainer().getTodos()
                .stream()
                .filter(t -> todoId.equals(t.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Todo not found for the given token"));
    	
    	User user = userRepository.findByFullName(currentUser.getFullName())
    			.orElseThrow(()-> new RuntimeException("User Not Found"));
    	
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setTodo(todo);
		comment.setUser(user);
		
		return commentRepository.save(comment);
    }
    
    
    public List<Comment> getCommentsFromSharedTodo(String token, Integer todoId) {
    	SharableLink link = shareableLinkRepository.findByToken(token)
    			.orElseThrow(()-> new RuntimeException("Sharable Link Not Found")) ;
    	
    	
    	
    	 // Ensure that todoId is not null
        if (todoId == null) {
            throw new IllegalArgumentException("Todo ID cannot be null");
        }
    	
    	
    	
    	Todo todo = link.getContainer().getTodos()
    		    .stream()
                .filter(t -> todoId.equals(t.getId()))  // Use equals method for safe comparison
    		    .findFirst()
    		    .orElseThrow(() -> new RuntimeException("Todo not found for the given token"));
    	   
    	   
    	return commentRepository.findByTodo_Id(todo.getId());

    	
    }
    
    
    
    //messaging-commenting
    //messaging-commenting
    //messaging-commenting
    //messaging-commenting
    //messaging-commenting
    //messaging-commenting
    //messaging-commenting
    //messaging-commenting
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  //ACCESS LOGS
    //ACCESS LOGS//ACCESS LOGS
    //ACCESS LOGS//ACCESS LOGS
    //ACCESS LOGS//ACCESS LOGS
    //ACCESS LOGS
	
	
	
    
    
    
    
    
    //TodoContainerImplementation
    //TodoContainerImplementation
    //TodoContainerImplementation
    //TodoContainerImplementation
    
    public TodoContainer createContainer(String title, String username) { //get the username from JWT function in controller
    	TodoContainer todoContainer = new TodoContainer();
    	todoContainer.setUsername(username);
    	todoContainer.setTitle(title);
    	return containerRepository.save(todoContainer);
    }
    
    @Transactional
    public TodoContainer deleteContainer(String username, Long containerId) {
    	//delete all children first
    	TodoContainer todoContainer = containerRepository.findById(containerId)
                .orElseThrow(() -> new RuntimeException("Container not found"));
    	
    	
    	if(!todoContainer.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to container");

    	}
    	
    	//delete all access log
    	
    	List<Todo> todos = repository.findByTodoContainer(todoContainer);
    	//if todo container == 1, then todo with containerId of 1 is todos (madami sila)
    	
    	for (Todo todo : todos) {
    		todoAccessLogRepository.deleteByTodo(todo);
    	}
    	
    	
    	//list of todos<> = repos.findByTodoCo.
    	//todoacceslogrepository.deleteByTodo(todos that havve ontainerid of this)
    	//use for loop to delete all
    	
    	//ONLY then delete todos inside container
    	
    	//sharable delete all
    	//then delete the actual cotainer
    	
    	
    	repository.deleteByTodoContainer(todoContainer);

    	shareableLinkRepository.deleteByContainer(todoContainer);
    	containerRepository.delete(todoContainer);
    	return null;
    }
    
    
    
    
    
    
    
    
    
    public TodoContainer updateContainer(String username, Long containerId, String title) {
    	TodoContainer todoContainer = containerRepository.findById(containerId)
    			.orElseThrow(() -> new RuntimeException("Container not found"));
    	
    	//verify if the user
    	if (!todoContainer.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to container");
    	}
    	
    	todoContainer.setTitle(title);
    	return containerRepository.save(todoContainer);
    }

    
    public List<TodoContainer> getContainersvYUsername(String username){
    	return containerRepository.findByUsername(username);
    }
    
    public List<Todo> getTodosInContainer(String username, Long containerId) {
        TodoContainer container = containerRepository.findById(containerId)
            .orElseThrow(() -> new RuntimeException("Container not found"));

        if (!container.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to container");
        }

        return container.getTodos();
    }
    
    
    public List<TodoContainer> getContainersWithTodosByUsername(String username) {
        return containerRepository.findByUsername(username);
    }
    //other methods such as deleting and editing container itself
    
    
    
    
    //TodoContainerImplementation
    //TodoContainerImplementation
    //TodoContainerImplementation
    //TodoContainerImplementation
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
	public Todo toggleDoneToTrue(int id, Todo todoState) {
		
		Todo todo = repository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Container not found"));
		
		
		todo.setDone(todoState.isDone());
		todo.setDescription(todoState.getDescription());
		todo.setTargetDate(todoState.getTargetDate());
		return repository.save(todo);
	}

	
	
	
	
	
}