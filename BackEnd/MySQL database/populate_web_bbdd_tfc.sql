
-- ======================================================
-- SCRIPT POPULATE BBDD Rocódromo ROCAVERSO - web_BBDD_TFC
-- Autor: Alberto Santos
-- Carga inicial de datos de prueba
-- ======================================================

use web_BBDD_TFC;

-- =======================================================
-- LIMPIEZA Y REINICIO DE TABLAS
-- =======================================================
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE vias_realizadas;
-- TRUNCATE TABLE vias;
-- TRUNCATE TABLE inscripciones;
-- TRUNCATE TABLE planificacion_curso;
-- TRUNCATE TABLE cursos;
-- TRUNCATE TABLE reservas;
-- TRUNCATE TABLE eventos;
-- TRUNCATE TABLE tipos;
-- TRUNCATE TABLE accesos;
-- TRUNCATE TABLE usuarios;
-- TRUNCATE TABLE rocodromo;
-- SET FOREIGN_KEY_CHECKS = 1;


-- =======================================================
-- INSERTS 
-- =======================================================

-- Insert rocodromo
INSERT INTO rocodromo (id_rocodromo, nombre, aforo_maximo, metros_cuadrados) VALUES 
(1, 'Rocaverso Logroño', 150, 2300);

-- Inserción de usuarios (admon y usuario y monitor)
-- =======================================================
-- ADMON
--   "email": "admin@correo.com",
--   "password": "admin123"
-- USUARIO
--   "email": "usuario@correo.com",
--   "password": "usuario123"
-- MONITOR
--    "email": "monitor@correo.com",
--    "password": "monitor123"
-- =======================================================
INSERT INTO `Usuarios` (email, nombre, apellidos, password, enabled, fecha_Registro, rol) VALUES
('usuario@correo.com', 'Alberto', 'Ginés', '$2b$12$64uoAx6WpAQcMfJDI1BC1eT4FEaem3IlK295jrdbBxHQ9CrFQlF9a', 1, '2025-10-18', 'USUARIO'),
('admin@correo.com', 'Pedro', 'Pérez', '$2b$12$ERYtMRsaUDSy1gRolXKrNeFoqGT0WlvljYkuPmqcQ/uPax.5saSfi', 1, '2025-10-05', 'ADMON'),
('monitor@correo.com', 'Maria', 'Parra', '$2b$12$0ZKmg2SeWCEu1B/agn2HkulkfLgGd/pmA9h9uEyEgILnBfOjjmZY2', 1, '2025-11-02', 'MONITOR');

