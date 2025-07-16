INSERT INTO TypeAdherant (nom_type, quota_emprunts, quota_reservations, quota_prolongements, jours_penalites, jours_prets) 
VALUES 
    ('etudiant', 2, 1, 3, 10, 7),
    ('enseignant', 3, 2, 5, 9, 9),
    ('professeur', 4, 3, 7, 8, 12);
       

INSERT INTO Livre (titre, auteur, age_minimum, isbn, categorie, langue)
VALUES 
    ('Les Misérables', 'Victor Hugo', 18, '9782070409189','Littérature classique','Français'),
    ('L\'Étranger', 'Albert Camus', 18, '9782070360022','Philosophie','Français'),
    ('Harry Potter à l\'école des sorciers', 'J.K. Rowling', 18, '9781122334455','Jeunesse / Fantastique','Français');

    INSERT INTO Exemplaire (id_exemplaire, id_livre, nom,statut)
VALUES
    (1, 1,'MIS001', 'DISPONIBLE'),
    (2, 1,'MIS002', 'DISPONIBLE'),
    (3, 1,'MIS003', 'DISPONIBLE'),
    (4, 2,'ETR001', 'DISPONIBLE'),
    (5, 2,'ETR002', 'DISPONIBLE'),
    (6, 3,'HAR001', 'DISPONIBLE');


-- Table Adherant
INSERT INTO Adherant (nom, prenom, date_naissance, email, mot_de_passe, id_type_adherant, num_adherant)
VALUES 
    ('Amine', 'Bensaïd', '2000-05-15', 'amine.ben@example.com', 'pass123', 1, 'ETU001'),
    ('Sarah', 'El Khattabi', '1985-03-22', 'sarah.el@example.com', 'pass456', 1, 'ETU002'),
    ('Youssef', 'Moujahid', '2006-07-10', 'youssef.m@example.com', 'pass789', 1, 'ETU003'),
    ('Nadia', 'Benali', '1990-11-05', 'nadia.benali@example.com', 'pass000', 2, 'ENS001'),
    ('Karim', 'Haddadi', '2000-11-05', 'karim.had@example.com', 'pass001', 2, 'ENS002'),
    ('Salima', 'Touhami', '2000-12-05', 'salima.to@example.com', 'pass002', 2, 'ENS003'),
    ('Rachid', 'El Mansouri', '2006-10-05', 'rachid.el@example.com', 'pass002', 3, 'PROF001'),
    ('Amina', 'Zerouali', '2006-09-15', 'amina.ze@example.com', 'pass003', 3, 'PROF002');


-- Table Abonnement
INSERT INTO Abonnement (id_adherant, date_debut, date_fin)
VALUES 
    (1, '2025-02-01', '2025-07-24'),
    (2, '2025-02-01', '2025-07-01'),
    (3, '2025-04-01', '2025-12-01'),
    (4, '2025-07-01', '2026-07-01'),
    (5, '2025-08-01', '2026-05-01'),
    (6, '2025-07-01', '2026-06-01'),
    (7, '2025-06-01', '2025-12-01'),
    (8, '2024-10-01', '2025-06-01');


-- Table JourFerier
INSERT INTO JourFerier (date_ferier, description)
VALUES 
    ('2025-07-13', 'Dimanche'),
    ('2025-07-20', 'Dimanche'),
    ('2025-07-27', 'Dimanche'),

    ('2025-08-03', 'Dimanche'),

    ('2025-08-10', 'Dimanche'),

    ('2025-08-17', 'Dimanche'),
    ('2025-07-26', 'Jour ferie'),
    ('2025-07-19', 'Jour ferie');



