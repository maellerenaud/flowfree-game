package gloo.flowfree.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gloo.flowfree.control.Controleur;


@SuppressWarnings( "serial" )
public class FenetreNiveau extends JFrame implements ActionListener {

    public static final int COTE_FENETRE = 500;
    private static final int HAUTEUR_BARRE_FENETRE = 20;
    private static final int HAUTEUR_BARRE_INFOS = 45;
    private Controleur controleur;
    private JButton aide;
    private JButton retourAccueil;
    

    public FenetreNiveau( Controleur controleur ) {
        this.controleur = controleur;

        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.setPreferredSize( new Dimension( COTE_FENETRE, COTE_FENETRE + HAUTEUR_BARRE_FENETRE + HAUTEUR_BARRE_INFOS));
        this.setTitle( "FreeFlow - Niveau " + controleur.getIdNiveau() );
        
        this.add( new PanneauNiveau( controleur ), BorderLayout.CENTER);
        
        // Ajouter barre d'informations
        JPanel barreInfos = new JPanel( new FlowLayout(FlowLayout.CENTER) );
        Font font = new Font("Courrier", Font.BOLD, 20);
        
        	//Bouton aide
        this.aide = new JButton("Aide");
        aide.setFont(font);
        barreInfos.add(aide);
        aide.addActionListener(this);
        
        	// Bouton de retour
        this.retourAccueil = new JButton("Retour à l'accueil");
        retourAccueil.setFont(font);
        barreInfos.add(retourAccueil);
        retourAccueil.addActionListener(this);
        
        this.add(barreInfos, BorderLayout.PAGE_END);

        this.pack();
        this.setVisible( true );
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// Actions réalisées quand on clique sur un des boutons
		if (e.getSource() == aide) {
			String aide = "But du jeu : relier les plots de même couleur avec des tuyaux !\n\nCliquez sur un plot et utilisez les flèches du clavier pour construire\nle tuyau.\nVous pouvez revenir en arrière quand vous vous êtes trompés\nou réinitialiser le tuyau en cliquant sur un des plots de la couleur.\n\nToutes les cases doivent être remplies par un plot ou un tuyau.";
			JOptionPane.showMessageDialog( this, aide, "Aide", JOptionPane.INFORMATION_MESSAGE );
		} else if (e.getSource() == retourAccueil) {
			controleur.nettoyerCouleurs();
			new FenetreAccueil ( controleur );
			this.dispose();
		}
		
	}

}
