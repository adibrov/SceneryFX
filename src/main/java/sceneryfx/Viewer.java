package sceneryfx;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOverlay;
import clearvolume.renderer.cleargl.overlay.Overlay;
import javafx.util.Pair;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.io.yaml.YamlConfigIO;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dibrov on 12/01/17.
 */
public class Viewer {
    private AcquisitonModel mAcquisitionModel;
    private Bdv bdv;
    private BdvOverlay mOverlay;
    private Color mColor;
    private StackSelectionLabel mStackSelectionLabel;
    private MouseMotionListener mMouseMotionListener;

    public Viewer(AcquisitonModel pAcquisitionModel){
        this.mAcquisitionModel = pAcquisitionModel;
        this.mColor = new  Color(1.0f,153.0f/255, 51.0f/255, 0.5f);

        this.mMouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Rectangle rect = new Rectangle();
                int lCurrentTimePoint = bdv.getBdvHandle().getViewerPanel().getState().getCurrentTimepoint();
                ArrayList<Rectangle> lLabels = getAcquisitionModel().getGeometry()[lCurrentTimePoint];
                Rectangle lRectToSelect = new Rectangle(0,0,0,0);
                int lRectToSelectArea = 100000000;
                for (int i = 1; i < lLabels.size(); i++){
                    if (lLabels.get(i).contains(e.getX(), e.getY()) && lLabels.get(i).width*lLabels.get(i).height <
                            lRectToSelectArea) {
                        lRectToSelect = lLabels.get(i);
                        lRectToSelectArea = lRectToSelect.height*lRectToSelect.width;
                    }
                }
                lLabels.set(0,lRectToSelect);
            }
        };


        mOverlay = new BdvOverlay() {
            @Override
            protected void draw(Graphics2D g) {
                int lCurrentZSlice = info.getTimePointIndex();
                ArrayList<Rectangle> lRectArray =mAcquisitionModel.getGeometry()[lCurrentZSlice];
                for (int i = 0; i< lRectArray.size(); i++) {
                    if (i != 0) {
                        g.setColor(mColor);
                        g.draw(lRectArray.get(i));
                        g.fill(lRectArray.get(i));
                    }
                    else{
                        g.setColor(mColor);
                        g.setStroke(new BasicStroke(4));
                        g.draw(lRectArray.get(i));
                    }


                }
            }
        };
    }

    public AcquisitonModel getAcquisitionModel() {
        return mAcquisitionModel;
    }

    public StackSelectionLabel getStackSelectionLabel() {
        return mStackSelectionLabel;
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
        this.mStackSelectionLabel = new StackSelectionLabel(bdv.getBdvHandle());



        bdv.getBdvHandle().getViewerPanel().getDisplay().addMouseMotionListener(mMouseMotionListener);

        BdvFunctions.showOverlay(mStackSelectionLabel.getOverlay(), "overlay", Bdv.options().addTo(bdv));
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