-- Usuarios para pruebas
INSERT INTO `Usuarios` (email, nombre, apellidos, password, enabled, fecha_Registro, rol) VALUES
('usuario1@example.com', 'Carlos', 'Pérez López', '$2a$12$BdMxbHFjqn5aOwxaIfZK0.C7qxLb41.K3bY8M7Yi8J/rsT9xDJHU6', 1, '2025-10-07', 'USUARIO'),
('usuario2@example.com', 'Ana', 'Martínez Ruiz', '$2a$12$QFVJRLeyRcuzEjJYDrYH6.bMs8bqT34dc2PM0.5z/79iwzoEn.O7C', 1, '2025-10-21', 'USUARIO'),
('usuario3@example.com', 'Luis', 'Gómez Sánchez', '$2a$12$S0BtBlyxVPVnL6GZkmWyOuY/RpPZsCP8IXX1L7RAgE9ksMTozHPTW', 1, '2025-10-29', 'USUARIO'),
('usuario4@example.com', 'María', 'Díaz Romero', '$2a$12$EAVaZjYo9e7OpqJ/T7lfzeB45CAhA8vPQaZ7IliW9wOyHgB/2fHZC', 1, '2025-11-03', 'USUARIO'),
('usuario5@example.com', 'Jorge', 'Fernández Torres', '$2a$12$RvttxiICnm98H5lktHaDUeqlz3JKwWjfd3N6egJ57NEDc9IsdZK1S', 1, '2025-11-08', 'USUARIO'),
('usuario6@example.com', 'Elena', 'Moreno García', '$2a$12$UeI5vHTvRWf7ak/3RoE3NeN4FJQ4e.Z7ehklBcKf84u7D97HuXf2y', 1, '2025-11-15', 'USUARIO'),
('usuario7@example.com', 'Raúl', 'Navarro Martín', '$2a$12$GgEONP0S6.Z1QsLO92Qc1efH/kzV3ZDqt6EcGR3Ryk0gnkxkdtAva', 1, '2025-11-19', 'USUARIO'),
('usuario8@example.com', 'Lucía', 'Santos Ortega', '$2a$12$blRpF9rKQkNWYJZJxOjc7OpIRkVEv2MrTPAcCoMw0p/HJP3kXsU7C', 1, '2025-11-23', 'USUARIO'),
('usuario9@example.com', 'Pedro', 'Molina Ríos', '$2a$12$Qf7yEZ3UJK8mTj8Bh0dZ6uArB6kHXbnjcVQXwN57WlH6UUIEfdJJu', 1, '2025-11-28', 'USUARIO'),
('usuario10@example.com', 'Sara', 'Cano Herrera', '$2a$12$NoYw3LJtrd35gYkEjKwPCuGbQnXGNDve6txpsrkAxJMEFIP6hf1q2', 1, '2025-12-01', 'USUARIO'),
('usuario11@example.com', 'Manuel', 'Iglesias Ruiz', '$2a$12$XkC5ENBo8Oa7Vj/rn5cG6OcFrJ3mYmYP1CCh6Bt/xuN78QsQmYMAW', 1, '2025-12-03', 'USUARIO'),
('usuario12@example.com', 'Alba', 'Gil Navarro', '$2a$12$5wAOdu6D9YhZbQUnpjmItOKAO.3AVRfUqny/kGHMaNlN5DdvCPzru', 1, '2025-12-05', 'USUARIO'),
('usuario13@example.com', 'David', 'Luna Muñoz', '$2a$12$dZaKkzWrVhHIFiGiMN9gGuVhlyZ1A63N8zDskKmcnMA0pDZKJfLBW', 1, '2025-12-07', 'USUARIO'),
('usuario14@example.com', 'Isabel', 'Serrano Ramos', '$2a$12$rPzCN4FPKTXbKUdjdT0AoOVF4wQFxC2VdDWtUnCEqCgOcTfsL1Y3q', 1, '2025-12-09', 'USUARIO'),
('usuario15@example.com', 'Andrés', 'Castro Domínguez', '$2a$12$gHrX/7HT3UClH3Nccg1rpeBjo6k1hRZTk9wQs1LgS77L5U7HyZDU2', 1, '2025-12-11', 'USUARIO'),
('usuario16@example.com', 'Patricia', 'Vega Morales', '$2a$12$4HpnsN4wMZ98YFiyqGyW8OazJEBpABypDs1qfFgxx9rJc2X8Cz6JW', 1, '2025-12-13', 'USUARIO'),
('usuario17@example.com', 'Álvaro', 'Silva Pérez', '$2a$12$KddP5sC/TmVmdHWWMs/kS.rXzv3ZoR1z4pNYP/6UWBZODtFEMMQm6', 1, '2025-12-14', 'USUARIO'),
('usuario18@example.com', 'Natalia', 'Cruz López', '$2a$12$IG8RZ8rwFxZX0xuw9XceLePYo7v7yJQ.DM8Ud9sEciHxW9ZhxZUzW', 1, '2025-12-16', 'USUARIO'),
('usuario19@example.com', 'Rubén', 'Reyes Gutiérrez', '$2a$12$eUpK2l/0N7DdUNX0jvAelOB43OCfskBCmQqK6RxmVLl5/Y0U8toZK', 1, '2025-12-18', 'USUARIO'),
('usuario20@example.com', 'Beatriz', 'Ortega Álvarez', '$2a$12$VGU5OnQ4UEAaZV5Yk1YXTuM28EVFyH4qVxPTrBQulYZnR5Ig1RMZO', 1, '2025-12-20', 'USUARIO'),
('usuario21@example.com', 'Daniel', 'Santos Ruiz', '$2a$12$IFODSWXQ2', 1, '2025-10-12', 'USUARIO'),
('usuario22@example.com', 'Marta', 'Vidal Torres', '$2a$12$IFODSWXQ3', 1, '2025-10-25', 'USUARIO'),
('usuario23@example.com', 'Álex', 'Romero Peña', '$2a$12$IFODSWXQ4', 1, '2025-11-06', 'USUARIO'),
('usuario24@example.com', 'Claudia', 'Nieto Sanz', '$2a$12$IFODSWXQ5', 1, '2025-11-17', 'USUARIO'),
('usuario25@example.com', 'Víctor', 'Domínguez Vera', '$2a$12$IFODSWXQ6', 1, '2025-11-27', 'USUARIO'),
('usuario26@example.com', 'Julia', 'Alonso Rico', '$2a$12$IFODSWXQ7', 1, '2025-12-02', 'USUARIO'),
('usuario27@example.com', 'Óscar', 'Benítez Mora', '$2a$12$IFODSWXQ8', 1, '2025-12-06', 'USUARIO'),
('usuario28@example.com', 'Pablo', 'Cano Ruiz', '$2a$12$IFODSWXQ9', 1, '2025-12-08', 'USUARIO'),
('usuario29@example.com', 'Lorena', 'Rubio León', '$2a$12$IFODSWXQ10', 1, '2025-12-15', 'USUARIO'),
('usuario30@example.com', 'Sergio', 'Varela Núñez', '$2a$12$IFODSWXQ11', 1, '2025-12-22', 'USUARIO'),

