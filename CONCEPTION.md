# CONCEPTION

---

**IMPORTANT** : télécharger le dossier suivant : [https://drive.switch.ch/index.php/s/WYjT0Z4HakXFRGV](https://drive.switch.ch/index.php/s/WYjT0Z4HakXFRGV)
<br>
Ensuite, le dézipper et le placer à la racine du projet (save doit être au même niveau que src/ et res/).
<br>
`world01` désigne le second niveau présent dans le menu **PLAY**.

## Modifications apportées à l'architecture

L'architecture globale n'a pas été grandement modifiée. Seuls des ajouts y ont été apportés. La différence notoire est que chaque `Actor` doit définir une `Box` et un `String` définissant sa `Sprite`.

Le premier choix a été fait afin d'harmoniser la déclaration des `Actor`. En effet, chaque `Actor` est défini par une position et sa taille, cela tombait donc sous le sens pour nous de donner un attribut `Box` à chaque `Actor`. Une méthode `setBox()` a dû être implémentée à cet effet.

Le second choix a été fait en raison de la sérialisation utilisée afin de sauvegarder les parties. En effet, `Sprite` était très difficilement `Serializable`, il a alors fallu apporter cette modification afin de rendre la sauvegarde possible.

Afin d'assurer la sérialisation, il a fallu que `platform.util.Box` et `platform.util.Vector` implémentent l'interface `Serializable`. Il s'agit là de l'unique modification apportée au dossier `platform.util`, à l'exception près du fichier `package-info.java` qui a été rajouté dans chaque package afin de donner des informations à la javadoc.

### Packages

Le package `platform.game` a été subdivisé en plusieurs sous-packages. Nous avons donc désormais les sous-packages suivants:

- `block` Contient `Block` et `WoodenBlock`.
- `button` Contient les extensions cliquable dans l'interface graphique (boutons de menu, etc.)
- `character` Contient le `Player` (aurait éventuellement contenu les classes des autres personnages)
- `graphic` Contient les éléments graphiques, interactifs ou non (`SelectGravity` est par ex. interactif).
- `item` Objets ramassables. Il n'y a que `Heart`, mais pourrait contenir par exemples les pièces où autres bonus ramassables
- `level` Contient tous les niveaux, que ce soit le `Menu`, les niveaux jouables et le constructeur, `Builder`.
- `linkable` Objets pouvant être liés à des signaux, comme `Door` ou `Mover`.
- `misc` Divers. Contient les `Limits`, les `Debris` et les `Particle`s.
- `passive` Objet ne faisant rien en soi, i.e. sans interaction joueur (`Spike` et `Jumper`).
- `signal` Objets faisant office de signaux. Tous présents ici sauf `Door` qui se situe dans `platform.game.linkable`
- `weapon` Objets faisant office d'armes, comme `Bomb`.

Le `package` principal conserve les classes `Actor`, `Arrow`, `Eraser`, `Oberlay`, `Simulator` et `World`, ainsi que l'énumération `Damage` et l'interface `Linkable`, qui ont soit une caractéristique globale au projet, soit besoin d'autorisations de package spécifiques.

## Classes

###### Description des changements apportés aux classes proposées et des extensions

##### > package `platform.game`

### World

- Ajout de quelques **variables statiques globales non modifiables**. Utiles pour avoir un point de référence fixe dans le monde (`defaultCenter` et `defaultRadius`). En effet, les éléments utilisés dans le niveau `Builder`, donc le mode constructions, doivent être alignés à la vue. La nécessité d'avoir un point de départ commun nous a donc incité à concevoir ces variables.
- Les deux autres variables, `pathSave` et `pathWorld`, permettent à l'ensemble du programme de savoir où se trouvent les **sauvegardes**.
- `getCenter()` et `getRadius()` permettent une **utilisation optimale des variables** précédemment citées. Un calcul de la différence permet en effet à des éléments de niveaux où se placer afin de suivre la vue.
- `getGravity()` n'a pas été implémenté tel que demandé dans l'énoncé. En effet, comme la gravité peut changer selon les vœux de l'utilisateur, il faut que la méthode retourne la **valeur d'une variable locale** *(cf. `Simulator`)*.
- `getGravityAngle()` est un raccourci utile permettant de ne pas appeler `getGravity().getAngle()` à chaque fois. Permet donc de signaler plus clairement ce que le programme cherche à faire dans le code.
- `link(linkable)` demande au simulateur de commencer à lier un `Actor` à un ou des signaux. *(cf `Simulator.update()`)*
- `setGravity(double)` permet de **changer l'angle de la gravité** *(cf. `SelectGravity`)*
- `saveAll()` permet de demander au `Simulator` d'effectuer la sauvegarde après la fin de l'actuel cycle d'`update`. Cette fonction est utile car l'appel à la sauvegarde (non dynamique) se fait depuis un `Actor`, donc en plein cycle de mise à jour. *(cf `Simulator.save(boolean)`, `Spawn`)*.
- `load(int)` permet de **charger** le niveau désiré.
<br>
Si `int` est positif, la méthode charge un monde ayant 3 chiffres significatifs (ex, pour 1, `world001`). Utilisé dans `platform.game.level.Loading`.
<br>
Si `int` est égal à zéro, elle charge systématiquement le monde `world000`. Utilisé par le chargement dynamique, via la touche **Y**. 
<br>
Si `int` est négatif, la méthode charge un monde ayant 2 chiffres significatifs (ex, pour 1, `world01`). Utilisé dans `platform.game.level.LevelSelection`. Désigne donc les mondes construits dans le Builder et utilisés dans le monde **PLAY**.
- `delete(File)`. Outil permettant de détruire un fichier/dossier.
- `keyboardCode(int)`. Outil retournant le raccourci correspondant au chiffre entré (de **0** à **10**).

Tous ces ajouts ont été apportés afin que les `Actors` puissent les appeler. En effet, comme proposé dans l'énoncé, ils possèdent tous un attribut `world` appelable avec `getWorld()`. Concernant les deux dernières méthodes, il aurait également fait sens de créer une nouvelle classe dans `platform.util` pour les définir.

### Simulator

- `save(boolean)`, précédemment cité, permet de sauvegarder le monde actuel. Si la valeur `boolean` est `false`, alors le monde sera sauvegardé à la suite de tous les autres (si 12 mondes sont déjà enregistrés, de `world001` à `world012`, alors la nouvelle sauvegarde se nommera `world013`.
<br>
En revanche, la sauvegarde dynamique sauve le monde dans le dossier `world000`, réservé à cet effet et accessible uniquement depuis le menu principal ou avec la touche **X**.
- `maintainActors()`. Méthode privée servant à modulariser le code. Enregistre et désenregistre les `Actor`s présents dans `registered` et `unregistered`, comme demandé dans le code.
- `update(input, output)`. En plus de réaliser tout ce qui est demandé par l'énoncé, s'occupe d'autres tâches globales:
  - *lier* un objet à un signal (utilisé par `platform.game.button.Spawn` dans `platform.game.level.Builder`).
  - *retourner au Menu* à chaque demande de l'utilisateur via la touche **ESC**.
  - *déplacer la vue* dans le niveau `Builder` (cf *README*).
  - *commencer la sauvegarde* si un `Actor` l'a demandé.
  - *sauvegarder dynamiquement* la partie à chaque demande de l'utilisateur (touche **X**) sous certaines conditions : le `Player` doit être présent.
  - *chargement rapide* de la sauvegarde dynamique avec la touche **Y**.
  - *retourner au mode construction* à chaque demande de l'utilisateur (touche **C**). Uniquement dans un niveau précédemment construit par l'utilisateur et si le `Player` est présent.
  - *change l'image de fond* ssi le `Player` n'est pas présent et que l'on se trouve dans le niveau de construction.

Tout le reste est standard, tel que demandé par l'énoncé.

### Damage

Quatre types ont été rajoutés :

- `PRESENCE`. Le `Player` signale sa présence au monde. Permet au `Sprite` d'`ArrowDispenser` de le suivre dynamiquement et de l'attaquer.
- `KEY`. Utilisé par `Key`, se désenregistre si un `Actor` est réceptif à ce dégât (le `Player`).
- `ARROW`. Type de dégât utilisé par `Arrow`.
- `EXPLOSION`. Type de dégât utilisé par `Bomb`.

### Linkable (interface)

Désigne tous les `Actor`s ayant un comportement "liable", comme `Door`, `Mover` et `Exit`. Définit deux méthodes:
- `addSignal(signal)`. Permet au `Linkable` d'ajouter un signal (normalement via le signal `And`).
- `endSignal()`. Signifie au `Linkable` la fin du processus, le signal est confirmé.

### Actor

Deux modifications importantes ont été apportées aux `Actor`s, comme précédemment mentionné. L'ajout de `getSpriteName()`, `setSpriteName(String)` et `getSprite()`, qui désignent clairement ce qu'ils font.

Chaque `Actor` peut aussi définir une méthode `copie()`. Cette méthode sert à cloner l'`Actor` afin de pouvoir construire un niveau dans `Builder`. N'est pas analogue à `clone()`, donc cela fait complètement sens de définir une autre méthode.

### Arrow

Arme lancée par `ArrowDispenser`. Une fois que la flèche atteint un ennemi, elle se plante dans sa Box (facteur différent s'ils s'agit d'un joueur ou d'un bloc) pour ensuite suivre son mouvement. Elle disparaît si l'acteur disparaît et reste orientée selon sa (dernière) vitesse non-nulle.

Dès qu'elle touche un ennemi, se désenregistre et se réenregistre afin de changer sa priorité de manière dynamique. En effet, après avoir atterri, elle doit apparaître derrière les blocs, alors qu'elle a une priorité très haut auparavant.

### Overlay

Deux (voire trois) éléments ont été rajoutés : une barre de souffle et une barre de cooldown pour les bombes *(cf `Player`)*.  Il appelle et crée également le `GravitySelector`, l'élément situé en haut à droite de l'écran quand le joueur est présent.

L'`Overlay` a une priorité extrêmement haute afin de passer devant la grande majorité d'éléments visibles.

### Eraser

Utilisé dans le niveau `Builder`. Permet à l'utilisateur de supprimer tout élément déjà posé dans le niveau en le sélectionnant et appuyant sur l'élément concerné. Tout se fait à la souris. Son raccourci dans `Builder` est **DELETE**.


##### > package `platform.game.block`

### Block

Contrairement à ce que demande l'énoncé, le `Block` ne défini pas sa propre Box et sa propre Sprite, mais l'"envoie" à la super-classe.

### WoodenBlock

Désigne un `Block` sensible aux dégâts `FIRE` et `EXPLOSION`. S'il en reçoit, se désenregistre immédiatement et invoque des `Debris`.


##### > package `platform.game.button`

### Button

Désigne un `Block` cliquable. Ne fait rien en soi, mais change de `Sprite` lorsque la souris se trouve au-dessus de l'élément et se lie à un raccourci. Il définit également la méthode protégée `followView(Box)`, permettant à la Box de suivre le déplacement de la caméra. Utile aux éléments présents dans `Builder`.

### ToNextLevel

Désigne un bouton appelant un `Level` désigné à sa construction. Sert pour changer de menu et/ou lancer un niveau de jeu.

### LoadLevel

Permet de charger par simple clic gauche un niveau précédemment créé par le constructeur de niveau. Permet également de supprimer ledit niveau avec un clic droit. Réorganise ensuite tous les niveaux afin qu'ils restent dans un ordre croissant consécutif.

Exemple : on désire supprimer le monde 003. 5 mondes sont présent. Alors, après sa suppression, le monde 004 est renommé en monde 003 et le 005 en 004.

### Spawn

Cœur du niveau `Builder`. Désigne un `Button` permettant de créer un `Actor` pouvant être placé sur le niveau. Sa méthode `update` gère le placement de l'`Actor`.

Le comportement est bien sûr différent s'ils s'agit de faire apparaître un `Actor` standard ou le `Player`. Le spawn de `Player` est créé avec une liste d'éléments à désenregistrer (les éléments du menu) dès son appel.

Le `Spawn` de `Mover` est également différent. En effet, celui-ci copie la `Sprite` de l'`Actor` avec lequel on clique dessus afin de créer un `Mover` ayant le même `Sprite.` Supprime bien entendu l'`Actor` utilisé pour cela.

### SuperSpawner

Utilisé aussi dans `Builder`. Forme un super-menu de `Spawn`.

N'a pas de constructeur public, mais définit plusieurs méthodes statiques permettant d'en créer.


##### > package `platform.game.character`

### Player

Le joueur, en plus d'avoir une "barre de vie", a également une "barre de respiration", qui met 2 secondes à se vider et 10 à se remplir. S'il est essoufflé, il ne peut plus souffler pendant 10 secondes.

Ensuite, il possède également un cooldown pour les bombes. Il ne peut en lancer que toutes les 5 secondes.

Le `Player` est également responsable pour l'appel de l'`Overlay` et aussi de son désenregistrement.

Le déplacement du `Player` se fait en fonction de la gravité. La gravité est coupée en quartiers, donc quand l'utilisateur demande au joueur d'aller à gauche, le joueur va effectivement à sa gauche si la gravité est entre -Pi/4 et Pi/4, saute entre Pi/4 et 3Pi/4, etc. `colliding` a également dû être affiné afin qu'un contact avec le plafond n'autorise pas au joueur de sauter.

Le dégât de type `AIR` reçu par le `Player` a été modifié afin d'offrir un `Jumper` plus intuitif qui ne change que la vitesse verticale et pas la vitesse horizontale.
<br>
`HEAL` a également été modifié afin de ne pas prendre un `Heart` si la santé du joueur est déjà maximal.
<br>
Le dégât `ARROW` ajoute une vitesse au joueur afin de donner une impression d'impact.


##### > package `platform.game.graphic`

### Image

Désigne une simple image, comme l'image titre du Menu.

### BackgroundImage

Désigne un autre type d'image qui se greffe au fond de l'écran, comme les couleurs de fond ou les images de fond.

### Digit

Crée une image de nombre entre 0 et 99, utilisé par les sélecteurs de niveaux.

### End

Comme proposé par l'énoncé, offre une transition après la mort du `Player` ainsi qu'un dézoom progressif. Affiche également "WASTED" après 1 seconde...

### SelectGravity

Permet de modifier l'angle de la gravité, soit par les touches fléchées, soit par la molette, soit par la souris sur son affichage graphique. Utilisé par `Overlay`.


##### > package platform.game.item

### Heart

