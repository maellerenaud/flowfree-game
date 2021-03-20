package gloo.flowfree.model;

/**
 * <b>Classe représentant un plot du jeu (disque de couleur qu'il faut
 * relier au semblable par un tuyau.</b>
 * 
 * <p>Ce plot connait :
 * <ul>
 *     <li> La case qui le contient (son emplacement sur le plateau)
 *     <li> Sa couleur
 * </ul>
 * 
 * <p>Ce plot est connu :
 * <ul>
 *     <li> De la case qui le contient.
 * </ul>
 * 
 * @author Maëlle Renaud
 *
 */
public class Plot {
	
	/**
	 * <b>Case contenant le plot, donc emplacement du plot sur le 
	 * plateau.</b>
	 * <p>Cet attribut n'est pas modifié.
	 */
	private Case emplacement;
	
	/**
	 * <b>Couleur du plot</b>
	 * <p>Cet attribut n'est pas modifié.
	 * 
	 * @see Plot#getCouleur()
	 */
	private Couleur couleur;
	
	// Constructeur
	public Plot(Couleur couleur, Case emplacement) {
		this.couleur = couleur;
		this.emplacement = emplacement;
		this.emplacement.addPlot(this);
	}
	
	// Getters
	/**
	 * <b>Méthode appelée par la case pour connaître la couleur 
	 * de ce plot.</b>
	 * 
	 * @return La couleur de ce plot.
	 */
	public Couleur getCouleur() {
		return couleur;
	}
	
	// Autres méthodes
	/**
	 * <b>Méthode appelée lors de sélection de ce plot par le
	 * joueur pour créer un nouveau tuyau à partir de ce plot</b>
	 * 
	 * Demande à la couleur du plot de créer son nouveau tuyau 
	 * à partir de l'emplacement (la case) du plot.
	 * 
	 * @see Couleur#nouveauTuyau(Case)
	 * 
	 * @return Le nouveau tuyau de la couleur du plot démarrant à
	 *         l'emplacement du plot
	 */
	public Tuyau nouveauTuyau() {
		return couleur.nouveauTuyau(emplacement);
	}
	
	// Affichage
	/**
	 * <b>Méthode permettant d'afficher dans la console l'état actuel du plateau.</b>
	 * 
	 * <p> Un case contenant un plot est représentée par les premières lettres de la 
	 * couleur de ce plot.
	 * 
	 * <p> Demande sa représentation (ses premières lettres) à la couleur de ce plot.
	 * 
	 * @see Couleur#display()
	 * 
	 * @return Chaîne de caractère représentant la couleur du plot.
	 */
	public String display() {
		return couleur.display();
	}
}
