package gloo.flowfree.control;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import gloo.flowfree.ihm.FenetreNiveau;
import gloo.flowfree.model.Case;
import gloo.flowfree.model.Couleur;
import gloo.flowfree.model.Direction;
import gloo.flowfree.model.Niveau;
import gloo.flowfree.model.Plateau;
import gloo.flowfree.model.Plot;
import gloo.flowfree.model.TousNiveaux;
import gloo.flowfree.model.Tuyau;

/**
 * <b>Classe controleur faisant le lien entre l'IHM et les classes métiers.</b>
 * 
 * <p> On a une seule instance du controleur pendant toute la session de jeu.
 * 
 * <p>Ce controleur connaît :
 * <ul>
 *     <li> Un instance de la classe TousNiveaux
 *     <li> Son niveau courant choisi par le joueur
 *     <li> Son plateau
 *     <li> Son tuyau courant démarré par le plot choisi par le joueur
 * </ul>
 * 
 * <p>Ce controleur est connu :
 * <ul>
 *     <li> De l'IHM
 * </ul>
 * 
 * @author Maëlle Renaud
 *
 */
public class Controleur {
	
	/**
	 * <b>Classe répertoriant l'ensemble des niveaux pour les transmettre à  l'IHM 
	 * pour que l'utilisateur choisisse un niveau.</b>
	 * 
	 * <p>Attribut initialisé par le constructeur.
	 * 
	 * @see Controleur#Controleur()
	 */
	private TousNiveaux tousNiveaux;
	
	/**
	 * <b>Niveau courrant sélectionné par le joueur.</b>
	 * 
	 * <p>Attribut initialisé lors du lancement du niveau, après le choix du niveau
	 * par le joueur.
	 * 
	 * @see Controleur#lancerNiveau(int)
	 */
	private Niveau niveau;
	
	/**
	 * <b>Plateau courant associé au niveau courant.</b>
	 * 
	 * <p>Attribut initialisé lors du lancement d'un niveau, après le choix du niveau
	 * par le joueur.
	 * 
	 * @see Controleur#lancerNiveau(int)
	 */
	private Plateau plateau;
	
	/**
	 * <b>Tuyau créé lors de la sélection d'une case et aggrandi par l'appui
	 * sur les flèches du clavier.</b>
	 * 
	 * <p>Null tant qu'aucun plot n'a été sélectionné (il n'y a alors aucun tuyau
	 * dans le jeu).
	 * 
	 * @see Controleur#selectionCase(int, int)
	 * @see Controleur#action(Direction)
	 */
	private Tuyau tuyauCourant;
	
	// Constructeur
	/**
	 * <b>Constructeur appelé dans la méthode main de la classe Main pour commencer le jeu.</b>
	 * 
	 * <p>Appel du constructeur de TousNiveaux pour initialiser l'attribut tousNiveaux.
	 * 
	 * @see TousNiveaux#TousNiveaux()
	 */
	public Controleur() {
		this.tousNiveaux = new TousNiveaux();
	}
	
	// Autres méthodes
	
	    ///////////////////////////
		// Initialiser l'accueil //
		///////////////////////////
	/**
	 * <b>Méthode appelée par l'IHM pour récupérer les informations sur tous les niveaux
	 * pour que l'utilisateur choisisse un niveau.</b>
	 * 
	 * <p>Demande à la classe TousNiveaux les indices et le nombre de niveaux pour chaque taille de plateau.
	 * 
	 * @see TousNiveaux#getNiveauxParTaille()
	 * 
	 * @return Treemap dont les clés sont les tailles de plateau [nbLignes, nbColonnes] et les valeurs
	 *         [indice du premier niveau de cette taille, nombre de niveaux de cette taille]. L'indice
	 *         d'un niveau correspond à son indice dans le fichier niveaux.txt.
	 */
	public TreeMap<int[], int[]> getNiveauxParTaille() {
		return tousNiveaux.getNiveauxParTaille();
	}
	
	/**
	 * Méthode appelée par l'IHM pour déterminer si un niveau a été réussi par le joueur et l'afficher
	 * en vert le cas échéant.
	 * 
	 * @param idNiveau Identifiant du niveau
	 * 
	 * @return Vrai si le niveau demandé a été réussi par le joueur, faux sinon.
	 */
	public boolean niveauReussi(int idNiveau) {
		return tousNiveaux.getReussi(idNiveau);
	}
	
		///////////////////////////
		// Initialiser un niveau //
		///////////////////////////
	/**
	 * <b>Méthode appelée par l'IHM pour lancer un niveau.</b>
	 * 
	 * <p>Lancement du niveau
	 * <ul>
	 *     <li> Les spécificités du niveau sont récupérées (taille grille, positions plots).
	 *     <li> Les objets métiers (plateau, cases, plots) sont initialisés à partir des ces spécificités.
	 *     <li> Le niveau est lancé dans une nouvelle fenêtre.
	 * </ul>
	 * 
	 * @see TousNiveaux#getNiveau(int)
	 * @see Plateau#Plateau(Niveau)
	 * @see FenetreNiveau#FenetreNiveau(Controleur)
	 * 
	 * @param idNiveau Identifiant du niveau (indice dans le fichier niveaux.txt)
	 */
	public void lancerNiveau(int idNiveau) {
		this.niveau = tousNiveaux.getNiveau(idNiveau);
		this.plateau = new Plateau(niveau);
		this.tuyauCourant = null;
		new FenetreNiveau(this);
	}
	
