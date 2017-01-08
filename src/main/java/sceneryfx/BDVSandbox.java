package sceneryfx;


import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.junit.Test;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.io.yaml.YamlConfigIO;

import java.io.IOException;

/**
 * Created by dibrov on 07/01/17.
 */
public class BDVSandbox {

    public static void main(String[] args) {



        System.setProperty( "apple.laf.useScreenMenuBar", "true" );
        Img<UnsignedByteType> img2 = null;
        try {
            img2 = (Img<UnsignedByteType>)(new ImgOpener().openImgs("./sandbox/src/main/img/149_fused.tif").get(0));
        } catch (ImgIOException e) {
            e.printStackTrace();
        }
        InputTriggerConfig conf = null;
        try
        {
			/* load input config from file */
            conf = new InputTriggerConfig( YamlConfigIO.read( "./sandbox/src/main/img/bdvkeyconfig.yaml" ) );
        }
        catch ( IllegalArgumentException | IOException e )
        {
            e.printStackTrace();
        }

        final Bdv bdv3D = BdvFunctions.show( img2, "greens");
        BdvFunctions.show( img2, "reds", Bdv.options().is2D().inputTriggerConfig( conf )  );

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
