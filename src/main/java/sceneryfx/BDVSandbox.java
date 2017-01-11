package sceneryfx;


import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.scijava.ui.behaviour.*;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.io.InputTriggerDescription;
import org.scijava.ui.behaviour.io.yaml.YamlConfigIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dibrov on 07/01/17.
 */
public class BDVSandbox {

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        Img<UnsignedByteType> img2 = null;
        try {
            img2 = (Img<UnsignedByteType>) (new ImgOpener().openImgs("./sandbox/src/main/img/WingDiskStack8bit.tif").get
                    (0));
        } catch (ImgIOException e) {
            e.printStackTrace();
        }
        InputTriggerConfig conf = null;
        try {
            /* load input config from file */
            conf = new InputTriggerConfig(YamlConfigIO.read("./sandbox/src/main/img/bdvkeyconfig.yaml"));
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }

        //final Bdv bdv3D = BdvFunctions.show( img2, "greens");
        final Bdv bdv2D = BdvFunctions.show(img2, "reds", Bdv.options().is2D().inputTriggerConfig(conf));


        ClickBehaviour cb = new ClickBehaviour() {
            @Override
            public void click(int i, int i1) {
                System.out.println("hello!");
            }
        };

        OverlayBehaviour ob = new OverlayBehaviour(bdv2D.getBdvHandle());




        Behaviour click1 = new ClickBehaviour() {
            @Override
            public void click(int i, int i1) {
                System.out.println("click!");
            }
        };
        // behaviour
        BehaviourMap lBehMap = new BehaviourMap();
        lBehMap.put("clickOB", ob.getBehavior());



        // InputTriggers
        InputTriggerMap lInputTriggerMap = new InputTriggerMap();
        final List< InputTriggerDescription > triggers = new ArrayList<>();
        String[] tr = new String[]{"button1"};
        InputTriggerDescription lInputTriggerDescription = new InputTriggerDescription(tr, "click1", "no");

        InputTriggerConfig lInputTriggerConfig = new InputTriggerConfig(triggers);
        InputTriggerAdder adder = lInputTriggerConfig.inputTriggerAdder(lInputTriggerMap, "all");
        adder.put("clickOB", "button1");

      //  final InputTriggerConfig config = new InputTriggerConfig( triggers );


        bdv2D.getBdvHandle().getTriggerbindings().addBehaviourMap("beh", lBehMap);
        bdv2D.getBdvHandle().getTriggerbindings().addInputTriggerMap("map", lInputTriggerMap, "none");
        BdvFunctions.showOverlay(ob.getOverlay(), "overlay", Bdv.options().addTo(bdv2D));


      //  bdv2D.getBdvHandle().getViewerPanel().addOverlayAnimator(ob.getOverlayAnimator());

        //bdv2D.getBdvHandle().getViewerPanel().getDisplay().repaint();

        try {

            Thread.sleep(100);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