('monitor1@example.com', 'Lucía', 'Navas Cano', '$2a$12$rIF.WKqF2QxU2aWaZxjwVOaPi9nQMjDqGz3A1wmp2uU1KOT86M7cW', 1, '2025-10-09', 'MONITOR'),
('monitor2@example.com', 'Pedro', 'Salas Nieto', '$2a$12$CzvOxE34mTx9x1uZY0GLh.5LBgEUnA0GM4BC7P9To2LzOTsmY3s5q', 1, '2025-11-11', 'MONITOR'),
('monitor3@example.com', 'Nuria', 'Campos Vega', '$2a$12$DqDY9YyD.PnznqdiQK/bVORUv99wDWBLs6bUiwKxy5BqzCbjZedN6', 1, '2025-12-04', 'MONITOR');


-- Tipos para pruebas
INSERT INTO tipos (nombre, descripcion) VALUES
('Taller', 'Actividad práctica para desarrollar habilidades específicas'),
('Charla', 'Presentación breve sobre un tema concreto'),
('Concierto', 'Evento musical en directo para el público'),
('Campeonato', 'Competencia organizada con participantes y premios');

-- Eventos para pruebas
INSERT INTO eventos 
(nombre, descripcion, fecha_inicio, hora_inicio, hora_fin, estado, destacado, aforo_maximo, minimo_asistencia, precio, imagen, zona, id_tipo, id_rocodromo) VALUES
('Travesía por los Alpes', 'Un alpinista narra su experiencia cruzando los Alpes durante el invierno.', '2026-01-30', '10:00:00', '12:00:00', 'ACEPTADO', 'S', 20, 5, 0.00,
 'https://www.revistaoxigeno.es/uploads/s1/23/90/76/7/article-5clasicas-alpes-5864dfc4e5cf6.jpeg', 'AULAS', 2, 1),
('Taller de Nudos para Escalada', 'Aprende los nudos esenciales para asegurar y progresar en escalada.', '2026-02-02', '15:00:00', '16:00:00', 'ACEPTADO', 'N', 25, 8, 10.00,
 'https://i.ytimg.com/vi/m3Xql_BnkbA/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLA6BegTsx2KPchltWhhFFqHs84I8g', 'ENTRENAMIENTO', 1, 1),
('Campeonato de Escalada Juvenil', 'Competencia para jóvenes escaladores con pruebas por categorías.', '2026-02-06', '09:00:00', '10:00:00', 'ACEPTADO', 'S', 50, 15, 5.00,
 'https://www.fclm.com/wp-content/uploads/2021/08/218094907_3034139750246390_2787162727714473619_n-e1628685178334.jpg', 'BOULDER', 4, 1),
('Concierto de Rock Vertical', 'Música en directo junto a la zona de escalada. Ambiente montañero y festivo.', '2026-02-10', '20:00:00', '23:00:00', 'ACEPTADO', 'S', 60, 10, 8.00,
 'https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEi5Ow8zoEh0DDuDAy35RkDF8WQCiKick3-k5-B8zCWO0W4kkVM5fLWFhKsEDsfA_Kyu9vmejF0KGdwUzyqevnnYXVLxLgpJi_E3PqX75BKx6EjdiElOlpA7HKnZYhMyPtkxa18gFUBlDwx0/s1600/C.Boza+Prensa+_004+%25281%2529.JPG', 'CAFETERIA', 3, 1),
('Cuidado y Mantenimiento del Material', 'Cómo revisar, limpiar y conservar correctamente el equipo de escalada.', '2026-02-14', '11:00:00', '12:00:00', 'ACEPTADO', 'N', 15, 5, 7.50,
 'https://cdn.barrabes.com/blogs/large/21996.jpg', 'ENTRENAMIENTO', 1, 1),
('Taller Avanzado de Técnicas de Rápel', 'Sesión intensiva y personalizada sobre técnicas avanzadas de rápel en pared, ideal para escaladores experimentados.', '2026-02-18', '14:00:00', '15:00:00', 'ACEPTADO', 'N', 6, 3, 15.00,
 'https://dreampeaks.es/wp-content/uploads/2018/11/Curso_ra%CC%81pel_4.jpg', 'DEPORTIVA', 1, 1),
('Superación en la Montaña', 'Una escaladora profesional comparte cómo enfrentó retos físicos y mentales.', '2026-02-25', '10:00:00', '12:00:00', 'ACEPTADO', 'N', 30, 8, 0.00,
 'https://www.charlasmotivacionales.lat/wp-content/uploads/2024/05/Karla-Wheelock-Celebrando-25-Anos-de-Conquistar-el-Everest-y-Transformar-Vidas.png', 'AULAS', 2, 1);

