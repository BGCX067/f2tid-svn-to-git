package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Indexation {
	// scan des dossiers
	private File			dirCourant;
	private File[]			dossiers;
	private File[]			fichiers;
	// ouverture du fichier
	private BufferedReader 	lecteur = null;
	private String			ligne;
	private String[]		ligneSlit;
	
	// structure de données
	private String 			mot;
	private String			doc;
	private int 	nbDocuments;
	
	/**
	 * HashMap de mot, chaque mot est lié à une hashmap de document
	 */
	private HashMap<String, HashMap<String, Double>>	tf_idf;
	private HashMap<String, Integer> 					freqMotDoc;

	public Indexation() {
		tf_idf = new HashMap<String, HashMap<String,Double>>();
		freqMotDoc = new HashMap<String, Integer>();
	}
	
	public Indexation(String path) {
		int nbTermes = 0;
		nbDocuments = 0;
		tf_idf = new HashMap<String, HashMap<String,Double>>();
		freqMotDoc = new HashMap<String, Integer>();
		
		dirCourant = new File(path);
		dossiers = dirCourant.listFiles();
		
		HashMap<String, Double>	docs;
		
		// parcours du corpus
		for (File d : dossiers) { 
			System.out.println("d = " + d.getName());
			System.out.println("Memory : " + Runtime.getRuntime().totalMemory() + ", Free : " + Runtime.getRuntime().freeMemory());
			dirCourant = new File(d.toString());
			fichiers = dirCourant.listFiles();
			
			if (d.isDirectory()) {
				// parcours d'un theme du corpus
				for (File f : fichiers) {
					// on travail sur les .txt
					if (f.getName().endsWith(".lem")) {
						nbDocuments++;
						try {
							
							nbTermes = 0;
							lecteur = new BufferedReader(new FileReader(f.toString()));
							//doc = d.getName() + "_" + f.getName().substring(0, f.getName().lastIndexOf(".lem"));
							doc = f.toString().substring(0, f.toString().lastIndexOf(".lem"));
							System.out.println("doc = " + doc);
							while ((ligne = lecteur.readLine()) != null) {
								ligneSlit = ligne.split("\t");
								// on traite les catégories intéressantes
								if (!ligneSlit[1].equals("DT") && !ligneSlit[1].equals("TO") && !ligneSlit[1].equals("IN") && 
									!ligneSlit[1].equals("CD") && !ligneSlit[1].equals("CC")) {
									
									nbTermes++;
									mot = ligneSlit[2].toLowerCase();
									//System.out.println(mot + " : " + doc);
									
									if (!tf_idf.containsKey(mot)) {
										// si le mot n'existe pas on l'ajoute
										docs = new HashMap<String, Double>();
										docs.put(doc, 1.0);
										tf_idf.put(mot, docs);
										freqMotDoc.put(mot, 1);
									}
									else {
										// si le mot existe, on regarde s'il existe deja pour le document courant
										if (!tf_idf.get(mot).containsKey(doc)) {
											tf_idf.get(mot).put(doc, 1.0);
											freqMotDoc.put(mot, freqMotDoc.get(mot) + 1);
										}
										else {
											tf_idf.get(mot).put(doc, tf_idf.get(mot).get(doc) + 1.0);
										}
									}
									
								}
							}
							lecteur.close();
							// on termine le calcule de "tf" en divisant par le nombre de termes du document
							for (String m : tf_idf.keySet()) {
								if (tf_idf.get(m).containsKey(doc)) {
									tf_idf.get(m).put(doc, tf_idf.get(m).get(doc) / (double)nbTermes);
								}
							}
							
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		// on termine le calcul du poid final : tf * idf
		double poid;
		for (String m : tf_idf.keySet()) {
			for (String d : tf_idf.get(m).keySet()) {
				poid = tf_idf.get(m).get(d) * Math.log((double)nbDocuments / freqMotDoc.get(m));
				tf_idf.get(m).put(d, poid);
			}
		}
		
		// sauvegarde de l'indexation
		try {
			// ouverture d'un flux de sortie vers le fichier "personne.serial"
			FileOutputStream fos = new FileOutputStream(path + "index_corpus.tfidf");

			// création d'un "flux objet" avec le flux fichier
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			try {
				// sérialisation : écriture de l'objet dans le flux de sortie
				oos.writeObject(tf_idf); 
				// on vide le tampon
				oos.flush();
				System.out.println("tf_idf a ete serialise");
			} finally {
				//fermeture des flux
				try {
					oos.close();
				} finally {
					fos.close();
				}
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	public boolean loadIndex(String file) {
		
		try {
			// ouverture d'un flux d'entrée depuis le fichier "personne.serial"
			FileInputStream fis = new FileInputStream(file);
			// création d'un "flux objet" avec le flux fichier
			ObjectInputStream ois= new ObjectInputStream(fis);
			try {	
				// désérialisation : lecture de l'objet depuis le flux d'entrée
				tf_idf = (HashMap)ois.readObject();
			} finally {
				// on ferme les flux
				try {
					ois.close();
				} finally {
					fis.close();
				}
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		if(tf_idf != null) {
			System.out.println("index a ete deserialise");
		}
		return true;
	}
	
	public HashMap<String, Double> getTerme(String mot) {
		return tf_idf.get(mot);
	}
	
	public double getTerme(String mot, String doc) {
		return tf_idf.get(mot).get(doc);
	}
	
	public int size() {
		return this.tf_idf.size();
	}
	
	public String toString() {
		return "Taille de l'index : " + tf_idf.keySet().size() + " mots";
	}
	
	public static void main(String[] argv) {
		new Indexation("./corpus/");
		System.out.println("Memory : " + Runtime.getRuntime().totalMemory() + ", Free : " + Runtime.getRuntime().freeMemory());
	}
	
}
