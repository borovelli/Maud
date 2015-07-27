package fr.ensicaen.odfplot.engine;

import fr.ensicaen.odfplot.graphicInterface.MainWindow;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

public class Controller {
	private LinkedList<FunctionODF> listeFunctions = null;

//l	private MainWindow fenPrinc = null;

	private FunctionODF functionSelectionnee = null;

	public Controller(MainWindow p) {
//l		this.fenPrinc = p;
		this.listeFunctions = new LinkedList<FunctionODF>();

	}
	
	public void export(File f, Image i) {
		RenderedImage im = (RenderedImage) i;
		String nom = f.getName();
		File fich;

		if (nom.contains("jpg")||nom.contains("JPG")||nom.contains("Jpg")){
			fich = new File(f.getAbsolutePath());
		}else{
			fich = new File(f.getAbsolutePath() + ".jpg");
			 
		}
			
		try {
			
			ImageIO.write(im, "JPG", fich);
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void identifierFichier(File f) {
		StringTokenizer st = new StringTokenizer(f.getName(), ".");
		String extention = "";

		for (int i = 0; i <= st.countTokens(); i++) {
			extention = st.nextToken();
		}

		extention.toLowerCase();

		if (extention.equals("par"))
			this.chargerPar(f);
		if (extention.equals("xod"))
			this.chargerBear(f);
	}

  public void identifierFichier(double alphaStart, double alphaEnd, double alphaStep,
                         double betaStart, double betaEnd, double betaStep, double[][][] map3ToPlot,
                         String label) {
    FunctionODF tmp = new FunctionODF();
    Parametre p = tmp.getParametre();


    p.setAlphaStep(alphaStep);
    p.setBetaStep(betaStep);
    p.setGammaStep(alphaStep);

    p.setAlphaMin(alphaStart);
    p.setBetaMin(betaStart);
    p.setGammaMin(alphaStart);

    p.setAlphaMax(alphaEnd);
    p.setBetaMax(betaEnd);
    p.setGammaMax(alphaEnd);

    int alphaSlices = ((int) ((alphaEnd - alphaStart) / alphaStep)) + 1;
    int betaSlices = ((int) ((betaEnd - betaStart) / betaStep)) + 1;
    int gammaSlices = ((int) ((alphaEnd - alphaStart) / alphaStep)) + 1;

    for (int n = 0; n < alphaSlices; n++) {
      double alpha = alphaStart + alphaStep * n;
      for (int i = 0; i < betaSlices; i++) {
        double beta = betaStart + betaStep * i;
        for (int j = 0; j < gammaSlices; j++) {
          double gamma = alphaStart + alphaStep * j;
          double value = map3ToPlot[n][i][j];
          ODFPoint point = new ODFPoint(alpha, beta, gamma, value);
          if (value > p.getMaxValue())
            p.setValeurMax(value);
          if (value < p.getMinValue())
            p.setValeurMin(value);
          tmp.ajouterPoint(point);

        }
      }
    }


    tmp.setParametre(p);
    tmp.setNom(label);
    listeFunctions.add(tmp);
    functionSelectionnee = listeFunctions.getFirst();
  }

  private void chargerPar(File f) {
//		System.out.println("charger par");
		try {

			/*
			 * outils pour lire le fichier
			 */
			String line = "";
			FileReader fr;
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			/*
			 * on se positionne au debut des Donnees de l'ODF.
			 */
			while (line != null) {

				while (line != null
						&& (!line.contains("_rita_wimv_odf_resolution"))) {
					line = br.readLine();
				}
				if (line != null) {
					/*
					 * Lecture des donnees de l'ODF.
					 */

					FunctionODF tmp = new FunctionODF();
					Parametre p = tmp.getParametre();

					StringTokenizer st = new StringTokenizer(line, " ");

					// definition de la resolution de la fonction.
					st.nextToken();
					String resolution = st.nextToken();
					double resol = Double.parseDouble(resolution);

					p.setAlphaStep(resol);
					p.setBetaStep(resol);
					p.setGammaStep(resol);

					// on se deplace vers les donnees des points.
					while (!line.contains("_rita_wimv_odf_values")) {
						line = br.readLine();
					}

					double alpha = 0;
					double beta = 0;
					double gamma = 0;

					p.setAlphaMin(alpha);
					p.setBetaMin(beta);
					p.setGammaMin(gamma);

					while (!line.contains("#end_custom_object_odf")) {

						line = br.readLine();
						// lecture d'un paragraphe.
						while (line.length() > 1) {

							StringTokenizer str = new StringTokenizer(line, " ");

							// lecture d'une ligne.
							while (str.hasMoreTokens()) {
								String token = str.nextToken();
								double valeur = Double.parseDouble(token);
								ODFPoint point = new ODFPoint(alpha, beta,
										gamma, valeur);

								// definition max
								if (alpha > p.getAlphaMax())
									p.setAlphaMax(alpha);
								if (beta > p.getBetaMax())
									p.setBetaMax(beta);
								if (gamma > p.getGammaMax())
									p.setGammaMax(gamma);

								if (valeur > p.getMaxValue())
									p.setValeurMax(valeur);

								if (valeur < p.getMinValue())
									p.setValeurMin(valeur);

								tmp.ajouterPoint(point);
								alpha += p.getAlphaStep();
							}// fin de lecture d'une linge.

							line = br.readLine();
							alpha = 0.0;
							beta += p.getBetaStep();

						}// fin de lecture d'un paragraphe.

						alpha = 0.0;
						beta = 0.0;
						gamma += p.getGammaStep();
						// positionnement debut paragraphe suivant.
						while (!(line.length() > 1)) {
							line = br.readLine();
						}

					}

					// finition.
					tmp.setParametre(p);
					tmp.setNom(f.getName());
					this.listeFunctions.add(tmp);
					this.functionSelectionnee = this.listeFunctions.getFirst();
				}
			}
			// ferme le buffer de lecture
			br.close();
			// ferme le lecteur de fichier
			fr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void chargerBear(File f) {

		double alpha = 0, beta = 0, gamma = 0;

		double tmpGamma;

		FunctionODF functionTmp = new FunctionODF();
		functionTmp.setNom(f.getName());

		try {
			alpha = 0;
			beta = 0;
			gamma = 0;

			/*
			 * declaration de variables locales
			 */
			String line = "";

			// outils pour lire le fichier
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			/*
			 * lecture du fichier
			 */

			// on se positionne aux debut des infos e lire dans le fichier
			// on passe 5 lignes
			for (int i = 0; i < 5; i++) {
				line = br.readLine();
				if (line == null) {
//l					this.fenPrinc.afficherErreur("The File is Empty",
//l							"Empty File");
					break;
				}
			}

			// on lit l'entete du bloc de fichier.
			br.readLine();
			line = br.readLine();

			/*
			 * lecture des donnees sur l'angle beta
			 */

			functionTmp.getParametre().setBetaMin(
					Double.parseDouble(line.substring(10, 15).trim()));
			functionTmp.getParametre().setBetaMax(
					Double.parseDouble(line.substring(15, 20).trim()));
			functionTmp.getParametre().setBetaStep(
					Double.parseDouble(line.substring(20, 25).trim()));

			/*
			 * lecture des donnees sur l'angle alpha
			 */
			functionTmp.getParametre().setAlphaMin(
					Double.parseDouble(line.substring(25, 30).trim()));
			functionTmp.getParametre().setAlphaMax(
					Double.parseDouble(line.substring(30, 35).trim()));
			functionTmp.getParametre().setAlphaStep(
					Double.parseDouble(line.substring(35, 40).trim()));

			// controle des donnees chargees.
			// this.getParam().print();

			// chargement de Gamma

			gamma = Double.parseDouble(line.substring(65, 70).trim());
			functionTmp.getParametre().setGammaMin(gamma);

			/*
			 * lecture de l'integralite des donnees du fichier.(36 blocs valeur
			 * 37)
			 */

			for (int l = 0; l < 37; l++) {
				/*
				 * lecture d'un bloc de donne (une seule valeur de gamma)
				 */

				while (beta <= (int) (functionTmp.getParametre().getBetaMax())) {
					// System.out.println(beta);

					/*
					 * nombre de caractere sur une ligne moins l'espace aux
					 * debut de la ligne.
					 */

					while (alpha < (int) (functionTmp.getParametre()
							.getAlphaMax())) {

						line = br.readLine();

						// System.out.println(line);
						/*
						 * on doit maintenant separer les valeurs entre elles
						 * sur la ligne recuperee. on a 72 carateres dans une
						 * ligne.
						 */
						int tmp = line.length() - 1;
						for (int t = 1; t < tmp; t = t + 4) {

							/*
							 * on recupere la valeur lue.
							 */
							String valeur = line.substring(t, t + 4);

							// suprimme les espaces de la chaine de caractere.
							valeur = valeur.trim();

							double intensite = Double.parseDouble(valeur);
							intensite /= 100.00;

							if (intensite > functionTmp.getParametre()
									.getMaxValue())
								functionTmp.getParametre().setValeurMax(
										intensite);

							if (intensite < functionTmp.getParametre()
									.getMinValue())
								functionTmp.getParametre().setValeurMin(
										intensite);

							/*
							 * on creer un point avec les coordonnees alpha,
							 * beta, et gamma et la densite lue
							 */
							functionTmp.ajouterPoint(new ODFPoint(alpha, beta,
									gamma, intensite));
							/*
							 * Pour chaque valeur lue alpha est incrementee d'un
							 * de x degres. x etant defini au debut du
							 * fichier(AlphaStep).
							 */
							alpha = alpha
									+ functionTmp.getParametre().getAlphaStep();

						} // fin : for (int t = 1; t < tmp; t = t + 4)

					} // fin : while (alpha>= (int)
					// (this.getParam().getAlphaMax()))

					alpha = 0;
					beta = beta + functionTmp.getParametre().getBetaStep();

				} // fin : while (beta <= (int)
				// (this.getParam().getBetaMax()))

				beta = 0;

				// on passe 7 lignes entre les blocs
				for (int k = 0; k < 7; k++) {
					line = br.readLine();
				}

				if (line != null) {

					// on recupere la valeur de gamma
					line = br.readLine();
					tmpGamma = gamma;
					gamma = Double.parseDouble(line.substring(65, 70).trim());
					functionTmp.getParametre().setGammaStep(gamma - tmpGamma);
					functionTmp.getParametre().setGammaMax(gamma);
					beta = 0;
				}

			} // fin : for (int l = 0; l <37; l++)

			// ferme le buffer de lecture
			br.close();
			// ferme le lecteur de fichier
			fr.close();
			this.listeFunctions.add(functionTmp);
			this.functionSelectionnee = this.listeFunctions.getFirst();

		} catch (IOException e) {
//l			this.fenPrinc.afficherErreur("Can not Read the file\n" + e,
//l					"Unreadable File");

		} catch (NullPointerException e) {
			// System.err.println(e);
			// e.printStackTrace();
//l			this.fenPrinc.afficherErreur("Reading File error \n" + e, "Error");

		} catch (NumberFormatException e) {
//l			this.fenPrinc.afficherErreur("Reading File error \n" + e, "Error");
		}
	}

	public FunctionODF getSelectedFunction() {
		return functionSelectionnee;
	}

	public void setFonctionSelectionnee(FunctionODF functionSelectionnee) {
		this.functionSelectionnee = functionSelectionnee;
	}

	public LinkedList<FunctionODF> getListeFonctions() {
		return listeFunctions;
	}

}
