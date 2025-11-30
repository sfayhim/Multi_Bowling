package fr.irit.bastide.multiplayerbowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MultiPlayerBowlingTest {
	private PartieMultiJoueurs multiGame;
	
	@BeforeEach
	public void setUp() {
		multiGame = new PartieMultiJoueurs();
	}

	@Test
	void testRookieVersusChampion() {
		String[] playerNames = {"Rookie", "Champion"};
		multiGame.demarreNouvellePartie(playerNames);
		for (int tour = 0; tour < 10; tour ++) {
			// Le rookie fait deux rigoles
			multiGame.enregistreLancer(0);			
			multiGame.enregistreLancer(0);			
			// Le champion fait un strike
			multiGame.enregistreLancer(10);
		}
		// Le champion a droit à deux boules de plus
		multiGame.enregistreLancer(10);
		multiGame.enregistreLancer(10);	
		assertEquals(  0, multiGame.scorePour("Rookie"),
				"Le rookie n'a fait que des rigoles, score 0");
		assertEquals(300, multiGame.scorePour("Champion"),
				"Le champion n'a fait que des strikes, score 300");
	}
        
	@Test 
	void needOnePlayer() throws Exception {
		String[] playerNames = {};
		assertThrows(IllegalArgumentException.class, 
		()-> multiGame.demarreNouvellePartie(playerNames),
		"Une partie doit avoir au moins un joueur");
	}

	@Test
	void goodStartMessage()  {
		String[] playerNames = {"Zorglub", "Albator"};
		String result = multiGame.demarreNouvellePartie(playerNames);
		assertEquals("Prochain tir : joueur Zorglub, tour n° 1, boule n° 1", result);
	}
	
	@Test
	void testenregistreLancerStrike() {
		String[] playerNames = {"Zorglub", "Albator"};
		multiGame.demarreNouvellePartie(playerNames);
		String result = rollStrike();
		assertEquals("Prochain tir : joueur Albator, tour n° 1, boule n° 1", result);
	}

	@Test
	void unLancerDeTropLeveException() {
		String[] playerNames = {"Zorglub", "Albator"};
		multiGame.demarreNouvellePartie(playerNames);
		// Tout dans la rigole : n joueurs * 10 tours * 2 boules par tour
		rollMany(playerNames.length * 10 * 2, 0);
		// Un enregistreLancer de trop doit lever une exception
		assertThrows(IllegalStateException.class, 
			()-> multiGame.enregistreLancer(0),
			"La partie est terminée, on ne peut plus lancer de boule");
	}

	@Test
	void testGameFinishes() {
		String[] playerNames = {"Zorglub", "Albator"};
		multiGame.demarreNouvellePartie(playerNames);
		// Tout dans la rigole : n joueurs * 10 tours * 2 boules par tour
		String message = rollMany(playerNames.length * 10 * 2, 0);
		assertEquals("Partie terminée", message);
	}
	
	@Test
	void scoreForUnknownPlayer() throws Exception {
		String[] playerNames = {"Zorglub", "Albator"};
		multiGame.demarreNouvellePartie(playerNames);
		assertThrows(IllegalArgumentException.class, 
			()-> multiGame.scorePour("Unknown"),
			"Le joueur Unknown n'existe pas");
	}

	// Quelques methodes utilitaires pour faciliter l'écriture des tests
	private String rollMany(int n, int pins) {
		String result = "";
		for (int i = 0; i < n; i++) {
			result= multiGame.enregistreLancer(pins);
		}
		return result;
	}

	private String rollSpare() {
		multiGame.enregistreLancer(7);
		return multiGame.enregistreLancer(3);
	}

	private String rollStrike() {
		return multiGame.enregistreLancer(10);
	}	
}
