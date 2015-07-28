package ij;

//import ij.gui.MessageDialog;
import it.unitn.ing.rista.diffr.DataFileSet;
import it.unitn.ing.rista.util.Constants;
import it.unitn.ing.rista.util.Misc;

public class AreaImage extends ImageJ {

  static DataFileSet data = null;

  public AreaImage() {
    super(EMBEDDED);
    setTitle("Area Image");
	  this.exitWhenQuitting(false);
  }

  public void setDataSet(DataFileSet adata) {
    data = adata;
  }

  public static DataFileSet getData() {
    return data;
  }

	public void quit() {
		Prefs.savePreferences();
		if (!WindowManager.closeAllWindows())
			return;
		setVisible(false);
		data = null;
	}

}
