package sceneryfx;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvHandle;
import bdv.util.BdvOverlay;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.scijava.ui.behaviour.*;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.io.InputTriggerDescription;
import org.scijava.ui.behaviour.io.yaml.YamlConfigIO;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Created by dibrov on 11/01/17.
 */
public class StackSelectionLabel {

    private BdvHandle mBdvHandle;
    private DragBehaviour mDragBehaviour;
    private RectangleLabel mLabelFirst;
    private RectangleLabel mLabelLast;
    private RectangleLabel mLabelInterm;
    private BdvOverlay mOverlay;
    private boolean mFirstLabelExists;
    private boolean mLastLabelExists;
    private Rectangle mRectangle;
    private int x0, y0, x1, y1;
    private ClickBehaviour mClickBehaviour;

    public StackSelectionLabel(BdvHandle pBdvHandle) {
        x0 = 0;
        x1 = 0;
        y0 = 0;
        y1 = 0;
        this.mBdvHandle = pBdvHandle;
        this.mFirstLabelExists = false;
        this.mLastLabelExists = false;
        this.mRectangle = new Rectangle(0, 0, 0, 0);

        mLabelFirst = new RectangleLabel();
        mLabelLast = new RectangleLabel();
        mLabelInterm = new RectangleLabel();
        mLabelLast.setColor(new Color(0, 0, 1, 0.5f));
        mLabelInterm.setColor(new Color(1, 1, 1, 0.25f));

        this.mDragBehaviour = new DragBehaviour() {
            @Override
            public void init(int i, int i1) {
                if (!mFirstLabelExists) {
                    mFirstLabelExists = false;
                    mLastLabelExists = false;
                    x0 = i;
                    y0 = i1;
                    mRectangle.setBounds(x0, y0, 0, 0);
                    mLabelFirst.setRectangle(mRectangle);
                    mBdvHandle.getViewerPanel().getDisplay().repaint();
                }

            }

            @Override
            public void drag(int i, int i1) {
                if (!mFirstLabelExists) {
                    x1 = i;
                    y1 = i1;
                    mRectangle.setBounds(min(x0, x1), min(y0, y1), abs(x1 - x0), abs(y1 - y0));
                    mLabelFirst.setRectangle(mRectangle);
                    mBdvHandle.getViewerPanel().getDisplay().repaint();
                }
            }

            @Override
            public void end(int i, int i1) {

                if (!mFirstLabelExists) {
                    mFirstLabelExists = true;
                    mLabelFirst.setTimePointIndex(mBdvHandle.getViewerPanel().getState().getCurrentTimepoint());
                    mLabelFirst.setRectangle(mRectangle);
                    mLabelInterm.setRectangle(mRectangle);
                    mBdvHandle.getViewerPanel().getDisplay().repaint();
                }

            }
        };

        this.mOverlay = new BdvOverlay() {
            @Override
            protected void draw(Graphics2D g) {
                int lTimePointIndex = info.getTimePointIndex();
                if (mFirstLabelExists && mLastLabelExists) {
                    if (lTimePointIndex == mLabelFirst.getmTimePointIndex()) {
                        g.setColor(mLabelFirst.getColor());
                        g.fill(mLabelFirst.getRectangle());
                        g.setStroke(new BasicStroke(mLabelFirst.getLineWidth()));
                        g.draw(mLabelFirst.getRectangle());
                    }
                    else if (lTimePointIndex == mLabelLast.getmTimePointIndex()) {
                        g.setColor(mLabelLast.getColor());
                        g.fill(mLabelLast.getRectangle());
                        g.setStroke(new BasicStroke(mLabelLast.getLineWidth()));
                        g.draw(mLabelLast.getRectangle());
                    }
                    else if (mLabelFirst.getmTimePointIndex() < lTimePointIndex && lTimePointIndex < mLabelLast
                            .getmTimePointIndex()) {
                        g.setColor(mLabelInterm.getColor());
                        g.fill(mLabelInterm.getRectangle());
                        g.setStroke(new BasicStroke(mLabelInterm.getLineWidth()));
                        g.draw(mLabelInterm.getRectangle());
                    }
                } else if (mFirstLabelExists && !mLastLabelExists) {
                    if (lTimePointIndex == mLabelFirst.getmTimePointIndex()) {
                        g.setColor(mLabelFirst.getColor());
                        g.fill(mLabelFirst.getRectangle());
                        g.setStroke(new BasicStroke(mLabelFirst.getLineWidth()));
                        g.draw(mLabelFirst.getRectangle());
                    } else {
                        g.setColor(mLabelInterm.getColor());
                        g.fill(mLabelInterm.getRectangle());
                        g.setStroke(new BasicStroke(mLabelInterm.getLineWidth()));
                        g.draw(mLabelInterm.getRectangle());
                    }
                }
                else
                {
                    g.setColor(mLabelFirst.getColor());
                    g.fill(mRectangle);
                    g.setStroke(new BasicStroke(mLabelFirst.getLineWidth()));
                    g.draw(mRectangle);
                }


            }
        };

        this.mClickBehaviour = new ClickBehaviour() {
            @Override
            public void click(int i, int i1) {
                if (mFirstLabelExists && !mLastLabelExists) {
                    if (mLabelFirst.getRectangle().contains(i, i1)) {
                        mLabelLast.setRectangle(mLabelFirst.getRectangle());
                        mLabelLast.setTimePointIndex(mBdvHandle.getViewerPanel().getState().getCurrentTimepoint());
                        mLastLabelExists = true;
                    } else {
                        mFirstLabelExists = false;
                        mLabelFirst.updateCoords(0, 0, 0, 0);
                    }
                } else if (mFirstLabelExists && mLastLabelExists) {
                    if (!mLabelFirst.getRectangle().contains(i, i1)) {
                        mFirstLabelExists = false;
                        mLastLabelExists = false;
                        mLabelFirst.updateCoords(0, 0, 0, 0);
                        mLabelLast.updateCoords(0, 0, 0, 0);
                    }
                }

            }
        };

        BehaviourMap lBehMap = new BehaviourMap();
        lBehMap.put("drag", this.mDragBehaviour);
        lBehMap.put("click", this.mClickBehaviour);

        InputTriggerMap lInputTriggerMap = new InputTriggerMap();
        final List<InputTriggerDescription> triggers = new ArrayList<>();
        String[] tr = new String[]{"button1"};
        InputTriggerConfig lInputTriggerConfig = new InputTriggerConfig(triggers);
        InputTriggerAdder adder = lInputTriggerConfig.inputTriggerAdder(lInputTriggerMap, "all");
        adder.put("drag", "button1");
        adder.put("click", "button1");

        mBdvHandle.getTriggerbindings().addBehaviourMap("beh", lBehMap);
        mBdvHandle.getTriggerbindings().addInputTriggerMap("map", lInputTriggerMap, "none");
    }


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

        StackSelectionLabel lStackSelectionLabel = new StackSelectionLabel(bdv2D.getBdvHandle());
        BdvFunctions.showOverlay(lStackSelectionLabel.mOverlay, "overlay", Bdv.options().addTo(bdv2D));
    }
}
