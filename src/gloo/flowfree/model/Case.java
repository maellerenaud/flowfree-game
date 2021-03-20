package gloo.flowfree.model;

/**
 * <b>Classe représentant une case du plateau, un carré dans la grille de jeu
 * affichée.</b>
 * 
 * La case peut être dans 4 états :
 * <ul>
 *     <li> Vide
 *     <li> Contenant un plot
 *     <li> Contenant un tuyau
 *     <li> Contenant un plot et un tuyau
 * </ul>
 * 
 * La case connait :
 * <ul>
 *     <li> Le plateau qui la contient
 *     <li> Le plot éventuel qu'elle contient
 *     <li> Le tuyau éventuel qu'elle contient
 * </ul>
 * 
 * La case est connue :
 * <ul>
 *     <li> Du plateau qui la contient
 *     <li> Du plot éventuel qu'elle contient
 *     <li> Du tuyau éventuel qu'elle contient
 * </ul>
 * 
 * @author Maëlle Renaud
 * 
 */
public class Case {
	
	/**
	 * <b>Plateau contennant cette case.</b>
	 */
	private Plateau plateau;
	
	/**
	 * <b>Plot éventuel que contient cette case, null sinon.</b>
	 * 
	 * <p> Eventuellement initialisé par le plot lui-même lors de sa construction 
	 * au lancement d'un niveau. Non modifié ensuite.
	 * 
	 * @see Case#addPlot(Plot)
	 * @see Case#getPlot()
	 */
	private Plot plot;
	
	/**
	 * <b>Tuyau éventuel que contient cette case, null sinon.</b>
	 * 
	 * <p> Modifiable au cours de la partie.
	 * 
	 * @see Case#setTuyau(Tuyau)
	 * @see Case#accepteTuyau(Tuyau)
	 * @see Case#retirerTuyau()
	 */
	private Tuyau tuyau;
	
	// Constructeur
	/**
	 * <b>Constructeur appelé par le plateau pour initaliser ses éléments.</b>
	 * 
	 * <p>Initialise uniquement l'attribut plateau, seul attribut nécessairement
	 * non null.
	 * 
	 * @param plateau Plateau qui contient cette case.
	 */
	public Case(Plateau plateau) {
		this.plateau = plateau;
	}
	
	// Getters et setters
	/**
	 * <b>Méthode appelée par le controleur lorsque le joueur clique sur une case.</b>
	 * 
	 * <p>La case est sélectionnée uniquement si elle contient un plot. Elle peut
	 * alors servir de point de départ à un nouveau tuyau.
	 * 
	 * @see Case#plot
	 * 
	 * @return Le plot éventuel contenu dans la case, null sinon.
	 */
	public Plot getPlot() {
		return plot;
	}
	
	/**
	 * <b>Setter de l'attribut tuyau de cette case.</b>
	 * 
	 * <p>Il n'y a pas besoin de faire de vérifications sur la disponibilité de cette case
	 * car cette méthode n'est appelée que lors de la création d'un tuyau initiée par le plot
	 * contenu dans cette case.
	 * 
	 * @see Case#tuyau
	 * 
	 * @param tuyau Tuyau nouvellement formé et ne contenant que cette case.
	 */
	public void setTuyau(Tuyau tuyau) {
		this.tuyau = tuyau;
	}
	
	// Autres méthodes
	/**
	 * <b>Setter de l'attribut plot, appelé par le plot lui-même lors de sa création 
	 * au lancement du niveau.</b>
	 * 
	 * @see Case#plot
	 * 
	 * @param plot Plot que contient la case
	 */
	public void addPlot(Plot plot) {
		this.plot = plot;
	}
	
