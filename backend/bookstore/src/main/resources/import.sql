--roles

INSERT INTO tb_role (id, authority) VALUES (1, 'ROLE_CUSTOMER');
INSERT INTO tb_role (id, authority) VALUES (2, 'ROLE_OPERATOR');
INSERT INTO tb_role (id, authority) VALUES (3, 'ROLE_ADMIN');

--usuarios

INSERT INTO tb_user (email, firstname, lastname, password) VALUES ('leo@gmail.com', 'Leonardo', 'Poletto Casagrande', '$2a$10$YYdXGIgspPM/gnVuk1S/m.NqKmE3Gg3gkWLk74PRlpnnqELnpyW7O');
INSERT INTO tb_user (email, firstname, lastname, password) VALUES ('admin@gmail.com', 'Admin', 'Admin', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_user (email, firstname, lastname, password) VALUES ('john@gmail.com', 'John', 'Doe', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_user (email, firstname, lastname, password) VALUES ('customer@gmail.com', 'Customer', 'Customer', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

--user_role

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 3);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 3);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (4, 1);
