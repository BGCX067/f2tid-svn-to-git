package slurper;

import java.io.*;
import java.net.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

/**
 * Classe permettant d'extraire le texte d'un document HTML (les différentes balises sont supprimées)
 * @author Alex
 *
 */
class GetHTMLText {
	
	private EditorKit kit;
	private Document doc;
	
	public static void main(String[] args) throws Exception {
		GetHTMLText H2T = new GetHTMLText();
		
		// Create a reader on the HTML content.
		String rd = H2T.getText("./corpus/gaming/1.html");
 
		System.out.println( rd );
	}
	
	public GetHTMLText() {
		kit = new HTMLEditorKit();
	}
 
	// Returns a reader on the HTML data. If 'uri' begins
	// with "http:", it's treated as a URL; otherwise,
	// it's assumed to be a local filename.
	public String getText(String uri) throws IOException {
		doc = kit.createDefaultDocument();
		// The Document class does not yet handle charset's properly.
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		
		Reader rd;
		
		// Retrieve from Internet.
		if (uri.startsWith("http:")) {
			URLConnection conn = new URL(uri).openConnection();
			rd = new InputStreamReader(conn.getInputStream());
		}
		// Retrieve from file.
		else {
			rd =  new FileReader(uri);
		}
		// Parse the HTML.
		try {
			kit.read(rd, doc, 0);
			// The HTML text is now stored in the document
			return doc.getText(0, doc.getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
}
