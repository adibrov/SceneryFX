package sceneryfx;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOverlay;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.ui.TransformListener;
import org.scijava.ui.behaviour.BehaviourMap;
import org.scijava.ui.behaviour.ClickBehaviour;
import org.scijava.ui.behaviour.InputTriggerAdder;
import org.scijava.ui.behaviour.InputTriggerMap;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.io.InputTriggerDescription;
import org.scijava.ui.behaviour.io.yaml.YamlConfigIO;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.*;

import static tools.RectangleTransform.transformRectangle;

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
    private boolean mSelectionExists;
    private ClickBehaviour mSelectBehaviour;
    private Rectangle mSelectedLabel;

    public Bdv getBdv() {
        return bdv;
    }

    public Viewer(AcquisitonModel pAcquisitionModel) {
        this.mAcquisitionModel = pAcquisitionModel;
        this.mColor = new Color(1.0f, 153.0f / 255, 51.0f / 255, 0.5f);
        this.mSelectionExists = false;
        this.mMouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (!mSelectionExists) {
                    Rectangle rect = new Rectangle();
                    AffineTransform3D lAffine3D = new AffineTransform3D();
                    getBdv().getBdvHandle().getViewerPanel().getState().getViewerTransform(lAffine3D);
                    int lCurrentTimePoint = bdv.getBdvHandle().getViewerPanel().getState().getCurrentTimepoint();
                    ArrayList<Rectangle> lLabels = getAcquisitionModel().getGeometry()[lCurrentTimePoint];
                    Rectangle lRectToSelect = new Rectangle(0, 0, 0, 0);
                    int lRectToSelectArea = 100000000;
                    for (int i = 1; i < lLabels.size(); i++) {
                        rect = transformRectangle(lLabels.get(i), lAffine3D);
                        if (rect.contains(e.getX(), e.getY()) && lLabels.get(i).width * lLabels.get(i).height <
                                lRectToSelectArea) {
                            lRectToSelect = lLabels.get(i);
                            lRectToSelectArea = lRectToSelect.height * lRectToSelect.width;
                        }
                    }
                    lLabels.set(0, lRectToSelect);
                }
            }
        };

        this.mSelectBehaviour = new ClickBehaviour() {
            @Override
            public void click(int i, int i1) {

                if (!mSelectionExists) {
                    Rectangle rect = new Rectangle();
                    AffineTransform3D lAffine3D = new AffineTransform3D();
                    getBdv().getBdvHandle().getViewerPanel().getState().getViewerTransform(lAffine3D);
                    int lCurrentTimePoint = bdv.getBdvHandle().getViewerPanel().getState().getCurrentTimepoint();
                    ArrayList<Rectangle> lLabels = getAcquisitionModel().getGeometry()[lCurrentTimePoint];
                    Rectangle lRectToSelect = new Rectangle(0, 0, 0, 0);
                    int lRectToSelectArea = 100000000;
                    for (int j = 1; j < lLabels.size(); j++) {
                        rect = transformRectangle(lLabels.get(j), lAffine3D);
                        if (rect.contains(i, i1) && lLabels.get(j).width * lLabels.get(j).height <
                                lRectToSelectArea) {
                            mSelectionExists = true;
                            lRectToSelect = lLabels.get(j);
                            lRectToSelectArea = lRectToSelect.height * lRectToSelect.width;
                        }
                    }


                    if (mSelectionExists) {
                        mSelectedLabel = lRectToSelect;
                    }
                    ArrayList<Rectangle>[] lGeom = getAcquisitionModel().getGeometry();

                    AcquisitionUnit lAU = getAcquisitionModel().getLabelRectangleMap().get(lRectToSelect);
                    if (mSelectionExists) {
                        for (int j = (int) lAU.getLocation()[2]; j < (int) (lAU.getLocation()[2] + lAU.getDimZ());
                             j++) {
                            lGeom[j].set(0,lRectToSelect);
                        }
                    }
                }
                else {
                    mSelectionExists = false;
                    mSelectedLabel = null;
                }


            }

        };







        mOverlay = new BdvOverlay() {
            @Override
            protected void draw(Graphics2D g) {
                int lCurrentZSlice = info.getTimePointIndex();
                ArrayList<Rectangle> lRectArray = mAcquisitionModel.getGeometry()[lCurrentZSlice];
                for (int i = 0; i < lRectArray.size(); i++) {
                    if (i != 0) {
                        AffineTransform3D lAffine3D = new AffineTransform3D();
                        info.getViewerTransform(lAffine3D);
                        g.setColor(mColor);
                        g.setStroke(new BasicStroke(1));
                        g.draw(transformRectangle(lRectArray.get(i), lAffine3D));
                        g.fill(transformRectangle(lRectArray.get(i), lAffine3D));
                    } else {
                        AffineTransform3D lAffine3D = new AffineTransform3D();
                        info.getViewerTransform(lAffine3D);
                        g.setColor(mColor);
                        g.setStroke(new BasicStroke(10));
                        g.draw(transformRectangle(lRectArray.get(i), lAffine3D));
                    }


                }
            }
        };
    }

    public Rectangle getSelectedLabel() {
        return mSelectedLabel;
    }

    public AcquisitonModel getAcquisitionModel() {
        return mAcquisitionModel;
    }


    public StackSelectionLabel getStackSelectionLabel() {
        return mStackSelectionLabel;
    }


    public void startViewer() {

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


        TransformListener<AffineTransform3D> tl = new TransformListener<AffineTransform3D>() {
            @Override
            public void transformChanged(AffineTransform3D affineTransform3D) {
                System.out.println("changed");

            }
        };
        BehaviourMap lBehMap = new BehaviourMap();
        lBehMap.put("select", mSelectBehaviour);
        InputTriggerMap lInputTriggerMap = new InputTriggerMap();
        final java.util.List<InputTriggerDescription> triggers = new ArrayList<>();
        String[] tr = new String[]{"button1"};
        InputTriggerConfig lInputTriggerConfig = new InputTriggerConfig(triggers);
        InputTriggerAdder adder = lInputTriggerConfig.inputTriggerAdder(lInputTriggerMap, "all");

        adder.put("select", "button1");
        bdv.getBdvHandle().getViewerPanel().addTransformListener(tl);
        this.getBdv().getBdvHandle().getTriggerbindings().addBehaviourMap("beh1", lBehMap);
        this.getBdv().getBdvHandle().getTriggerbindings().addInputTriggerMap("map1", lInputTriggerMap, "none");


    }

    public static void main(String[] args) {
        AcquisitonModel am = new AcquisitonModel(1000, 1000, 1000);
        Viewer v = new Viewer(am);
        v.startViewer();

        AcquisitionUnit au1 = new AcquisitionUnit(new long[]{0, 0, 0});
        AcquisitionUnit au2 = new AcquisitionUnit(new long[]{150, 150, 0});

        am.addAcquisitionUnitWithLabel(au1, new Rectangle((int) au1.getLocation()[0], (int) au1.getLocation()[1],
                au1.getDimX(),
                au1.getDimY()), au1.getLocation(), au1.getDimZ());
    }

}
