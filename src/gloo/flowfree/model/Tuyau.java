package gloo.flowfree.model;

import java.util.ArrayList;

/**
 * <b>Classe représentant un tuyau, entité qui relie 2 plots d'une même
 * couleur.</b>
 * 
 * Le tuyau connait :
 * <ul>
 *     <li> La liste des cases le représentant
 *     <li> La liste des directions entre 2 cases successives le représentant.
 *     <li> Sa couleur
 * </ul>
 * 
 * Le tuyau est connu :
 * <ul>
 *     <li> De sa couleur
 *     <li> De chacune des cases qu'il contient
 * </ul>
 * 
 * @author Maëlle Renaud
 *
 */
public class Tuyau {
	
	/**
	 * <b>Liste des cases constituant le tuyau</b>
	 */
	private ArrayList<Case> casesList;
	
	/**
	 * <b>Liste des directions successives entre 2 cases constituant
	 * le tuyau</b>
	 * 
	 * @see Tuyau#getDirList()
	 */
	private ArrayList<Direction> dirList;
	
	/**
	 * <b>Couleur du tuyau, donc du plot de départ du tuyau.</b>
	 * 
	 * <p> Attribut non modifiable.
	 * 
	 * @see Tuyau#getCouleur()
	 */
	private Couleur couleur;
	
	// Constructeur
	/**
	 * <b>Constructeur appelée par la couleur pour récréer un tuyau
	 * lorsqu'un des plots de cette couleur est sélectionné.</b>
	 * 
	 * <p> La case départ enregistre ce tuyau comme attribut.
	 * 
	 * @see Case#setTuyau(Tuyau)
	 * 
	 * @param debut Case de départ du tuyau, contient un plot de la
	 *              couleur du tuyau.
	 * 
	 * @param couleur Couleur du tuyau.
	 */
	public Tuyau(Case debut, Couleur couleur) {
		this.casesList = new ArrayList<Case>();
		this.casesList.add(debut);
		debut.setTuyau(this);
		this.dirList = new ArrayList<Direction>();
		this.couleur = couleur;
	}
	
	// Getters
	public ArrayList<Direction> getDirList() {
		return dirList;
	}
	
	public Couleur getCouleur() {
		return couleur;
	}
	
	// Autres méthodes
	public Case getCaseDepart() {
		return casesList.get(0);
	}
	
	/**
	 * <b>Méthode appelée par la case pour savoir si elle constitue le 
	 * premier plot du tuyau.</b>
	 * 
	 * <p> Si la case contient le premier plot du tuyau, elle ne contient pas
	 * le plot final à atteindre, donc la case n'accepte pas de rejoindre
	 * le tuyau.
	 * 
	 * @param emplacement Case dont on veut savoir si elle début le tuyau.
	 * 
	 * @return Vrai si la case est la première du tuyau, faux sinon.
	 */
	public boolean debuteTuyau(Case emplacement) {
		return emplacement == casesList.get(0);
	}
	
	/**
	 * <b>Méthode appelée par le controleur lorsque ce tuyau est le tuyau
	 * courant et que l'utilisateur souhaite le faire progresser.</b>
	 * 
	 * <p> Demande à la dernière case sa case vosiine dans la direction
	 * passée en paramètre.
	 * 
	 * <ul>
	 *     <li> Si le tuyau est déjà complet ou que la direction demandée
	 *          fait sortir du plateau (case voisine null), il ne se passe
	 *          rien.
	 *     <li> Sinon, on regarde si la modification correspond à un retour
	 *          en arrière (si la case visée est l'avant dernière case du tuyau).
	 *          <ul>
	 *              <li> Si c'est un retour en arrière, on efface la dernière case
	 *                   et la dernière direction constituant le tuyau.
	 *              <li> Sinon, on demande à la case visée si elle accepte de faire
	 *                   partie du tuyau. Si oui, on ajoute cette case et la direction
	 *                   de propagation demandée aux attributs du tuyau.
	 *          </ul>
	 * </ul>
	 * 
	 * @see Case#getCaseVoisine(Direction)
	 * @see Tuyau#estComplet()
	 * @see Case#retirerTuyau()
	 * @see Case#accepteTuyau(Tuyau)
	 * 
	 * @param dir Direction de progression du tuyau demandée par l'utilisateur.
	 */
	public void modifier(Direction dir) {
		Case derniereCase = casesList.get(casesList.size() - 1);
		Case prochaineCase = derniereCase.getCaseVoisine(dir);
		int indexAvtDerr = casesList.size() - 2;
		
		if (!estComplet() && prochaineCase != null) {
			if (indexAvtDerr >= 0 && prochaineCase == casesList.get(indexAvtDerr)) {	// Effacement de la dernière case du tuyau si retour en arrière
				derniereCase.retirerTuyau();
				this.casesList.remove(indexAvtDerr + 1);
				this.dirList.remove(indexAvtDerr);
			}
			else if (prochaineCase.accepteTuyau(this)) {
				this.casesList.add(prochaineCase);
				this.dirList.add(dir);
			}
		}
	}
	
	/**
	 * <b>Méthode pour que chacune des cases du tuyau l'oublie et puisse
	 * être utilisée dans un aure tuyau.</b>
	 * 
	 * <p> Méthode appelée par la couleur lorsque le joueur clique sur
	 * un plot de cette couleur alors que ce tuyau est enregistré comme
	 * tuyau courant.
	 * 
	 * @see Case#retirerTuyau()
	 */
	public void detruireTuyau() {
		for (Case caseTuyau : casesList) {
			caseTuyau.retirerTuyau();
		}
	}
	
	/**
	 * <b>Méthode indiquant si le tuyau est complet, c'est-à-dire qu'il
	 * est non trivial (départ != arrivée) et la dernière case contient
	 * un plot.</b>
	 * 
	 * <p> Vu la méthode Case.accepteTuyau(Tuyau), si la dernière case contient
	 * un plot, c'est forcément le deuxième de la couleur du tuyau.
	 * 
	 * <p> Méthode appelée par la couleur pour savoir si elle est complète (si
	 * son tuyau courant est complet) et déterminer si le niveau est réussi.
	 * 
	 * @see Case#getPlot()
	 * 
	 * @return Vrai si le tuyau est non trivial (départ != arrivée) et que la 
	 *         dernière case contient un plot.
	 */
	public boolean estComplet() {
		Case depart = casesList.get(0);
		Case arrivee = casesList.get(casesList.size() - 1);
		return depart!=arrivee && arrivee.getPlot() != null;
	}
	
	// Affichage
	/**
	 * <b>Méthode permettant d'afficher dans la console l'état actuel du plateau.</b>
	 * 
	 * <p> Un case contenant un tuyau est représentée par les premières lettres de la 
	 * couleur de ce tuyau.
	 * 
	 * <p> Demande sa représentation (ses premières lettres) à la couleur de ce tuyau.
	 * 
	 * @see Couleur#display()
	 * 
	 * @return Chaîne de caractère représentant la couleur du tuyau.
	 */
	public String display() {
		return couleur.display();
	}
}
