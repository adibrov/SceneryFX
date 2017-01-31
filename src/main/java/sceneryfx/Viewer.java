package sceneryfx;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOverlay;
import ij.plugin.DragAndDrop;
import javafx.geometry.Pos;
import net.imagej.Position;
import net.imglib2.Positionable;
import net.imglib2.RealPoint;
import net.imglib2.RealPositionable;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.ui.TransformListener;
import org.scijava.ui.behaviour.*;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.io.InputTriggerDescription;
import org.scijava.ui.behaviour.io.yaml.YamlConfigIO;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DragSourceAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

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
    private DraggableComponent mJPanel;
    private boolean labelsAreVisible;


    private Color lTransparentColor = new Color(0,0,0,0.0f);
    public Bdv getBdv() {
        return bdv;
    }


    public boolean labelsAreVisible() {
        return labelsAreVisible;
    }

    public Viewer(AcquisitonModel pAcquisitionModel) {
        this.mAcquisitionModel = pAcquisitionModel;
        this.mColor = new Color(1.0f, 153.0f / 255, 51.0f / 255, 0.5f);
        this.mSelectionExists = false;
        this.labelsAreVisible = true;
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
                Rectangle rect;
                AffineTransform3D lAffine3D = new AffineTransform3D();
                getBdv().getBdvHandle().getViewerPanel().getState().getViewerTransform(lAffine3D);
                int lCurrentTimePoint = bdv.getBdvHandle().getViewerPanel().getState().getCurrentTimepoint();
                ArrayList<Rectangle> lLabels = getAcquisitionModel().getGeometry()[lCurrentTimePoint];
                Rectangle lRectToSelect = new Rectangle(0, 0, 0, 0);
                int lRectToSelectArea = 100000000;
                ArrayList<Rectangle>[] lGeom = getAcquisitionModel().getGeometry();


                if (!mSelectionExists) {

                    for (int j = 1; j < lLabels.size(); j++) {
                        rect = transformRectangle(lLabels.get(j), lAffine3D);
                        if (rect.contains(i, i1) && lLabels.get(j).width * lLabels.get(j).height <
                                lRectToSelectArea) {
                            mSelectionExists = true;
                            lRectToSelect = new Rectangle(lLabels.get(j));
                            lRectToSelectArea = lRectToSelect.height * lRectToSelect.width;
                        }
                    }


                    if (mSelectionExists) {
                        mSelectedLabel = lRectToSelect;
                        AcquisitionUnit lAU = getAcquisitionModel().getLabelRectangleMap().get(lRectToSelect);
                        for (int j = (int) lAU.getLocation()[2]; j < (int) (lAU.getLocation()[2] + lAU.getDimZ());
                             j++) {
                            lGeom[j].set(0, lRectToSelect);

                        }
                        Rectangle rightRect = transformRectangle(lRectToSelect, lAffine3D);
                        mJPanel.setBounds(rightRect);
                        System.out.println("setting color");
                        mJPanel.setLocation(rightRect.x, rightRect.y);
                      //  mJPanel.setBackground(new Color(0, 255, 0));
                        mJPanel.setVisible(true);
                        mJPanel.setBackground(new Color(0,0,0,0.0f));
                      //  mJPanel.setDragEnabled(true);
                        mJPanel.setImg(getAcquisitionModel().getLabelRectangleMap().get(lRectToSelect).getSubstack());

                    }
                } else {
                    mJPanel.setVisible(false);


                    for (int j = 0; j < lGeom.length; j++) {
                        lGeom[j].get(0).setBounds(0, 0, 0, 0);
                    }
                    mSelectedLabel = null;
                    mSelectionExists = false;

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
                        g.setColor(labelsAreVisible? mColor:lTransparentColor);
                        g.setStroke(new BasicStroke(1));
                        g.draw(transformRectangle(lRectArray.get(i), lAffine3D));
                        g.fill(transformRectangle(lRectArray.get(i), lAffine3D));
                    } else {
                        AffineTransform3D lAffine3D = new AffineTransform3D();
                        info.getViewerTransform(lAffine3D);
                        g.setColor(labelsAreVisible?mColor:lTransparentColor);
                        g.setStroke(new BasicStroke(10));
                        g.draw(transformRectangle(lRectArray.get(i), lAffine3D));
                    }


                }
            }
        };
    }

    public void setLabelsVisible(boolean flag) {
        if (flag != labelsAreVisible) {
            labelsAreVisible = flag;
            getBdv().getBdvHandle().getViewerPanel().getDisplay().repaint();
        }
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

        long[] dims = getAcquisitionModel().getSampleSpace().getDimensions();
        this.bdv = BdvFunctions.show(mAcquisitionModel.getData(), "reds", Bdv.options().is2D().inputTriggerConfig
                (conf).preferredSize((int)dims[0],(int)dims[1]));

//        bdv.getBdvHandle().getTriggerbindings().getConcatenatedBehaviourMap().remove("2d scroll translate");

        Behaviour lScrollBehaviour = new ScrollBehaviour() {
            @Override
            public void scroll(double wheelRotation, boolean isHorizontal, int x, int y) {
                AffineTransform3D lAffine3D = new AffineTransform3D();
                bdv.getBdvHandle().getViewerPanel().getState().getViewerTransform(lAffine3D);
                float[] lPosOld = {x,y,0};
                float[] lPosNew = {0,0,0};
                lAffine3D.applyInverse(lPosNew, lPosOld);
                Point p = bdv.getBdvHandle().getViewerPanel().getMousePosition(true);

                synchronized ( lAffine3D )
                {
                    double d = -wheelRotation * 10;



                    if ( isHorizontal  ){
                        if (d < 0 && lPosNew[0] - d > 2000 || d >0 && lPosNew[0] - d < - 500) {
                            d = 0.0;
                        }
                        lAffine3D.translate( d, 0, 0 );
                        System.out.println("x and y is: " + lPosNew[0]+ " " + lPosNew[1]);


                    }
                    else {
                        if (d < 0 && lPosNew[1] + d > 2000 ||d > 0 && lPosNew[1] - d < - 500) {
                            d = 0.0;
                        }
                        lAffine3D.translate(0, d, 0);
                        System.out.println("x and y is: " + lPosNew[0] + " "+lPosNew[1]);
                    }
                    bdv.getBdvHandle().getViewerPanel().transformChanged(lAffine3D);
                }
            }
        };
//        bdv.getBdvHandle().getTriggerbindings().getConcatenatedBehaviourMap().put("2d scroll translate", lScrollBehaviour);


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

        this.mJPanel = new DraggableComponent();

        this.mJPanel.setVisible(false);
        bdv.getBdvHandle().getViewerPanel().getDisplay().add(this.mJPanel);


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
