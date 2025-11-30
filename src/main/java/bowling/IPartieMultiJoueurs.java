package bowling;

/**
 * Interface pour une partie de bowling multi-joueurs.
 * Permet de gérer plusieurs joueurs qui jouent tour à tour.
 */
public interface IPartieMultiJoueurs {
    
    /**
     * Démarre une nouvelle partie avec les joueurs spécifiés.
     * 
     * @param playerNames tableau des noms des joueurs
     * @return message indiquant le prochain lancer
     * @throws IllegalArgumentException si le tableau est null ou vide
     */
    String demarreNouvellePartie(String[] playerNames) throws IllegalArgumentException;
    
    /**
     * Enregistre un lancer pour le joueur courant.
     * 
     * @param nombreDeQuillesAbattues nombre de quilles abattues
     * @return message indiquant le prochain lancer ou la fin de partie
     * @throws IllegalStateException si la partie est terminée
     */
    String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException;
    
    /**
     * Retourne le score actuel d'un joueur.
     * 
     * @param nomDuJoueur nom du joueur
     * @return score du joueur
     * @throws IllegalArgumentException si le joueur n'existe pas
     */
    int scorePour(String nomDuJoueur) throws IllegalArgumentException;
}