-- CREACIÓN DE MODULOS
INSERT INTO module (name, base_path) VALUES ('AUTH', '/auth');
INSERT INTO module (name, base_path) VALUES ('DASHBOARD', '/dashboard');
INSERT INTO module (name, base_path) VALUES ('ROLES', '/roles');
INSERT INTO module (name, base_path) VALUES ('USERS', '/users');
INSERT INTO module (name, base_path) VALUES ('CLIENTS', '/clients');
INSERT INTO module (name, base_path) VALUES ('ORDERS', '/orders');
INSERT INTO module (name, base_path) VALUES ('ORDERS_DETAILS', '/order-details');
INSERT INTO module (name, base_path) VALUES ('STRUCTURE', '/files');
INSERT INTO module (name, base_path) VALUES ('DESIGN', '/designs');
INSERT INTO module (name, base_path) VALUES ('MATERIALS', '/materials');
INSERT INTO module (name, base_path) VALUES ('FEEDBACK', '/ratings');
INSERT INTO module (name, base_path) VALUES ('VIEWORDERS', '/client');
INSERT INTO module (name, base_path) VALUES ('MILESTONES', '/milestones');

-- CREACIÓN DE OPERACIONES
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('AUTHENTICATE','/authenticate', 'POST', true, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('VALIDATE-TOKEN','/validate-token', 'GET', true, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_MY_PROFILE','/profile','GET', true, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('LOGOUT','/logout','POST', true, 1);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('SHOW_REPORT_GRAPHICS','/reports','GET', false, 2);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_ROL','','POST', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_ROLES','','GET', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_ROL','/[0-9]*','PUT', false, 3);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_ONE_ROL','/[0-9]*','DELETE', false, 3);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_USER','','POST', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_USERS','','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_USER','/[0-9]*','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_USER','/[0-9]*','PUT', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_USER','/[0-9]*/disable','PUT', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CHANGE_PASSWORD','/[0-9]*/change-password','PUT', false, 4);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('EXPORT_PDF','/export/pdf','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('EXPORT_EXCEL','/export/excel','GET', false, 4);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('EXPORT_CSV','/export/csv','GET', false, 4);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_CLIENT','','POST', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_CLIENTS','','GET', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_CLIENT','/[0-9]*','GET', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_CLIENT','/[0-9]*','PUT', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_CLIENT','/[0-9]*/disable','PUT', false, 5);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CLIEN_EXPORT_PDF','/export/pdf','GET', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CLIENT_EXPORT_EXCEL','/export/excel','GET', false, 5);
-- Operaciones de Órdenes
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_ORDER','','POST', false, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_ORDERS','','GET', false, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_ORDER','/[0-9]*','GET', false, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_ORDER','/[0-9]*','PUT', false, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CANCEL_ONE_ORDER','/[0-9]*/cancel','PUT', false, 6);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('ORDER_EXPORT_PDF','/export/pdf','GET', false, 6);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_DETAIL','/[0-9]*','PUT', false, 7);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPLOAD_FILE','/{category}','POST', false, 8);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('GET_FILE','/{category}/{fileName}','GET', true, 8);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CHECK_FILE_EXISTS','/{category}/{fileName}/exists','GET', false, 8);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_FILE','/{category}/{fileName}','DELETE', false, 8);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_DESIGN','','POST', false, 9);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_DESIGN','/[0-9]*','PUT', false, 9);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('GET_DESIGN','/[0-9]*','GET', false, 9);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('GET_DESIGN_BY_STRUCTURE_ID','/structure/[0-9]*','GET', false, 9);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_DESIGN','/[0-9]*','DELETE', false, 9);
-- Operaciones de Materiales
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_MATERIAL','','POST', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_MATERIAL','/[0-9]*','PUT', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_MATERIALS','','GET', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_MATERIAL_BY_CODE','/[0-9]*','GET', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_MATERIAL_BY_ID','/[0-9]*','GET', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_MATERIAL_MOVEMENTS','/movements','GET', false, 10);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_MOVEMENTS_FROM_ONE_MATERIAL','/movements/[a-zA-Z0-9]*','GET', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('REGISTER_MOVEMENTS','/movements/[a-zA-Z0-9]*','POST', false, 10);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_INVENTORY_BY_MATERIAL_CODE','/inventory/[a-zA-Z0-9]*','GET', false, 10);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_CATEGORIES','/category','GET', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_CATEGORY','/category','POST', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_CATEGORY','/category/[0-9]*','PUT', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_MATERIAL_CATEGORY','/category/[0-9]*','GET', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_CATEGORY','/category/[0-9]*/disable','POST', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_ONE_CATEGORY','/category/[0-9]*','DELETE', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_ONE_MATERIAL','/[0-9]*','DELETE', false, 10);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_SUPPLIER','/supplier','POST', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_SUPPLIER','/supplier/[0-9]*','PUT', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_SUPPLIERS','/supplier','GET', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_SUPPLIER','/supplier/[0-9]*','GET', false, 10);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_SUPPLIER','/supplier/[0-9]*/disable','POST', false, 10);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CHANGE_OWN_PASSWORD', '/change-password', 'PUT', false, 4);

