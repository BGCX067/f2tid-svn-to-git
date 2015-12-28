package engine;

import java.util.HashMap;

/**
 * Classe representant un vecteur de termes, cela permet ensuite de faire 
 * un produit scalaire entre vecteur
 * @author Alex
 *
 */
public class VecteurTermes implements Comparable<VecteurTermes> {
	
	private String 					nom;
	private boolean					doc;
	private HashMap<String, Terme>	termes;
	private VecteurTermes			requete;
	
	/**
	 * Constructeur pour un vecteur de termes d'une requete
	 *
	 */
	public VecteurTermes() {
		this.nom = "Requete";
		this.doc = false;
		this.termes = new HashMap<String, Terme>();
		this.requete = null;
	}
	
	/**
	 * Constructeur pour un vecteur de termes d'un document, la requete courante est passée en parametre
	 * @param n		nom du document
	 * @param isDoc	
	 * @param req	le vecteur de terme de la requete courante
	 */
	public VecteurTermes(String n, boolean isDoc, VecteurTermes req) {
		this.nom = n;
		this.doc = isDoc;
		this.termes = new HashMap<String, Terme>();
		this.requete = req;
	}
	
	/**
	 * Ajouter un terme au vecteur dans le cas d'un document
	 * @param m
	 * @param p
	 */
	public void addTerme(String m, double p) {
		this.termes.put(m, new Terme(m.toLowerCase(), p));
	}
	
	/**
	 * Ajouter un terme au vecteur dans le cas d'une requete
	 * @param s
	 */
	public void addTerme(String[] s) {
		this.termes.put(s[2].toLowerCase(), new Terme(s));
	}

	public boolean isDoc() {
		return doc;
	}

	public String getNom() {
		return nom;
	}
	
	public Terme getTerme(String m) {
		return this.termes.get(m);
	}
	
	public HashMap<String, Terme> getTermes() {
		return termes;
	}
	
	public VecteurTermes getRequete() {
		return this.requete;
	}
	
	/**
	 * Fait le produit scalaire en le vecteur courant et celui passe en parametre
	 * @param vec
	 * @return
	 */
	public double produitScalaire(VecteurTermes vec) {
		double res = 0.0;
		
		for (String m : this.termes.keySet()) {
			if (vec.getTerme(m) != null)
				res += this.termes.get(m).getPoid() * vec.getTerme(m).getPoid();
		}
		
		return res;
	}
	
	/**
	 * Retourne le poid du document par rapport a la requete
	 * @return
	 */
	public double getPoid() {
		return this.produitScalaire(this.requete);
	}

	/**
	 * Permet de comparer le poid de 2 vecteurs pour les trier par ordre decroissant
	 */
	public int compareTo(VecteurTermes arg0) {
		// TODO Auto-generated method stub
		double p1 = this.getPoid();
		double p2 = arg0.getPoid();
	    if (p1 > p2)  return -1; 
	    else if(p1 == p2) return 0; 
	    else return 1;
	}
	
	public String toString() {
		String s = this.nom + " : [ ";
		for (String t : this.termes.keySet()) {
			s += this.termes.get(t);
		}
		s += "]";
		return s;
	}
	
}
