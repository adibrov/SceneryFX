package sceneryfx;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOverlay;
import clearvolume.renderer.cleargl.overlay.Overlay;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.io.yaml.YamlConfigIO;

import java.awt.*;
import java.io.IOException;

/**
 * Created by dibrov on 12/01/17.
 */
public class Viewer {
    private AcquisitonModel mAcquisitionModel;
    private Bdv bdv;
    private BdvOverlay mOverlay;

    public Viewer(AcquisitonModel pAcquisitionModel){
        this.mAcquisitionModel = pAcquisitionModel;


        mOverlay = new BdvOverlay() {
            @Override
            protected void draw(Graphics2D g) {
                int i = info.getTimePointIndex();
                for (Rectangle rect: mAcquisitionModel.getGeometry()[i]) {
                    g.draw(rect);
                }
            }
        };
    }

    public void startViewer(){

        InputTriggerConfig conf = null;
        try {
            /* load input config from file */
            conf = new InputTriggerConfig(YamlConfigIO.read("./sandbox/src/main/img/bdvkeyconfig.yaml"));
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
        this.bdv = BdvFunctions.show(mAcquisitionModel.getData(), "reds", Bdv.options().is2D().inputTriggerConfig
                (conf));



        BdvFunctions.showOverlay(mOverlay, "overlay", Bdv.options().addTo(bdv));


    }

    public static void main(String[] args) {
        AcquisitonModel am = new AcquisitonModel(1000,1000,1000);
        Viewer v = new Viewer(am);
        v.startViewer();

        AcquisitionUnit au1 = new AcquisitionUnit(new int[] {0,0,0});
        AcquisitionUnit au2 = new AcquisitionUnit(new int[] {150,150,0});

        am.addAcquisitionUnitWithLabel(au1, new Rectangle(au1.getLocation()[0], au1.getLocation()[1], au1.getDimX(),
                au1.getDimY()), au1.getLocation(), au1.getDimZ());
    }

}