	/**
	 * <b>Méthode appelée pour faire progresser le tuyau courant dans une direction 
	 * donnée.</b>
	 * 
	 * <p>Demande au plateau sa case voisine dans la direction demandée.
	 * 
	 * @see Plateau#getMaCaseVoisine(Case, Direction)
	 * 
	 * @param dir Direction de progression du tuyau, direction de la case voisine
	 *            recherchée par rapport à cette case.
	 * @return La case située dans la direction donné par rapport à cette case, null
	 *         si elle n'existe pas.
	 */
	public Case getCaseVoisine(Direction dir) {
		return plateau.getMaCaseVoisine(this, dir);
	}
	
	/**
	 * <b>Méthode appelée par le tuyau lui-même lors de sa progression pour demander à la 
	 * case si elle accepte d'être la prochaine case de ce tuyau.</b>
	 * 
	 * <p>Cette case accepte de participer au tuyau si :
	 * <ul>
	 *     <li> Elle est innocupée, c'est-à-dire qu'elle n'a ni plot ni tuyau.
	 *     <li> OU
	 *     <li> Elle contient le second plot (donc l'arrivée) de ce tuyau. Dans ce cas, il
	 *          faut vérifier que la case contient un plot, que ce plot est de la même couleur
	 *          que le tuyau courant et que ce plot ne débute pas le tuyau (ce n'est pas le
	 *          premier plot).
	 * </ul>
	 * <p> Dans ce cas, ce tuyau est enregistré dans l'attribut tuyau de la case et la méthode
	 * renvoie true.
	 * 
	 * <p> Dans le cas contraire, la méthode renvoie false.
	 * 
	 * @see Plot#getCouleur()
	 * @see Tuyau#getCouleur()
	 * @see Tuyau#debuteTuyau(Case)
	 * 
	 * @param tuyau Le tuyau courant demandant à cette case de participer à sa construction.
	 * @return Vrai si la case accepte d'être la prochaine case du tuyau, faux sinon.
	 */
	public boolean accepteTuyau(Tuyau tuyau) {
		boolean isInoccupee = this.plot == null && this.tuyau == null;
		boolean isSecondPlot = this.plot != null && this.plot.getCouleur() == tuyau.getCouleur() && !tuyau.debuteTuyau(this);
		if (isInoccupee || isSecondPlot) {
			this.tuyau = tuyau;
			return true;
		}
		return false;
	}
	
	/**
	 * <b>Méthode appelée par le plateau pour vérifier s'il est complet.</b>
	 * 
	 * @return Vrai si la case est utilisée par un plot ou un tuyau, faux sinon.
	 */
	public boolean estOccupee() {
		return plot != null || tuyau != null;
	}
	
	/**
	 * <b>Méthode permettant à la case d'oublier son tuyau et d'être à nouveau disponible
	 * pour accepter un nouveau tuyau.</b>
	 * 
	 * <p> Setter du tuyau à null.
	 * 
	 * <p> Méthode appelée par le tuyau lui-même lorsque le joueur retire la dernière
	 * case du tuyau en revenant en arrière ou lorsque le joueur supprime ce tuyau en
	 * cliquant sur un des plots de la couleur.
	 * 
	 * @see Case#tuyau
	 */
	public void retirerTuyau() {
		this.tuyau = null;
	}
	
	// Affichage
	/**
	 * <b>Méthode permettant d'afficher dans la console l'état actuel du plateau.</b>
	 * 
	 * <p> Demande sa représentation au plot ou au tuyau contenu dans cette case
	 * le cas échéant.
	 * 
	 * <p>Chaque case est repésentée par :
	 * <ul>
	 *     <li> les premières lettres de la couleur du plot, si elle contient un plot
	 *     <li> les premières lettres de la couleur du tuyau, si elle contient un tuyau
	 *          et ne contient pas de plot
	 *     <li> 2 espaces sinon
	 * </ul>
	 * 
	 * @see Plot#display()
	 * @see Tuyau#display()
	 * 
	 * @return Chaîne de caractère représentant la couleur éventuelle contenue dans la case, 
	 *         2 espaces sinon.
	 */
	public String display() {
		if (plot != null) {
			return plot.display();
		} else if (tuyau != null) {
			return tuyau.display();
		} else {
			return "  ";
		}
	}

}
