	/**
	 * <b>Méthode appelée par l'IHM pour afficher l'id du niveau dans le titre de la fenêtre du niveau.</b>
	 * 
	 * <p>Demande son identifiant au niveau courant.
	 * 
	 * @see Niveau#getId()
	 * 
	 * @return L'identifiant du niveau courant (indice dans le fichier niveaux.txt)
	 */
	public int getIdNiveau() {
		return niveau.getId();
	}
	
	/**
	 * <b>Méthode appelée par l'IHM pour obtenir le nombre de lignes du niveau courant.</b>
	 * 
	 * <p>Demande son nombre de lignes au niveau courant.
	 * 
	 * @see Niveau#getNbLignes()
	 * 
	 * @return Le nombre de lignes du niveau courant.
	 */
	public int getNbLignes() {
		return niveau.getNbLignes();
	}
	
	/**
	 * <b>Méthode appelée par l'IHM pour obtenir le nombre de colonnes du niveau courant.</b>
	 * 
	 * <p>Demande son nombre de colonnes au niveau courant.
	 * 
	 * @see Niveau#getNbColonnes()
	 * 
	 * @return Le nombre de colonnes du niveau courant.
	 */
	public int getNbColonnes() {
		return niveau.getNbColonnes();
	}
	
		////////////////////////
		// Dessiner un niveau //
		////////////////////////
	/**
	 * <b>Méthode appelée par l'IHM pour obtenir obtenir toutes les couleurs du niveaux pour 
	 * dessiner les plots et tuyaux correspondants.</b>
	 * 
	 * <p>Demande son ensemble de couleurs au niveau courant.
	 * 
	 * @see Niveau#getCouleursNiveau()
	 * 
	 * @return L'ensemble des instances de l'enumération Couleur utilisées dans le niveau courant.
	 */
	public Set<Couleur> getCouleursNiveau() {
		return niveau.getCouleursNiveau();
	}
	
	/**
	 * <b>Méthode appelée par l'IHM pour obtenir obtenir les coordonnées des plots de la couleur passée en paramètre
	 * pour les dessiner.</b>
	 * 
	 * <p>Demande la position des plots de la couleur demandée au niveau courant.
	 * 
	 * @see Niveau#getPlotsCouleur(Couleur)
	 * 
	 * @param c Couleur dont on cherche la position des plots
	 * 
	 * @return Tableau d'entiers avec les coordonnées [ligne,colonne] des cases des 2 plots de la couleur c
	 */
	public int[][] getPositionPlots(Couleur c) {
		return niveau.getPlotsCouleur(c);
	}
	
	/**
	 * <b>Méthode appelée par l'IHM pour savoir s'il y a un tuyau à dessiner pour la couleur passée en paramètre.</b>
	 * 
	 * <p>Demande à la couleur passée en paramètre si elle a un tuyau démarré.
	 * 
	 * @see Couleur#hasTuyau()
	 * 
	 * @param c Couleur dont on veut savoir si elle a un tuyau
	 * 
	 * @return vrai si la Couleur c a un tuyau, faux sinon
	 */
	public boolean hasTuyau(Couleur c) {
		return c.hasTuyau();
	}
	
	/** 
	 * <b>Méthode appelée par l'IHM pour connaître les coordonnées du premier plot de la Couleur
	 * passée en paramètre.</b>
	 * 
	 * <p> Méthode appelée seulement si cette Couleur a un tuyau.
	 * <p>Demande à cette Couleur la Case de départ de son tuyau.
	 * Demande au Plateau les coordonnées de cette Case.
	 * 
	 * @see Couleur#getCaseDepart()
	 * @see Plateau#findPositionCase(Case)
	 * 
	 * @param c Couleur dont on cherche les coordonnées du premier plot.
	 * 
	 * @return Les coordonnées [ligne,colonne] du premier plot (selon l'ordre inscrit dans le fichier niveaux.txt)
	 * 		   de la couleur c.
	 */
	public int[] getCoordDepart(Couleur c) {
		Case caseDepart = c.getCaseDepart();
		return plateau.findPositionCase(caseDepart);
	}
	
	/**
	 * <b>Méthode appelée par l'IHM pour obtenir la liste des directions constituant le tuyau de la Couleur
	 * passée en paramètre pour dessiner ce tuyau.</b>
	 * 
	 * <p>Méthode appelée uniquement si cette Couleur a un tuyau.
	 * <p>Demande à cette Couleur les directions constituant son tuyau.
	 * 
	 * @see Couleur#getDirections()
	 * 
	 * @param c Couleur dont on chercher à dessiner le tuyau
	 * @return Liste de Directions successives constituant le tuyau de la Couleur c.
	 */
	public ArrayList<Direction> getDirections(Couleur c) {
		return c.getDirections();
	}
	
