# README
---

## Contributeurs
- Robin Mamié (robinmamie)
- Maxence Jouve (jouvemax)

## Lancer le programme

Compiler et exécuter `src\platform\Program.java`.

## Commandes

### Menu principal

Dans le menu principal, trois – voire quatre – modes sont disponibles.

1. **PLAY**, accessible en appuyant sur **1** ou en cliquant dessus. Ce mode vous permet d'ensuite sélectionner un des niveaux, soit en sélectionnant sur le raccourci clavier correspondant (**1**,**2**,...), soit en cliquant dessus. Notez qu'un niveau ne peut être sélectionné que si le précédent a été réussi. Si vous désirez toutefois atteindre le niveau que vous désirez directement, sélectionnez CONTINUE dans le menu principal et les variables seront mises à jour.
<br>
Si vous ne désirez pas débloquer les niveaux avec votre dur labeur, vous pouvez cliquer sur le niveau 42 afin de tout débloquer.
2. **LOAD**, accessible en appuyant sur **2** ou en cliquant dessus. Ce mode vous permet de sélectionner parmi max. 50 niveaux que vous aurez créés dans le menu BUILD. Vous pouvez les sélectionner de la même façon que dans le menu PLAY. Vous pouvez supprimer le niveau que vous désirez avec un simple clic droit.
3. **BUILD**, accessible en appuyant sur **3** ou en cliquant dessus. Vous permet de construire le niveau que vous désirez de manière graphique. Sera expliqué dans la section correspondante.
4. **CONTINUE**, accessible en appuyant sur **4** ou **Y** ou en cliquant dessus. Reprend la partie là où vous l'avez sauvegardée pour la dernière fois (en appuyant sur **X**). Peut aussi être appelé en jeu ou dans les menus avec la touche **Y**. Peut être supprimé en cliquant droit dessus.

Notez que vous pouvez revenir au menu principal à partir de chaque écran du jeu en appuyant sur **ESC** (échap).

### En jeu

#### Le joueur
Vous pouvez:

- vous *déplacer* en utilisant les touches **WASD** (ou **ZQSD** pour permettre la compatibilité avec les claviers français).
- *souffler* en appuyant sur **SHIFT** (maj). Notez que le joueur dispose d'une barre de souffle qui dure 2 secondes et se remplit entièrement en 10 secondes. Si le souffle du joueur arrive à 0 à un moment donné, vous devez attendre 10 secondes pour que le joueur reprenne entièrement son souffle avant de pouvoir réutiliser la fonction.
- *lancer des boules de feu* en appuyant sur la **barre espace**
- *lancer des bombes* en appuyant sur **B**. Disponible 5 secondes après un lancer.
- *activer les éléments* avec la touche **E**. Sert pour sortir du niveau, les levier, etc.

#### La gravité
L'angle de la gravité (mais pas sa force) peut être modifiée en utilisant les **touches fléchées**, la **molette** de la souris ou en **cliquant sur le cadran** présent en haut à droite de l'écran.

### Mode construction
Dans le mode **BUILD** décrit plus haut, vous pouvez construire votre propre niveau. Il suffit de sélectionner un élément et de le placer directement sur l'affichage graphique. Vous pouvez:

- vous *déplacer* sur la carte en usant des **touches fléchées**
- *zoomer* en appuyant sur **M**
- *dézommer* en appuyant sur **N**
- Retrouver le *zoom de départ* en appuyant sur **B**
- Retrouver la *position de départ* en appuyant sur **V**
- *Changer l'image* de fond avec **I**

 Vous trouverez de haut en bas :

- **Les blocs horizontaux**. Trois différents ratios, deux `Sprite`s différentes chacun (cassé et normal).
- **Le joueur**. À utiliser tout à la fin. Sélectionnable aussi avec **P**.
- **Les blocs verticaux**. Trois différents ratios, deux `Sprite`s différentes chacun (cassé et normal).
- **Les blocs carrés**. Deux différents ratios, deux `Sprite`s différentes chacun (cassé et normal). Blocs destructible en bois aussi disponible.
- **Les signaux**. 4 clés, une torche allumée et une torche éteinte et un levier.
- **Les "déblocables"**. Après avoir placé l'élément, sélectionnez en cliquant gauche avec la souris le/les signal/aux responsable/s pour son ouverture. Ils seront liés en `AND`. Après avoir sélectionné vos éléments, faites un clic droit avec la souris pour valider.
- **Les divers**. Disponibles : le cœur, le `Jumper`, les piques et le lanceur de flèches.
- **Les ascenseurs**. (deux flèches rouges et bleues) Donne de base un ascenseur carré. Mais il est possible de prendre un autre élément, par exemple le 2ème bloc horizontal, et de cliquer sur l'icône des ascenseurs avec celui-ci. Cela créera un nouveau bloc qui sera un ascenseur avec le `Sprite` correspondant. Ensuite, on peut le placer sur la carte et sélectionner les signaux de la même manières que pour un déblocable (clics gauches puis clic droit). Ensuite, on peut appuyer sur **O** pour désigner que le signal devra osciller, ou **P** pour revenir à la sélection précédente. La deuxième position de l'ascenseur peut être choisie avec les touches **WASD** ou **ZQSD**. Pour valider la sélection finale, appuyer sur **ENTRÉE**.
- **L'effaceur**. Efface tout élément déjà posé dans le monde qui serait indésirable. Sélectionnable aussi avec **DELETE**.

