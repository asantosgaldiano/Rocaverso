
-- ======================================================
-- SCRIPT COMPLETO BBDD Rocódromo ROCAVERSO - web_BBDD_TFC
-- Autor: Alberto Santos
-- Creación de la BBDD
-- ======================================================

-- DROP DATABASE IF EXISTS web_BBDD_TFC;
create database web_BBDD_TFC;
use web_BBDD_TFC;

-- ======================================================
-- Eliminación de tablas en orden inverso (por dependencias)
-- ======================================================
-- DROP TABLE IF EXISTS accesos;
-- DROP TABLE IF EXISTS vias_realizadas;
-- DROP TABLE IF EXISTS vias;
-- DROP TABLE IF EXISTS inscripciones;
-- DROP TABLE IF EXISTS planificacion_curso;
-- DROP TABLE IF EXISTS cursos;
-- DROP TABLE IF EXISTS reservas;
-- DROP TABLE IF EXISTS eventos;
-- DROP TABLE IF EXISTS tipos;
-- DROP TABLE IF EXISTS rocodromo;
-- DROP TABLE IF EXISTS usuarios;

-- ======================================================
-- Creación de tablas
-- ======================================================

-- Tabla de Usuarios (email como PRIMARY KEY)
-- DROP TABLE IF EXISTS `Usuarios`;
CREATE TABLE `usuarios` (
  email varchar(45) NOT NULL PRIMARY KEY,
  nombre varchar(45) NOT NULL,
  apellidos varchar(100) not null,
  password varchar(100) NOT NULL,
  enabled int NOT NULL DEFAULT 1,
  fecha_Registro date,
  rol enum('ADMON','USUARIO','MONITOR') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla rocodromo
-- DROP TABLE IF EXISTS `rocodromo`;
CREATE TABLE `rocodromo` (
    id_rocodromo INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    aforo_maximo INT NOT NULL,
    metros_cuadrados INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de tipos de Eventos
-- DROP TABLE IF EXISTS `tipos`;
CREATE TABLE `tipos` (
    id_tipo INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL,
    descripcion VARCHAR(200)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de Eventos
-- DROP TABLE IF EXISTS `eventos`;
CREATE TABLE `eventos` (
    id_evento INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200),
    fecha_inicio DATE,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    estado ENUM('ACEPTADO','TERMINADO','CANCELADO'),
    destacado ENUM('S','N'),
    aforo_maximo INT,
    minimo_asistencia INT,
    precio DECIMAL(9,2),
    imagen VARCHAR(255),
    zona ENUM('DEPORTIVA','BOULDER','ENTRENAMIENTO','INFANTIL','VESTUARIOS','AULAS','CAFETERIA','EXTERIOR'),
    id_tipo INT NOT NULL,
    id_rocodromo INT,
    FOREIGN KEY (id_tipo) REFERENCES Tipos(id_tipo),
    FOREIGN KEY (id_rocodromo) REFERENCES rocodromo(id_rocodromo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de reservas a Eventos
-- DROP TABLE IF EXISTS `reservas`;
CREATE TABLE `reservas` (
    id_reserva INT PRIMARY KEY AUTO_INCREMENT,
    email_usuario VARCHAR(45) NOT NULL,
    id_evento INT NOT NULL,
    fecha_reserva DATE,
    FOREIGN KEY (email_usuario) REFERENCES usuarios(email),
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento),
    UNIQUE (email_usuario, id_evento)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de cursos
-- DROP TABLE IF EXISTS `cursos`;
CREATE TABLE `cursos` (
    id_curso INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200),
    fecha_inicio DATE,
    fecha_fin DATE,
    estado ENUM('ACEPTADO','TERMINADO','CANCELADO'),
    aforo_maximo INT,
    minimo_asistencia INT,
    precio DECIMAL(9,2),
    zona ENUM('DEPORTIVA','BOULDER','ENTRENAMIENTO','INFANTIL','VESTUARIOS','AULAS','CAFETERIA','EXTERIOR'),
    id_rocodromo INT,
    email_monitor VARCHAR(45) NOT NULL,
    FOREIGN KEY (id_rocodromo) REFERENCES rocodromo(id_rocodromo),
    FOREIGN KEY (email_monitor) REFERENCES usuarios(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de planificacion_curso
-- DROP TABLE IF EXISTS `planificacion_curso`;
CREATE TABLE `planificacion_curso` (
    id_planificacion INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    id_curso INT NOT NULL,
    dia_semana ENUM('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    FOREIGN KEY (id_curso) REFERENCES cursos(id_curso) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de inscripciones a Cursos
-- DROP TABLE IF EXISTS `inscripciones`;
CREATE TABLE `inscripciones` (
    id_inscripcion INT PRIMARY KEY AUTO_INCREMENT,
    email_usuario VARCHAR(45) NOT NULL,
    id_curso INT NOT NULL,
    fecha_inscripcion DATE,
    FOREIGN KEY (email_usuario) REFERENCES usuarios(email),
    FOREIGN KEY (id_curso) REFERENCES cursos(id_curso),
    UNIQUE (email_usuario, id_curso)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de Vías de Escalada
-- DROP TABLE IF EXISTS `vias`;
CREATE TABLE `vias` (
    id_via INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    dificultad ENUM('INICIACION', 'FACIL', 'INTERMEDIO', 'AVANZADO', 'EXPERTO', 'PROFESIONAL') NOT NULL,
    tipo ENUM('DEPORTIVA', 'BOULDER', 'TRAVESIAS', 'MOONBOARD') NOT NULL,
    estado ENUM('ACTIVA', 'INACTIVA') NOT NULL,
    ubicacion ENUM('DEPORTIVA','BOULDER','ENTRENAMIENTO','INFANTIL','VESTUARIOS','AULAS','CAFETERIA','EXTERIOR') NOT NULL,
    id_rocodromo INT,
    FOREIGN KEY (id_rocodromo) REFERENCES rocodromo(id_rocodromo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de Vías Realizadas por Usuarios
-- DROP TABLE IF EXISTS `vias_realizadas`;
CREATE TABLE `vias_realizadas` (
    id_via_realizada INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email_usuario VARCHAR(45) NOT NULL,
    id_via INT NOT NULL,
    fecha_realizacion DATE,
    FOREIGN KEY (email_usuario) REFERENCES usuarios(email),
    FOREIGN KEY (id_via) REFERENCES vias(id_via)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de Accesos de Usuarios al Rocódromo
-- DROP TABLE IF EXISTS `accesos`;
CREATE TABLE `accesos` (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email_usuario VARCHAR(45) NOT NULL,
    fecha_hora_entrada TIMESTAMP NOT NULL,
    fecha_hora_salida TIMESTAMP DEFAULT NULL,
    FOREIGN KEY (email_usuario) REFERENCES usuarios(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================================
-- FIN DEL SCRIPT
-- ======================================================