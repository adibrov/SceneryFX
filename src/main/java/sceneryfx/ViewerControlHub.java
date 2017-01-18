package sceneryfx;

/**
 * Created by dibrov on 18/01/17.
 */
public class ViewerControlHub {

    public static void main(String[] args) {

        // acq model
        AcquisitonModel lAcquisitionModel = new AcquisitonModel(1000, 1000, 1000);



        // viewer
        Viewer lViewer = new Viewer(lAcquisitionModel);

        // java fx
        JavaFXPanel.start();

        lViewer.startViewer();




    }
}
