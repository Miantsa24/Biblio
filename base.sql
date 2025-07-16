-- Création de la base de données
CREATE DATABASE biblio;
USE biblio;

-- Table des livres
CREATE TABLE Livre (
    id_livre INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255),
    age_minimum INT NOT NULL DEFAULT 0, -- Âge minimum requis pour emprunter (ex. 18 pour certains livres)
    isbn VARCHAR(13) UNIQUE,
    categorie VARCHAR(50),
    langue VARCHAR(50)
);

-- Table des exemplaires
CREATE TABLE Exemplaire (
    id_exemplaire INT AUTO_INCREMENT PRIMARY KEY,
    id_livre INT NOT NULL,
    nom VARCHAR(50) UNIQUE,
    statut ENUM('DISPONIBLE', 'EMPRUNTE', 'RESERVE') NOT NULL DEFAULT 'DISPONIBLE',
    FOREIGN KEY (id_livre) REFERENCES Livre(id_livre) ON DELETE CASCADE
);

-- Table des types d'adhérants
CREATE TABLE TypeAdherant (
    id_type_adherant INT AUTO_INCREMENT PRIMARY KEY,
    nom_type VARCHAR(255) UNIQUE NOT NULL,
    quota_emprunts INT NOT NULL,
    quota_reservations INT NOT NULL,
    quota_prolongements INT NOT NULL,
    jours_penalites INT NOT NULL,
    jours_prets INT NOT NULL
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
    num_adherant VARCHAR(50) UNIQUE,
    quota_restant INT NOT NULL ,
    quota_restant_reservation INT NOT NULL ,
    quota_restant_prolongement INT NOT NULL , -- Nouvelle colonne
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

-- Script pour corriger les quotas des adhérants existants
-- Ce script met à jour les quotas restants en fonction du type d'adhérant

-- Mettre à jour les quotas restants pour tous les adhérants
UPDATE Adherant a
JOIN TypeAdherant ta ON a.id_type_adherant = ta.id_type_adherant
SET 
    a.quota_restant = ta.quota_emprunts - (
        SELECT COUNT(*) 
        FROM Pret p 
        WHERE p.id_adherant = a.id_adherant 
        AND p.date_retour_reelle IS NULL
    ),
    a.quota_restant_reservation = ta.quota_reservations - (
        SELECT COUNT(*) 
        FROM Reservation r 
        WHERE r.id_adherant = a.id_adherant 
        AND r.statut IN ('EN_ATTENTE', 'HONOREE')
    ),
    a.quota_restant_prolongement = ta.quota_prolongements - (
        SELECT COUNT(*) 
        FROM Prolongement pr 
        WHERE pr.id_adherant = a.id_adherant 
        AND pr.statut = 'APPROUVE'
    );

-- Vérifier les résultats
SELECT 
    a.id_adherant,
    CONCAT(a.nom, ' ', a.prenom) as nom_complet,
    ta.nom_type,
    ta.quota_emprunts,
    a.quota_restant,
    ta.quota_reservations,
    a.quota_restant_reservation,
    ta.quota_prolongements,
    a.quota_restant_prolongement
FROM Adherant a
JOIN TypeAdherant ta ON a.id_type_adherant = ta.id_type_adherant
ORDER BY a.id_adherant;

-- Optionnel : Modifier les valeurs par défaut pour éviter les problèmes futurs
-- (Bien que votre service gère déjà l'initialisation)

ALTER TABLE Adherant 
MODIFY COLUMN quota_restant INT NOT NULL DEFAULT 0,
MODIFY COLUMN quota_restant_reservation INT NOT NULL DEFAULT 0,
MODIFY COLUMN quota_restant_prolongement INT NOT NULL DEFAULT 0;