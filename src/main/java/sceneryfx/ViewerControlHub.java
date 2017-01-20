package sceneryfx;

import java.awt.*;

/**
 * Created by dibrov on 18/01/17.{}
 */
public class ViewerControlHub {

    public static void main(String[] args) {

        // Acquisition model
        AcquisitonModel lAcquisitionModel = new AcquisitonModel(1000, 1000, 1000);

        // Viewer
        Viewer lViewer = new Viewer(lAcquisitionModel);

        AcquisitionUnit au1 = new AcquisitionUnit(new long[] {0,0,0});
        AcquisitionUnit au2 = new AcquisitionUnit(new long[] {150,150,0});


        // Java fx panel
        JavaFXPanel.start(lViewer);

        // Starting theR viewer
        lViewer.startViewer();


    }
}
