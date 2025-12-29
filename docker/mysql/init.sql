-- DBs (une DB par microservice)
CREATE DATABASE IF NOT EXISTS userdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS eventdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS reservationdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS paymentdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- User partagé (simple pour un projet). Tu peux changer le mot de passe si tu veux.
CREATE USER IF NOT EXISTS 'eventapp'@'localhost' IDENTIFIED BY 'AEcOED3y5O7mcyO1eDiW';
GRANT ALL PRIVILEGES ON userdb.*        TO 'eventapp'@'localhost';
GRANT ALL PRIVILEGES ON eventdb.*       TO 'eventapp'@'localhost';
GRANT ALL PRIVILEGES ON reservationdb.* TO 'eventapp'@'localhost';
GRANT ALL PRIVILEGES ON paymentdb.*     TO 'eventapp'@'localhost';
FLUSH PRIVILEGES;
