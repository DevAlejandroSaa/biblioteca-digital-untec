-- create database

CREATE DATABASE IF NOT EXISTS untec
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE untec;

-- create tables

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    rol ENUM('ESTUDIANTE', 'PERSONAL') NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE reglas_puntos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(150) NOT NULL,
    puntos INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE puntos_estudiante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL UNIQUE,
    puntos TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_puntos_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB;

CREATE TABLE libros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    editorial VARCHAR(150),
    anio_publicacion YEAR,
    categoria VARCHAR(100),
    cantidad INT NOT NULL DEFAULT 1,
    cantidad_disponible INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    libro_id INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_limite DATE NOT NULL,
    fecha_devolucion DATE NULL,
    estado ENUM('SOLICITADO', 'PRESTADO', 'DEVUELTO') NOT NULL DEFAULT 'SOLICITADO',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_prestamo_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_prestamo_libro FOREIGN KEY (libro_id) REFERENCES libros(id)
) ENGINE=InnoDB;


-- index

CREATE INDEX idx_usuarios_rol ON usuarios (rol);

CREATE INDEX idx_puntos_valor ON puntos_estudiante (puntos);

CREATE INDEX idx_libros_titulo ON libros (titulo);
CREATE INDEX idx_libros_autor ON libros (autor);
CREATE INDEX idx_libros_categoria ON libros (categoria);

CREATE INDEX idx_prestamos_usuario ON prestamos (usuario_id);
CREATE INDEX idx_prestamos_libro ON prestamos (libro_id);
CREATE INDEX idx_prestamos_estado ON prestamos (estado);
CREATE INDEX idx_prestamos_fecha_limite ON prestamos (fecha_limite);
CREATE INDEX idx_prestamos_usuario_estado ON prestamos (usuario_id, estado);

-- insert usuarios

INSERT INTO usuarios (
    username,
    password,
    nombre,
    apellido,
    email,
    rol
) VALUES
(
    'estudiante',
    '1234',
    'Juan',
    'Pérez',
    'estudiante@untec.cl',
    'ESTUDIANTE'
),
(
    'personal',
    '1234',
    'María',
    'González',
    'personal@untec.cl',
    'PERSONAL'
);


-- insert reglas de puntos
INSERT INTO reglas_puntos (
    codigo,
    descripcion,
    puntos
) VALUES
(
    'DEVOLUCION_A_TIEMPO',
    'Devolución realizada dentro de la fecha límite',
    10
),
(
    'DEVOLUCION_ATRASADA',
    'Devolución realizada después de la fecha límite',
    -10
);

-- insert puntos estudiante

INSERT INTO puntos_estudiante (
    usuario_id,
    puntos
) VALUES
(
    1,
    80
);


-- insert libros

INSERT INTO libros (
    titulo,
    autor,
    isbn,
    editorial,
    anio_publicacion,
    categoria,
    cantidad,
    cantidad_disponible
) VALUES
('Clean Code', 'Robert C. Martin', '9780132350884', 'Prentice Hall', 2008, 'Programación', 1, 1),
('Clean Architecture', 'Robert C. Martin', '9780134494166', 'Prentice Hall', 2017, 'Arquitectura', 1, 1),
('Effective Java', 'Joshua Bloch', '9780134685991', 'Addison-Wesley', 2018, 'Java', 1, 1),
('Java: The Complete Reference', 'Herbert Schildt', '9781260440232', 'McGraw-Hill', 2018, 'Java', 1, 1),
('Head First Java', 'Kathy Sierra', '9780596009205', 'O''Reilly', 2005, 'Java', 1, 1),
('Spring in Action', 'Craig Walls', '9781617294945', 'Manning', 2018, 'Spring', 1, 1),
('Spring Boot in Action', 'Craig Walls', '9781617292545', 'Manning', 2016, 'Spring Boot', 1, 1),
('Pro Spring 5', 'Marten Deinum', '9781484228074', 'Apress', 2017, 'Spring', 1, 1),
('Java Persistence with Spring Data and Hibernate', 'Catalin Tudose', '9781484281338', 'Apress', 2023, 'Persistencia', 1, 1),
('Beginning Hibernate', 'Jeff Linwood', '9781118007484', 'Wrox', 2012, 'Hibernate', 1, 1),

