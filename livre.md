Fonctionnalités:

- Livre

- Exemplaire

- Pret
	lecture sur place
	emporter
	reserver

- Adherant (quota différent pour chaque adhérant)
	etudiant
	Professionnel
	professeur

- Penalite (tsy afaka maka boky, ho any rehetra fa samy manana nombre de jours par profil, à partir date retour réel no + nbr de jour de pénalité fa tsy le date prévu)

- cotisation

- inscription

- Reservation 
	apres reservation , le livre n est plus disponible
	
- prolongement
	combien de fois/date
	
- gestion de jour ferrier
	pas de penalite si c est un jour ferrier le depot 
	avant ou apres 

le prolongement ou le reservation qui arrive en premier est en priorite

validation par le bibliothequaire dispo si tout est reglos
par profil
Manoratra regles de gestion 

fonctionnalites (cas d utilisation)
- Preter un livre (exemplaire)
	Objectifs:.... (textuel)
	Acteur: biliothecaire
	Entree: ref exemplaire, ref adherant
	scenario nominal: (Acteur) se connecte , click sur le menu preter un livre, remplir champ adherant; click sur le bouton preter
	Regles de gestion: (sinon page d erreur et message d erreur (raison))
	- Adherant doit exister
	- exemplaire doit etre disponible
	- quota par personne
	- pas de sanction
	- l adehrant est il abonner
	- Rendre livre
	- ajouter adherant
	- est ce adapter a la personne
	Resultat:
	adherant +pret, quota-, exemplaire indispo

Cahier de charge : ensemble des fonctionnalites
UML: methotologie pour un systeme de conception ou textuel
NPD: Tables
Mettre dans une couche service ou OO
avoir une class de test (pre-service,pre-test,...) pour verification
assertion (JInit - Java): class de test

conception 
- donnees
- Architecture


## Cahier de charge

- **Nom**:
	- #Preter_livre
- **Objectifs**: 
	- Prete un livre a un adherant 
- **Acteur**: 
	- Bibliothecaire
- **Entree**: 
	- ref exemplaire
	- ref adherant
	- date actuelle
- **Scenario nominal**: 
	- L adherant veux preter un livre 
	- on entre ses infos 
	- on entre les infos du livre
	- on clique sur 'preter'
- **Regles de gestion**:
	- L adherant doit exister
	- l exemplaire doit etre disponible
	- le pret ne doit pas depasser le quota
	- l adherant n est pas sanctionner
	- l adherant doit etre abonner
	- le livre est t il adapter a la personne
- **Scenario alternatif**:
	- message d erreur et alerte avec les raisons
- **Resultat**:
	- le livre devient indisponible
	- quota de l adherant -1
	- pret +1

---

- **Nom**:
	 
- **Objectifs**: 
	- Prete un livre a un adherant 
- **Acteur**: 
	- Bibliothecaire
- **Entree**: 
	- ref exemplaire
	- ref adherant
	- date actuelle
- **Scenario nominal**: 
	- L adherant veux preter un livre 
	- on entre ses infos 
	- on entre les infos du livre
	- on clique sur 'preter'
- **Regles de gestion**:
	- L adherant doit exister
	- l exemplaire doit etre disponible
	- le pret ne doit pas depasser le quota
	- l adherant n est pas sanctionner
	- l adherant doit etre abonner
	- le livre est t il adapter a la personne
- **Scenario alternatif**:
	- message d erreur et alerte avec les raisons
- **Resultat**:
	- le livre devient indisponible
	- quota de l adherant -1
	- pret +1

---
