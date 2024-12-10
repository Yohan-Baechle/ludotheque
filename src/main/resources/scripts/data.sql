-- Insertion des utilisateurs avec des mots de passe encodés
INSERT INTO utilisateurs (id, username, password, enabled) VALUES
(1, 'admin', '{bcrypt}$2a$10$.PLO3oejkvv0CIYJ41K6k.CbRgs3bYoIb18xqVNyXtvtccPhPNytm', true),  -- Mot de passe: admin123
(2, 'employee', '{bcrypt}$2a$10$P5eZqammkZNjGYGdrbWxgOO7JF.5gLQ5UUxMK6aHNSYeDxvpAo/9G', true); -- Mot de passe: employee123

-- Insertion des rôles pour chaque utilisateur
INSERT INTO roles (utilisateur_id, role) VALUES
(1, 'ROLE_ADMIN'),  -- Rôle admin pour admin
(2, 'ROLE_EMPLOYEE'); -- Rôle employee pour employee

-- Insertion de données fictives dans la table clients
INSERT INTO clients (nom, prenom, email, no_telephone, rue, code_postal, ville) VALUES
('Dupont', 'Jean', 'jean.dupont@example.com', '0612345678', '10 Rue des Lilas', '75000', 'Paris'),
('Durand', 'Marie', 'marie.durand@example.com', '0623456789', '15 Avenue des Champs', '69000', 'Lyon'),
('Martin', 'Paul', 'paul.martin@example.com', '0634567890', '20 Boulevard Haussmann', '31000', 'Toulouse'),
('Lemoine', 'Sophie', 'sophie.lemoine@example.com', '0645678901', '25 Rue de la Paix', '59000', 'Lille'),
('Bernard', 'Claire', 'claire.bernard@example.com', NULL, '30 Place Bellecour', '44000', 'Nantes'),
('Petit', 'Lucas', 'lucas.petit@example.com', '0656789012', '35 Rue de Verdun', '35000', 'Rennes'),
('Blanc', 'Emma', 'emma.blanc@example.com', '0667890123', '40 Avenue de la République', '13000', 'Marseille'),
('Morel', 'Thomas', 'thomas.morel@example.com', NULL, '45 Rue Nationale', '67000', 'Strasbourg'),
('Fournier', 'Julie', 'julie.fournier@example.com', '0678901234', '50 Rue Saint-Honoré', '80000', 'Amiens'),
('Girard', 'Hugo', 'hugo.girard@example.com', '0689012345', '55 Avenue Victor Hugo', '33000', 'Bordeaux');

-- Insertion de données fictives dans la table genres
INSERT INTO genres (libelle) VALUES
('Familiaux'),
('Cartes'),
('Stratégie'),
('Extérieur'),
('Rôle'),
('Construction'),
('Plateau'),
('Vidéo'),
('Enfants'),
('Réflexion');

-- Insertion de données fictives dans la table jeux
INSERT INTO jeux (titre, reference, description, tarif_journee, age_min, duree) VALUES
('Monopoly', 'REF001', 'Jeu de société classique où l''objectif est d''acheter et vendre des propriétés pour gagner de l''argent.', 15.00, 8, 60),
('Catan', 'REF002', 'Jeu de stratégie où les joueurs construisent des colonies, récoltent des ressources et échangent.', 20.00, 10, 90),
('Uno', 'REF003', 'Jeu de cartes rapide où les joueurs essaient de se débarrasser de toutes leurs cartes en fonction des couleurs et des chiffres.', 5.00, 7, 30),
('Risk', 'REF004', 'Jeu de stratégie où les joueurs se battent pour dominer le monde en plaçant des armées et en lançant des dés.', 25.00, 12, 120),
('Jenga', 'REF005', 'Jeu d''adresse où les joueurs retirent des blocs d''une tour sans la faire tomber.', 10.00, 6, 20),
('Cluedo', 'REF006', 'Jeu d''enquête où les joueurs doivent découvrir qui a commis le meurtre, avec quelle arme et dans quelle pièce.', 12.00, 8, 45),
('Carcassonne', 'REF007', 'Jeu de tuiles où les joueurs construisent des villes, des routes et des champs pour marquer des points.', 18.00, 10, 45),
('Twister', 'REF008', 'Jeu physique où les joueurs doivent placer leurs mains et pieds sur des cercles colorés sans tomber.', 8.00, 6, 30),
('Dixit', 'REF009', 'Jeu d''imagination où les joueurs utilisent des cartes illustrées pour raconter des histoires et deviner les cartes des autres.', 20.00, 8, 40),
('Pandemic', 'REF010', 'Jeu coopératif où les joueurs doivent travailler ensemble pour arrêter la propagation de maladies et sauver le monde.', 22.00, 12, 45);

-- Insertion de données fictives dans la table jeux_genres
INSERT INTO jeux_genres (jeu_id, genre_id) VALUES
(1, 1), (1, 2), -- Monopoly avec les genres "Familiaux" et "Cartes"
(2, 3), -- Catan avec le genre "Stratégie"
(3, 4), -- Uno avec le genre "Extérieur"
(4, 5), -- Risk avec le genre "Rôle"
(5, 6), -- Jenga avec le genre "Construction"
(6, 7), -- Cluedo avec le genre "Plateau"
(7, 8), -- Carcassonne avec le genre "Vidéo"
(8, 9), -- Twister avec le genre "Enfants"
(9, 10); -- Dixit avec le genre "Réflexion"

-- Insertion de données fictives dans la table exemplaires_jeux
INSERT INTO exemplaires_jeux (code_barre, louable, jeu_id) VALUES
('EX001', TRUE, 1), ('EX002', TRUE, 1), -- Exemplaires pour Monopoly
('EX003', TRUE, 2), ('EX004', TRUE, 2), -- Exemplaires pour Catan
('EX005', TRUE, 3), -- Exemplaire pour Uno
('EX006', TRUE, 4), ('EX007', FALSE, 4), -- Exemplaires pour Risk
('EX008', TRUE, 5), -- Exemplaire pour Jenga
('EX009', TRUE, 6), ('EX010', TRUE, 6), -- Exemplaires pour Cluedo
('EX011', TRUE, 7), -- Exemplaire pour Carcassonne
('EX012', TRUE, 8), -- Exemplaire pour Twister
('EX013', TRUE, 9), -- Exemplaire pour Dixit
('EX014', TRUE, 10), ('EX015', FALSE, 10); -- Exemplaires pour Pandemic

-- Insertion de données fictives dans la table locations
INSERT INTO locations (exemplaire_id, date_debut, date_fin, retour) VALUES
(1, '2024-12-01', '2024-12-05', TRUE),  -- Location pour un exemplaire de Monopoly
(2, '2024-12-02', '2024-12-06', TRUE),  -- Location pour un autre exemplaire de Monopoly
(3, '2024-12-03', '2024-12-07', FALSE), -- Location en cours pour un exemplaire de Catan
(5, '2024-12-04', NULL, FALSE),         -- Location ouverte pour un exemplaire de Uno
(6, '2024-11-28', '2024-12-02', TRUE),  -- Location terminée pour un exemplaire de Risk
(9, '2024-12-03', '2024-12-10', FALSE), -- Location pour un exemplaire de Cluedo
(11, '2024-12-04', NULL, FALSE),        -- Location ouverte pour Carcassonne
(13, '2024-12-05', NULL, FALSE),        -- Location ouverte pour Dixit
(15, '2024-11-30', '2024-12-04', TRUE); -- Location terminée pour Pandemic
