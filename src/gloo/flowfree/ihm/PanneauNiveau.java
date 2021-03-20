package gloo.flowfree.ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gloo.flowfree.control.Controleur;
import gloo.flowfree.model.Couleur;
import gloo.flowfree.model.Direction;


@SuppressWarnings( "serial" )
public class PanneauNiveau extends JPanel implements MouseListener, KeyListener {

    private static final float EPAISSEUR = 4;
    private boolean premierAffichage = true;
    private Controleur controleur;
    private int nbLignes;
    private int nbColonnes;
    private int coteCase;
    private int diametrePlot;
    private int demiRayon;
    private int largeurTuyau;
    private int arcRoundRect;
    private int[] selection;

    public PanneauNiveau( Controleur controleur ) {
        this.controleur = controleur;
        this.addMouseListener( this );
        this.addKeyListener( this );
        this.setFocusable(true);
    }

    private void calculeParametres() {
        nbLignes = controleur.getNbLignes();
        int cote_l = getSize().height / nbLignes;
        nbColonnes = controleur.getNbColonnes();
        int cote_c = getSize().width / nbColonnes;
        coteCase = cote_l < cote_c ? cote_l : cote_c;
        diametrePlot = coteCase * 2 / 3;
        demiRayon = diametrePlot / 4;
        largeurTuyau = coteCase / 4;
        arcRoundRect = coteCase / 4;
        premierAffichage = false;
    }
    
    public void paint(Graphics g) {
    	if (premierAffichage) calculeParametres();
    	
    	g.clearRect(0, 0, getSize().width, getSize().height);
    	
    	// Affichage des lignes de la grille de jeu
    	g.setColor( Color.BLACK );
    	for (int i=0 ; i <= nbLignes ; i++) {
    		g.drawLine(0, i * coteCase, nbColonnes * coteCase, i * coteCase);	// Lignes horizontales
    	}
    	for( int j = 0; j <= nbColonnes; ++j ) {
            g.drawLine(j * coteCase, 0, j * coteCase, nbLignes * coteCase);	// Lignes verticales
        }
    	
    	for( Couleur couleur : controleur.getCouleursNiveau() ) {
            setCouleurGraphique( g, couleur );
            // Affichage des plots
            int[][] coordPlots = controleur.getPositionPlots( couleur );
            for (int[] coordOnePlot : coordPlots) {
            	g.fillOval( coordOnePlot[1] * coteCase + demiRayon,
            			    coordOnePlot[0] * coteCase + demiRayon,
                            diametrePlot, diametrePlot );
            }
            // Affichage de l'éventuel tuyau
            if (controleur.hasTuyau(couleur)) {
            	paintDirections( g, controleur.getCoordDepart( couleur ), controleur.getDirections( couleur ));
            }
    	
    	    // Dessin de l'éventuel plot sélectionné
			if (selection != null) {
				g.setColor( Color.BLACK );
		        Graphics2D g2 = ( Graphics2D ) g;
		        Stroke s = g2.getStroke();
		        g2.setStroke( new BasicStroke( EPAISSEUR ));
		        g.drawOval( selection[1] * coteCase + demiRayon,
		        		selection[0] * coteCase + demiRayon,
		                diametrePlot, diametrePlot );
		        g2.setStroke( s );
			}
    	}
    }

    private void paintDirections( Graphics g, int[] coordDepart, ArrayList<Direction> directions ) {
        int x0 = coordDepart[1] * coteCase + coteCase / 2 - largeurTuyau / 2;
        int y0 = coordDepart[0] * coteCase + coteCase / 2 - largeurTuyau / 2;
        for( Direction dir : directions ) {
            int w = largeurTuyau;
            int h = largeurTuyau;
            int x1 = x0;
            int y1 = y0;
            switch( dir ) {
            case HAUT:
                y0 -= coteCase;
                y1 -= coteCase;
                h += coteCase;
                break;
            case BAS:
                h += coteCase;
                y1 += coteCase;
                break;
            case GAUCHE:
                x0 -= coteCase;
                x1 -= coteCase;
                w += coteCase;
                break;
            case DROITE:
                w += coteCase;
                x1 += coteCase;
                break;
            }
            g.fillRoundRect( x0, y0, w, h, arcRoundRect, arcRoundRect );
            x0 = x1;
            y0 = y1;
        }
    }

    private void setCouleurGraphique( Graphics g, Couleur c ) {
    	Color couleurPanneau = Color.WHITE;
        switch( c ) {
            case ROUGE : couleurPanneau = Color.RED; break;
            case ORANGE : couleurPanneau = Color.ORANGE; break;
            case BLEU : couleurPanneau = Color.BLUE; break;
            case VERT : couleurPanneau = Color.GREEN; break;
            case JAUNE : couleurPanneau = Color.YELLOW; break;
            case TURQUOISE : couleurPanneau = Color.CYAN; break;
            case ROSE : couleurPanneau = Color.PINK; break;
            case VIOLET : couleurPanneau = Color.MAGENTA; break;
            case BORDEAUX : couleurPanneau = new Color(157, 8, 8); break;
        };
        g.setColor(couleurPanneau);
    }

    @Override
    public void mouseClicked( MouseEvent e ) {
        if( controleur.selectionCase( e.getPoint().y / coteCase, e.getPoint().x / coteCase )) {
            selection = new int[] { e.getPoint().y / coteCase, e.getPoint().x / coteCase };
        }
        this.requestFocusInWindow();
        repaint();
    }

    @Override
    public void mousePressed( MouseEvent e ) {
        // nothing
    }

    @Override
    public void mouseReleased( MouseEvent e ) {
        // nothing
    }

    @Override
    public void mouseEntered( MouseEvent e ) {
        // nothing
    }

    @Override
    public void mouseExited( MouseEvent e ) {
        // nothing
    }
    
    @Override
    public void keyTyped( KeyEvent e ) {
        // nothing
    }

    @Override
    public void keyPressed( KeyEvent e ) {
        Direction direction;
        switch( e.getKeyCode() ) {
            case KeyEvent.VK_UP    : direction = Direction.HAUT; break;
            case KeyEvent.VK_DOWN  : direction = Direction.BAS; break;
            case KeyEvent.VK_LEFT  : direction = Direction.GAUCHE; break;
            case KeyEvent.VK_RIGHT : direction = Direction.DROITE; break;
            default                : direction = null; break;
        };
        if( direction == null ) return;
        boolean jeuFini = controleur.action( direction );
        if(jeuFini) {
            repaint();
            JOptionPane.showMessageDialog( this, "Vous avez gagné !" );
        }
        repaint();
    }

    @Override
    public void keyReleased( KeyEvent e ) {
        // nothing
    }
}
