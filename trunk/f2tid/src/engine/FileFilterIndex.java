package engine;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Classe permettant de filtrer les fichier a faire apparaitre dans le JFileChooser
 * @author Alex
 */
public class FileFilterIndex extends FileFilter{

	public boolean accept(File arg0) {
		if ((arg0.isDirectory()) || (arg0.toString().endsWith(".tfidf")))
			return true;
		else
			return false;
	}

	@Override
	public String getDescription() {
		return "Fichier index tf*idf";
	}

}
