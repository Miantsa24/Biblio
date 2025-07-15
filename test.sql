-- Insertion d'adhérants
INSERT INTO Adherant (nom, prenom, date_naissance, email, mot_de_passe, id_type_adherant, quota_restant, quota_restant_reservation, quota_restant_prolongement)
VALUES 
    ('Dupont', 'Jean', '1995-03-15', 'jean.dupont@email.com', 'hashed_password_1', 1, 3, 2, 2), -- Étudiant
    ('Martin', 'Sophie', '1980-07-22', 'sophie.martin@email.com', 'hashed_password_2', 2, 5, 3, 2), -- Professionnel
    ('Leroy', 'Paul', '1970-11-10', 'paul.leroy@email.com', 'hashed_password_3', 3, 5, 5, 3); -- Professeur

-- Insertion d'abonnements
INSERT INTO Abonnement (id_adherant, date_debut, date_fin)
VALUES 
    (1, '2025-01-01', '2025-12-31'), -- Jean Dupont
    (2, '2025-01-01', '2025-12-31'), -- Sophie Martin
    (3, '2025-01-01', '2025-12-31'); -- Paul Leroy

-- Insertion de prêts
INSERT INTO Pret (id_pret, id_exemplaire, id_adherant, date_pret, date_retour_prevue, date_retour_reelle, type_pret, nombre_prolongements)
VALUES 
    (1, 1, 1, '2025-07-01', '2025-07-15', NULL, 'A_EMPORTER', 0), -- Jean emprunte Le Petit Prince
    (2, 3, 2, '2025-07-05', '2025-07-19', NULL, 'A_EMPORTER', 0), -- Sophie emprunte 1984
    (3, 2, 3, '2025-07-10', '2025-07-24', NULL, 'LECTURE_SUR_PLACE', 0); -- Paul emprunte Le Petit Prince

-- Insertion de prolongements
INSERT INTO Prolongement (id_pret, id_adherant, id_exemplaire, date_demande, nouvelle_date_retour_prevue, statut)
VALUES 
    (1, 1, 2, '2025-07-13', '2025-07-30', 'EN_ATTENTE'); -- Prolongement pour Jean
    (2, 2, 3, '2025-07-15', '2025-08-02', 'EN_ATTENTE'); -- Prolongement pour Sophie

-- Insertion d'une réservation
INSERT INTO Reservation (id_reservation, id_exemplaire, id_adherant, date_reservation, date_demande, statut)
VALUES 
    (3, 4, 3, '2025-07-20', '2025-07-13 12:47:00', 'EN_ATTENTE');-- Sophie réserve l'exemplaire 1 (Le Petit Prince)

-- Insertion de pénalités
INSERT INTO Penalite (id_adherant, id_pret, type_penalite, date_debut_penalite, nombre_jours, date_fin_penalite)
VALUES 
    (3, NULL, 'RETARD', '2025-07-01', 7, '2025-07-08'); -- Pénalité passée pour Paul

-- Insertion de jours fériés
INSERT INTO JourFerier (date_ferier, description)
VALUES 
    ('2025-07-29', 'Fête nationale'),
    ('2025-07-30', 'Fête nationale prolongée');