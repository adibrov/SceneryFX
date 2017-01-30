package sceneryfx;

import coremem.enums.NativeTypeEnum;

import java.awt.*;

/**R
 * Created by dibrov on 18/01/17.{}
 */
public class ViewerControlHub {

    public static void main(String[] args) {

        // Sample space
        SampleSpace lSampleSpace = SampleSpace.createStandardSampleSpaceFromFile("./sandbox/src/main/img/WingDiskStack8bit.tif");

        // Acquisition model
        AcquisitonModel lAcquisitionModel = new AcquisitonModel(lSampleSpace);
//        AcquisitonModel lAcquisitionModel = new AcquisitonModel(1000,1000,1000);

        // Viewer
        Viewer lViewer = new Viewer(lAcquisitionModel);

        AcquisitionUnit au1 = new AcquisitionUnit(new long[] {0,0,0});
        AcquisitionUnit au2 = new AcquisitionUnit(new long[] {150,150,0});




        // Starting theR viewer
        lViewer.startViewer();

        // CVU
        ClearVolumeUnit lClearVolumeUnit = new ClearVolumeUnit(NativeTypeEnum.UnsignedByte, true);
        byte[] byteArr = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        lClearVolumeUnit.initializeAndShowInSwingWindow(byteArr,3,3,3);

        // Java fx panel
        JavaFXPanel.start(lViewer, lClearVolumeUnit);


    }
}
