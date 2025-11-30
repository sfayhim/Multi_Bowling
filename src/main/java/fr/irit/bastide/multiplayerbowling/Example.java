package fr.irit.bastide.multiplayerbowling;

import bowling.IPartieMultiJoueurs;

/**
 *
 * @author rbastide
 */
public class Example {

	public static void main(String[] args) throws Exception {
		String[] players = { "John", "Paul", "Georges", "Ringo" };
		IPartieMultiJoueurs game = new PartieMultiJoueurs();
		System.out.println(game.demarreNouvellePartie(players));
		System.out.println(game.enregistreLancer(10)); // Strike for John
		System.out.println(game.enregistreLancer(3));
		System.out.println(game.enregistreLancer(7)); // Spare for Paul
		System.out.println(game.enregistreLancer(0));
		System.out.println(game.enregistreLancer(0)); // 0 for Georges
		System.out.println(game.enregistreLancer(0));
		System.out.println(game.enregistreLancer(0)); // 0 for Ringo
		System.out.println(game.enregistreLancer(6));
		System.out.println(game.enregistreLancer(3)); // 9 for john, + strike bonus
		System.out.println(game.enregistreLancer(5));
		System.out.println(game.enregistreLancer(0)); // 5 for Paul, + spare bonus
		
		for (String playerName : players)
			System.out.printf("Joueur: %s, score: %d %n",
				playerName,
				game.scorePour(playerName));
	}
	
}