('Design Patterns', 'Erich Gamma', '9780201633610', 'Addison-Wesley', 1994, 'Patrones de Diseño', 1, 1),
('Refactoring', 'Martin Fowler', '9780134757599', 'Addison-Wesley', 2018, 'Ingeniería de Software', 1, 1),
('Domain-Driven Design', 'Eric Evans', '9780321125217', 'Addison-Wesley', 2003, 'Arquitectura', 1, 1),
('Patterns of Enterprise Application Architecture', 'Martin Fowler', '9780321127426', 'Addison-Wesley', 2002, 'Arquitectura', 1, 1),
('Test-Driven Development', 'Kent Beck', '9780321146533', 'Addison-Wesley', 2002, 'Testing', 1, 1),
('Effective Unit Testing', 'Lasse Koskela', '9781935182023', 'Testing', 2013, 'Testing', 1, 1),
('The Pragmatic Programmer', 'David Thomas', '9780135957059', 'Addison-Wesley', 2019, 'Programación', 1, 1),
('Code Complete', 'Steve McConnell', '9780735619678', 'Microsoft Press', 2004, 'Ingeniería de Software', 1, 1),
('Software Engineering', 'Ian Sommerville', '9780133943030', 'Pearson', 2015, 'Ingeniería de Software', 1, 1),
('Introduction to Algorithms', 'Thomas H. Cormen', '9780262033848', 'MIT Press', 2009, 'Algoritmos', 1, 1),

('Algorithms', 'Robert Sedgewick', '9780321573513', 'Addison-Wesley', 2011, 'Algoritmos', 1, 1),
('Data Structures and Algorithms in Java', 'Robert Lafore', '9780672324536', 'Sams Publishing', 2002, 'Estructuras de Datos', 1, 1),
('Computer Networking', 'James F. Kurose', '9780133594140', 'Pearson', 2016, 'Redes', 1, 1),
('Computer Networks', 'Andrew S. Tanenbaum', '9780132126953', 'Pearson', 2010, 'Redes', 1, 1),
('Operating System Concepts', 'Abraham Silberschatz', '9781119456339', 'Wiley', 2018, 'Sistemas Operativos', 1, 1),
('Modern Operating Systems', 'Andrew S. Tanenbaum', '9780133591620', 'Pearson', 2014, 'Sistemas Operativos', 1, 1),
('Database System Concepts', 'Abraham Silberschatz', '9780078022159', 'McGraw-Hill', 2019, 'Bases de Datos', 1, 1),
('Fundamentals of Database Systems', 'Ramez Elmasri', '9780133970777', 'Pearson', 2015, 'Bases de Datos', 1, 1),
('SQL Cookbook', 'Anthony Molinaro', '9780596009762', 'O''Reilly', 2005, 'SQL', 1, 1),
('Learning SQL', 'Alan Beaulieu', '9781492057611', 'O''Reilly', 2020, 'SQL', 1, 1),

