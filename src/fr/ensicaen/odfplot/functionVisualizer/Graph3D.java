package fr.ensicaen.odfplot.functionVisualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.util.LinkedList;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.LineArray;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import fr.ensicaen.odfplot.engine.GenerateurPlageCouleur;
import fr.ensicaen.odfplot.engine.ODFPoint;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Graph3D extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double min, max;

	private final int NBR_MAX = 4000;

	private Transform3D trans;

	private TransformGroup objSpin;

	private TransformGroup objRotIni, objRotIni2;

	private Transform3D tempspin;

	private Transform3D spin;

	private GenerateurPlageCouleur cou; // Degrade de couleur.

	private Frame3D parent;

	private BoundingSphere bounds;

	// variable necessaire e l'affichage en 3 Dimensions

	private BranchGroup root;

	private BranchGroup scene;

	private Canvas3D canvas3D;

	private SimpleUniverse u;

	private LineArray la;

	private Shape3D shape;

	/*
	 * compisition du graphe scenique.
	 */

	public BranchGroup createSceneGraph(SimpleUniverse su) {
		// cree la racine du graphe

		BranchGroup objRoot = new BranchGroup();
		TransformGroup objTrans = new TransformGroup();
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objTrans.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		BoundingSphere bounds;
		bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

		// lumiere directionnelle
		Vector3f ldir = new Vector3f(1.0F, 1.0F, -1.0F);
		Color3f lcouldl = new Color3f(1.0F, 1.0F, 1.0F);
		DirectionalLight dl = new DirectionalLight(lcouldl, ldir);
		dl.setInfluencingBounds(bounds);

		// lumiere ambiante
		Color3f lcoulal = new Color3f(1.0F, 1.0F, 1.0F);
		AmbientLight al = new AmbientLight(lcoulal);
		al.setInfluencingBounds(bounds);

		objRoot.addChild(al);
		objRoot.addChild(dl);

		/*
		 * creation et positionnnement des elements
		 */

		LinkedList<ODFPoint> liste = this.parent.getFonction().getListePoints();

		for (ODFPoint pttmp : liste) {

			if (pttmp.getValeur() > min && pttmp.getValeur() < max) {

				cou = new GenerateurPlageCouleur(this.parent.getFonction());
				Color c;

				double x = (double) pttmp.getAlpha();
				double y = (double) pttmp.getBeta();
				double z = (double) pttmp.getGamma();

				c = cou.doubleVersCouleur(pttmp.getValeur());

				TransformGroup objTrans2 = new TransformGroup();

				Transform3D tmp = new Transform3D();
				tmp.setTranslation(new Vector3d(((x / 100)), ((y / 100)),
						((z / 100))));
				objTrans2.setTransform(tmp);

				// definit l'apparence de la sphere
				Appearance a = new Appearance();
				Material m = new Material();

				// definit la couleur de l'objet.
				m.setDiffuseColor(new Color3f(c));
				m.setAmbientColor(new Color3f(c));
				a.setMaterial(m);

				objTrans2.addChild(new Sphere(0.03F, a));
				objTrans.addChild(objTrans2);
			}

		} // fin positionnement elements
		/*
		 * fin de creation er de positionnement des elements
		 */

		double alpha = this.parent.getFonction().getParametre().getAlphaMax() / 100;
		double beta = this.parent.getFonction().getParametre().getBetaMax() / 100;
		double gamma = this.parent.getFonction().getParametre().getGammaMax() / 100;

		// definit le trace des droites
		Point3d[] pts = new Point3d[6];
		pts[0] = new Point3d(0.0, 0.0, 0.0);
		pts[1] = new Point3d(alpha + 10, 0.0, 0.0);
		pts[2] = new Point3d(0.0, 0.0, 0.0);
		pts[3] = new Point3d(0.0, beta + 10, 0.0);
		pts[4] = new Point3d(0.0, 0.0, 0.0);
		pts[5] = new Point3d(0.0, 0.0, gamma + 10);
		// definit la couleur des traits de chaque axe
		Color3f[] couls = new Color3f[6];
		couls[0] = new Color3f(1.0f, 0.0f, 0.0f);
		couls[1] = new Color3f(1.0f, 0.0f, 0.0f);
		couls[2] = new Color3f(0.0f, 1.0f, 0.0f);
		couls[3] = new Color3f(0.0f, 1.0f, 0.0f);
		couls[4] = new Color3f(0.0f, 0.0f, 1.0f);
		couls[5] = new Color3f(0.0f, 0.0f, 1.0f);

		//
		la = new LineArray(8, LineArray.COORDINATES | LineArray.COLOR_3);
		la.setCoordinates(0, pts);
		la.setColors(0, couls);
		Appearance a = new Appearance();
		PolygonAttributes attr = new PolygonAttributes();
		attr.setCullFace(PolygonAttributes.CULL_NONE);
		a.setPolygonAttributes(attr);
		shape = new Shape3D(la, a);

		objTrans.addChild(shape);
		/*
		 * affichage legende
		 * 
		 */

		// TransformGroup objTrans3 = new TransformGroup();
		// Transform3D tmp1 = new Transform3D();
		// tmp1.setTranslation(new Vector3d(alpha , 0, 0));
		// objTrans3.setTransform(tmp1);
		// Text2D textAlpha =
		// new Text2D(
		// "alpha",
		// new Color3f(1.0f, 1.0f, 1.0f),
		// "Serif",
		// 100,
		// Font.BOLD);
		// objTrans3.addChild(textAlpha);
		// objTrans.addChild(objTrans3);
		//
		// TransformGroup objTrans4 = new TransformGroup();
		// Transform3D tmp2 = new Transform3D();
		// tmp2.setTranslation(new Vector3d(0, beta, 0));
		// objTrans3.setTransform(tmp2);
		// Text2D textBeta =
		// new Text2D(
		// "beta",
		// new Color3f(1.0f, 1.0f, 1.0f),
		// "Serif",
		// 100,
		// Font.BOLD);
		// objTrans4.addChild(textBeta);
		// objTrans.addChild(objTrans4);
		//
		// TransformGroup objTrans5 = new TransformGroup();
		// Transform3D tmp3 = new Transform3D();
		// tmp3.setTranslation(new Vector3d(0, 0, gamma));
		// objTrans3.setTransform(tmp3);
		// Text2D textGamma =
		// new Text2D(
		// "gamma",
		// new Color3f(1.0f, 1.0f, 1.0f),
		// "Serif",
		// 100,
		// Font.BOLD);
		// objTrans5.addChild(textGamma);
		// objTrans.addChild(objTrans5);
		// rotation du graph a l'aide de la souri
		MouseRotate behavior = new MouseRotate(objTrans);
		objTrans.addChild(behavior);
		behavior.setSchedulingBounds(bounds);

		// Zoom
		MouseZoom behavior2 = new MouseZoom(objTrans);
		objTrans.addChild(behavior2);
		behavior2.setSchedulingBounds(bounds);

		// Translation
		MouseTranslate behavior3 = new MouseTranslate(objTrans);
		objTrans.addChild(behavior3);
		behavior3.setSchedulingBounds(bounds);

		objRoot.addChild(objTrans);

		objRoot.compile();
		return objRoot;

	} // fin du graph scenique.

	/*
	 * Constructeur
	 */

	public Graph3D(Frame3D pr, double min, double max) {
		this.parent = pr;
		this.min = min;
		this.max = max;

		setLayout(new BorderLayout());
		GraphicsConfiguration config;
		config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D c = new Canvas3D(config);
		add("Center", c);
		SimpleUniverse u = new SimpleUniverse(c);
		BranchGroup scene = createSceneGraph(u);
		u.getViewingPlatform().setNominalViewingTransform();
		u.addBranchGraph(scene);

	}

}