-- Reservas para pruebas
INSERT INTO reservas (email_usuario, id_evento, fecha_reserva) VALUES
('usuario@correo.com', 1, '2026-01-02'),
('usuario1@example.com', 1, '2026-01-03'),
('usuario2@example.com', 1, '2026-01-04'),
('usuario3@example.com', 1, '2026-01-05'),
('usuario4@example.com', 1, '2026-01-06'),
('usuario5@example.com', 1, '2026-01-07'),
('usuario6@example.com', 1, '2026-01-08'),
('usuario7@example.com', 1, '2026-01-09'),
('usuario8@example.com', 1, '2026-01-10'),
('usuario9@example.com', 1, '2026-01-11'),
('usuario10@example.com', 1, '2026-01-12'),
('usuario11@example.com', 1, '2026-01-13'),
('usuario12@example.com', 1, '2026-01-14'),
('usuario13@example.com', 1, '2026-01-15'),
('usuario14@example.com', 1, '2026-01-05'),
('usuario15@example.com', 1, '2026-01-06'),
('usuario16@example.com', 1, '2026-01-07'),
('usuario17@example.com', 1, '2026-01-08'),
('usuario18@example.com', 1, '2026-01-09'),
('usuario19@example.com', 1, '2026-01-10');   -- 20/20 aforo completo

INSERT INTO reservas (email_usuario, id_evento, fecha_reserva) VALUES
('usuario20@example.com', 2, '2026-01-03'),
('usuario21@example.com', 2, '2026-01-04'),
('usuario22@example.com', 2, '2026-01-05'),
('usuario23@example.com', 2, '2026-01-06'),
('usuario24@example.com', 2, '2026-01-07'),
('usuario25@example.com', 2, '2026-01-08'),
('usuario26@example.com', 2, '2026-01-09'),
('usuario27@example.com', 2, '2026-01-10'),
('usuario28@example.com', 2, '2026-01-11'),
('usuario29@example.com', 2, '2026-01-12'),
('usuario30@example.com', 2, '2026-01-13');     -- 11/25 aforo medio

INSERT INTO reservas (email_usuario, id_evento, fecha_reserva) VALUES
('usuario1@example.com', 6, '2026-01-04'),
('usuario2@example.com', 6, '2026-01-04'),
('usuario3@example.com', 6, '2026-01-05'),
('usuario4@example.com', 6, '2026-01-05'),
('usuario5@example.com', 6, '2026-01-06'),
('usuario6@example.com', 6, '2026-01-06');      -- 6/6 aforo completo

INSERT INTO reservas (email_usuario, id_evento, fecha_reserva) VALUES
('usuario20@example.com', 5, '2026-01-02'),
('usuario21@example.com', 5, '2026-01-03'),
('usuario22@example.com', 5, '2026-01-04'),
('usuario23@example.com', 5, '2026-01-05'),
('usuario24@example.com', 5, '2026-01-06'),
('usuario25@example.com', 5, '2026-01-07'),
('usuario26@example.com', 5, '2026-01-08'),
('usuario27@example.com', 5, '2026-01-09'),
('usuario28@example.com', 5, '2026-01-10'),
('usuario29@example.com', 5, '2026-01-11'),
('usuario7@example.com', 5, '2026-01-12'),
('usuario8@example.com', 5, '2026-01-13');      -- 12/15 aforo limite

-- Cursos para pruebas
INSERT INTO cursos 
(nombre, descripcion, fecha_inicio, fecha_fin, estado, aforo_maximo, minimo_asistencia, precio, zona, id_rocodromo, email_monitor) VALUES
('Curso Escalada Iniciación', 'Curso básico para aprender técnicas de escalada.', '2026-01-30', '2026-03-06', 'ACEPTADO', 18, 5, 100.00, 'DEPORTIVA', 1, 'monitor@correo.com'),
('Curso Escalada Intermedio', 'Curso para mejorar tus habilidades en escalada.', '2026-02-02', '2026-03-12', 'ACEPTADO', 20, 8, 120.00, 'BOULDER', 1, 'monitor@correo.com'),
('Curso Escalada Avanzado', 'Curso avanzado para escaladores experimentados.', '2026-02-05', '2026-03-19', 'ACEPTADO', 12, 8, 150.00, 'DEPORTIVA', 1, 'monitor@correo.com'),
('Curso Escalada en Roca', 'Aprende a escalar en roca natural con seguridad.', '2026-02-09', '2026-03-23', 'ACEPTADO', 15, 5, 130.00, 'EXTERIOR', 1, 'monitor@correo.com'),