('MySQL Cookbook', 'Paul DuBois', '9781449374020', 'O''Reilly', 2014, 'MySQL', 1, 1),
('PostgreSQL: Up and Running', 'Regina Obe', '9781491963418', 'O''Reilly', 2017, 'PostgreSQL', 1, 1),
('MongoDB: The Definitive Guide', 'Shannon Bradshaw', '9781491954461', 'O''Reilly', 2019, 'MongoDB', 1, 1),
('Redis in Action', 'Josiah L. Carlson', '9781617290855', 'Manning', 2013, 'Redis', 1, 1),
('Kafka: The Definitive Guide', 'Gwen Shapira', '9781491936160', 'O''Reilly', 2017, 'Kafka', 1, 1),
('Docker Deep Dive', 'Nigel Poulton', '9781521822807', 'Leanpub', 2020, 'Docker', 1, 1),
('Docker in Action', 'Jeff Nickoloff', '9781617294761', 'Manning', 2019, 'Docker', 1, 1),
('Kubernetes in Action', 'Marko Luksa', '9781617293720', 'Manning', 2018, 'Kubernetes', 1, 1),
('Terraform: Up and Running', 'Yevgeniy Brikman', '9781098116743', 'O''Reilly', 2022, 'DevOps', 1, 1),
('Jenkins: The Definitive Guide', 'John Ferguson Smart', '9781449305352', 'O''Reilly', 2011, 'DevOps', 1, 1),

('Git Pocket Guide', 'Richard E. Silverman', '9781449325862', 'O''Reilly', 2013, 'Git', 1, 1),
('Pro Git', 'Scott Chacon', '9781484200773', 'Apress', 2014, 'Git', 1, 1),
('Continuous Delivery', 'Jez Humble', '9780321601919', 'Addison-Wesley', 2010, 'DevOps', 1, 1),
('Site Reliability Engineering', 'Betsy Beyer', '9781491929124', 'O''Reilly', 2016, 'DevOps', 1, 1),
('Web Application Security', 'Andrew Hoffman', '9781492053118', 'O''Reilly', 2020, 'Seguridad', 1, 1),
('OAuth 2 in Action', 'Justin Richer', '9781617293276', 'Manning', 2017, 'Seguridad', 1, 1),
('API Design Patterns', 'JJ Geewax', '9781617295850', 'Manning', 2021, 'APIs', 1, 1),
('RESTful Web APIs', 'Leonard Richardson', '9781449358068', 'O''Reilly', 2013, 'APIs', 1, 1),
('Building Microservices', 'Sam Newman', '9781491950357', 'O''Reilly', 2021, 'Microservicios', 1, 1),
('Microservices Patterns', 'Chris Richardson', '9781617294549', 'Manning', 2018, 'Microservicios', 1, 1);


-- insert prestamos

-- libro actualmente prestado
INSERT INTO prestamos (
    usuario_id,
    libro_id,
    fecha_prestamo,
    fecha_limite,
    fecha_devolucion,
    estado
) VALUES (
    1,
    1,
    '2026-09-05',
    '2026-09-15',
    NULL,
    'PRESTADO'
);


-- libro actualmente prestado
INSERT INTO prestamos (
    usuario_id,
    libro_id,
    fecha_prestamo,
    fecha_limite,
    fecha_devolucion,
    estado
) VALUES (
    1,
    2,
    '2026-09-06',
    '2026-09-16',
    NULL,
    'PRESTADO'
);


-- libro devuelto dentro del plazo
INSERT INTO prestamos (
    usuario_id,
    libro_id,
    fecha_prestamo,
    fecha_limite,
    fecha_devolucion,
    estado
) VALUES (
    1,
    3,
    '2026-08-01',
    '2026-08-11',
    '2026-08-08',
    'DEVUELTO'
);


-- libro devuelto con atraso
INSERT INTO prestamos (
    usuario_id,
    libro_id,
    fecha_prestamo,
    fecha_limite,
    fecha_devolucion,
    estado
) VALUES (
    1,
    4,
    '2026-07-01',
    '2026-07-11',
    '2026-07-16',
    'DEVUELTO'
);


-- libro solicitado
INSERT INTO prestamos (
    usuario_id,
    libro_id,
    fecha_prestamo,
    fecha_limite,
    fecha_devolucion,
    estado
) VALUES (
    1,
    5,
    '2026-09-09',
    '2026-09-19',
    NULL,
    'SOLICITADO'
);


-- actualizar disponibilidad de libros actualmente prestados

UPDATE libros
SET cantidad_disponible = 0
WHERE id IN (1, 2);