-- Operaciones de Calificaciones
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_RATING','','POST', false, 11);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_RATINGS','','GET', false, 11);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_RATING','/[0-9]*','GET', false, 11);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_RATINGS_BY_ORDER','/order/[0-9]*','GET', false, 11);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ORDERS_BY_CLIENT','/orders','GET', false, 12);

-- OPERACIONES DE LÍNEAS DE TIEMPO
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_MILESTONE','', 'POST', false, 13);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_MILESTONE','/[0-9]*', 'PUT', false, 13);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_MILESTONES','/structure/[0-9]*', 'GET', true, 13);

-- ORDEN POR NUMERO DE ORDEN
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ORDEN_BY_ORDERNUMBER','/by-ordernumber/{ordernumber}', 'GET', true, 6);

-- CREACIÓN DE ROLES
INSERT INTO role (name) VALUES ('Administrador');
INSERT INTO role (name) VALUES ('Recepcionista');
INSERT INTO role (name) VALUES ('Operador');
INSERT INTO role (name) VALUES ('Cliente');

-- CREACIÓN DE PERMISOS
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 5);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 6);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 7);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 8);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 9);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 10);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 11);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 12);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 13);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 14);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 15);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 16);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 17);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 18);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 19);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 20);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 21);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 22);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 23);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 24);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 25);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 26);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 27);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 28);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 29);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 30);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 31);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 32);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 33);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 34);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 35);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 36);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 37);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 38);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 39);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 40);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 41);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 42);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 43);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 44);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 45);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 46);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 47);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 48);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 49);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 50);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 51);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 52);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 53);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 54);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 55);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 56);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 57);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 58);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 59);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 60);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 61);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 62);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 63);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 65);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 66);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 67);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 69);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 70);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 71);


INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 5);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 19);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 20);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 21);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 22);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 23);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 24);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 25);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 63);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 69);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 70);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 71);

INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 5);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 63);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 69);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 70);
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 71);

-- PERMISOS PARA CLIENTES
INSERT INTO granted_permission (role_id, operation_id) VALUES (4, 64);
INSERT INTO granted_permission (role_id, operation_id) VALUES (4, 68);

-- CREACIÓN DE USUARIOS
--INSERT INTO users (username, name, password, role_id) VALUES ('elviscocho', 'Edson Ugaz', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 1);

-- Usuario: elviscocho
INSERT INTO users (name, lastname, email, contacto, role_id, username, password, status)
SELECT * FROM (SELECT 'Edson', 'Ugaz', 'edsonuj40@gmail.com', '123456789', 1, 'elviscocho', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 'ENABLED') AS tmp
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE username = 'elviscocho'
);

-- Usuario: savilar
INSERT INTO users (name, lastname, email, contacto, role_id, username, password, status)
SELECT * FROM (SELECT 'Sergio', 'Avila', 'avilita@gmail.com', '976548376', 2, 'savilar', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 'ENABLED') AS tmp
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE username = 'savilar'
);

-- Usuario: programador16
INSERT INTO users (name, lastname, email, contacto, role_id, username, password, status)
SELECT * FROM (SELECT 'Frank', 'Gutierrez', 'gutierrez@gmail.com', '987654321', 3, 'programador16', '$2a$10$AoaNRa/7G8HQmoYT2HyZCeRhjvVDjWH6.xF.vK4xxHA2WWQYpIkLK', 'ENABLED') AS tmp
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE username = 'programador16'
);

-- Usuario: admin123
INSERT INTO users (name, lastname, email, contacto, role_id, username, password, status)
SELECT * FROM (SELECT 'Administrador', 'Principal', 'admin@gmail.com', '123456789', 1, 'admin123', '$2a$10$3TvD8JgJmDyoLCpROkHGru80c1rGrA4LvDHgoAE.vd2iRbyNZQtFu', 'ENABLED') AS tmp
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE username = 'admin123'
);