('Curso de Yoga y Estiramientos', 'Curso para mejorar flexibilidad y bienestar con yoga y estiramientos.', '2026-02-03', '2026-03-10', 'ACEPTADO', 22, 5, 80.00, 'AULAS', 1, 'monitor1@example.com'),
('Curso Gestión del Miedo y Autoasegurar', 'Técnicas para controlar el miedo y autoasegurarse en escalada.', '2026-02-12', '2026-03-05', 'ACEPTADO', 10, 5, 110.00, 'AULAS', 1, 'monitor2@example.com'),
('Curso de Escalada para Niños', 'Curso divertido y seguro para niños que quieren iniciarse en la escalada.', '2026-02-06', '2026-03-06', 'ACEPTADO', 14, 4, 90.00, 'INFANTIL', 1, 'monitor3@example.com');

-- Curso 1: Curso Escalada Iniciación
INSERT INTO planificacion_curso (id_curso, dia_semana, hora_inicio, hora_fin) VALUES
(1, 'LUNES', '18:00:00', '20:00:00'),
(1, 'MIERCOLES', '18:00:00', '20:00:00');

-- Curso 2: Curso Escalada Intermedio
INSERT INTO planificacion_curso (id_curso, dia_semana, hora_inicio, hora_fin) VALUES
(2, 'MARTES', '19:00:00', '21:00:00'),
(2, 'JUEVES', '19:00:00', '21:00:00');

-- Curso 3: Curso Escalada Avanzado
INSERT INTO planificacion_curso (id_curso, dia_semana, hora_inicio, hora_fin) VALUES
(3, 'LUNES', '20:00:00', '22:00:00'),
(3, 'MIERCOLES', '20:00:00', '22:00:00');

-- Curso 4: Curso Escalada en Roca
INSERT INTO planificacion_curso (id_curso, dia_semana, hora_inicio, hora_fin) VALUES
(4, 'SABADO', '10:00:00', '14:00:00');

-- Curso 5: Curso de Yoga y Estiramientos
INSERT INTO planificacion_curso (id_curso, dia_semana, hora_inicio, hora_fin) VALUES
(5, 'MARTES', '09:00:00', '10:30:00'),
(5, 'JUEVES', '09:00:00', '10:30:00');

-- Curso 6: Curso Gestión del Miedo y Autoasegurar
INSERT INTO planificacion_curso (id_curso, dia_semana, hora_inicio, hora_fin) VALUES
(6, 'VIERNES', '18:00:00', '20:00:00');

-- Inscripciones para pruebas

-- Inscripciones para llenar Curso 1 (aforo 18 → rellenamos 18)
INSERT INTO inscripciones (email_usuario, id_curso, fecha_inscripcion) VALUES
('usuario@correo.com', 1, '2026-01-02'),
('usuario1@example.com', 1, '2026-01-02'),
('usuario2@example.com', 1, '2026-01-02'),
('usuario3@example.com', 1, '2026-01-02'),
('usuario4@example.com', 1, '2026-01-02'),
('usuario5@example.com', 1, '2026-01-02'),
('usuario6@example.com', 1, '2026-01-02'),
('usuario7@example.com', 1, '2026-01-02'),
('usuario8@example.com', 1, '2026-01-02'),
('usuario9@example.com', 1, '2026-01-02'),
('usuario10@example.com', 1, '2026-01-02'),
('usuario11@example.com', 1, '2026-01-02'),
('usuario12@example.com', 1, '2026-01-02'),
('usuario13@example.com', 1, '2026-01-02'),
('usuario14@example.com', 1, '2026-01-02'),
('usuario15@example.com', 1, '2026-01-02'),
('usuario16@example.com', 1, '2026-01-02'),
('usuario17@example.com', 1, '2026-01-02');


-- Curso 2 (aforo 20 → lo dejamos a medio aforo, 12 inscripciones)
INSERT INTO inscripciones (email_usuario, id_curso, fecha_inscripcion) VALUES
('usuario@correo.com', 2, '2026-01-03'),
('usuario18@example.com', 2, '2026-01-03'),
('usuario19@example.com', 2, '2026-01-03'),
('usuario20@example.com', 2, '2026-01-03'),
('usuario21@example.com', 2, '2026-01-03'),
('usuario22@example.com', 2, '2026-01-03'),
('usuario23@example.com', 2, '2026-01-03'),
('usuario24@example.com', 2, '2026-01-03'),
('usuario25@example.com', 2, '2026-01-03'),
('usuario26@example.com', 2, '2026-01-03'),
('usuario27@example.com', 2, '2026-01-03'),
('usuario28@example.com', 2, '2026-01-03');


