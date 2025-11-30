package fr.irit.bastide.multiplayerbowling;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import bowling.IPartieMultiJoueurs;
import bowling.PartieMonoJoueur;

public class PartieMultiJoueurs implements IPartieMultiJoueurs {

	private static final String DISPLAY = "Prochain tir : joueur %s, tour n° %d, boule n° %d";
	private static final String FINISHED = "Partie terminée";
	// Associe chaque joueur à son jeu
	// note : on déclare la variable avec une interface (Map)
	private final Map<String, PartieMonoJoueur> parties;
	// Permet de parcourir les noms de joueurs
	private Iterator<String> playerIterator;
	// Le nom du joueur courant
	private String nomDuJoueurCourant;
	// Le jeu du joueur courant
	private PartieMonoJoueur partieCourante;
	// Est-ce que le jeu est en cours
	private boolean partieEnCours = false;

	public PartieMultiJoueurs() {
		// note : on initialise la variable en choisissant une implémentation (LinkedHashMap)
		// cf. https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/LinkedHashMap.html
		parties = new LinkedHashMap<>();
		// games = new TreeMap<>(); // Avec TreeMap ça ne marche pas, pourquoi ?
	}

	@Override
	public String demarreNouvellePartie(String[] playerNames) throws IllegalArgumentException {
		if ((playerNames == null) || playerNames.length == 0) {
			throw new IllegalArgumentException("Il faut au moins un joueur");
		}

		parties.clear(); // On efface le jeu précédent

		// On associe à chaque joueur son jeu
		for (String name : playerNames) {
			parties.put(name, new PartieMonoJoueur());
		}

		// On initialise le premier joueur
		// On itère sur les noms des joueurs (keyset(), ensemble des clés de la map)
		playerIterator = parties.keySet().iterator();
		changeToNextPlayer();

		// C'est parti !
		partieEnCours = true;

		return message();
	}

	@Override
	public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
		if (!partieEnCours) {
			throw new IllegalStateException(FINISHED);
		}

		// On enregistre le lancer courant
		boolean encoreUneBoule = partieCourante.enregistreLancer(nombreDeQuillesAbattues);

		// Si le tour du joueur est terminé
		if (partieCourante.estTerminee() || !encoreUneBoule) {
			// On passe au joueur suivant
			partieEnCours = changeToNextPlayer();
		}

		return message();
	}

	/**
	 * 
	 * @return true si le jeu doit continuer
	 */
	private boolean changeToNextPlayer() {
		if (!playerIterator.hasNext()) { // On a passé tous les joueurs
			if (partieCourante.estTerminee()) { // Le dernier joueur a fini
				return false; // Le jeu est terminé
			} else { // On démarre un nouveau tour
				playerIterator = parties.keySet().iterator(); // On réinitialise l'itérateur
			}
		}
		nomDuJoueurCourant = playerIterator.next();
		partieCourante = parties.get(nomDuJoueurCourant);
		return true;
	}

	/**
	 * 
	 * @return le message à afficher après chaque lancer
	 */
	private String message() {
		if (!partieEnCours) {
			return FINISHED;
		} else {
			int tour = partieCourante.numeroTourCourant();
			int ball = partieCourante.numeroProchainLancer();
			return String.format(DISPLAY, nomDuJoueurCourant, tour, ball);
		}
	}

	@Override
	public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
		// On trouve le jeu associé au nom du joueur
		PartieMonoJoueur game = parties.get(nomDuJoueur);

		if (game == null)
			throw new IllegalArgumentException("Joueur inconnu");

		return game.score();
	}

	public static void main(String[] args) {
		var partie = new PartieMultiJoueurs();
		String[] players = { "Alice", "Bob" };
		System.out.println(partie.demarreNouvellePartie(players));
		// Simulate a few rolls
		System.out.println(partie.enregistreLancer(10)); // Alice strikes
		System.out.println(partie.enregistreLancer(7)); // Bob rolls 7
		System.out.println(partie.enregistreLancer(2)); // Bob rolls 2
		// Print scores
		for (String player : players) {
			System.out.println(player + " score: " + partie.scorePour(player));
		}

	}
}
