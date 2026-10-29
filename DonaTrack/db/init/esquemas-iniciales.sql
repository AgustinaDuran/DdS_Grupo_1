CREATE DATABASE IF NOT EXISTS donaciones;
CREATE DATABASE IF NOT EXISTS incentivos;
CREATE DATABASE IF NOT EXISTS notificaciones;

CREATE USER IF NOT EXISTS 'donaciones'@'%'     IDENTIFIED BY 'donaciones';
CREATE USER IF NOT EXISTS 'incentivos'@'%'     IDENTIFIED BY 'incentivos';
CREATE USER IF NOT EXISTS 'notificaciones'@'%' IDENTIFIED BY 'notificaciones';

-- cada usuario solo ve SU esquema
GRANT ALL PRIVILEGES ON donaciones.*     TO 'donaciones'@'%';
GRANT ALL PRIVILEGES ON incentivos.*     TO 'incentivos'@'%';
GRANT ALL PRIVILEGES ON notificaciones.* TO 'notificaciones'@'%';
FLUSH PRIVILEGES;