package engine;

import indexer.Indexation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;



/**
 * Cette classe permet d'interroger l'index à partir d'une requete qui lui est fournit
 * @author Alexandre Burgert - Ossama Cadoret
 */
public class Interrogation {

	private Indexation index;
	private HashMap<String, VecteurTermes> 	results;
	private Vector<VecteurTermes> 			resSorted;
	
	/**
	 * Constructeur : il charge l'index fournit en parametre
	 * @param file l'index a charger en memoire
	 */
	public Interrogation(String file) {
		index = new Indexation();
		index.loadIndex(file);
		results = new HashMap<String, VecteurTermes>();
		resSorted = new Vector<VecteurTermes>();
	}
	
	/**
	 * Methode permettant d'interroger l'index a partir d'une requete
	 * @param requete
	 * @param english
	 * @return
	 */
	public Vector<VecteurTermes> interroge(String requete, boolean english) {
		english = true;
		FileWriter fw;
		BufferedWriter writer;
		BufferedReader 	lecteur;
		String cmd, ligne;
		String[] ligneSplit;
		VecteurTermes vecRequete = new VecteurTermes();
		Process procCourant;
		results = new HashMap<String, VecteurTermes>();
		resSorted = new Vector<VecteurTermes>();
		
		System.out.println("Search results");
		try {
			// on enregistre la requete dans un fichier
			fw = new FileWriter("../requete.req", false);
			writer = new BufferedWriter(fw);
			writer.write(requete.replaceAll("\\p{Punct}+", " "));
			writer.flush();
			writer.close();
			// on lémmatise la requete
			if (english)
				cmd = "../TreeTagger/bin/tag-english.bat " + "../requete.req" + " " + "../requete.lem";
			else 
				cmd = "../TreeTagger/bin/tag-french.bat " + "../requete.req" + " " + "../requete.lem";
			
			System.out.println("CMD req : " + cmd);
			
			try {
				procCourant = Runtime.getRuntime().exec(cmd);
				procCourant.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// on ouvre le fichier contenant la requete lemmatisée
			lecteur = new BufferedReader(new FileReader("../requete.lem"));
			// création de la requete
			while ((ligne = lecteur.readLine()) != null) {
				ligneSplit = ligne.split("\t");
				
				if (ligneSplit.length == 3) {
					vecRequete.addTerme(ligneSplit);
					System.out.println(vecRequete);
				}
			}
			System.out.println("Taille index = " + index.size());
			for (String t : vecRequete.getTermes().keySet()) {
				if (index.getTerme(t) != null) { 
					for (String doc : index.getTerme(t).keySet()) {
						if (!results.containsKey(doc)) {
							results.put(doc, new VecteurTermes(doc, true, vecRequete));
						}
						results.get(doc).addTerme(t, index.getTerme(t, doc));
					}
				}
			}
			
			System.out.println("nb doc trouvés : " + results.size());
			for (String doc : results.keySet()) {
				if (results.get(doc).getPoid() > 0) 
					resSorted.add(results.get(doc));
			}
			Collections.sort(resSorted);
			System.out.println("nb doc triés : " + resSorted.size());
			for (VecteurTermes vec : resSorted) {
				System.out.println(vec.getNom() + " : " + vec.getPoid());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resSorted;
	}
	
	public static void main(String[] argv) {
		Interrogation interro;
		System.out.println("Load start");
		interro = new Interrogation("./corpusLight/index_corpusLight.tfidf");
		System.out.println("Load end");
		
		interro.interroge("play test game", true);
	}
	
}
