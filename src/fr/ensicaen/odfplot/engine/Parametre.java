package fr.ensicaen.odfplot.engine;

import it.unitn.ing.rista.util.Misc;

public class Parametre {

	/*
	 * Variable d'intance: les variables d'instance sont de simples variables
	 * que possederons tous les objets de la meme classe.
	 */

	private double alphaMin, alphaMax, alphaStep;

	private double betaMin, betaMax, betaStep;

	private double gammaMin, gammaMax, gammaStep;

	private double valeurMin, valeurMax;

	public Parametre() {
		this.valeurMax =0.0;
		this.valeurMin =0.0;
	}

	public Parametre(double alphaMin, double alphaMax, double alphaStep,
			double betaMin, double betaMax, double betaStep) {

		this.alphaMin = alphaMin;
		this.alphaMax = alphaMax;
		this.alphaStep = alphaStep;

		this.betaMin = betaMin;
		this.betaMax = betaMax;
		this.betaStep = betaStep;

	}

	public void print() {
		System.out.println("Alpha:");
		System.out.println("[" + alphaMin + "," + alphaMax + "," + alphaStep
				+ "]");

		System.out.println("Beta:");
		System.out
				.println("[" + betaMin + "," + betaMax + "," + betaStep + "]");

		System.out.println("Gamma :");
	}

	/**
	 * @return
	 */
	public double getAlphaMax() {
		return alphaMax;
	}

	/**
	 * @return
	 */
	public double getAlphaMin() {
		return alphaMin;
	}

	/**
	 * @return
	 */
	public double getAlphaStep() {
		return alphaStep;
	}

	/**
	 * @return
	 */
	public double getBetaMax() {
		return betaMax;
	}

	/**
	 * @return
	 */
	public double getBetaMin() {
		return betaMin;
	}

	/**
	 * @return
	 */
	public double getBetaStep() {
		return betaStep;
	}

	/**
	 * @return
	 */
	public double getGammaMax() {
		return gammaMax;
	}

	/**
	 * @return
	 */
	public double getGammaMin() {
		return gammaMin;
	}

	/**
	 * @return
	 */
	public double getGammaStep() {
		return gammaStep;
	}

	/**
	 * @param d
	 */
	public void setAlphaMax(double d) {
		alphaMax = d;
	}

	/**
	 * @param d
	 */
	public void setAlphaMin(double d) {
		alphaMin = d;
	}

	/**
	 * @param d
	 */
	public void setAlphaStep(double d) {
		alphaStep = d;
	}

	/**
	 * @param d
	 */
	public void setBetaMax(double d) {
		betaMax = d;
	}

	/**
	 * @param d
	 */
	public void setBetaMin(double d) {
		betaMin = d;
	}

	/**
	 * @param d
	 */
	public void setBetaStep(double d) {
		betaStep = d;
	}

	/**
	 * @param d
	 */
	public void setGammaMax(double d) {
		gammaMax = d;
	}

	/**
	 * @param d
	 */
	public void setGammaMin(double d) {
		gammaMin = d;
	}

	/**
	 * @param d
	 */
	public void setGammaStep(double d) {
		gammaStep = d;
	}

	public double getMinValue() {
		return valeurMin;
	}

	public void setValeurMin(double valeurMin) {
		this.valeurMin = valeurMin;
	}

	public double getMaxValue() {
		return valeurMax;
	}

	public void setValeurMax(double valeurMax) {
		this.valeurMax = valeurMax;
	}

}
