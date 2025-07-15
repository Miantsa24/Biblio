-- Création de la base de données
CREATE DATABASE biblio;
USE biblio;

-- Table des livres
CREATE TABLE Livre (
    id_livre INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255),
    age_minimum INT NOT NULL DEFAULT 0, -- Âge minimum requis pour emprunter (ex. 18 pour certains livres)
    isbn VARCHAR(13) UNIQUE
);

-- Table des exemplaires
CREATE TABLE Exemplaire (
    id_exemplaire INT AUTO_INCREMENT PRIMARY KEY,
    id_livre INT NOT NULL,
    statut ENUM('DISPONIBLE', 'EMPRUNTE', 'RESERVE') NOT NULL DEFAULT 'DISPONIBLE',
    FOREIGN KEY (id_livre) REFERENCES Livre(id_livre) ON DELETE CASCADE
);

-- Table des types d'adhérants
CREATE TABLE TypeAdherant (
    id_type_adherant INT AUTO_INCREMENT PRIMARY KEY,
    nom_type VARCHAR(255) UNIQUE NOT NULL,
    quota_emprunts INT NOT NULL,
    quota_reservations INT NOT NULL,
    quota_prolongements INT NOT NULL
);

-- Table des adhérants
CREATE TABLE Adherant (
    id_adherant INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    date_naissance DATE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    id_type_adherant INT NOT NULL,
    quota_restant INT NOT NULL DEFAULT 0,
    quota_restant_reservation INT NOT NULL DEFAULT 0,
    quota_restant_prolongement INT NOT NULL DEFAULT 0, -- Nouvelle colonne
    FOREIGN KEY (id_type_adherant) REFERENCES TypeAdherant(id_type_adherant) ON DELETE RESTRICT
);

-- Table des abonnements
CREATE TABLE Abonnement (
    id_abonnement INT AUTO_INCREMENT PRIMARY KEY,
    id_adherant INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    CHECK (date_fin > date_debut),
    FOREIGN KEY (id_adherant) REFERENCES Adherant(id_adherant) ON DELETE CASCADE
);

-- Table des prêts
CREATE TABLE Pret (
    id_pret INT AUTO_INCREMENT PRIMARY KEY,
    id_exemplaire INT NOT NULL,
    id_adherant INT NOT NULL,
    date_pret DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_reelle DATE,
    type_pret ENUM('LECTURE_SUR_PLACE', 'A_EMPORTER') NOT NULL,
    nombre_prolongements INT DEFAULT 0,
    CHECK (date_retour_prevue >= date_pret),
    FOREIGN KEY (id_exemplaire) REFERENCES Exemplaire(id_exemplaire) ON DELETE CASCADE,
    FOREIGN KEY (id_adherant) REFERENCES Adherant(id_adherant) ON DELETE CASCADE
);

-- Table des réservations
CREATE TABLE Reservation (
    id_reservation INT AUTO_INCREMENT PRIMARY KEY,
    id_exemplaire INT NOT NULL,
    id_adherant INT NOT NULL,
    date_reservation DATE NOT NULL,
    date_demande DATETIME NOT NULL,
    statut ENUM('EN_ATTENTE', 'HONOREE', 'ANNULEE', 'TERMINEE') NOT NULL DEFAULT 'EN_ATTENTE',
    FOREIGN KEY (id_exemplaire) REFERENCES Exemplaire(id_exemplaire) ON DELETE CASCADE,
    FOREIGN KEY (id_adherant) REFERENCES Adherant(id_adherant) ON DELETE CASCADE
);

-- Ajouter un index pour optimiser les requêtes de tri sur date_demande
CREATE INDEX idx_reservation_date_demande ON Reservation (date_demande);

-- Table des pénalités
CREATE TABLE Penalite (
    id_penalite INT AUTO_INCREMENT PRIMARY KEY,
    id_adherant INT NOT NULL,
    id_pret INT, -- Lien vers le prêt spécifique (facultatif)
    type_penalite ENUM('RETARD') NOT NULL,
    date_debut_penalite DATE NOT NULL,
    nombre_jours INT NOT NULL,
    date_fin_penalite DATE NOT NULL,
    CHECK (date_fin_penalite >= date_debut_penalite),
    FOREIGN KEY (id_adherant) REFERENCES Adherant(id_adherant) ON DELETE CASCADE,
    FOREIGN KEY (id_pret) REFERENCES Pret(id_pret) ON DELETE SET NULL
);

-- Table des jours fériés
CREATE TABLE JourFerier (
    id_jour_ferier INT AUTO_INCREMENT PRIMARY KEY,
    date_ferier DATE NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Ajout de la table Admin
CREATE TABLE Admin (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL
);

-- Table des prolongements
CREATE TABLE Prolongement (
    id_prolongement INT AUTO_INCREMENT PRIMARY KEY,
    id_pret INT NOT NULL,
    id_adherant INT NOT NULL,
    id_exemplaire INT NOT NULL,
    date_demande DATETIME NOT NULL,
    nouvelle_date_retour_prevue DATE NOT NULL,
    statut ENUM('EN_ATTENTE', 'APPROUVE', 'REFUSE') NOT NULL DEFAULT 'EN_ATTENTE',
    FOREIGN KEY (id_pret) REFERENCES Pret(id_pret) ON DELETE CASCADE,
    FOREIGN KEY (id_adherant) REFERENCES Adherant(id_adherant) ON DELETE CASCADE,
    FOREIGN KEY (id_exemplaire) REFERENCES Exemplaire(id_exemplaire) ON DELETE CASCADE
);

-- -- Insertion d'un admin par défaut (le mot de passe sera haché dans le code)
-- INSERT INTO Admin (nom, email, mot_de_passe)
-- VALUES ('Admin Bibliothèque', 'admin@biblio.com', 'admin123'); -- Placeholder, sera remplacé par un mot de passe haché

-- Données pour TypeAdherant
INSERT INTO TypeAdherant (nom_type, quota_emprunts, quota_reservations, quota_prolongements)
VALUES ('étudiant', 3, 2, 2),
       ('professionnel', 5, 3, 2),
       ('professeur', 5, 5, 3);

INSERT INTO Livre (titre, auteur, age_minimum, isbn)
VALUES 
    ('Le Petit Prince', 'Antoine de Saint-Exupéry', 0, '9781234567890'),
    ('1984', 'George Orwell', 16, '9780987654321'),
    ('Harry Potter', 'J.K. Rowling', 10, '9781122334455');

    INSERT INTO Exemplaire (id_exemplaire, id_livre, statut)
VALUES
    (1, 1, 'EMPRUNTE'),
    (2, 1, 'DISPONIBLE'),
    (3, 2, 'EMPRUNTE'),
    (4, 2, 'DISPONIBLE');
