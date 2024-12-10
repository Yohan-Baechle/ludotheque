-- Suppression des tables de liaison et des genres, avec cascade
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS utilisateurs CASCADE;
DROP TABLE IF EXISTS exemplaires_jeux CASCADE;
DROP TABLE IF EXISTS jeux_genres CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS jeux CASCADE;
DROP TABLE IF EXISTS clients CASCADE;
DROP TABLE IF EXISTS locations CASCADE;

-- Création de la table utilisateurs
CREATE TABLE utilisateurs (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Création de la table roles
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    utilisateur_id INT NOT NULL,
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

-- Création de la table clients
CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    no_telephone VARCHAR(20),
    rue VARCHAR(100) NOT NULL,
    code_postal VARCHAR(5) NOT NULL,
    ville VARCHAR(50) NOT NULL,
    date_naissance DATE
);

-- Création de la table genres
CREATE TABLE genres (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

-- Création de la table jeux
CREATE TABLE jeux (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(100) NOT NULL,
    reference VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    tarif_journee DECIMAL(10, 2) NOT NULL,
    age_min INT NOT NULL,
    duree INT NOT NULL,
    CONSTRAINT check_tarif_journee CHECK (tarif_journee >= 0),
    CONSTRAINT check_age_min CHECK (age_min >= 0),
    CONSTRAINT check_duree CHECK (duree >= 0)
);

-- Création de la table jeux_genres pour la liaison entre jeux et genres
CREATE TABLE jeux_genres (
    jeu_id INT NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (jeu_id, genre_id),
    FOREIGN KEY (jeu_id) REFERENCES jeux(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

-- Création de la table exemplaires_jeux
CREATE TABLE exemplaires_jeux (
    id SERIAL PRIMARY KEY,
    code_barre VARCHAR(50) NOT NULL UNIQUE,
    louable BOOLEAN NOT NULL DEFAULT TRUE,
    jeu_id INT NOT NULL,
    FOREIGN KEY (jeu_id) REFERENCES jeux(id) ON DELETE CASCADE
);

-- Création de la table locations
CREATE TABLE locations (
    id SERIAL PRIMARY KEY,
    exemplaire_id INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE,
    retour BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (exemplaire_id) REFERENCES exemplaires_jeux(id) ON DELETE CASCADE
);

