# Projet_bruitage_Groupe_4

## Table des matières

1. [Description](#description)
2. [Fonctionnalités](#fonctionnalités)
3. [Installation](#installation)
4. [Utilisation](#utilisation)

---

## Description

Ce projet se porte sur le débruitage d'images par Analyse en Composantes Principales (ACP). Plus précisément, l’objectif principal est d'implémenter une méthode performante de réduction de bruit dans des images numériques, en se basant sur la parcimonie, la redondance et régularité intrinsèque des données visuelles.

## Fonctionnalités

- Ajout de bruit gaussien contrôlé à une image
- Extraction de patchs (globale ou locale)
- Vectorisation des patchs pour l’ACP
- Calcul de :
  - Vecteur moyen
  - Matrice de covariance
  - Base ACP (via décomposition SVD)
- Projection et seuillage (dur / doux)
- Reconstruction de l’image par fusion pondérée des patchs
- Comparaison visuelle des résultats (bruitée vs. débruitée)

## Installation

1. Pré-requis :
  - Java JDK (version ≥ 11)
  - IDE recommandé : Eclipse, IntelliJ, ou VS Code
  - Librairie externe : Apache Commons Math 3.6.1
    
2. Ajout de la librairie externe
  - Télécharger commons-math3-3.6.1.jar
  - Dans Eclipse :
    -Clic droit sur le projet → Build Path → Add External Archives...
    -Sélectionner commons-math3-3.6.1.jar
   
## Utilisation

### 1. Paramètres modifiables (`Main.java`)
```java
// === PARAMÈTRES ===
String cheminImage = "images_test/lemurien.jpeg";
double sigma = 50;       // niveau de bruit
int taillePatch = 8;     // taille des patchs s × s
double lambda = 30;      // seuil pour seuillage
```

### 2. Modes disponibles
- **ACP globale** (base unique)
- **ACP locale** (base recalculée pour chaque bloc de taille `Ws × Ws`)
- **Seuillage doux** (soft thresholding)
- **Seuillage dur** (hard thresholding)

### 3. Dossier de sortie

```
out/
├── image_bruitee.jpeg
├── global/
│   ├── image_debruitee_doux.jpeg
│   └── image_debruitee_dur.jpeg
└── local/
    ├── image_debruitee_doux.jpeg
    └── image_debruitee_dur.jpeg
```

---

## Exécution via fichier .jar

Un fichier `.jar` exécutable est également fourni pour lancer directement le projet sans ouvrir l'IDE.

### Lancer le projet :
Ouvrir le terminal et placez-vous à la racine du projet. 
```bash
java -jar DebruitageACP.jar
```

---