Une fois un menu ouvert, vous pouvez sélectionner l'élément que vous désirez avec les touches numériques (**1**-**9**).

Le jeu sauvegarde automatiquement toute partie créée dans le `Builder` et devient ainsi accessible dans le menu **LOAD**.

Pour revenir en mode construction après avoir posé le joueur, il suffit d'appuyer sur **C**. Fonctionne aussi dans les niveaux sauvegardés.


## Résoudre les niveaux

Pour utiliser la porte de sortie, il faut l'activer en appuyant sur **E**.

### Niveau 1

1. Il faut casser le bloc en bois situé sur le `Jumper`.
2. Il faut se retrouver au second étage et prendre la clé. La clé, en plus d'ouvrir la porte jaune, débloque un mécanisme et referme la sortie vers le rez.
3. Il faut éteindre les deux torches afin de débloquer le second ascenseur, situé à droite de la position de départ.
4. La porte de sortie s'en retrouver fermée. Pour l'ouvrir, il faut rallumer la torche se situant en bas et vite ressortir pour ne pas se retrouver bloqué.

Le niveau est faisable sans utiliser le changement de la gravité ! 

### Niveau 2

1. Il faut éteindre la torche se situant au-dessus du joueur afin de bouger le bloc bloquant le passage en haut à droite.
2. Il faut prendre la clé jaune se situant tout en haut à droite, juste à la droite de la nouvelle position du bloc mouvant. Utiliser la gravité pour s'y rendre.
3. Tout en bas, prendre la clé orange et allumer la torche. La première débloque la porte orange, la seconde 
ouvre la porte de sortie.

### Niveau 3

Ce niveau a été spécialement conçu afin de tester si vous maîtrisez les changements de gravité.

Pour réussir ce niveau, il est d’abord nécessaire de remplir deux tâches : récupérer la clé jaune et la clé bleue. Dès lors, la porte qui se trouve en bas va s’ouvrir et il faudra s’y rendre.

#### Comment procéder ?

![Image offrant une vue complète sur le niveau.](http://i.imgur.com/Rb9sjVw.png)

Il faut avant toute chose se rendre dans le compartiment central noté 1 *(voir image)*. Pour cela, un peu d’adresse et quelques sauts contre le mur devraient suffire.

Il faut maintenant récupérer les 2 clés :

- **Clé jaune** : Positionnez la gravité en haut *(rappel : flèche haut)* afin d’atterrir sur les blocs en bois. Vous pouvez les casser avec une boule de feu *(touche espace)* ou une bombe *(B)*.
<br>
Vous accédez alors à un compartiment dans lequel vous remarquerez 3 torches allumées. L’objectif pour vous est de les éteindre (il faut utiliser la fonctionnalité permettant de changer la gravité).
<br> 
Empruntez le chemin noir afin d’éteindre la torche. Ceci ouvre la porte et il faut désormais récupérer la clé qui se trouve au bout du chemin rouge.
Pour finir, sortez du labyrinthe en faisant attention où vous mettez les pieds. Rejoignez le compartiment central.

![Image montrant le chemin à prendre dans le labyrinthe.](http://i.imgur.com/cVx2Too.png)

- **Clé bleue** : Cassez les blocs en bois situés à droite du compartiment central. Entrez dans le compartiment et changez la gravité vers la droite (flèche droite).
<br>
Vous devez alors vous déplacer sur le mur puis changer la gravité à gauche, puis de nouveau à droite.
<br>
Le dernier obstacle est d’activer le levier situé en bas à gauche.
<br>
Ce levier va faire disparaître le bloc bleu, il ne vous reste plus qu’à récupérer la clé bleue et faire demi-tour.

![Image montrant une vue globale sur l'aile de la clé bleue](http://i.imgur.com/E6jZ2GZ.png)

Vous êtes donc de retour dans le compartiment central. Cassez les blocs en bois situés en bas du compartiment. Vous vous trouvez désormais dans un goulet de blocs en bois.
<br>
Frayez-vous un chemin en les cassant avec les boules de feu ou les bombes, mais attention ! Si vous êtes trop pressés, vous risquez de tomber sur les pics mortels. Soyez prudent et placez-vous correctement avant de faire le grand saut. Une dernière manœuvre pendant le saut et vous revoilà sur la terre ferme avec la porte ouverte qui vous attend.

![Image montrant la fin du niveau](http://i.imgur.com/22jeWfb.png)
