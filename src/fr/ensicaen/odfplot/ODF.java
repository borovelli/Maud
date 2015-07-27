package fr.ensicaen.odfplot;

import fr.ensicaen.odfplot.graphicInterface.MainWindow;

import javax.swing.UIManager;

public class ODF{
	
		/**
		 * Lancement du programme.
		 * @param args
		 */
	
	public static void main(String[] args) {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch (Exception e) {
					System.out.println ("Cannot load Windows Skins");
				}
				new MainWindow("ODF plot v4.2");
	}
	

}
