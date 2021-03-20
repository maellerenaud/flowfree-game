package gloo.flowfree.model;

/**
 * <b>Classe permettant de stocker l'ensemble des cases avec leurs coordonnées.</b>
 * 
 * <p>Seul le plateau connaît les coordonnées (posiotions [indiceLigne, indiceColonne]
 * des cases.
 * 
 * <p>Ce plateau connaît :
 * <ul>
 *     <li> Toutes ses cases
 * </ul>
 * 
 * <p>Ce plateau est connu :
 * <ul>
 *     <li> Du controleur
 *     <li> De toutes ses cases
 * </ul>
 * 
 * @author Maëlle Renaud
 *
 */
public class Plateau {
	
	/**
	 * <b>Nombre de lignes de la grille de jeu.</b>
	 * 
	 * <p>Initialisé à partir du nombre de lignes stocké dans le niveau courant
	 * pour éviter de réinterroger le Niveau.
	 * 
	 * @see Niveau#nbLignes
	 */
	private int nbLignes;
	
	/**
	 * <b>Nombre de colonnes de la grille de jeu.</b>
	 * 
	 * <p>Initialisé à partir du nombre de colonnes stocké dans le niveau courant
	 * pour éviter de réinterroger le Niveau.
	 * 
	 * @see Niveau#nbColonnes
	 */
	private int nbColonnes;
	
	/**
	 * Matrice des cases représentant le tableau de taille [nbLignes][nbColonnes].
	 */
	private Case[][] cases;
	
	// Constructeur
	/**
	 * <b>Constructeur appelé par le controleur lors du lancement d'un niveau.</b>
	 * 
	 * <p>Ce constructeur initialise ou demande l'initialisation de tous les objets
	 * métiers nécessaires pour démarrer le jeu.
	 * 
	 * <ul>
	 *     <li> Initialisation de nbLignes à partir du nombre de lignes du niveau
	 *          passé en paramètre.
     *     <li> Initialisation de nbColonnes à partir du nombre de colonnes du niveau
	 *          passé en paramètre.
	 *     <li> Initialisation de la matrices cases
	 *     <li> Construction de tous les élements de la matrice cases
	 *     <li> Récupération des couleurs utilisées dans le niveau et des positions des
	 *          plots associés à chacun de ces couleurs.
	 *     <li> Construction de chaque plot
	 * </ul>
	 * 
	 * @see Niveau#getNbLignes()
	 * @see Niveau#getNbColonnes()
	 * @see Case#Case(Plateau)
	 * @see Niveau#getCouleursNiveau()
	 * @see Niveau#getPlotsCouleur(Couleur)
	 * @see Plot#Plot(Couleur, Case)
	 * 
	 * @param niveau Niveau à dessiner et jouer sur ce plateau
	 */
	public Plateau(Niveau niveau) {
		this.nbLignes = niveau.getNbLignes();
		this.nbColonnes = niveau.getNbColonnes();
		this.cases = new Case[nbLignes][nbColonnes];
		
		// Créer les cases
		for (int i=0 ; i<nbLignes ; i++) {
			for (int j=0 ; j<nbColonnes ; j++) {
				cases[i][j] = new Case(this);
			}
		}
		
		// Placer les plots
		for (Couleur c : niveau.getCouleursNiveau()) {
			int[][] coordPlots = niveau.getPlotsCouleur(c);
			for (int[] coordOnePlot : coordPlots) {
				new Plot(c, cases[coordOnePlot[0]][coordOnePlot[1]]);
			} 
		}
	}
	
	// Autres méthodes
	/**
	 * <b>Méthode appelée par le controleur pour sélectionner une case si elle contient
	 * un plot.</b>
	 * 
	 * <p>Demande à la case consernée si elle possède un plot.
	 * 
	 * @see Case#getPlot()
	 * 
	 * @param i Indice de ligne de la case demandée.
	 * @param j Indice de colonne de la case demandée.
	 * @return Le plot présent dans la case s'il existe, null sinon.
	 */
	public Plot getPlot(int i, int j) {
		return cases[i][j].getPlot();
	}
	
