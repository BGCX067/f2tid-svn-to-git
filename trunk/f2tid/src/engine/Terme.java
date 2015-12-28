package engine;

/**
 * Classe representant un terme et son poids associe
 * @author Alexandre Burgert - Ossama Cadoret
 *
 */
public class Terme {

	private String	mot;
	private double	poid;
	
	/**
	 * Constructeur pour un terme d'un document
	 * @param m
	 * @param p
	 */
	public Terme(String m, double p) {
		this.mot = m.toLowerCase();
		this.poid = p;
	}
	
	/**
	 * Constructeur pour un terme de la requete, il assigne automatique 
	 * un poid au terme en fonction de sa catégorie
	 * @param s
	 */
	public Terme(String[] s) {
		this.mot = s[2].toLowerCase();
		// on assigne un poid différent en fonction de la catégorie du mot
		if ((s[1].equals("NP")) || (s[1].equals("NPS"))) this.poid = 0.8;
		else if ((s[1].equals("NN")) || (s[1].equals("NNS"))) this.poid = 0.6;
		else this.poid = 0.3;
	}

	public String getMot() {
		return mot;
	}

	public double getPoid() {
		return poid;
	}

	public void setMot(String mot) {
		this.mot = mot;
	}

	public void setPoid(double poid) {
		this.poid = poid;
	}
	
	public String toString() {
		return this.mot + " (" + this.poid + ") ";
	}
	
}
