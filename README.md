[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/hAE1BRhd)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=19392752)

💊 Système de Gestion des Pharmacies

Ce projet est une application complète permettant aux pharmaciens de gérer les stocks de médicaments, avec des alertes automatiques en cas de :
- Quantité en stock faible
- Médicaments expirés ou proches de l'expiration

Il est structuré autour de trois composants :
- Backend : API Spring Boot
- Frontend Web : ReactJS
- Frontend Mobile : Kotlin Android
- Surveillance et Logs : Prometheus, Grafana, et ELK Stack
- CI/CD : GitHub Actions

📁 Arborescence du projet

├── BACKEND                     # API Spring Boot
├── FRONTEND                   # Interface ReactJS
├── .github/workflows          # Fichiers CI/CD GitHub Actions
├── docker-compose.prod.yml    # Déploiement multi-conteneurs
├── docker-compose.monitoring.yml # Stack monitoring (Prometheus, Grafana)
├── prometheus.yml             # Configuration Prometheus
├── README.md

⚙️ Pré-requis

- Docker & Docker Compose
- Node.js (v18+)
- Java JDK 17+
- Android Studio (pour l’application mobile)
- Git

🚀 Lancer le projet localement

1. Cloner le dépôt

git clone https://github.com/iir-24-25/projet-devops-g5-xg5_g1.git
cd projet-devops-g5-xg5_g1

2. Lancer via Docker (environnement de production)

docker-compose -f docker-compose.prod.yml up --build

Cela lancera :
- L’API backend Spring Boot
- Le frontend React
- Les bases de données (PostgreSQL/Mongo selon config)
- L’interface mobile (via une API si intégrée)
- Prometheus, Grafana, ELK (si activés)

3. Lancer le monitoring

docker-compose -f docker-compose.monitoring.yml up -d

4. Accéder aux services

| Service             | URL                         |
|---------------------|------------------------------|
| Frontend Web        | http://localhost:3000        |
| API Backend         | http://localhost:8080        |
| Prometheus          | http://localhost:9090        |
| Grafana             | http://localhost:3001        |
| Kibana              | http://localhost:5601        |

🧪 Tests Automatisés

Backend (Spring Boot)

cd BACKEND
./mvnw test

Les tests couvrent :
- Contrôleurs REST
- Services métier
- Repositories JPA

Frontend (React)

cd FRONTEND
npm install
npm test

Utilisation de Jest et React Testing Library.

🔄 Intégration et Déploiement Continus (CI/CD)

GitHub Actions

Deux pipelines sont configurés dans .github/workflows :
- CI - Frontend (React) : vérifie l’intégrité du code React (build + test)
- CI/CD - Full Pipeline : pipeline global qui effectue :
  - Compilation backend + tests
  - Build Docker
  - Déploiement avec docker-compose
  - Monitoring d'état des services

💥 Historique d'exécution

Derniers pipelines visibles dans l'onglet Actions GitHub :
- ✅ Success : pipeline complété
- ❌ Échec : erreurs de build, tests ou configuration corrigées dans les dernières versions

📊 Monitoring et Logging

Le fichier docker-compose.monitoring.yml installe les outils suivants :

📌 Auteur & Contributeurs

Projet réalisé dans le cadre du module DevOps, par le groupe G5_XG5_G1.

Contributeurs principaux :
- @unessaem (Youness AIT ELMOUDEN)
- @hamzaan5555555 (Hamza ANOUAR)
- Youssef NAOUM
- Younes GUERMAT

📄 Licence

Ce projet est à but éducatif (GitHub Classroom).