		///////////
		// Jouer //
	    ///////////
	/**
	 * <b>Méthode appelée par l'IHM quand le joueur clique sur une case.</b>
	 * 
	 * <p>Demande au Plateau si la case à cette position a un plot.
	 * <ul>
	 * <li> Si cette case a un plot :
	 * 	   <ul>
	 *     <li> Si un tuyau de a même couleur que le plot existe, il est détruit.
	 *     <li> Le plot initialise un nouveau tuyau de sa couleur à partir de 
	 * 	   sa case.
	 *     <li> Ce nouveau tuyau est enregistré comme tuyau courant pour le controleur.
	 *     <li> L'état actuel du Plateau est affiché dans la console.
	 *     <li> La méthode renvoie true.
	 *     </ul>
	 * <li> Sinon, la méthode renvoie false.
	 * </ul>
	 * 
	 * @see Plateau#getPlot(int, int)
	 * @see Plot#nouveauTuyau()
	 * @see Plateau#display()
	 * 
	 * @param i Identifiant de la ligne de la Case à sélectionner
	 * @param j Identifiant de la colonne de la Case à sélectionner
	 * 
	 * @return Vrai si la case contient un plot (la case a bien été sélectionnée), faux sinon.
	 */
	public boolean selectionCase(int i, int j) {
		Plot plotCourant = plateau.getPlot(i, j);
		if (plotCourant != null) {
			tuyauCourant = plotCourant.nouveauTuyau();
			System.out.println(plateau.display());
			return true;
		};
		return false;
	}
	
	/**
	 * <b>Méthode appelée par l'IHM quand le joueur appuie sur une flèche directionnelle du clavier.</b>
	 * 
	 * <p>S'il n'y a pas de tuyau courant, rien ne se passe.
     * <p>S'il y a un tuyau courant :
     * <ul>
     *     <li> Celui-ci doit s'agrandir, s'il le peut, dans la direction indiquée en argument.
     *     <li> L'état actuel du plateau est affiché dans la console.
     *     <li> On teste si le jeu est terminé : si tous les plots sont reliés et si toutes les cases sont
     *          utilisées par un plot ou un tuyau. Si oui, la méthode indique au niveau courant qu'il a 
     *          été réussi et renvoie true. Sinon, false est renvoyé.
     * </ul>
     * 
     * @see Tuyau#modifier(Direction)
     * @see Plateau#display()
     * @see Controleur#couleursCompletes()
     * @see Plateau#plateauComplet()
     * @see Niveau#setReussi(boolean)
	 * 
	 * @param dir Direction de la progression demandée par le joueur.
	 * @return Vrai si cette action a permis de terminer le niveau, faux sinon.
	 */
	public boolean action(Direction dir) {
		if (tuyauCourant != null) {
			tuyauCourant.modifier(dir);
			System.out.println(plateau.display());
			boolean niveauReussi = couleursCompletes() && plateau.plateauComplet();
			if (niveauReussi) {
				niveau.setReussi(true);
			}
			return niveauReussi;
			}
		return false;
	}
	
	/**
	 * <b>Méthode privée appelée par le controleur pour vérifier si le niveau est
	 * réussi dans la configuration courante, c'est-à-dire si tous les plots sont
	 * reliés (les couleurs ont des tuyaux complets).</b>
	 * 
	 * <p>Demande au niveau courant l'ensemble des couleurs utilisées, puis demande à
	 * chaque couleur si elle est complète.
	 * 
	 * @see Niveau#getCouleursNiveau()
	 * @see Couleur#estComplete()
	 * 
	 * @return Vrai si toutes les couleurs du niveau courant ont des tuyaux complets, 
	 *         faux sinon.
	 */
	private boolean couleursCompletes() {
		for (Couleur c : niveau.getCouleursNiveau()) {
			if (!c.estComplete()) return false;
		}
		return true;
	}
	
		/////////////////////////////////////////////////////////
		// Réinitialiser les composants pour changer de niveau //
		/////////////////////////////////////////////////////////
	/**
	 * <b>Méthode appelée par l'IHM quand la fenêtre du niveau est fermée pour supprimer les
	 * tuyaux associés à chaque instance de la classe Couleur.</b>
	 * 
	 * <p>Les instances de la classe Couleur sont les seules instances métiers 
	 * qui ne changent pas d'un niveau à l'autre.
	 * <p>Les attributs tuyaux des couleurs sont réinitialisés pour lancer un nouveau niveau.
	 * 
	 * @see Couleur#setNullTuyau()
	 */
	public void nettoyerCouleurs() {
		for (Couleur c : Couleur.values()) {
			c.setNullTuyau();
		}
	}
	
}
