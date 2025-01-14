import 'bootstrap/dist/css/bootstrap.min.css'; // Ensure Bootstrap CSS is imported
import { useAuth } from "./Security/AutoContext";
import { createTodoContainerAPI, deleteByIdApi, deleteContainerApi, getAllContainersWithTodos, retrieveByUserApi, shareTodoContainerApi, updateCOntainerTitleApi, updateTodoApi } from './api/TodoApiServices';
import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../../PopupForm.css';
import { Button, Modal } from 'react-bootstrap';

export default function ContainersOfTodos() {
    const authContext = useAuth();
    const navigate = useNavigate();
    const [containers, setContainers] = useState([]);
    const [containerTitle, setContainerTitle] = useState("blank");
    const [shareLink, setShareLink] = useState("");
    const [showPopup, setShowPopup] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [newTodoTitle, setNewTodoTitle] = useState("");
    const [editTodoTitle, setEditTodoTitle] = useState("");
    const [selectedContainerId, setSelectedContainerId] = useState(null);

    useEffect(() => reloadContainers(), []); // Reload containers on component mount

    const handleCreateTodoClick = () => {
        setShowPopup(true); // Show the create container popup
    };

    const handleClosePopup = () => {
        setShowPopup(false); // Close the create container popup
    };

    const handleCreateTodo = () => {
        createTodoContainerAPI(authContext.loggedUser, newTodoTitle)
            .then(response => {
                setContainers(prevContainers => [...prevContainers, response.data]); // Add new container
                setShowPopup(false); // Close popup
            })
            .catch(error => console.log(error));
    };

    const handleEditContainer = (containerId, title) => {
        setSelectedContainerId(containerId);
        setEditTodoTitle(title);
        setShowEditModal(true); // Show edit modal
    };

    const handleDeleteContainer = (containerId) => {
        setSelectedContainerId(containerId);
        setShowDeleteModal(true); // Show delete modal
    };

    const handleUpdateContainer = () => {
        updateCOntainerTitleApi(authContext.loggedUser, selectedContainerId, { title: editTodoTitle })
            .then(response => {
                setContainers(prevContainers =>
                    prevContainers.map(container =>
                        container.id === selectedContainerId ? { ...container, title: editTodoTitle } : container
                    )
                );
                setShowEditModal(false); // Close the edit modal after updating
            })
            .catch(error => console.log(error));
    };

    const handleConfirmDelete = () => {
        deleteContainerApi(authContext.loggedUser, selectedContainerId)
            .then(() => {
                setContainers(prevContainers => prevContainers.filter(container => container.id !== selectedContainerId));
                setShowDeleteModal(false); // Close the delete modal after deletion
            })
            .catch(error => console.log(error));
    };

    return (
        <div className="card mb-4">
            <div className="card-header">
                <h2>Containers of Todos</h2>
                <p>Created by: Jaspher</p>
            </div>

            <button className="btn btn-primary" onClick={handleCreateTodoClick}>
                CREATE TODO CONTAINER
            </button>

            <div className="card-body">
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Username</th>
                            <th>SEE TODOS</th>
                            <th>SHARE TODOS</th>
                            <th>EDIT</th>
                            <th>DELETE</th>
                        </tr>
                    </thead>
                    <tbody>
                        {containers.map(containerInfo => (
                            <tr key={containerInfo.id}>
                                <td>{containerInfo.id}</td>
                                <td>{containerInfo.title}</td>
                                <td>{containerInfo.username}</td>
                                <td><Link className="nav-link" to={`/welcome/${authContext.loggedUser}/todo-page/containers/${containerInfo.id}/${containerInfo.title}/todos`}><span style={{ backgroundColor: 'red', color: 'white', padding: '4px', borderRadius: '50%', display: 'inline-block' }}> SEE TODOS</span></Link></td>
                                <td><button onClick={() => handleShareButton(containerInfo.id)}>SHARE</button></td>
                                <td><button onClick={() => handleEditContainer(containerInfo.id, containerInfo.title)}>EDIT</button></td>
                                <td><button onClick={() => handleDeleteContainer(containerInfo.id)}>DELETE</button></td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {/* Create Todo Container Popup */}
            {showPopup && (
                <div className="popup">
                    <div className="popup-content">
                        <h3>Create New Container</h3>
                        <input
                            type="text"
                            className="form-control mb-3"
                            value={newTodoTitle}
                            onChange={(e) => setNewTodoTitle(e.target.value)}
                            placeholder="Enter TODO Container Title"
                        />
                        <button className="btn btn-success" onClick={handleCreateTodo}>
                            Create
                        </button>
                        <button className="btn btn-danger" onClick={handleClosePopup}>
                            Cancel
                        </button>
                    </div>
                </div>
            )}

            {/* Edit Todo Container Modal */}
            <Modal show={showEditModal} onHide={() => setShowEditModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit Container Title</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <input
                        type="text"
                        className="form-control"
                        value={editTodoTitle}
                        onChange={(e) => setEditTodoTitle(e.target.value)}
                    />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowEditModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handleUpdateContainer}>
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>

            {/* Delete Todo Container Confirmation Modal */}
            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Deletion</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Are you sure you want to delete this container?
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleConfirmDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );

    function reloadContainers() {
        getAllContainersWithTodos(authContext.loggedUser)
            .then(response => setContainers(response.data))
            .catch(error => console.log(error));
    }

    async function handleShareButton(containerId) {
        try {
            const response = await shareTodoContainerApi(containerId, { username: authContext.loggedUser });
            const shareLink = response.data;
            setShareLink(shareLink);
            navigate(`/share/${shareLink}`);
        } catch (error) {
            console.log(error);
        }
    }
}
