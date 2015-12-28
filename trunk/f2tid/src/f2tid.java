/**
 * IFIPS info08
 * Jennifer Henry - Matthieu Lux
 * EIT - f2tid prononcer "fétide"
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import engine.Interrogation;
import engine.VecteurTermes;

/**
 * Notre application est visible via un applet intégré dans une page web
 * @author Cassius
 *
 */
public class f2tid extends JApplet implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7379587371723737961L;
	private JTextField searchField;
	private JLabel label;
	private JLabel labelNbResults;
	private JButton button;
	private JTable jTableResults;
	private JScrollPane jScrollPaneResults;
	private Interrogation interrogation;

	public void init(){
		// Initialisation de l'application
		interrogation = new Interrogation("../corpus/index_corpus.tfidf");

		// Initialisation de l'interface
		setLayout(new BorderLayout(1, 2));
		setBackground(Color.white);
		setSize(600, 500);

		jScrollPaneResults = new JScrollPane();
		jTableResults = new JTable();
		jTableResults.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {
				},
				new String [] {
						"Documents"
				}
		));
//		jTableResults.addMouseListener(new java.awt.event.MouseAdapter() {
//		public void mouseClicked(java.awt.event.MouseEvent evt) {
//		jTableResultsMouseClicked(evt);
//		}
//		});
		jScrollPaneResults.setViewportView(jTableResults);

		searchField = new JTextField("");
		searchField.setToolTipText("Requête...");
		searchField.addKeyListener(new KeyAdapter() {

			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (evt.getKeyChar() == evt.VK_ENTER) {
					actionPerformed(null);
				}
			}

		});
		label = new JLabel("IFIPS 2008");
		labelNbResults = new JLabel("");
		button = new JButton("Recherche f2tid");
		button.addActionListener(this);
		add("North", searchField);
		//add("North", button);
		//add("North", labelNbResults);
		add("Center", jScrollPaneResults);
		add("South", label);

	}

	public void start(){
		System.out.println("Applet starting.");
	}

	public void stop(){
		System.out.println("Applet stopping.");
	}

	public void destroy(){
		System.out.println("Destroy method called.");
	}

	public void actionPerformed(ActionEvent event) {
		// On a cliqué sur le bouton, on affiche les resultats de la recherche

		// Lancer la requete
		String reqCourante = searchField.getText();

		Vector<VecteurTermes> res = interrogation.interroge(reqCourante, true);

		labelNbResults.setText("Documents trouvés : " + res.size());

		DefaultTableModel model = new DefaultTableModel(new Object[] {"Document"}, res.size()) {
			public boolean isCellEditable(int iRowIndex, int iColumnIndex) {
				return false;
			}
		};
		int i = 0;
		for (VecteurTermes vec : res) {
			model.setValueAt(vec.getNom(), i, 0);
			i++;
		}

		jTableResults.setModel(model);

	}

}