-- Curso 3 (aforo 12 → lo llenamos)
INSERT INTO inscripciones (email_usuario, id_curso, fecha_inscripcion) VALUES
('usuario@correo.com', 3, '2026-01-04'),
('usuario1@example.com', 3, '2026-01-04'),
('usuario2@example.com', 3, '2026-01-04'),
('usuario3@example.com', 3, '2026-01-04'),
('usuario4@example.com', 3, '2026-01-04'),
('usuario5@example.com', 3, '2026-01-04'),
('usuario6@example.com', 3, '2026-01-04'),
('usuario7@example.com', 3, '2026-01-04'),
('usuario8@example.com', 3, '2026-01-04'),
('usuario9@example.com', 3, '2026-01-04'),
('usuario10@example.com', 3, '2026-01-04'),
('usuario12@example.com', 3, '2026-01-04');


-- Curso 4 (aforo 15 → 11 inscripciones)
INSERT INTO inscripciones (email_usuario, id_curso, fecha_inscripcion) VALUES
('usuario@correo.com', 4, '2026-01-05'),
('usuario13@example.com', 4, '2026-01-05'),
('usuario14@example.com', 4, '2026-01-05'),
('usuario15@example.com', 4, '2026-01-05'),
('usuario16@example.com', 4, '2026-01-05'),
('usuario17@example.com', 4, '2026-01-05'),
('usuario18@example.com', 4, '2026-01-05'),
('usuario19@example.com', 4, '2026-01-05'),
('usuario20@example.com', 4, '2026-01-05'),
('usuario21@example.com', 4, '2026-01-05'),
('usuario22@example.com', 4, '2026-01-05');


-- Curso 5 (aforo 22 → 16 inscripciones)
INSERT INTO inscripciones (email_usuario, id_curso, fecha_inscripcion) VALUES
('usuario@correo.com', 5, '2026-01-06'),
('usuario23@example.com', 5, '2026-01-06'),
('usuario24@example.com', 5, '2026-01-06'),
('usuario25@example.com', 5, '2026-01-06'),
('usuario26@example.com', 5, '2026-01-06'),
('usuario27@example.com', 5, '2026-01-06'),
('usuario28@example.com', 5, '2026-01-06'),
('usuario29@example.com', 5, '2026-01-06'),
('usuario30@example.com', 5, '2026-01-06'),
('usuario1@example.com', 5, '2026-01-06'),
('usuario2@example.com', 5, '2026-01-06'),
('usuario3@example.com', 5, '2026-01-06'),
('usuario4@example.com', 5, '2026-01-06'),
('usuario5@example.com', 5, '2026-01-06'),
('usuario6@example.com', 5, '2026-01-06'),
('usuario7@example.com', 5, '2026-01-06');


-- Curso 6 (aforo 10 → 8 inscripciones)
INSERT INTO inscripciones (email_usuario, id_curso, fecha_inscripcion) VALUES
('usuario8@example.com', 6, '2026-01-07'),
('usuario9@example.com', 6, '2026-01-07'),
('usuario10@example.com', 6, '2026-01-07'),
('usuario11@example.com', 6, '2026-01-07'),
('usuario12@example.com', 6, '2026-01-07'),
('usuario13@example.com', 6, '2026-01-07'),
('usuario14@example.com', 6, '2026-01-07'),
('usuario15@example.com', 6, '2026-01-07');


-- Curso 7 (aforo 14 → 11 inscripciones)
INSERT INTO inscripciones (email_usuario, id_curso, fecha_inscripcion) VALUES
('usuario16@example.com', 7, '2026-01-08'),
('usuario17@example.com', 7, '2026-01-08'),
('usuario18@example.com', 7, '2026-01-08'),
('usuario19@example.com', 7, '2026-01-08'),
('usuario20@example.com', 7, '2026-01-08'),
('usuario21@example.com', 7, '2026-01-08'),
('usuario22@example.com', 7, '2026-01-08'),
('usuario23@example.com', 7, '2026-01-08'),
('usuario25@example.com', 7, '2026-01-08'),
('usuario26@example.com', 7, '2026-01-08'),
('usuario27@example.com', 7, '2026-01-08');

