-- Inserting data into users table
INSERT INTO users (id, full_name, email, password, created_at, updated_at) VALUES 
(1001, 'JohnDoe', 'john.doe@example.com', '$2a$10$7QTOv8J6MwU/LiM2qZx/2eA/Jz/N.4PI7G4SeQazN0WqSOQ1O.DFe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1002, 'JaneSmith', 'jane.smith@example.com', '$2a$10$9HTzN8C/SN/T/2G5i6P/OeD.Vx/Z7U7G5J2QSeV1W1A3SOQ2N.ADe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserting data into todo_container table
INSERT INTO todo_container (id, username, title) VALUES 
(2001, 'JohnDoe', 'Create Messaging App'),
(2002, 'JaneSmith', 'Create E-commerce App');

-- Inserting data into todos table, linked to a container
INSERT INTO todo (id, user_id, container_id, description, target_date, done) VALUES 
(1, 1001, 2001, 'Learn HTML, CSS, JavaScript', '2024-12-31', false),
(2, 1001, 2001, 'Do frontend', '2024-12-31', false),
(3, 1001, 2001, 'Do backend', '2024-12-31', false),
(4, 1002, 2002, 'Setup database', '2024-12-15', false),
(5, 1002, 2002, 'Implement payment gateway', '2024-12-20', false);
