
-- Table Adherant
INSERT INTO Adherant (nom, prenom, date_naissance, email, mot_de_passe, id_type_adherant, quota_restant)
VALUES 
    ('Dupont', 'Jean', '2000-05-15', 'jean.dupont@example.com', 'pass123', 1, 3),
    ('Martin', 'Sophie', '1985-03-22', 'sophie.martin@example.com', 'pass456', 2, 5),
    ('Lefevre', 'Paul', '2010-07-10', 'paul.lefevre@example.com', 'pass789', 1, 3),
    ('Durand', 'Marie', '1990-11-05', 'marie.durand@example.com', 'pass000', 3, 5);

-- Table Abonnement
INSERT INTO Abonnement (id_adherant, date_debut, date_fin)g
VALUES 
    (1, '2025-01-01', '2026-01-01'),
    (2, '2025-06-01', '2026-06-01'),
    (3, '2024-01-01', '2025-01-01'),
    (4, '2025-06-01', '2026-06-01');

-- Table Reservation
INSERT INTO Reservation (id_exemplaire, id_adherant, date_reservation, statut)
VALUES 
    (1, 2, '2025-07-03', 'EN_ATTENTE'),
    (4, 1, '2025-07-03', 'EN_ATTENTE');

-- Table JourFerier
INSERT INTO JourFerier (date_ferier, description)
VALUES 
    ('2025-07-04', 'Fête nationale'),
    ('2025-07-14', 'Fête de la Bastille');

