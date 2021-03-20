package gloo.flowfree.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * <b>Classe permettant de récupérer et de stocker tous les niveaux.</b>
 * 
 * <p>Permet d'afficher les informations sur tous les niveaux dans la
 * fenêtre d'accueil (avant qu'un niveau ne soit choisi) et de lancer 
 * un niveau.
 * 
 * <p>Les informations sur les niveaux sont stockées dans le fichier
 * texte ressources/niveaux.txt.
 * 
 * <p> On a une seule instance de TousNiveaux durant toute la session de
 * jeu.
 * 
 * <p>Cette instance connaît :
 * <ul>
 *     <li> Tous les niveaux disponibles.
 * </ul>
 * 
 * <p>Cette instance est connue :
 * <ul>
 *     <li> Du controleur.
 * </ul>
 * 
 * @see Niveau
 * 
 * @author Maëlle Renaud
 *
 */
public class TousNiveaux {
	
	/**
	 * <b>Attribut regroupant la liste de l'ensemble des niveaux,
	 * rangés dans l'ordre dans lequel ils apparaissent dans le fichier
	 * niveaux.txt.</b>
	 * 
	 * <p> Le niveau d'indice 0 est null (fichier commençent par une 
	 * ligne vide).
	 * 
	 * @see TousNiveaux#TousNiveaux()
	 */
	private Niveau[] listeNiveaux;
	
	/**
	 * <b>Dictionnaire dont les clés sont les différentes tailles de grille
	 * [nombre de lignes, nombre de colonnes] et les valeurs sont [identifiant 
	 * premier niveau de cette taille, nombre de niveaux de cette taille]</b>
	 * 
	 * <p> Grâce à la classe TreeMap, les niveaux sont rangées par ordre de taille
	 * de grille (comparaison du nombre de lignes, puis du nombre de colonnes).
	 * 
	 * <p> L'identifiant du niveau est sa position dans l'attribut listeNiveaux,
	 * ou de manière équivalente dans le fichier niveaux.txt.
	 * 
	 * <p> Attribut servant à organiser la page d'accueil pour choisir un niveau
	 * en fonction des tailles de grille disponibles.
	 * 
	 * @see TousNiveaux#getNiveauxParTaille()
	 */
	private TreeMap<int[],int[]> niveauxParTaille;
	
