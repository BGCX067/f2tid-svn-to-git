package indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Lemmatisation {
	// scan des dossiers
	private File			dirCourant;
	private File[]			dossiers;
	private File[]			fichiers;
	// ouverture du fichier
	private BufferedReader 	lecteur = null;
	private BufferedWriter	writer = null;
	private FileWriter 		fw;
	private String			ligne;
	private Process			procCourant;
	
	public Lemmatisation() {
		dossiers = null;
		fichiers = null;
		dirCourant = new File("./corpus/");
		dossiers = dirCourant.listFiles(); 
		
		// parcours du corpus
		for (File d : dossiers) { 
			System.out.println("d = " + d.toString());
			dirCourant = new File(d.toString());
			fichiers = dirCourant.listFiles();
			
			// parcours d'un theme du corpus
			if (d.isDirectory()) {
				for (File f : fichiers) {
					// on travail sur les .txt
					if (f.getName().endsWith(".txt")) {
						// on clean le texte des caractères superflus
						try {
							lecteur = new BufferedReader(new FileReader(f.toString()));
							fw = new FileWriter(f.toString().replaceAll(".txt", ".clean"), false);
							writer = new BufferedWriter(fw);
							while ((ligne = lecteur.readLine()) != null) {
								//ligne = ligne.replaceAll("[.?,!:_()/'\"]", " ");
								ligne = ligne.replaceAll("\\p{Punct}+", " ");
								writer.write(ligne);
								writer.newLine();
							}
							writer.flush();
							writer.close();
							lecteur.close();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}catch (IOException e1) {
							e1.printStackTrace();
						}
						// on lemmatise le fichier
						String cmd = "./TreeTagger/bin/tag-english.bat " + f.toString().replaceAll(".txt", ".clean") + " " + f.toString().replaceAll(".txt", ".lem");
						System.out.println(cmd);
						
						try {
							procCourant = Runtime.getRuntime().exec(cmd);
							procCourant.waitFor();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] argv) {
		new Lemmatisation();
	}
	
}
