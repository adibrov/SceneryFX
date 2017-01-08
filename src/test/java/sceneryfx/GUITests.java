package sceneryfx;

import coremem.enums.NativeTypeEnum;
import io.scif.img.ImgOpener;
import javafxgui.JavaFXGUIControlPanelJava;
import javafxgui.JavaFXGUIControlPanelKotlin;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.junit.Test;

/**
 * Created by dibrov on 08/01/17.
 */
public class GUITests {
    @Test
    public void GUIMainTest() {

        // Greetings!
        System.out.println("Hello world! About to start a JavaFXGUIControlPanelKotlin test...");

        try {
            // Loading a stack for the model
            Img<UnsignedByteType> tModelStack = (Img<UnsignedByteType>) new ImgOpener().openImgs("" +
                    "./pics/149_fused" +
                    ".tif").get(0);

            // Creating a model
            AcquisitionGUIModelJava tModel = new AcquisitionGUIModelJava(tModelStack, 5, 5);

            // Creating a ClearVolume renderer unit
            ClearVolumeUnit tCVUnit = new ClearVolumeUnit(tModel.getSubStack(0, 0), 100, 100, 50, NativeTypeEnum
                    .UnsignedByte);
            tCVUnit.initializeAndShow();

            // Creating a JavaFXGUIControlPanelKotlin
            JavaFXGUIControlPanelJava.start(tModel, tCVUnit);


            // Creating a scenery model
            RenderModelJava tRenderModel = new RenderModelJava(tModel, null, null, null);
            tRenderModel.main();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
