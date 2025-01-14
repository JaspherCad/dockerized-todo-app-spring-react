import { Link, useNavigate } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css'; // Ensure Bootstrap CSS is imported
import {useAuth} from "./Security/AutoContext";
import { useState } from "react";

import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
//USE CONTEXT LEARN HERE

//USE CONTEXT LEARN HERE
//USE CONTEXT LEARN HERE
//USE CONTEXT LEARN HERE
//USE CONTEXT LEARN HERE

export default function Header() {

    //const authContext = useContext(AuthContext); //now I can access all the SHARED context from AuthContext.js
    const authContext = useAuth();


    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
  

    const navigate = useNavigate();



    return (
        <>
            <header className="header bg-light py-3">
                <div className="container">
                    <nav className="navbar navbar-expand-lg navbar-light">
                        <ul className="navbar-nav mr-auto">
                            {authContext.isAuthenticated ? (
                                <>
                                <li className="nav-item">
                                <Link className="nav-link" to={`/welcome/${authContext.loggedUser}`}>WELCOME, {authContext.loggedUser}!</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to={`/welcome/${authContext.loggedUser}`}>My Todos</Link>
                            </li>
                                </>
                            ):
                            <>
                                <li className="nav-item">
                                    <Link className="nav-link" to={`/signup`}>Sign-up</Link>
                                </li>
                            </>}
                            
                        </ul>
                        <ul className="navbar-nav ml-auto">
                        {authContext.isAuthenticated ? (
                            <>
                                <li className="nav-item">
                                    <span className="nav-link" onClick={handleShow}>Logout</span>
                                </li>
                            </>
                        ) : (
                            <>
                                <li className="nav-item">
                                    <Link className="nav-link" to="/login">Login: {authContext.token}</Link>
                                </li>
                            </>
                        )}
                        <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>LOGOUT?</Modal.Title>
        </Modal.Header>
        <Modal.Body>Woohoo, Thank you, GOODBYE!</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" onClick={handleLogout}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>
                    </ul>
                </nav>
            </div>
        </header>
    </>
);


function handleLogout(){
    authContext.logout()
    navigate(`/login`);
    handleClose()
}
}
