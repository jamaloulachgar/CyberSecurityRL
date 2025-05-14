# CyberSecurityRL

Projet de simulation de cybersécurité utilisant l'apprentissage par renforcement (Q-Learning) pour modéliser les interactions entre un attaquant et un défenseur dans un réseau informatique.  
<<<<<<< HEAD
Implémenté en Java avec JavaFX pour l'interface graphique, JFreeChart pour les visualisations, et une analyse des vulnérabilités en temps réel.
=======
Implémenté en Java avec JavaFX pour l’interface graphique, JFreeChart pour les visualisations, et une analyse des vulnérabilités en temps réel.
>>>>>>> 9885ecfde8c6b5bda1d9d880886267b2fec5473f

---

## 🚀 Objectif du projet

<<<<<<< HEAD
Simuler un scénario réaliste de cybersécurité où un attaquant et un défenseur s'affrontent dans un réseau de 4 nœuds, chacun associé à un port courant (80, 445, 23, 443).  
Les deux agents apprennent et optimisent leurs stratégies grâce à l'algorithme Q-Learning.
=======
Simuler un scénario réaliste de cybersécurité où un attaquant et un défenseur s’affrontent dans un réseau de 4 nœuds, chacun associé à un port courant (80, 445, 23, 443).  
Les deux agents apprennent et optimisent leurs stratégies grâce à l’algorithme Q-Learning.
>>>>>>> 9885ecfde8c6b5bda1d9d880886267b2fec5473f

---

## 🎯 Fonctionnalités principales

<<<<<<< HEAD
- **Simulation d'attaques et de défenses** avec apprentissage par renforcement (Q-Learning)
- **Interface graphique JavaFX** : visualisation des nœuds, ports, connexions, scores et vulnérabilités
- **Graphiques JFreeChart** : évolution des victoires et du risque global sur plusieurs simulations
- **Analyse de vulnérabilité en temps réel** : score par nœud, recommandations et affichage couleur
- **Simulation de ports réseau** : attribution de ports réalistes et affichage façon `netstat -an`
=======
- **Simulation d’attaques et de défenses** avec apprentissage par renforcement (Q-Learning)
- **Interface graphique JavaFX** : visualisation des nœuds, ports, connexions, scores et vulnérabilités
- **Graphiques JFreeChart** : évolution des victoires et du risque global sur plusieurs simulations
- **Analyse de vulnérabilité en temps réel** : score par nœud, recommandations et affichage couleur
- **Simulation de ports réseau** : attribution de ports réalistes et affichage façon `netstat -an`
>>>>>>> 9885ecfde8c6b5bda1d9d880886267b2fec5473f

---

## 🛠️ Structure du code

<<<<<<< HEAD
- `Main.java` : Logique principale, interface graphique, affichage des scores et graphiques
- `Attacker.java` : Logique de l'attaquant (Q-Learning, attaques, DDoS)
- `Defender.java` : Logique du défenseur (Q-Learning, protection)
- `Simulation.java` : Orchestration des épisodes, conditions de victoire
- `NetworkEnvironment.java` : Définition du réseau, gestion des ports et connexions
- `VulnerabilityScanner.java` : Analyse et affichage des vulnérabilités
=======
- `Main.java` : Logique principale, interface graphique, affichage des scores et graphiques
- `Attacker.java` : Logique de l’attaquant (Q-Learning, attaques, DDoS)
- `Defender.java` : Logique du défenseur (Q-Learning, protection)
- `Simulation.java` : Orchestration des épisodes, conditions de victoire
- `NetworkEnvironment.java` : Définition du réseau, gestion des ports et connexions
- `VulnerabilityScanner.java` : Analyse et affichage des vulnérabilités
>>>>>>> 9885ecfde8c6b5bda1d9d880886267b2fec5473f

---

## 📊 Exemples de visualisation

<<<<<<< HEAD
- **Interface principale** :  
  ![Interface principale](./docs/interface.png)
- **Graphique des résultats** :  
=======
- **Interface principale** :  
  ![Interface principale](./docs/interface.png)
- **Graphique des résultats** :  
>>>>>>> 9885ecfde8c6b5bda1d9d880886267b2fec5473f
  ![Graphique JFreeChart](./docs/graphique.png)

---

## 💡 Améliorations apportées

- Conversion complète du code Python vers Java
<<<<<<< HEAD
- Ajout d'une interface graphique moderne (JavaFX)
=======
- Ajout d’une interface graphique moderne (JavaFX)
>>>>>>> 9885ecfde8c6b5bda1d9d880886267b2fec5473f
- Intégration de JFreeChart pour les statistiques
- Simulation réaliste des ports et de la commande `netstat -an`
- Analyse de vulnérabilité en temps réel et recommandations automatiques

---

## 📦 Installation & Lancement

1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/jamaloulachgar/CyberSecurityRL.git
   cd CyberSecurityRL
   ```
2. **Compiler le projet avec Maven**
   ```bash
   mvn clean install
   ```
<<<<<<< HEAD
3. **Lancer l'application**
=======
3. **Lancer l’application**
>>>>>>> 9885ecfde8c6b5bda1d9d880886267b2fec5473f
   ```bash
   mvn javafx:run
   ```

---

## 👨‍💻 Auteurs

<<<<<<< HEAD
- Projet adapté et amélioré par [Votre Nom] à partir d'un projet initial en Python.
=======
- Projet adapté et amélioré par Jamal OULACHGAR en collab avec mon collègue Mohamed Amine AABID  à partir d’un projet initial en Python.
>>>>>>> 9885ecfde8c6b5bda1d9d880886267b2fec5473f

---

## 📄 Licence

<<<<<<< HEAD
Ce projet est open-source et libre d'utilisation à des fins pédagogiques.
=======
Ce projet est open-source et libre d’utilisation à des fins pédagogiques.
>>>>>>> 9885ecfde8c6b5bda1d9d880886267b2fec5473f