	/**
	 * <b>Méthode permettant de trouver la position dans le plateau de la case passée en
	 * paramètre.</b>
	 * 
	 * <p>Cette méthode est appelée par 
	 * <ul>
	 *     <li> le controleur pour donner à l'IHM les coordonnées des cases de départ 
	 *          des tuyaux.
	 *     <li> le plateau pour trouver les cases voisines d'une case.
	 * </ul>
	 * 
	 * <p>Cette méthode compare la référence de la case passée en paramètre avec chacune
	 * des références des cases du plateau.
	 * 
	 * @param emplacement Case à trouver dans le plateau.
	 * @return Coordonnées [indiceLigne, indiceColonne] de la case dans le plateau si
	 *         la case passée en paramètre référence un objet du plateau, null sinon.
	 */
	public int[] findPositionCase(Case emplacement) {
		for (int i=0 ; i<nbLignes ; i++) {
			for (int j=0 ; j<nbColonnes ; j++) {
				if (emplacement == cases[i][j]) {
					return new int[] {i, j};
				}
			}
		}
		return null;
	}
	
	/**
	 * <b>Méthode appelée par la case elle-même lors de la propagation d'un tuyau pour
	 * déterminer sa case voisine dans la direction demandée par le joueur.</b>
	 * 
	 * <p>Demande les coordonnées de la case passée en paramètre et si elle n'est pas 
	 * sur le bord, renvoie la case voisine suivant la direction demandée.
	 * 
	 * @see Plateau#findPositionCase(Case)
	 * 
	 * @param emplacement Case dont on veut la voisine (dernière case actuelle du 
	 *        tuyau courant)
	 * @param dir Direction de propagation du tuyau courant demandée par le joueur
	 * 
	 * @return La case voisine dans la direction demandée si elle existe, null sinon.
	 */
	public Case getMaCaseVoisine(Case emplacement, Direction dir) {
		int[] coord = findPositionCase(emplacement);
		int i = coord[0];
		int j = coord[1];
		switch(dir) {
		case HAUT: if (i != 0) return cases[i-1][j]; break;
		case BAS: if (i < nbLignes - 1) return cases[i+1][j]; break;
		case DROITE: if (j < nbColonnes - 1) return cases[i][j+1]; break;
		case GAUCHE: if (j != 0) return cases[i][j-1]; break;
		}
		return null;
	}
	
	/**
	 * <b>Méthode appelée par le controleur à la fin de chaque action pour vérifier si toutes les
	 * cases sont utilisées soit par un plot, soit par un tuyau, une des conditions pour réussir 
	 * le niveau.</b>
	 * 
	 * <p>Demande à chaque case si elle est occupée.
	 * 
	 * @see Case#estOccupee()
	 * 
	 * @return Vrai si toutes les cases sont utilisées soit par un plot, soit par un tuyau, faux
	 *         sinon.
	 */
	public boolean plateauComplet() {
		for (int i=0 ; i<nbLignes ; i++) {
			for (int j=0 ; j<nbColonnes ; j++) {
				if (!cases[i][j].estOccupee()) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Affichage
	/**
	 * <b>Méthode permettant d'afficher dans la console l'état actuel du plateau.</b>
	 * 
	 * <p>Demande à chaque case sa représentation sous forme de chaîne de caractères
	 * à afficher dans la console. Cette représentation indique le cas échéant la 
	 * couleur contenue dans la case.
	 * <p>La représentation des cases est visuellement organisée sous forme de matrice.
	 * Les représentations des cases d'une même ligne sont séparées par des virgules.
	 * 
	 * @see Case#display()
	 * 
	 * @return Chaîne de caractère sur autant de lignes que le plateau en comporte
	 *         représentant la couleur contenue dans chaque case.
	 */
	public String display() {
		String strPlateau = "";
		for (int i=0 ; i<nbLignes ; i++) {
			for (int j=0 ; j<nbColonnes ; j++) {
				strPlateau += cases[i][j].display() + ",";	// Chaque ligne se termine par une virgule par soucis de simplicité
			}
			strPlateau += "\n";
		}
		return strPlateau;
	}
	
}













