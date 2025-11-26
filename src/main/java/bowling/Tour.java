package bowling;

/**
 * Correspond à un tour "normal" du jeu, constitué de 1 ou 2 lancers
 * Connait son tour suivant, pour calculer le bonus éventuel
 * pour le strike ou le spare.
 */
public class Tour {

	protected int numero;
	protected int quillesAbattuesLancer1 = 0;
	protected int quillesAbattuesLancer2 = 0;
	protected int boulesLancees = 0;

	protected Tour suivant;
	
	protected Tour() {
		// Vide pour l'héritage
	}

	/**
	 * Crée un nouveau tour
	 * @param number : numéro de ce tour
	 * @param next : le tour suivant
	 */
	public Tour(int number, Tour next) {
		if (number < 1 || number > 9) {
			throw new IllegalArgumentException("Les tours normaux sont numérotés de 1 to 9");
		}
		if (next == null) {
			throw new IllegalArgumentException("Les tours normaux doivent avoir un suivant");
		}

		numero = number;
		suivant = next;
	}

	/**
	 * Combien de boules ont été lancées dans ce tour ?
	 * @return the number of balls already thrown in this frame
	 **/
	public int getBoulesLancees() {
		return boulesLancees;
	}

	/**
	 * Numéro de ce tour
	 * @return le numéro de ce tour
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * Est-ce que ce tour est terminé ?
	 * @return vrai si ce tour est fini, faux sinon
	 */
	public boolean estTermine() {
		return estUnStrike() || (boulesLancees == 2);
	}

	/**
	 * enregistrer le résultat d'un lancer
	 *
	 * @param combien : nombre de quilles abattues à ce lancer
	 */
	void enregistreLancer(int combien) {
		if (combien < 0) {
			throw new IllegalArgumentException("Ne peut pas être négatif");
		}
		if ((combien + quillesAbattuesLancer1) > 10) {
			throw new IllegalArgumentException("Max 10 points dans un tour");
		}
		if (boulesLancees > 1) {
			throw new IllegalStateException("Max 2 boules dans un tour normal");
		}

		boulesLancees++;
		if (boulesLancees == 1) {
			quillesAbattuesLancer1 = combien;
		} else if (boulesLancees == 2) {
			quillesAbattuesLancer2 = combien;
		}
	}

	/**
	 * @return vrai si ce tour est un strike, faux sinon
	 */
	boolean estUnStrike() {
		return quillesAbattuesLancer1 == 10;
	}

	/**
	 * @return vrai si ce tour est un spare, faux sinon
	 */
	boolean estUnSpare() {
		return (quillesAbattuesLancer1 + quillesAbattuesLancer2) == 10;
	}

	/**
	 * @return le bonus accordé par ce tour en cas de spare au tour précédent
	 */
	int bonusPourSpare() {
		return quillesAbattuesLancer1;
	}

	/**
	 * @return le bonus accordé par ce tour en cas de strike au tour précédent
	 */
	int bonusPourStrike() {
		if (estUnStrike()) {
			return quillesAbattuesLancer1 + suivant.bonusPourSpare();
		} else {
			return quillesAbattuesLancer1 + quillesAbattuesLancer2;
		}
	}

	/**
	 * @return le score réalisé à partir de ce tour en tenant compte des bonus éventuels
	 */
	int scoreCumule() {
		int scoreDeCeTour;
		if (estUnStrike()) {
			scoreDeCeTour = 10 + suivant.bonusPourStrike();
		} else if (estUnSpare()) {
			scoreDeCeTour = 10 + suivant.bonusPourSpare();
		} else {
			scoreDeCeTour = quillesAbattuesLancer1 + quillesAbattuesLancer2;
		}
		return scoreDeCeTour + suivant.scoreCumule();
	}

	/**
	 * @return le tour suivant ce tour
	 */
	Tour getSuivant() {
		return suivant;
	}

	@Override
	public String toString() {
		return "Tour# " + numero + ", premier Lancer: " + quillesAbattuesLancer1 + ", secondLancer: " + quillesAbattuesLancer2;
	}
}
