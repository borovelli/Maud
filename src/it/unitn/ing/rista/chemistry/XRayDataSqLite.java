package it.unitn.ing.rista.chemistry;

import it.unitn.ing.rista.io.StringNumber;
import it.unitn.ing.rista.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class XRayDataSqLite {

	public static int atomsNumber = 94;

	public static String[] shellIDs = {"K", "L1", "L2", "L3", "M1", "M2", "M3", "M4", "M5",
			"N1", "N2", "N3", "N4", "N5", "N6", "N7", "O1", "O2", "O3", "O4", "O5", "O6", /*"O7", "O8", "O9",*/
			"P1", "P2", "P3"/*, "P4", "P5", "P6", "P7", "P8", "P9"*/};

	public static final int K = 0, L1 = 1, L2 = 2, L3 = 3, M1 = 4, M2 = 5, M3 = 6, M4 = 7, M5 = 8,
			N1 = 9, N2 = 10, N3 = 11, N4 = 12, N5 = 13, N6 = 14, N7 = 15, O1 = 16, O2 = 17, O3 = 18, O4 = 19, O5 = 20, O6 = 21,
			P1 = 22, P2 = 23, P3 = 24;

	public static int[] mainShellNumber = {0, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5};
	public static int[] mainShellIndex = {K, L1, L1, L1, M1, M1, M1, M1, M1, N1, N1, N1, N1, N1, N1, N1,
			O1, O1, O1, O1, O1, O1, P1, P1, P1};

	public static void main(String[] args) throws ClassNotFoundException
	{
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try
		{
			// create a database connection
//			connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.filesfolder + Constants.pathSeparator + "xraydata.db");
//			connection = DriverManager.getConnection("jdbc:sqlite:" + "/Volumes/xspider/Users/luca/Applications/Maud/files/xraydata.db");
			connection = DriverManager.getConnection("jdbc:sqlite:" + "files/xraydata.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.

/*			for (int i = 1; i <= atomsNumber; i++) {
				String elementNumberAsString = Integer.toString(i);
				ResultSet rs = statement.executeQuery("SELECT * FROM xraydata_ebel_elastic WHERE Z_id = " + elementNumberAsString);
				while(rs.next())
				{
					// read the result set
					System.out.println("z_id = " + rs.getString("z_id"));
					System.out.print("Coeffs = ");
					for (int j = 2; j < 8; j++) {
						System.out.print(rs.getDouble(j) + " ");
					}
					System.out.println();
				}
			}*/
			for (int i = 1; i <= atomsNumber; i++) {
				String elementNumberAsString = Integer.toString(i);
				String queryString = "SELECT * FROM xraydata_ebel_tau_shell WHERE Z_id = " + elementNumberAsString;
				ResultSet rs = statement.executeQuery(queryString);
				while (rs.next()) {
					String shellID = rs.getString("shell_id");
					double[] coeffs = new double[6];
					System.out.print(i + " " + shellID + " ");
					for (int k = 1; k < 7; k++) {
						coeffs[k-1] = rs.getDouble("coeff" + Integer.toString(k));
						System.out.print(coeffs[k-1] + " ");
					}
					System.out.println();
				}
			}
		}
		catch(SQLException e)
		{
			// if the error message is "out of memory",
			// it probably means no database file is found
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		finally
		{
			try
			{
				if(connection != null)
					connection.close();
			}
			catch(SQLException e)
			{
				// connection close failed.
				System.err.println(e);
			}
		}
		loadEbelAndShellTables(false);
	}

	// Ebel table

	private static String[] coefficientIDs = {"coeff1", "coeff2", "coeff3", "coeff4", "coeff5", "coeff6"};
	private static String[] a_rho = {"A", "rho"};
	private static String[] energyLevels = {"energy_eV", "jump", "level_width_eV"};
	private static double[] trasformEL = {0.001, 1.0, 0.001};
	private static String[] yield = {"fluo_yield"};
	private static String[] costerKronigLabels = {"f1", "f12", "f13", /*"fp13", */"f23"};
	private static String[] costerKronigLabels_m = {"fM12", "fM13", "fM14", "fM15", "fM23", "fM24", "fM25", "fM34", "fM35", "fM45"};
   private static int costerKronigMax_k = 1;
	private static int costerKronigMax_l = 2;
	private static int costerKronigMax_m = 4;
	private static String[] transitionLabels = {"transition_id", "energy_eV", "probability"};
	private static double[] trasformL = {1.0, 0.001, 1.0};
	private static String[] henkef1f2Labels = {"energy_eV", "f1", "f2"};
	private static double[] trasformH = {0.001, 1.0, 1.0};
	public static Vector<double[]> ebelElastic = null;
	public static Vector<double[]> ebelInelastic = null;
	public static Vector<double[]> aRho = null;
	public static Vector<Vector<double[]>> ebelTauShell = null;
	public static Vector<Vector<double[]>> ebelTauRange = null;
	public static Vector<AtomShellData> shellData = null;
	public static Vector<Vector<double[]>> yieldData = null;
	public static Hashtable<Integer, Hashtable<Integer, double[][]>> costerKronigData = new Hashtable<>();
	public static Vector<Vector<int[]>> transitionShellIDs = null;
	public static Vector<Vector<String>> transitionShellIDlabels = null;
	public static Vector<Vector<double[]>> transitionEnergies = null;
	public static Vector<double[][]> henkeEnergyf1f2 = null;
	public static double linesMinimumEnergy = 0.1;
   public static Hashtable<Integer, Vector<double[]>> auger_probability = new Hashtable<Integer, Vector<double[]>>();
	public static boolean ebelAndShellLoaded = false;


	public static boolean loadEbelAndShellTables(boolean forceReload) {

		if (ebelElastic != null && !forceReload)
			return true;

		linesMinimumEnergy = MaudPreferences.getDouble("fluorescenceLines.minimum_keV", linesMinimumEnergy);

		// load the sqlite-JDBC driver using the current class loader
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		Connection connection = null;
		try {
			// create a database connection
			String xray_database = MaudPreferences.getPref("fluorescence.database", Constants.documentsDirectory + "xraydata.db");
			connection = DriverManager.getConnection("jdbc:sqlite:" + xray_database);
//			connection = DriverManager.getConnection("jdbc:sqlite:" + xray_database);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.

			ebelElastic = new Vector<double[]>(atomsNumber, 1);
			ResultSet rs = statement.executeQuery("SELECT * FROM xraydata_ebel_elastic");
			while(rs.next()) {
				double[] coeffs = new double[coefficientIDs.length];
				for (int k = 0; k < coefficientIDs.length; k++)
					coeffs[k] = rs.getDouble(coefficientIDs[k]);
				ebelElastic.add(coeffs);
			}

			ebelInelastic = new Vector<>(atomsNumber, 1);
			rs = statement.executeQuery("SELECT * FROM xraydata_ebel_inelastic");
			while(rs.next()) {
				double[] coeffs = new double[coefficientIDs.length];
				for (int k = 0; k < coefficientIDs.length; k++)
					coeffs[k] = rs.getDouble(coefficientIDs[k]);
				ebelInelastic.add(coeffs);
			}


			Hashtable<Integer, double[][]> costerKronigData_k = new Hashtable<>(atomsNumber, 1);
			for (int j = 0; j < atomsNumber; j++) {
            double[][] coeffs = new double[costerKronigMax_k][costerKronigMax_k];
            costerKronigData_k.put(j, coeffs);
			}
         costerKronigData.put(K, costerKronigData_k);

			Hashtable<Integer, double[][]> costerKronigData_l = new Hashtable<>(atomsNumber, 1);
			rs = statement.executeQuery("SELECT * FROM xraydata_coster_kronig_l");
			while(rs.next()) {
				int z_id = rs.getInt("Z_id");
				double[][] coeffs = new double[costerKronigMax_l][costerKronigMax_l];
				for (int k = 0; k < costerKronigMax_l; k++)
               for (int l = 0; l < k + 1; l++) {
                  StringBuffer tmp = new StringBuffer("f");
                  tmp.append(String.valueOf(l + 1));
                  tmp.append(String.valueOf(k + 2));
                  coeffs[l][k] = rs.getDouble(tmp.toString());
               }
				costerKronigData_l.put(z_id - 1, coeffs);
			}
         costerKronigData.put(L1, costerKronigData_l);

			Hashtable<Integer, double[][]> costerKronigData_m = new Hashtable<>(atomsNumber, 1);
			rs = statement.executeQuery("SELECT * FROM xraydata_coster_kronig_m");
//         System.out.println("Reading xraydata_coster_kronig_m:");
			while(rs.next()) {
				int z_id = rs.getInt("Z_id");
//            System.out.println("AtomSite: " + z_id);
				double[][] coeffs = new double[costerKronigMax_m][costerKronigMax_m];
//            System.out.print("Coeff: ");
				for (int k = 0; k < costerKronigMax_m; k++)
               for (int l = 0; l < k + 1; l++)  {
                  StringBuffer tmp = new StringBuffer("fM");
                  tmp.append(String.valueOf(l + 1));
                  tmp.append(String.valueOf(k + 2));
                  coeffs[l][k] = rs.getDouble(tmp.toString());
//                  System.out.print(" " + tmp.toString() + "=" + coeffs[l][k] + " ");
               }
				costerKronigData_m.put(z_id - 1, coeffs);
//            System.out.println();
			}
         costerKronigData.put(M1, costerKronigData_m);

			aRho = new Vector<>(atomsNumber, 1);
			ebelTauShell = new Vector<>(atomsNumber, 1);
			ebelTauRange = new Vector<>(atomsNumber, 1);
			shellData = new Vector<>(atomsNumber, 1);
			yieldData = new Vector<>(atomsNumber, 1);
			transitionShellIDs = new Vector<>(atomsNumber, 1);
			transitionEnergies = new Vector<>(atomsNumber, 1);
			transitionShellIDlabels = new Vector<>(atomsNumber, 1);
			henkeEnergyf1f2 = new Vector<>(atomsNumber, 1);
			for (int i = 0; i < atomsNumber; i++) {
				aRho.add(new double[0]);
				ebelTauShell.add(null);
				ebelTauRange.add(null);
				shellData.add(null);
				yieldData.add(null);
				transitionEnergies.add(null);
				transitionShellIDs.add(null);
				transitionShellIDlabels.add(null);
				henkeEnergyf1f2.add(null);
			}

			Vector<double[]> oneElement = new Vector<>();
			Vector<String> idElement = new Vector<>();
			double[] coeffs = new double[yield.length];
			oneElement.add(coeffs);
			yieldData.setElementAt(oneElement, 0); // we set 0 for the H and He that are not in the database
			yieldData.setElementAt(oneElement, 1);

			oneElement = new Vector<double[]>();
			for (int i = 0; i < 4; i++) // no transitions for the first 4 elements
				transitionEnergies.setElementAt(oneElement, i);
			Vector<int[]> secondElement = new Vector<>();
			for (int i = 0; i < 4; i++) // no transitions for the first 4 elements
				transitionShellIDs.setElementAt(secondElement, i);
			for (int i = 0; i < 4; i++) // no transitions for the first 4 elements
				transitionShellIDlabels.setElementAt(idElement, i);

			rs = statement.executeQuery("SELECT * FROM xraydata_ebel_a_rho");
			while(rs.next()) {
				int z_id = rs.getInt("z_id");
				coeffs = new double[a_rho.length];
				for (int k = 0; k < a_rho.length; k++)
					coeffs[k] = rs.getDouble(a_rho[k]);
				aRho.setElementAt(coeffs, z_id - 1);
//				System.out.println("Setting rho for element: " + z_id);
			}

			for (int z_id = 0; z_id < aRho.size(); z_id++) {

				oneElement = new Vector<double[]>();
				String elementNumberAsString = Integer.toString(z_id + 1);
				ResultSet rsr = statement.executeQuery("SELECT * FROM xraydata_ebel_tau_shell WHERE Z_id = " +
						elementNumberAsString);
				while (rsr.next()) {
//					String shellID = rsr.getString("shell_id");
					coeffs = new double[coefficientIDs.length];
					for (int k = 0; k < coefficientIDs.length; k++)
						coeffs[k] = rsr.getDouble(coefficientIDs[k]);
					oneElement.add(coeffs);
				}
				ebelTauShell.setElementAt(oneElement, z_id);

				oneElement = new Vector<double[]>();
				rsr = statement.executeQuery("SELECT * FROM xraydata_ebel_tau_range WHERE Z_id = " +
						elementNumberAsString);
				while (rsr.next()) {
//					String shellID = rsr.getString("gen_shell_id");
					coeffs = new double[coefficientIDs.length];
					for (int k = 0; k < coefficientIDs.length; k++)
						coeffs[k] = rsr.getDouble(coefficientIDs[k]);
					oneElement.add(coeffs);
				}
				ebelTauRange.setElementAt(oneElement, z_id);

				AtomShellData dataElement = new AtomShellData(z_id);
				rsr = statement.executeQuery("SELECT * FROM xraydata_shell_data WHERE Z_id = " +
						elementNumberAsString);
				while (rsr.next()) {
					String shellID = rsr.getString("shell_id");
					ShellDataAndID newData = new ShellDataAndID(shellID);
					coeffs = new double[energyLevels.length];
					for (int k = 0; k < energyLevels.length; k++)
						coeffs[k] = rsr.getDouble(energyLevels[k]) * trasformEL[k];  // in KeV
					newData.addData(coeffs);
					dataElement.addData(newData);
				}
//				System.out.println("Setting shellData for element: " + z_id + ", number of shells: " + oneElement.size());
				shellData.setElementAt(dataElement, z_id);
/*				System.out.println(z_id + " ");
				for (int i = 0; i < oneElement.size(); i++)
					System.out.print(oneElement.elementAt(i)[0] + " ");
				System.out.println();*/

				oneElement = new Vector<double[]>();
				rsr = statement.executeQuery("SELECT * FROM xraydata_fluo_yield WHERE Z_id = " +
						elementNumberAsString);
				while (rsr.next()) {
//					String shellID = rsr.getString("shell_id");
					coeffs = new double[yield.length];
					for (int k = 0; k < yield.length; k++)
						coeffs[k] = rsr.getDouble(yield[k]);
					oneElement.add(coeffs);
				}
				yieldData.setElementAt(oneElement, z_id);

				oneElement = new Vector<>();
				secondElement = new Vector<>();
				idElement = new Vector<>();
				rsr = statement.executeQuery("SELECT * FROM xraydata_transition_data WHERE Z_id = " +
						elementNumberAsString);
				while (rsr.next()) {
//					String shellID = rsr.getString("shell_id");
					String transitionID = rsr.getString(transitionLabels[0]);
					boolean intrashell = isIntraShell(transitionID);
					if (!intrashell) {
						coeffs = new double[transitionLabels.length - 1];
						for (int k = 1; k < transitionLabels.length; k++)
							coeffs[k - 1] = rsr.getDouble(transitionLabels[k]) * trasformL[k];
						oneElement.add(coeffs);
						int[] shellNumbers = new int[2];
						idElement.add(transitionID);
						if (transitionID.startsWith("K")) {
							shellNumbers[0] = K;
							shellNumbers[1] = getShellNumberFromLabel(transitionID.substring(1));
						} else {
							shellNumbers[0] = getShellNumberFromLabel(transitionID.substring(0, 2));
							shellNumbers[1] = getShellNumberFromLabel(transitionID.substring(2, 4));
						}
						secondElement.add(shellNumbers);
					}
				}
				transitionEnergies.setElementAt(oneElement, z_id);
				transitionShellIDs.setElementAt(secondElement, z_id);
				transitionShellIDlabels.setElementAt(idElement, z_id);

				oneElement = new Vector<>();
				rsr = statement.executeQuery("SELECT * FROM xraydata_henke_f1f2 WHERE Z_id = " +
						elementNumberAsString);
				while (rsr.next()) {
					coeffs = new double[henkef1f2Labels.length];
					for (int k = 0; k < henkef1f2Labels.length; k++)
						coeffs[k] = rsr.getDouble(henkef1f2Labels[k]) * trasformH[k];
					oneElement.add(coeffs);
				}
				double[][] anElement = new double[henkef1f2Labels.length][oneElement.size()];
				for (int i = 0; i < oneElement.size(); i++) {
					coeffs = oneElement.elementAt(i);
					for (int j = 0; j < henkef1f2Labels.length; j++)
						anElement[j][i] = coeffs[j];
				}
				henkeEnergyf1f2.setElementAt(anElement, z_id);
			}
		} catch(SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			e.printStackTrace();
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// connection close failed.
				e.printStackTrace();
				System.err.println(e);
			}
		}
/*		System.out.println("Sensitivity: Fe K L1");
		for (double energy = 1.0; energy <= 20.0; energy += 1.0)
			System.out.println(energy + " " + getSensitivity(25, K, energy) * getFluorescenceYield(25, K) + " " + getSensitivity(25, L1, energy) * getFluorescenceYield(25, L1));
		System.out.println("Sensitivity: Pb M2 M5");
		for (double energy = 1.0; energy <= 20.0; energy += 1.0)
			System.out.println(energy + " " + getSensitivity(81, M2, energy) * getFluorescenceYield(81, M2) + " " + getSensitivity(81, M5, energy) * getFluorescenceYield(81, M5));
		System.out.println("Tau: Fe K L1");
		for (double energy = 1.0; energy <= 20.0; energy += 1.0)
			System.out.println(energy + " " + getTauShell(25, K, energy) + " " + getTauShell(25, L1, energy));
		System.out.println("Tau: Pb M2 M5");
		for (double energy = 1.0; energy <= 20.0; energy += 1.0)
			System.out.println(energy + " " + getTauShell(81, M2, energy) + " " + getTauShell(81, M5, energy));*/
		return true;
	}

	public static boolean isIntraShell(String transitionID) {
		if (getFirstLetter(transitionID) == getSecondLetter(transitionID))
			return true;
		return false;
	}

	public static char getFirstLetter(String transitionID) {
		if (transitionID.length() > 0)
			return transitionID.toUpperCase().charAt(0);
		return 'X';
	}

	public static char getSecondLetter(String transitionID) {
		int index = 1;
		while (index < transitionID.length()) {
			char letter = transitionID.toUpperCase().charAt(index++);
			if (!StringNumber.is0to9(letter))
				return letter;
		}
		return 'X';
	}

	public static int getShellNumberFromLabel(String label) {
		for (int i = 0; i < shellIDs.length; i++)
			if (label.equalsIgnoreCase(shellIDs[i]))
				return i;
		return -1; // not recognized
	}

	public static double getTotalAbsorptionForAtomAndEnergy(int atomNumber, double energyInKeV) {
		atomNumber--; // start from 0
		if (atomNumber < 0 || atomNumber >= ebelElastic.size())
			return 0.0;
		loadEbelAndShellTables(false);
		return getCoherentScatteringForAtomAndEnergy(atomNumber, energyInKeV) +
				getIncoherentScatteringForAtomAndEnergy(atomNumber, energyInKeV) +
				getPhotoAbsorptionForAtomAndEnergy(atomNumber, energyInKeV);
	}

	public static double[] getF1F2FromHenkeForAtomAndEnergy(int atomNumber, double energyInKeV) {
		atomNumber--; // start from 0
		loadEbelAndShellTables(false);
		double[] f1f2 = new double[2];
		if (atomNumber >= 0 || atomNumber < henkeEnergyf1f2.size()) {
			double[][] energyf1f2 = henkeEnergyf1f2.elementAt(atomNumber);
			int index = 1;
			while (index < energyf1f2[0].length-1 && energyInKeV > energyf1f2[0][index]) {
				index++;
			}
			int lowindex = index - 1;
			double x_part = (energyInKeV - energyf1f2[0][lowindex]) / (energyf1f2[0][index] - energyf1f2[0][lowindex]);
			f1f2[0] = energyf1f2[1][lowindex] + (energyf1f2[1][index] - energyf1f2[1][lowindex]) * x_part;
			f1f2[1] = energyf1f2[2][lowindex] + (energyf1f2[2][index] - energyf1f2[2][lowindex]) * x_part;
		}
//		System.out.println("AtomSite #:" + (atomNumber + 1) + " " + energyInKeV + " " + f1f2[0] + " " + f1f2[1]);
		return f1f2;
	}

	public static double getCoherentScatteringForAtomAndEnergy(int atomNumber_1, double energyInKeV) {
		return MoreMath.getEbelLogarithmicInterpolation(ebelElastic.elementAt(atomNumber_1), energyInKeV);
	}

	public static double getIncoherentScatteringForAtomAndEnergy(int atomNumber_1, double energyInKeV) {
		return MoreMath.getEbelLogarithmicInterpolation(ebelInelastic.elementAt(atomNumber_1), energyInKeV);
	}

	public static double getPhotoAbsorptionForAtomAndEnergyDiv(int atomNumber_1, double energyInKeV) {
		int shellNumber = getHighestShellIdForAtomAndEnergy(atomNumber_1, energyInKeV);
		if (shellNumber < 0)
			return -1;
		int mainShell = mainShellNumber[shellNumber];
		Vector<double[]> elementData = ebelTauRange.elementAt(atomNumber_1);
		if (mainShell >= elementData.size())
			mainShell = elementData.size() - 1;
//		System.out.println((atomNumber_1 + 1) + " " + energyInKeV + " " + shellNumber + " " + mainShellNumber);
		double result = MoreMath.getEbelLogarithmicInterpolation(elementData.elementAt(mainShell), energyInKeV);
		// now we divide by the jump ratio for certain shellIDs
/*		if (shellNumber > L1 && shellNumber < M1) {
			AtomShellData atomData = shellData.elementAt(atomNumber_1);
//			Vector<double[]> shellEnergiesAndJumps = shellData.elementAt(atomNumber_1);
//			System.out.print("Lx, Dividing by: ");
			for (int i = shellNumber; i > L1; i--) {
				result /= atomData.data.elementAt(i - 1).data[1];
//				System.out.print(atomData.data.elementAt(i - 1).data[1] + " ");
			}
//			System.out.println();
		} else if (shellNumber > M1 && shellNumber < N1) {
			AtomShellData atomData = shellData.elementAt(atomNumber_1);
			// Vector<double[]> shellEnergiesAndJumps = shellData.elementAt(atomNumber_1);
//			System.out.print("Mx, Dividing by: ");
			for (int i = shellNumber; i > M1; i--) {
				result /= atomData.data.elementAt(i - 1).data[1];
//				System.out.print(atomData.data.elementAt(i - 1).data[1] + " ");
			}
//			System.out.println();
		}*/
		return result;
	}

	public static double getPhotoAbsorptionForAtomAndEnergy(int atomNumber_1, double energyInKeV) {
		int shellNumber = getHighestShellIdForAtomAndEnergy(atomNumber_1, energyInKeV);
		if (shellNumber < 0)
			return 0;
		int mainShell = mainShellNumber[shellNumber];
		Vector<double[]> elementData = ebelTauRange.elementAt(atomNumber_1);
		if (mainShell >= elementData.size())
			return 0;
//		System.out.println((atomNumber_1 + 1) + " " + energyInKeV + " " + shellNumber + " " + mainShellNumber);
		double result = MoreMath.getEbelLogarithmicInterpolation(elementData.elementAt(mainShell), energyInKeV);
		// now we divide by the jump ratio for certain shellIDs
		if (shellNumber > L1 && shellNumber < M1) {
			if (mainShell + 1 < elementData.size()) {
				AtomShellData atomData = shellData.elementAt(atomNumber_1);
				double result1 = MoreMath.getEbelLogarithmicInterpolation(elementData.elementAt(mainShell + 1), energyInKeV);
				double delta = result - result1;
				for (int i = shellNumber; i > L1; i--)
					result -= delta / atomData.data.elementAt(i - 1).data[1];
			} else {
//				System.out.println((atomNumber_1 + 1) + " " + energyInKeV + " " + elementData.size() + " " + mainShell + " " + shellNumber);
			}
		} else if (shellNumber > M1 && shellNumber < N1) {
			if (mainShell + 1 < elementData.size()) {
				AtomShellData atomData = shellData.elementAt(atomNumber_1);
				double result1 = MoreMath.getEbelLogarithmicInterpolation(elementData.elementAt(mainShell + 1), energyInKeV);
				double delta = result - result1;
				for (int i = shellNumber; i > M1; i--)
					result -= delta / atomData.data.elementAt(i - 1).data[1];
			} else {
//				System.out.println((atomNumber_1 + 1) + " " + energyInKeV + " " + elementData.size() + " " + mainShell + " " + shellNumber);
			}
		}
		return result;
	}

	public static int getHighestShellIdForAtomAndEnergy(int atomNumber_1, double energyInKeV) {
		try {
			AtomShellData atomData = shellData.elementAt(atomNumber_1);
			if (atomData == null) {
				System.out.println("Warning: problem with atom number(-1) " + atomNumber_1);
			} else {
//			System.out.println("Finding shell for energy: " + energyIneV + " and atom number: " + atomNumber_1);
				for (int i = 0; i < atomData.data.size(); i++) {
//				System.out.println("Check energy: " + shellEnergies.elementAt(i)[0]);
					if (atomData.data.elementAt(i).data[0] <= energyInKeV)
						return i;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception retrieving photo absorption data for atom number: " + atomNumber_1 + ", at energy: " + energyInKeV + " KeV");
			e.printStackTrace(System.out);
		}
		return -1;
	}

	public static double getFluorescenceYield(int atomNumber_1, int shellID) {
		if (shellID < 0 || shellID >= yieldData.elementAt(atomNumber_1).size())
			return 0;
		return yieldData.elementAt(atomNumber_1).elementAt(shellID)[0];
	}

	public static double getJumpRatio(int atomNumber_1, int shellID) {
		ShellDataAndID data = shellData.elementAt(atomNumber_1).getDataFor(shellID);
		if (data != null)
			return data.data[1];
		return 0;
	}

	public static double getAbsorptionEdge(int atomNumber_1, int shellID) {
		ShellDataAndID data = shellData.elementAt(atomNumber_1).getDataFor(shellID);
		if (data != null)
			return data.data[0];
		return 0;
	}

	public static double getTauShell(int atomNumber_1, int shellID, double energyInKeV) {
//		System.out.println(atomNumber_1 + " " + shellID + " " + energyInKeV + " " + getAbsorptionEdge(atomNumber_1, shellID));
		if (shellID < 0) // || getAbsorptionEdge(atomNumber_1, shellID) > energyInKeV)
			return 0;
		if (shellID >= ebelTauShell.elementAt(atomNumber_1).size())
			shellID = ebelTauShell.elementAt(atomNumber_1).size() - 1;
		return MoreMath.getEbelLogarithmicInterpolation(ebelTauShell.elementAt(atomNumber_1).elementAt(shellID),
				energyInKeV);
	}
        
   public static double getAugerProbability(int atom_number_1, int innerShell, int outerShell1, int outerShell2) {
      return 0.0;
   }

	public static double getSensitivityByCascade(int atomNumber_1, int shellID, double energyInKeV) {
            
            double absorptionEdgeEnergy = getAbsorptionEdge(atomNumber_1, shellID);
            boolean energy_over_shell = (energyInKeV >= absorptionEdgeEnergy);
            if (!energy_over_shell)
                return getSensitivity(atomNumber_1, shellID, energyInKeV);
            
            double[] ck_coeff = new double[1];
            int shellID1 = shellID + 1;
            double[] fluorescence_yeld = new double[shellID1];
            double[] shellSensitivity = new double[shellID1];
            double[] tau_shell = new double[shellID1];
            for (int i = 0; i < shellID1; i++) {
                fluorescence_yeld[i] = getFluorescenceYield(atomNumber_1, i);
                tau_shell[i] = getTauShell(atomNumber_1, i, energyInKeV);
            }
            if (shellID >= K)
                shellSensitivity[K] = tau_shell[K];
            else 
                return 0;
            if (shellID >= L1) {
                double aug_K_L1_out1 = getAugerProbability(atomNumber_1, K, L1, -1);
                double aug_K_L1_out2 = getAugerProbability(atomNumber_1, K, -1, L1);
                double pare_K_L1 = aug_K_L1_out1 + aug_K_L1_out2;
                shellSensitivity[L1] = tau_shell[L1] + (1.0 - fluorescence_yeld[K]) * tau_shell[K] * pare_K_L1;
            } else return shellSensitivity[shellID];
/*
            case L1:
				sensitivity = getTauShell(atomNumber_1, shellID, energyInKeV);
//				System.out.println((atomNumber_1 + 1) + " " + shellID + " " + energyInKeV + " " + sensitivity + " " + ck_coeff[0]);
				break;
			case L2:
				ck_coeff = costerKronigData_l.elementAt(atomNumber_1);
				sensitivity = getTauShell(atomNumber_1, L2, energyInKeV) +
						ck_coeff[1] * getTauShell(atomNumber_1, L1, energyInKeV);
				break;
			case L3:
				ck_coeff = costerKronigData_l.elementAt(atomNumber_1);
				sensitivity = getTauShell(atomNumber_1, L3, energyInKeV) +
						ck_coeff[4] * getTauShell(atomNumber_1, L2, energyInKeV) +
						(ck_coeff[2] + ck_coeff[3] + ck_coeff[1] * ck_coeff[4]) * getTauShell(atomNumber_1, L1, energyInKeV);
				break;
			default: {}
		}*/
//		System.out.println((atomNumber_1 + 1) + " " + shellID + " " + energyInKeV + " " + sensitivity);
            return shellSensitivity[shellID];
	}
   
	public static double getSensitivity(int atomNumber_1, int shellID, double energyInKeV) {
      if (mainShellNumber[shellID] > 2) return 0;
		int msi = mainShellIndex[shellID];
		Hashtable<Integer, double[][]> ckv = costerKronigData.get(msi);
		if (ckv == null) return 0;
		double[][] ck_coeff = ckv.get(atomNumber_1);
		if (ck_coeff == null) return 0;
      double[] sensitivityTr = new double[shellID - msi + 1];
//		if (atomNumber_1 == 39)
//			System.out.println(energyInKeV + " " + getTauShell(atomNumber_1, 0, energyInKeV));
      for (int i = msi; i <= shellID; i++) {
         sensitivityTr[i - msi] = getTauShell(atomNumber_1, i, energyInKeV);
         for (int j = msi; j < i; j++)
	         sensitivityTr[i - msi] += sensitivityTr[j - msi] * ck_coeff[j - msi][i - msi - 1];
      }
 		return sensitivityTr[shellID - msi];
  	}

	public static Vector<FluorescenceLine> getFluorescenceLinesFor(int atomNumber, double energyInKeV) {
		return getFluorescenceLinesFor(atomNumber, energyInKeV, linesMinimumEnergy);
	}

	public static void checkMinimumEnergy() {
		linesMinimumEnergy = MaudPreferences.getDouble("fluorescenceLines.minimum_keV", linesMinimumEnergy);
	}

	public static Vector<FluorescenceLine> getFluorescenceLinesFor(int atomNumber, double energyInKeV,
	                                                               double minimumEnergyInKeV) {
		atomNumber--;
		Vector<FluorescenceLine> linesForAtom = new Vector(0, 10);
		loadEbelAndShellTables(false);

		Vector<int[]> shellIDsData = transitionShellIDs.elementAt(atomNumber);
		Vector<double[]> shellEnergies = transitionEnergies.elementAt(atomNumber);
		Vector<String> idLabels = transitionShellIDlabels.elementAt(atomNumber);
		for (int i = 0; i < shellIDsData.size(); i++) {
			double[] transitionEnergy = shellEnergies.elementAt(i);
			int innerShell = shellIDsData.elementAt(i)[0];
			String id = idLabels.elementAt(i);
			if (innerShell >= 0 && transitionEnergy[0] > minimumEnergyInKeV && energyInKeV > transitionEnergy[0]) {
				double fluorescenceYield = getFluorescenceYield(atomNumber, innerShell);
				double sensitivity = getSensitivity(atomNumber, innerShell, energyInKeV);
				FluorescenceLine aLine = new FluorescenceLine(transitionEnergy[0], innerShell, getAbsorptionEdge(atomNumber, innerShell), id);
				aLine.setFluorescenceYield(fluorescenceYield);
//				if (atomNumber == 39)
//					System.out.println(energyInKeV + " " + transitionEnergy[0] + " " + sensitivity + " " +
//							innerShell + " " + transitionEnergy[1] + " " + fluorescenceYield + " " + getAbsorptionEdge(atomNumber, innerShell));
				if (sensitivity < 0)
					sensitivity = 0;
				aLine.setIntensity(fluorescenceYield * sensitivity * transitionEnergy[1]); // this is the probability
				aLine.setTransitionProbability(transitionEnergy[1]);
				linesForAtom.addElement(aLine);
			}
		}
		return linesForAtom;
	}

	public static Vector<FluorescenceLine> getFluorescenceLinesNoSensitivityFor(int atomNumber, double energyInKeV) {
//		linesMinimumEnergy = MaudPreferences.getDouble("fluorescenceLines.minimum_keV", linesMinimumEnergy);
		return getFluorescenceLinesNoSensitivityFor(atomNumber, energyInKeV, linesMinimumEnergy);
	}

	public static Vector<FluorescenceLine> getFluorescenceLinesNoSensitivityFor(int atomNumber, double energyInKeV,
	                                                               double minimumEnergyInKeV) {
		atomNumber--;
		Vector<FluorescenceLine> linesForAtom = new Vector(0, 10);
		loadEbelAndShellTables(false);

		Vector<int[]> shellIDsData = transitionShellIDs.elementAt(atomNumber);
		Vector<double[]> shellEnergies = transitionEnergies.elementAt(atomNumber);
		Vector<String> idLabels = transitionShellIDlabels.elementAt(atomNumber);
		for (int i = 0; i < shellIDsData.size(); i++) {
			double[] transitionEnergy = shellEnergies.elementAt(i);
			int innerShell = shellIDsData.elementAt(i)[0];
			String id = idLabels.elementAt(i);
			if (innerShell >= 0 && transitionEnergy[0] > minimumEnergyInKeV && energyInKeV > transitionEnergy[0]) {
				double fluorescenceYield = getFluorescenceYield(atomNumber, innerShell);
				FluorescenceLine aLine = new FluorescenceLine(transitionEnergy[0], innerShell, getAbsorptionEdge(atomNumber, innerShell), id);
				aLine.setFluorescenceYield(fluorescenceYield);
				aLine.setIntensity(fluorescenceYield * transitionEnergy[1]); // this is the probability without sensitivity
				aLine.setTransitionProbability(transitionEnergy[1]);
				linesForAtom.addElement(aLine);
/*				if (atomNumber > 80)
					System.out.println(atomNumber + ", Line: " + energyInKeV + " " + transitionEnergy[0] + " " +
							innerShell + " " + transitionEnergy[1] + " " + fluorescenceYield + " " + getAbsorptionEdge(atomNumber, innerShell));*/
			}
		}
		return linesForAtom;
	}

}

class AtomShellData {
	int atomNumber = -1;
	Vector<ShellDataAndID> data;

	public AtomShellData(int anAtomNumber) {
		atomNumber = anAtomNumber;
		data = new Vector<ShellDataAndID>();
	}

	public void addData(ShellDataAndID newData) {
		data.add(newData);
	}

	public ShellDataAndID getDataFor(int shellID) {
		for (int i = 0; i < data.size(); i++)
			if (data.elementAt(i).shellID == shellID)
				return data.elementAt(i);
		return null;
	}
}

class ShellDataAndID {
	int shellID = -1;
	double[] data = null;

	public ShellDataAndID(String shellIDs) {
		shellID = XRayDataSqLite.getShellNumberFromLabel(shellIDs);
	}

	public void addData(double[] newData) {
		data = newData;
	}
}

