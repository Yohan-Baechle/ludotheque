# Ludothèque

[![CI](https://github.com/Yohan-Baechle/ludotheque/actions/workflows/ci.yml/badge.svg)](https://github.com/Yohan-Baechle/ludotheque/actions/workflows/ci.yml)

Application web de gestion de ludothèque développée avec **Spring Boot 3** et une
architecture en couches (BO / DAL / BLL / Controllers). Projet réalisé durant ma
formation **Concepteur Développeur d'Applications (CDA)** à l'ENI.

## Fonctionnalités

- Gestion des **clients** et de leurs adresses
- Gestion des **jeux** et de leurs **genres**
- Gestion des **exemplaires** de jeux
- Gestion des **locations** (emprunts)
- Authentification et contrôle d'accès via Spring Security

## Architecture

Le projet suit une architecture en couches classique :

```
bo/           Objets métier (Client, Jeu, Genre, Exemplaire, Location)
dal/          Couche d'accès aux données (repositories JDBC)
bll/          Couche métier (services + interface générique ICrudService)
controllers/  Contrôleurs Spring MVC + convertisseurs String -> entité
```

## Stack technique

- **Java 17**
- **Spring Boot 3.4** (Web, Security, Validation, JDBC)
- **Thymeleaf** + `thymeleaf-extras-springsecurity6` pour les vues
- **PostgreSQL**
- **Gradle** (wrapper inclus)

## Mise en route

Prérequis : JDK 17 et une base PostgreSQL.

1. Configurer la connexion à la base dans `src/main/resources/application.properties`
   (URL, utilisateur, mot de passe).
2. Lancer l'application :

```bash
./gradlew bootRun
```

L'application est ensuite accessible sur http://localhost:8080.

## Build

```bash
./gradlew build
```

## Licence

Distribué sous licence MIT. Voir [LICENSE](LICENSE).