	/**
	 * <b>Constructeur permettant de lire le fichier niveaux.txt, générer
	 * tous les niveaux et initialiser les attributs de cette instance.</b>
	 * 
	 * <p> Méthode appelée une seule fois durant le jeu, lors de la construction
	 * du controleur. La lecture dans le fichier texte, assez coûteuse en temps, 
	 * n'est donc réalisée qu'une seule fois.
	 * 
	 * <p> Le fichier niveaux.txt présente tous les niveaux de la manière suivante :
	 * <br>&emsp; Niveau
     * <br>&emsp; 5,5				&emsp; (nombre de lignes, nombre de colonnes)
	 * <br>&emsp; ROUGE;0,0;4,1	    &emsp; (Couleur ; coordonnées 1er plot ; coordonnées 2ème plot)
     * <br>&emsp; VERT;0,2;3,1
     * <br>&emsp; BLEU;1,2;4,2
     * <br>&emsp; JAUNE;0,4;3,3
     * <br>&emsp; ORANGE;1,4;4,3
     * 
	 * <p> Les niveaux de même taille de grille sont les uns à la suite des autres.
	 * 
	 * <p> Etapes :
	 * <ul>
	 *     <li> Lecture du fichier niveaux.txt.
	 *     <li> Découpage de la chaîne de caractères par niveau.
	 *     <li> Pour chaque niveau, construction du niveau qui récupère les
	 *          informations de sa chaîne de caractères et demande de sa taille
	 *          de grille.
	 *     <li> Enregistrement du niveaux dans l'attribut listeNiveaux.
	 *     <li> Tous les tailles de grilles sont enregistrées comme clés
	 *          de l'attribut Treemap niveauxParTaille.
	 *     <li> La valeur correspondante est le couple (identifiant premier niveau de 
	 *          cette taille, nombre de niveaux de cette taille).
	 * </ul>
	 * 
	 * @see TousNiveaux#listeNiveaux
	 * @see TousNiveaux#niveauxParTaille
	 * @see Niveau#Niveau(String, int)
	 * @see Niveau#getNbLignes()
	 * @see Niveau#getNbColonnes()
	 */
	@SuppressWarnings("resource")
	public TousNiveaux() {
		// Lire tous les niveaux
		String strTousNiveaux = "";
		String workingDirectory = System.getProperty("user.dir");
		String absoluteFilePath = String.join(File.separator, workingDirectory, "ressources", "niveaux.txt");
		File niveaux = new File(absoluteFilePath);
		try {
			Scanner scan = new Scanner(niveaux);
			while (scan.hasNextLine()) {
		    	strTousNiveaux += scan.nextLine() + "\n";
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String[] listeStr = strTousNiveaux.split("\nNiveau\n");  // Le premier élément contient le String vide.
		int nbNiveaux = listeStr.length;
		
		// Générer les niveaux et calculer le nombre de niveaux de chaque taille (nbLignes, nbColonnes)
		this.niveauxParTaille = new TreeMap<int[], int[]>(Arrays::compare);
		this.listeNiveaux = new Niveau[nbNiveaux];
		
		for (int id=1 ; id < nbNiveaux ; id++) {	// Donc le niveau d'indice 0 est null.
			Niveau niveau = new Niveau(listeStr[id], id);
			this.listeNiveaux[id] = niveau;
			
			int[] taille = new int[] { niveau.getNbLignes() , niveau.getNbColonnes() };
			int[] infos = this.niveauxParTaille.get(taille);
			if (infos == null) {
				this.niveauxParTaille.put(taille, new int[] {id, 1});
			} else {
				int idNiveauDebut = infos[0];
				int nbNiveauxCetteTaille = infos[1];
				this.niveauxParTaille.put( taille, new int[] {idNiveauDebut , nbNiveauxCetteTaille + 1} );
			}
		}
	}
	
	// Getters
	/**
	 * <b>Méthode appelée pour l'IHM pour organiser la page d'accueil présentant
	 * les niveaux en fonction des tailles de grille disponibles.</b>
	 * 
	 * @see TousNiveaux#niveauxParTaille
	 * 
	 * @return L'attribut TreeMap niveauxParTaille
	 */
	public TreeMap<int[], int[]> getNiveauxParTaille() {
		return niveauxParTaille;
	}
	
	// Autres méthodes
	/**
	 * <b>Méthode permettant de lancer un niveau choisi par l'utilisateur.</b>
	 * 
	 * @see TousNiveaux#listeNiveaux
	 * 
	 * @param idNiveau Identifiant du niveau choisi (position dans l'attribut 
	 *        listeNiveaux = position dans le fichier niveaux.txt)
	 * 
	 * @return Le niveau choisi
	 */
	public Niveau getNiveau(int idNiveau) {
		return listeNiveaux[idNiveau];
	}
	
	/**
	 * <b>Méthode appelée pour savoir si à un niveau a été réussi par l'utilisateur
	 * durant la session de jeu</b>
	 * 
	 * <p> Appelée pour différencier les niveaux réussi des autres dans la fenêtre
	 * d'accueil.
	 * 
	 * <p> Demande au niveau demandé s'il est réussi.
	 * 
	 * @see Niveau#getReussi()
	 * 
	 * @param idNiveau Identifiant du niveau dont on veut savoir s'il a été réussi
	 *                 (position dans l'attribut listeNiveaux = position dans 
	 *                 le fichier niveaux.txt)
	 * @return Vrai si le niveau a été réussi durant la session de jeu, faux sinon.
	 */
	public boolean getReussi(int idNiveau) {
		return listeNiveaux[idNiveau].getReussi();
	}

}
