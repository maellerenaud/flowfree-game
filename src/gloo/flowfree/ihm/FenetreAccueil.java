package gloo.flowfree.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gloo.flowfree.control.Controleur;

@SuppressWarnings("serial")
public class FenetreAccueil extends JFrame  implements MouseListener {
	
	public int largeurFenetre = 400;
	public int hauteurFenetre;
    private int hauteurBarreFenetre = 20;
    private static int coteCarre = 50;
    private static int paddingGlobal = 80;
    private static int paddingCarre = 10;
    private Controleur controleur;
    private TreeMap<int[],int[]> niveauxParTaille;

    public FenetreAccueil( Controleur controleur ) {
        this.controleur = controleur;
        this.niveauxParTaille = controleur.getNiveauxParTaille();
        this.hauteurFenetre = niveauxParTaille.keySet().size() * 2 * coteCarre + paddingGlobal * 2;

        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.setPreferredSize( new Dimension( largeurFenetre, hauteurFenetre ));
        this.setTitle( "FreeFlow" );
        this.addMouseListener(this);
        
        drawPanel();
        
        this.pack();
        this.setVisible( true );
    }
    
    private void drawPanel() {
    	JPanel panel = new JPanel();
    	panel.setLayout(null);
    	
    	JLabel labelSubtitle = new JLabel("Choisissez votre niveau !", JLabel.CENTER);
    	labelSubtitle.setBounds(0, 0, largeurFenetre, paddingGlobal);
    	Font font = new Font("Courrier", Font.BOLD, 20);
    	labelSubtitle.setFont(font);
    	panel.add(labelSubtitle);
    	
    	int y = paddingGlobal;
    	
    	for (int[] taille : niveauxParTaille.keySet()) {
    		
    		JLabel labelTaille = new JLabel(taille[0] + "x" + taille[1], JLabel.CENTER);
			labelTaille.setBounds(paddingGlobal, y, coteCarre, coteCarre);
			labelTaille.setFont(font);
	        panel.add(labelTaille);
	        y += coteCarre ;
	        
	        int[] infos = niveauxParTaille.get(taille);
	        int nbNiveaux = infos[1];
    		for (int id=1; id <= nbNiveaux; id ++) {
				int xRect = (id - 1) * (coteCarre + paddingCarre) + paddingGlobal;
				JLabel label = new JLabel(Integer.toString(id), JLabel.CENTER);
		        label.setBounds(xRect, y, coteCarre, coteCarre);
		        label.setFont(font);
		        int idNiveau = infos[0] + id - 1;
		        if (controleur.niveauReussi(idNiveau)) {
		        	label.setBackground(Color.GREEN);
		        } else {
		        	label.setBackground(Color.RED);
		        }
		        label.setOpaque(true);
		        panel.add(label);
    		}
    		
    		y += coteCarre ;
    	}
    	
        this.add(panel);
    }
    
    
    @Override
	public void mouseClicked(MouseEvent e) {
    	
    	int x = e.getPoint().x - paddingGlobal;
    	int y = e.getPoint().y - hauteurBarreFenetre - paddingGlobal + 
    			10;
    	int rangTaille = y / (2*coteCarre + 10);
    	
    	if (y > 0 && y % (2*coteCarre + 10) >= coteCarre) {
    		int rangNiveau = x / (coteCarre + paddingCarre);
    		if ( x > 0 && x % (coteCarre + paddingCarre) <=  coteCarre ) {
    			int[] taille = ( new ArrayList<int[]>(niveauxParTaille.keySet()) ).get(rangTaille);
    			if ( rangNiveau < niveauxParTaille.get(taille)[1] ) {		// niveauxParTaille.get(taille)[1] désigne le nombre de niveaux de cette taille
    				int idNiveau = niveauxParTaille.get(taille)[0] + rangNiveau; // niveauxParTaille.get(taille)[0] désigne l'indice du premier niveau de cette taille
    			controleur.lancerNiveau(idNiveau);
    			this.dispose();
    			}
    		}
    	}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// nothing
	}

}
