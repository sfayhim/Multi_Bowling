package bowling;

/**
 * Classe représentant une partie de bowling pour un seul joueur.
 * Gère les 10 tours d'une partie de bowling avec les règles des strikes et spares.
 */
public class PartieMonoJoueur {
    
    private Tour premierTour;
    private Tour tourCourant;
    private boolean partieTerminee = false;
    
    /**
     * Constructeur - initialise une nouvelle partie.
     */
    public PartieMonoJoueur() {
        // Construire la chaîne de tours (du dernier au premier)
        Tour dernierTour = new DernierTour();
        Tour tour = dernierTour;
        
        // Créer les 9 premiers tours en chaînage inversé
        for (int i = 9; i >= 1; i--) {
            tour = new Tour(i, tour);
        }
        
        premierTour = tour;
        tourCourant = premierTour;
    }
    
    /**
     * Enregistre un lancer.
     * 
     * @param nombreDeQuillesAbattues nombre de quilles abattues (0-10)
     * @return true s'il faut une autre boule pour ce tour, false sinon
     */
    public boolean enregistreLancer(int nombreDeQuillesAbattues) {
        if (partieTerminee) {
            throw new IllegalStateException("La partie est terminée");
        }
        
        tourCourant.enregistreLancer(nombreDeQuillesAbattues);
        
        if (tourCourant.estTermine()) {
            if (tourCourant.numero == 10) {
                partieTerminee = true;
                return false;
            } else {
                tourCourant = tourCourant.suivant;
                return false; // Nouveau tour, donc pas d'autre boule pour l'ancien tour
            }
        }
        
        return true; // Le tour continue
    }
    
    /**
     * @return le score total de la partie
     */
    public int score() {
        return premierTour.scoreCumule();
    }
    
    /**
     * @return true si la partie est terminée
     */
    public boolean estTerminee() {
        return partieTerminee;
    }
    
    /**
     * @return le numéro du tour courant (1-10)
     */
    public int numeroTourCourant() {
        return tourCourant.numero;
    }
    
    /**
     * @return le numéro du prochain lancer dans le tour courant (1, 2 ou 3)
     */
    public int numeroProchainLancer() {
        return tourCourant.boulesLancees + 1;
    }
}

/**
 * Classe représentant un tour de bowling.
 */
class Tour {
    public final int numero;
    public int boulesLancees = 0;
    protected int quillesAbattuesLancer1 = 0;
    protected int quillesAbattuesLancer2 = 0;
    protected Tour suivant;
    
    public Tour(int numero, Tour suivant) {
        this.numero = numero;
        this.suivant = suivant;
    }
    
    public void enregistreLancer(int quillesAbattues) {
        if (estTermine()) {
            throw new IllegalStateException("Tour déjà terminé");
        }
        
        boulesLancees++;
        if (boulesLancees == 1) {
            quillesAbattuesLancer1 = quillesAbattues;
        } else {
            quillesAbattuesLancer2 = quillesAbattues;
        }
    }
    
    public boolean estTermine() {
        return boulesLancees >= 2 || estUnStrike();
    }
    
    public boolean estUnStrike() {
        return boulesLancees >= 1 && quillesAbattuesLancer1 == 10;
    }
    
    public boolean estUnSpare() {
        return boulesLancees >= 2 && !estUnStrike() && 
               quillesAbattuesLancer1 + quillesAbattuesLancer2 == 10;
    }
    
    public int bonusPourStrike() {
        if (suivant == null) return 0;
        
        if (suivant.boulesLancees >= 1) {
            int bonus = suivant.quillesAbattuesLancer1;
            if (suivant.boulesLancees >= 2) {
                bonus += suivant.quillesAbattuesLancer2;
            } else if (suivant.estUnStrike() && suivant.suivant != null && suivant.suivant.boulesLancees >= 1) {
                // Strike au tour suivant, prendre le premier lancer du tour d'après
                bonus += suivant.suivant.quillesAbattuesLancer1;
            }
            return bonus;
        }
        return 0;
    }
    
    public int bonusPourSpare() {
        if (suivant == null || suivant.boulesLancees < 1) return 0;
        return suivant.quillesAbattuesLancer1;
    }
    
    public int scoreCumule() {
        int scoreDeBase = quillesAbattuesLancer1 + quillesAbattuesLancer2;
        int bonus = 0;
        
        if (estUnStrike()) {
            bonus = bonusPourStrike();
        } else if (estUnSpare()) {
            bonus = bonusPourSpare();
        }
        
        int scoreTotal = scoreDeBase + bonus;
        
        if (suivant != null) {
            scoreTotal += suivant.scoreCumule();
        }
        
        return scoreTotal;
    }
    
    @Override
    public String toString() {
        return String.format("Tour %d: %d/%d (boules: %d)", 
                           numero, quillesAbattuesLancer1, quillesAbattuesLancer2, boulesLancees);
    }
}

/**
 * Dernier tour spécial (10ème) qui peut avoir jusqu'à 3 lancers.
 */
class DernierTour extends Tour {
    private int quillesAbattuesLancer3 = 0;
    
    public DernierTour() {
        super(10, null);
    }
    
    @Override
    public void enregistreLancer(int quillesAbattues) {
        if (estTermine()) {
            throw new IllegalStateException("Tour déjà terminé");
        }
        
        boulesLancees++;
        if (boulesLancees == 1) {
            quillesAbattuesLancer1 = quillesAbattues;
        } else if (boulesLancees == 2) {
            quillesAbattuesLancer2 = quillesAbattues;
        } else {
            quillesAbattuesLancer3 = quillesAbattues;
        }
    }
    
    @Override
    public boolean estTermine() {
        if (boulesLancees < 2) return false;
        
        // Si strike ou spare au 1er/2ème lancer, on a droit à un 3ème lancer
        if ((estUnStrike() || estUnSpare()) && boulesLancees < 3) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int scoreCumule() {
        // Le dernier tour n'a pas de bonus suivant, juste la somme des quilles
        return quillesAbattuesLancer1 + quillesAbattuesLancer2 + quillesAbattuesLancer3;
    }
}