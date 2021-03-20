package gloo.flowfree.main;

import javax.swing.SwingUtilities;

import gloo.flowfree.control.Controleur;
import gloo.flowfree.ihm.FenetreAccueil;

/**
 * <b>Classe implémentant l'interface Runnable et contenant une 
 * méthode main, à exécuter pour lancer le jeu FlowFree</b>
 * 
 * @author Maëlle Renaud
 *
 */
public class Main implements Runnable {

	public static void main( String[] args ) {
        SwingUtilities.invokeLater( new Main() );
	}
	
	/**
	 * <b>Méthode permettant de lancer le jeu.</b>
	 * 
	 * <p>Création d'un controleur (unique pendant toute la session
	 * de jeu) et d'une fenêtre d'accueil présentant les niveaux
	 * disponibles.
	 * 
	 * @see Controleur
	 * @see Controleur#Controleur()
	 * @see FenetreAccueil
	 * @see FenetreAccueil#FenetreAccueil(Controleur)
	 */
    @Override
    public void run() {
        new FenetreAccueil( new Controleur() );
    }
}
