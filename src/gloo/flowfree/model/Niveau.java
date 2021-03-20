package gloo.flowfree.model;

import java.util.HashMap;
import java.util.Set;

/**
 * <b> Classe représentant un niveau et toutes les caractéristiques de
 * la grille de départ</b>
 * 
 * <p>Ce niveau sert au controleur à récupérer les ressources brutes du 
 * fichier niveau.txt. Il ne connaît pas d'instances de classes.
 * 
 * <p>Ce niveau est connu :
 * <ul>
 *     <li> De l'instance de TousNiveaux
 *     <li> Du controleur si ce niveau est le niveau courant choisi par l'utilisateur
 * </ul>
 * 
 * @author Maëlle Renaud
 *
 */
public class Niveau {
	
	/**
	 * <b>Identifiant du niveau = position dans l'attribut listeNiveaux = position 
	 * dans le fichier niveaux.txt.</b>
	 * 
	 * @see Niveau#getId()
	 */
	private int id;
	
	/**
	 * <b>Nombre de lignes de la grille de ce niveau.</b>
	 * 
	 * @see Niveau#getNbLignes()
	 */
	private int nbLignes;
	
	/**
	 * <b>Nombre de colonnes de la grille de ce niveau.</b>
	 * 
	 * @see Niveau#getNbColonnes()
	 */
	private int nbColonnes;
	
	/**
	 * <b>Dictionnaire dont les clés sont les couleurs utilisées dans ce niveau
	 * et les valeues sont les coordonées des 2 plots de chaque couleur.</b>
	 * 
	 * <p> Les valeurs sont présentées sous la forme [ [i1,j1], [i2,j2] ] = 
	 * [ coord plot 1 , coord plot 2 ].
	 * 
	 * @see Niveau#getCouleursNiveau()
	 * @see Niveau#getPlotsCouleur(Couleur)
	 */
	private HashMap<Couleur,int[][]> positionPlots;
	
	/**
	 * <b>Booléen indiquant si le niveau a été réussi par l'utilisateur
	 * à un moment durant la session de jeu.</b>
	 * 
	 * <p> Attribut passant à true dès que le niveau a été dans une position 
	 * gagnante. Il ne peut pas repasser à false.
	 * 
	 * <p> Sert à différencier les niveaux réussis des autres dans la fenêtre
	 * d'accueil.
	 * 
	 * @see Niveau#getReussi()
	 */
	private boolean reussi;
	
	// Constructeur
	/**
	 * <b>Constructeur appelé au début de la session de jeu par TousNiveaux
	 * pour extraire les informations de la grille initiale dans la chaîne
	 * de caractères correspondatne du fichier niveaux.txt.</b>
	 * 
	 * <p> La chaîne de caractères se présente de la manière suivante :
	 * <br>&emsp; Niveau
     * <br>&emsp; 5,5				&emsp; (nombre de lignes, nombre de colonnes)
	 * <br>&emsp; ROUGE;0,0;4,1	    &emsp; (Couleur ; coordonnées 1er plot ; coordonnées 2ème plot)
     * <br>&emsp; VERT;0,2;3,1
     * <br>&emsp; BLEU;1,2;4,2
     * <br>&emsp; JAUNE;0,4;3,3
     * <br>&emsp; ORANGE;1,4;4,3
     * 
     * <p> Les nombres de lignes et colonnes et les positions des plots sont
     * extraites de cette chaîne et sauvegardés dans les attributs de ce Niveau.
     * 
	 * @param strNiveau Chaîne de caractères représentant le niveau dans le
	 *                  fichier niveaux.txt.
	 *                  
	 * @param id Identifiant du niveau (position du niveau dans le fichier
	 *           niveaux.txt)
	 */
	public Niveau(String strNiveau, int id) {
		this.id = id;
		
		String[] strLignes = strNiveau.split("\n");
		String[] infoGenerales = strLignes[0].split(",");
		
		this.nbLignes = Integer.parseInt(infoGenerales[0]);
		this.nbColonnes = Integer.parseInt(infoGenerales[1]);
		
		this.positionPlots = new HashMap<Couleur,int[][]>();
		for (int i=1 ; i < strLignes.length ; i++) {
			String[] ligne = strLignes[i].split(";");
			Couleur couleur = strToCouleur(ligne[0]);
			int[] premierPlot = getCoordonnees(ligne[1]);
			int[] deuxiemePlot = getCoordonnees(ligne[2]);
			this.positionPlots.put( couleur, new int[][] {premierPlot , deuxiemePlot} );
		}
		
		this.reussi = false;
	}
	/**
	 * <b>Méthode peremttant de passer du nom de la couleur à l'instance
	 * de la classe Couleur corresondante.</b>
	 * 
	 * @param str Nom de la couleur.
	 * @return Instance de la classe Couleur correpsondante, default si 
	 *        l'instance n'existe pas.
	 */
	private Couleur strToCouleur(String str) {
		switch(str) {
		case "ROUGE": return Couleur.ROUGE;
		case "ORANGE": return Couleur.ORANGE;
		case "BLEU": return Couleur.BLEU;
		case "VERT": return Couleur.VERT;
		case "JAUNE": return Couleur.JAUNE;
		case "TURQUOISE": return Couleur.TURQUOISE;
		case "VIOLET": return Couleur.VIOLET;
		case "BORDEAUX": return Couleur.BORDEAUX;
		case "ROSE": return Couleur.ROSE;
		default: return null;
		}
	}
	
	/**
	 * <b>Méthode pour passer de la chaîne de caractères des coordonnées
	 * d'un plot au tableau des coordonnées entières.</b>
	 * 
	 * @param str Coordonnées d'un plot sous la forme "i,j"
	 * 
	 * @return Tableau des coordonnées entières [i, j]
	 */
	private int[] getCoordonnees(String str) {
		String[] coordStr = str.split(",");
		return new int[] { Integer.parseInt(coordStr[0]) , Integer.parseInt(coordStr[1]) };
	}
	
	// Getters
	public int getId() {
		return id;
	}
	
	public int getNbLignes() {
		return nbLignes;
	}
	
	public int getNbColonnes() {
		return nbColonnes;
	}
	
	public boolean getReussi() {
		return reussi;
	}
	
	public void setReussi(boolean b) {
		this.reussi = b;
	}
	
	// Autres méthodes
	public int[][] getPlotsCouleur(Couleur c) {
		return positionPlots.get(c);
	}
	
	public Set<Couleur> getCouleursNiveau() {
		return positionPlots.keySet();
	}
	
}
