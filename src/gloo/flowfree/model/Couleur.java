package gloo.flowfree.model;

import java.util.ArrayList;

/**
 * <b>Enumeration représentant une couleur du jeu, qui permet d'identifier
 * les plots à relier sur le plateau.</b>
 * 
 * <p> Chaque paire de plots à relier est associée à une couleur.
 * 
 * <p> La couleur connaît :
 * <ul>
 *     <li> Son tuyau courant
 * </ul>
 * 
 * <p> La couleur est connue :
 * <ul>
 *     <li> Des tuyaux qu'elle a formés, dont son tuyau courant (ils ne sont pas
 *          détruits au fur et à mesure, ils sont juste oubliés, plus aucune référence
 *          ne pointe vers eux).
 *     <li> Des 2 plots la partageant.
 * </ul>
 * 
 * <p> L'ensemble des instances de cette énumération est connue par la classe Niveau.
 * 
 * <p>Les instances de la classe Couleur sont les seules instances métiers qui ne changent 
 * pas d'un niveau à l'autre.
 * 
 * @author Maëlle Renaud
 *
 */
public enum Couleur {
	/**
	 * Représenté par la chaîne "R " dans la console.
	 */
	ROUGE,
	
	/**
	 * Représenté par la chaîne "O " dans la console.
	 */
	ORANGE,
	
	/**
	 * Représenté par la chaîne "Bl" dans la console.
	 */
	BLEU,
	
	/**
	 * Représenté par la chaîne "Ve" dans la console.
	 */
	VERT,
	
	/**
	 * Représenté par la chaîne "J " dans la console.
	 */
	JAUNE,
	
	/**
	 * Représenté par la chaîne "T " dans la console.
	 */
	TURQUOISE,
	
	/**
	 * Représenté par la chaîne "Rose" dans la console.
	 */
	ROSE,
	
	/**
	 * Représenté par la chaîne "Vi" dans la console.
	 */
	VIOLET,
	
	/**
	 * Représenté par la chaîne "Bo" dans la console.
	 */
	BORDEAUX;
	
	/**
	 * <b>Tuyau courant de cette couleur (démarrant d'un plot de
	 * cette couleur), null sinon.</b>
	 * 
	 * @see Couleur#hasTuyau()
	 * @see Couleur#nouveauTuyau(Case)
	 * @see Couleur#setNullTuyau()
	 */
	private Tuyau tuyau;
	
	// Autres méthodes
	/**
	 * <b>Méthode appelée par le controleur lors de chaque repaint de
	 * l'IHM pour déterminer si cette couleur a un tuyau à dessiner.</b>
	 * 
	 * @return Vrai si le tuyau de cette couleur est non null, faux sinon.
	 */
	public boolean hasTuyau() {
		return tuyau != null;
	}
	
	/**
	 * <b>Méthode appelée par le controleur lors de chaque repaint de
	 * l'IHM dans le cas où cette couleur a un tuyau pour récupérer la
	 * première case de ce tuyau.</b>
	 * 
	 * <p> Demande sa première case au tuyau de cette couleur.
	 * 
	 * <p> Les coordonnées de cette case serviront à démarrer le dessin
	 * du tuyau par l'IHM.
	 * 
	 * @see Tuyau#getCaseDepart()
	 * 
	 * @return La première case du tuyau de cette couleur.
	 */
	public Case getCaseDepart() {
		return tuyau.getCaseDepart();
	}
	
	/**
	 * <b>Méthode appelée par le controleur lors de chaque repaint de
	 * l'IHM dans le cas où cette couleur a un tuyau pour récupérer la
	 * liste des directions de ce tuyau.</b>
	 * 
	 * <p> Demande sa liste de directions au tuyau de cette couleur.
	 * 
	 * <p> Cette liste de directions servira à dessiner le tuyau.
	 * 
	 * @see Tuyau#getDirList()
	 * 
	 * @return La liste des directions du tuyau de cette couleur.
	 */
	public ArrayList<Direction> getDirections() {
		return tuyau.getDirList();
	}
	
	/**
	 * <b>Méthode appelée par un plot de cette couleur pour construire
	 * un nouveau tuyau à partir de son emplacement (sa case).</b>
	 * 
	 * <p> Si la couleur possédait déjà un tuyau, celui-ci est détruit, 
	 * c'est-à-dire que les cases le constituant l'oublient.
	 * 
	 * <p> Un nouveau tuyau est créé à partir de cette couleur et de la
	 * case départ donnée en paramètre. Ce nouveau tuyau devient le tuyau
	 * courant de cette couleur et est retourné par la méthode.
	 * 
	 * @param emplacement Case départ du tuyau à créer.
	 * @return Le nouveau tuyau créé à partir de cette case et de cette
	 *         couleur, nouveau tuyau courant de cette couleur.
	 */
	public Tuyau nouveauTuyau(Case emplacement) {
		if (tuyau != null) {
			tuyau.detruireTuyau();
		}
		this.tuyau = new Tuyau(emplacement, this);
		return tuyau;
	}
	
	/**
	 * <b>Méthode appelée pour chaque couleur par le controleur à la fin de 
	 * chaque action pour vérifier si les deux plots de la couleur sont
	 * reliés.</b>
	 * 
	 * <p> La couleur est complète si les 2 plots sont réliés, c'est-à-dire
	 * si le tuyau de cette couleur est complet.
	 * 
	 * <p> Si toutes les couleurs sont complètes, le niveau est réussi.
	 * 
	 * @see Tuyau#estComplet()
	 * 
	 * @return Vrai si la couleur a un tuyau et qu'il est complet, faux
	 *         sinon.
	 */
	public boolean estComplete() {
		return tuyau != null && tuyau.estComplet();
	}
	
	/**
	 * <b>Méthode appelée par le controleur quand le niveau est fermé par le joueur
	 * pour que cette couleur oublie son tuyau</b>
	 * 
	 * <p> Les couleurs doivent ainsi être "nettoyées" pour lancer un nouveau niveau
	 * sans tuyaux initiaux.
	 * 
	 * <p> Les instances de la classe Couleur sont les seules instances métiers 
	 * avec le controleur qui ne changent pas d'un niveau à l'autre.
	 * 
	 */
	public void setNullTuyau() {
		this.tuyau = null;
	}
	
	// Affichage
	/**
	 * <b>Méthode permettant d'afficher dans la console l'état actuel du plateau.</b>
	 * 
	 * <p> Chaque couleur est représentée par les premières lettres de son nom en
	 * français.
	 * 
	 * <p> Distinction de cas switch selon l'instance de la couleur.
	 * 
	 * @return Chaîne de caractère représentant cette couleur (premières lettres de
	 *         de son nom) pour l'afficher dans la console.
	 */
	public String display() {
		switch (this) {
		case ROUGE: return "R ";
		case ORANGE: return "O ";
		case BLEU: return "Bl";
		case VERT: return "Ve";
		case JAUNE: return "J ";
		case TURQUOISE: return "T ";
		case VIOLET: return "Vi";
		case BORDEAUX: return "Bo";
		case ROSE: return "Rose";
		default: return "  ";
		}
	}
}