-- Inserts Vías
-- Insert 15 vías DEPORTIVA
INSERT INTO vias (dificultad, tipo, estado, ubicacion, id_rocodromo) VALUES
('INICIACION', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('FACIL', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('INTERMEDIO', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('AVANZADO', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('EXPERTO', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),

('INICIACION', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('FACIL', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('INTERMEDIO', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('AVANZADO', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('EXPERTO', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),

('INICIACION', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('FACIL', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('INTERMEDIO', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('AVANZADO', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1),
('EXPERTO', 'DEPORTIVA', 'ACTIVA', 'DEPORTIVA', 1);

-- Insert 15 vías BOULDER
INSERT INTO vias (dificultad, tipo, estado, ubicacion, id_rocodromo) VALUES
('INICIACION', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('FACIL', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('INTERMEDIO', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('AVANZADO', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('EXPERTO', 'BOULDER', 'ACTIVA', 'BOULDER', 1),

('INICIACION', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('FACIL', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('INTERMEDIO', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('AVANZADO', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('EXPERTO', 'BOULDER', 'ACTIVA', 'BOULDER', 1),

('INICIACION', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('FACIL', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('INTERMEDIO', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('AVANZADO', 'BOULDER', 'ACTIVA', 'BOULDER', 1),
('EXPERTO', 'BOULDER', 'ACTIVA', 'BOULDER', 1);

-- Insert 15 vías TRAVESIAS
INSERT INTO vias (dificultad, tipo, estado, ubicacion, id_rocodromo) VALUES
('INICIACION', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('FACIL', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('INTERMEDIO', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('AVANZADO', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('EXPERTO', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),

('INICIACION', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('FACIL', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('INTERMEDIO', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('AVANZADO', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('EXPERTO', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),

('INICIACION', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('FACIL', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('INTERMEDIO', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('AVANZADO', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1),
('EXPERTO', 'TRAVESIAS', 'ACTIVA', 'ENTRENAMIENTO', 1);

-- Insert 15 vías MOONBOARD
INSERT INTO vias (dificultad, tipo, estado, ubicacion, id_rocodromo) VALUES
('INICIACION', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('FACIL', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('INTERMEDIO', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('AVANZADO', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('EXPERTO', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),

('INICIACION', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('FACIL', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('INTERMEDIO', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('AVANZADO', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('EXPERTO', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),

('INICIACION', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('FACIL', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('INTERMEDIO', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('AVANZADO', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1),
('EXPERTO', 'MOONBOARD', 'ACTIVA', 'ENTRENAMIENTO', 1);

-- Insert vias_realizadas
-- DELETE FROM vias_realizadas;
INSERT INTO vias_realizadas (email_usuario, id_via, fecha_realizacion) VALUES
('usuario@correo.com', 1, '2026-01-02'),
('usuario@correo.com', 2, '2026-01-02'),
('usuario@correo.com', 3, '2026-01-03'),
('usuario@correo.com', 4, '2026-01-03'),
('usuario@correo.com', 5, '2026-01-04'),
('usuario@correo.com', 6, '2026-01-04'),
('usuario@correo.com', 7, '2026-01-05'),
('usuario@correo.com', 8, '2026-01-05'),
('usuario@correo.com', 9, '2026-01-06'),
('usuario@correo.com', 10, '2026-01-06'),
('usuario@correo.com', 11, '2026-01-07'),
('usuario@correo.com', 12, '2026-01-07'),
('usuario@correo.com', 13, '2026-01-08'),
('usuario@correo.com', 14, '2026-01-08'),
('usuario@correo.com', 15, '2026-01-09'),
('usuario@correo.com', 16, '2026-01-09'),
('usuario@correo.com', 17, '2026-01-10'),
('usuario@correo.com', 18, '2026-01-10'),
('usuario@correo.com', 19, '2026-01-11'),
('usuario@correo.com', 20, '2026-01-11'),
('usuario@correo.com', 21, '2026-01-12'),
('usuario@correo.com', 22, '2026-01-12'),
('usuario@correo.com', 23, '2026-01-13'),
('usuario@correo.com', 24, '2026-01-13'),
('usuario@correo.com', 25, '2026-01-14'),
('usuario@correo.com', 26, '2026-01-14'),
('usuario@correo.com', 27, '2026-01-15'),
('usuario@correo.com', 28, '2026-01-15'),
('usuario@correo.com', 29, '2026-01-16'),
('usuario@correo.com', 30, '2026-01-16');

INSERT INTO vias_realizadas (email_usuario, id_via, fecha_realizacion) VALUES
('usuario3@example.com', 1, '2026-01-11'),
('usuario4@example.com', 1, '2026-01-12'),
('usuario5@example.com', 1, '2026-01-12'),
('usuario6@example.com', 1, '2026-01-13'),
('usuario7@example.com', 1, '2026-01-13'),
('usuario8@example.com', 1, '2026-01-14'),
('usuario3@example.com', 2, '2026-01-13'),
('usuario5@example.com', 2, '2026-01-13'),
('usuario7@example.com', 2, '2026-01-14'),
('usuario9@example.com', 2, '2026-01-14'),
('usuario10@example.com', 2, '2026-01-15'),
('usuario4@example.com', 5, '2026-01-15'),
('usuario6@example.com', 5, '2026-01-16'),
('usuario8@example.com', 5, '2026-01-16'),
('usuario10@example.com', 5, '2026-01-17'),
('usuario3@example.com', 11, '2026-01-18'),
('usuario6@example.com', 11, '2026-01-18'),
('usuario9@example.com', 11, '2026-01-19'),
('usuario12@example.com', 11, '2026-01-19'),
('usuario14@example.com', 11, '2026-01-20'),
('usuario5@example.com', 21, '2026-01-20'),
('usuario8@example.com', 21, '2026-01-20'),
('usuario10@example.com', 21, '2026-01-21'),
('usuario13@example.com', 21, '2026-01-21'),
('usuario15@example.com', 21, '2026-01-22'),
('usuario16@example.com', 7, '2026-01-22'),
('usuario17@example.com', 9, '2026-01-23'),
('usuario18@example.com', 14, '2026-01-23'),
('usuario19@example.com', 28, '2026-01-24'),
('usuario20@example.com', 35, '2026-01-24'),
('usuario3@example.com', 33, '2026-01-26'),
('usuario4@example.com', 38, '2026-01-27'),
('usuario5@example.com', 40, '2026-01-27'),

('usuario1@example.com', 1, '2026-01-03'),
('usuario1@example.com', 2, '2026-01-03'),
('usuario1@example.com', 3, '2026-01-04'),
('usuario1@example.com', 4, '2026-01-04'),
('usuario1@example.com', 5, '2026-01-05'),
('usuario1@example.com', 6, '2026-01-05'),
('usuario1@example.com', 7, '2026-01-06'),
('usuario1@example.com', 8, '2026-01-06'),
('usuario1@example.com', 9, '2026-01-07'),
('usuario1@example.com', 10, '2026-01-07'),
('usuario1@example.com', 11, '2026-01-08'),
('usuario1@example.com', 12, '2026-01-08'),
('usuario1@example.com', 13, '2026-01-09'),
('usuario1@example.com', 14, '2026-01-09'),
('usuario1@example.com', 15, '2026-01-10'),
('usuario1@example.com', 16, '2026-01-10'),
('usuario1@example.com', 17, '2026-01-11'),
('usuario1@example.com', 18, '2026-01-11'),
('usuario1@example.com', 19, '2026-01-12'),
('usuario1@example.com', 20, '2026-01-12'),
('usuario1@example.com', 21, '2026-01-13'),
('usuario1@example.com', 22, '2026-01-13'),
('usuario1@example.com', 23, '2026-01-14'),
('usuario1@example.com', 24, '2026-01-14'),
('usuario1@example.com', 25, '2026-01-15'),

('usuario2@example.com', 1, '2026-01-04'),
('usuario2@example.com', 2, '2026-01-04'),
('usuario2@example.com', 3, '2026-01-05'),
('usuario2@example.com', 4, '2026-01-05'),
('usuario2@example.com', 5, '2026-01-06'),
('usuario2@example.com', 6, '2026-01-06'),
('usuario2@example.com', 7, '2026-01-07'),
('usuario2@example.com', 8, '2026-01-07'),
('usuario2@example.com', 9, '2026-01-08'),
('usuario2@example.com', 10, '2026-01-08'),
('usuario2@example.com', 11, '2026-01-09'),
('usuario2@example.com', 12, '2026-01-09'),
('usuario2@example.com', 13, '2026-01-10'),
('usuario2@example.com', 14, '2026-01-10'),
('usuario2@example.com', 15, '2026-01-11'),
('usuario2@example.com', 16, '2026-01-11'),
('usuario2@example.com', 17, '2026-01-12'),
('usuario2@example.com', 18, '2026-01-12'),
('usuario2@example.com', 19, '2026-01-13'),
('usuario2@example.com', 20, '2026-01-13');

-- Insert accesos
INSERT INTO accesos (email_usuario, fecha_hora_entrada, fecha_hora_salida) VALUES
('usuario1@example.com', NOW(), NULL),
('usuario2@example.com', NOW(), NULL),
('usuario3@example.com', NOW(), NULL),
('usuario4@example.com', NOW(), NULL),
('usuario5@example.com', NOW(), NULL),
('usuario6@example.com', NOW(), NULL),
('usuario7@example.com', NOW(), NULL),
('usuario8@example.com', NOW(), NULL),
('usuario9@example.com', NOW(), NULL),
('usuario10@example.com', NOW(), NULL),
('usuario11@example.com', NOW(), NULL),
('usuario12@example.com', NOW(), NULL),
('usuario13@example.com', NOW(), NULL),
('usuario14@example.com', NOW(), NULL),
('usuario15@example.com', NOW(), NULL),
('usuario16@example.com', NOW(), NULL),
('usuario17@example.com', NOW(), NULL),
('usuario18@example.com', NOW(), NULL),
('usuario19@example.com', NOW(), NULL),
('usuario20@example.com', NOW(), NULL);

-- =======================================================
-- FIN DEL SCRIPT
-- =======================================================
