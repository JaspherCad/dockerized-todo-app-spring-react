import 'bootstrap/dist/css/bootstrap.min.css'; 
import { useAuth } from "./Security/AutoContext";
import { useEffect, useState, useCallback, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { addCommentsApi, deleteSpecificTodoBySharableLinkApi, getCommentsApi, getTodosByShareableLinkApi, logInsideCOntainers, removeSharableLinksFromContainer, updateTodoApi, updateTodoDone } from './api/TodoApiServices';
import { Button, Modal, Table } from 'react-bootstrap';
import { connectWebSocket, disconnectWebSocket, sendComment } from './useWebSocket';
import '../../Messaging.css';

export default function SharedComponent() {
    const authContext = useAuth();
    const navigate = useNavigate();
    const { username, sharelink, containerId } = useParams();

    const [isActive, setIsActive] = useState(true)
    const [title, setTitle] = useState("titles");
    const [containerIdTo, setContainerIdTo] = useState(1);


    const [showDeleteModal, setShowDeleteModal] = useState(false); // New state for delete modal

    const [todos, setTodo] = useState([]);
    const [owner, setOwner] = useState("");
    const [actionLogs, setActionLogs] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [showCommentModal, setShowCommentModal] = useState(false);
    const [selectedTodo, setSelectedTodo] = useState(null);
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');

    const toggleModal = () => setShowModal(!showModal);
    const toggleDeleteModal = () => setShowDeleteModal(!showDeleteModal); // Toggle function for delete modal


    const reloadTodos = useCallback(() => {
        getTodosByShareableLinkApi(sharelink)
            .then(response => {
                setTodo(response.data.todos);
                setTitle(response.data.containerTitle);
                setContainerIdTo(response.data.containerId);
                setOwner(response.data.ownerOfTodo);
                setIsActive(true)
            })
            .catch(error => {
                console.log(error);
            if (error.response && error.response.data && error.response.data.detail) {
                setTitle(error.response.data.detail);
                setIsActive(false)
            } else {
                setTitle("An error occurred");
                setIsActive(false)
            }
            });

        logInsideCOntainers(sharelink)
            .then(response => setActionLogs(response.data))
            .catch(error => console.log(error));
    }, [sharelink]);

    const handleIncomingComment = useCallback((comment) => {
        if (comment.todoId === selectedTodo?.id) {
            setComments(prevComments => [...prevComments, comment]);
        }
    }, [selectedTodo]);






    
    // Initialize todos and comments
useEffect(() => {
    const initializeTodosAndComments = async () => {
        await reloadTodos();
        if (todos.length > 0 && !selectedTodo) {
            setSelectedTodo(todos[0]); // Or choose based on your logic
            loadComments(sharelink, todos[0].id);
        } else if (selectedTodo) {
            loadComments(sharelink, selectedTodo.id);
        }
    };

    initializeTodosAndComments();
}, [reloadTodos, todos, sharelink, selectedTodo]);

// Setup and cleanup WebSocket
const webSocketClientRef = useRef(null);
    
useEffect(() => {
    // Establish WebSocket connection
    webSocketClientRef.current = connectWebSocket(sharelink, handleIncomingComment);

    // Cleanup on component unmount
    return () => {
        disconnectWebSocket(webSocketClientRef.current);
    };
}, [sharelink, handleIncomingComment]);


    const createTodoHandler = () => {
        navigate(`/welcome/${authContext.loggedUser}/share/${sharelink}/todos/-1`);
    };

    const handleStopSharing = () => {
        removeSharableLinksFromContainer(containerIdTo)
        .then(() => {
            // Send the comment via WebSocket
            reloadTodos()
            navigate(`/welcome/${authContext.loggedUser}`)
        })
        .catch(error => console.error(error));



        console.log("Stop Sharing or Delete action triggered");
        toggleDeleteModal(); // Close the modal after the action
    };



    useEffect(() => {
        if (selectedTodo) {
            loadComments(sharelink, selectedTodo.id);
        }
    }, [selectedTodo, sharelink]);

    const handleRowClick = (todo) => {
        setSelectedTodo(todo);
        setShowCommentModal(true);
    };

    const handleCommentChange = (e) => {
        setNewComment(e.target.value);
    };

    const handleAddComment = () => {
        if (newComment.trim()) {
            addCommentsApi(sharelink, selectedTodo.id, { content: newComment })
                .then(() => {
                    // Send the comment via WebSocket
                    sendComment(sharelink, selectedTodo.id, newComment);
                    setNewComment("");
                })
                .catch(error => console.error(error));
        }
    };

    const loadComments = (sharelink, todoId) => {
        getCommentsApi(sharelink, todoId)
            .then(response => setComments(response.data))
            .catch(error => console.log(error));
    };

    return (
        <>
        
            <div className="container mt-5">
            <h5 style={{ marginTop: '-30px', color: 'gray', fontWeight: 'normal'   }}>Created by: {owner}</h5>
            <h1 className="mb-4">{title}</h1>
            <h5 style={{ marginTop: '-30px', color: 'gray', fontWeight: 'normal'   }}>instruction: copy the whole url above to any browser to see the shared todo: </h5>
            <h5 style={{ marginTop: '0', color: 'gray', fontWeight: 'normal'   }}>Authenticate if you want to edit!: Only authenticated users can EDIT DELETE ADD todo; otherwise just read </h5>
            

                {!authContext.isAuthenticated && (
                    <h1>VIEW ONLY! Log-in to update the list.</h1>
                )}
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
                                <th>Comments</th>
                            </tr>
                        </thead>
                        <tbody>
                            {todos.map(todo => (
                                <tr key={todo.id}>
                                    <td>{todo.id}</td>
                                    <td>{todo.description}</td>
                                    <td>{todo.targetDate ? todo.targetDate.toString() : 'No Date'}</td>
                                    <td>
                                        <button className='btn btn-secondary' disabled={!authContext.isAuthenticated} onClick={() => handleSetDone(todo, todo.id)}>
                                            {todo.done ? 'Yes' : 'No'}
                                        </button>
                                    </td>
                                    <td>
                                        <button className='deleteBtn' onClick={() => handleDelete(todo.id)} disabled={!authContext.isAuthenticated }>Delete</button>
                                    </td>
                                    <td>
                                        <button className='updateBtn' onClick={() => handleUpdate(todo.id)} disabled={!authContext.isAuthenticated}>Update</button>
                                    </td>
                                    <td>
                                        <button className='btn btn-info' onClick={() => handleRowClick(todo)} disabled={!authContext.isAuthenticated}>View/Add Comments</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    <button className='btn btn-success' onClick={createTodoHandler} disabled={!authContext.isAuthenticated || !isActive} >Create Todo</button>
                    <button className="btn btn-info ml-3" onClick={toggleModal} disabled={!authContext.isAuthenticated || !isActive}>View Action Logs</button>
                    <button className="btn btn-danger ml-3" onClick={toggleDeleteModal} disabled={!authContext.isAuthenticated || !isActive}>Stop Sharing</button> 
                </div>

                {/* Modal for Comments */}
                <Modal show={showCommentModal} onHide={() => setShowCommentModal(false)}>
                    <Modal.Header closeButton>
                        <Modal.Title>Comments for Todo: {selectedTodo && selectedTodo.description}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="comment-section">
                            {comments.length > 0 ? (
                                comments.map(comment => (
                                    <div key={comment.id} className={comment.user.id === authContext.loggedUser ? 'my-comment' : 'other-comment'}>
                                        <p><strong>{comment.user.fullName}:</strong> {comment.content}</p>
                                        <small className="text-muted">{new Date(comment.createdAt).toLocaleString()}</small>
                                    </div>
                                ))
                            ) : (
                                <p>No comments yet.</p>
                            )}
                            {authContext.isAuthenticated && (
                                <>
                                    <input
                                        type="text"
                                        value={newComment}
                                        onChange={handleCommentChange}
                                        placeholder="Add a comment"
                                        className="form-control"
                                    />
                                    <Button onClick={handleAddComment} className="mt-2">Add Comment</Button>
                                </>
                            )}
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowCommentModal(false)}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>

                {/* Modal for Action Logs */}
                <Modal show={showModal} onHide={toggleModal}>
                    <Modal.Header closeButton>
                        <Modal.Title>Action Logs</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Table striped bordered hover>
                            <thead>
                                <tr>
                                    <th>Accessed By</th>
                                    <th>Accessed At</th>
                                    <th>Action Type</th>
                                    <th>Details</th>
                                </tr>
                            </thead>
                            <tbody>
                                {actionLogs.map(log => (
                                    <tr key={log.id}>
                                        <td>{log.accessedBy}</td>
                                        <td>{new Date(log.accessedAt).toLocaleString()}</td>
                                        <td>{log.actionType}</td>
                                        <td>{log.details}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={toggleModal}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>



                {/* Modal for Deleting or Stopping Sharing */}
                <Modal show={showDeleteModal} onHide={toggleDeleteModal}>
                    <Modal.Header closeButton>
                    <Modal.Title>Are you sure you want to delete or stop sharing?</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <p>Deleting this will remove all shareable links associated with this TodoContainer. This action cannot be undone.</p>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={toggleDeleteModal}>
                            DON'T DELETE
                        </Button>
                        <Button variant="danger" onClick={handleStopSharing}>
                            DELETE?
                        </Button>
                    </Modal.Footer>
                </Modal>
                
            </div>
        </>
    );

    function handleDelete(id) {
        deleteSpecificTodoBySharableLinkApi(sharelink, id)
            .then(() => reloadTodos())
            .catch(error => console.log(error));
    }

    function handleUpdate(id) {
        navigate(`/welcome/${authContext.loggedUser}/share/${sharelink}/todos/${id}`);
    }

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
