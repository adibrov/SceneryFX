package sceneryfx;

import bdv.util.BdvHandle;
import bdv.util.BdvOverlay;
import org.scijava.ui.behaviour.Behaviour;
import org.scijava.ui.behaviour.DragBehaviour;

import java.awt.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Created by dibrov on 09/01/17.
 */
public class OverlayBehaviour {
    private Behaviour mBehaviour;
    private BdvOverlay mOverlay;
    private int x0 = 0, x1 = 0, y0 = 0, y1 = 0;
    private BdvHandle mBdvHandle;
    private Rectangle mRectangle = new Rectangle(min(x0, x1), min(y0, y1), abs(x1 - x0), abs(y1 - y0));
    private boolean isEnd = false;

    public OverlayBehaviour(BdvHandle pBdvHandle) {
        this.mBdvHandle = pBdvHandle;


//
//            this.mBehaviour = new ClickBehaviour() {
//                @Override
//                public void click(int i, int i1) {
//                    System.out.println("tada!");
//                    mRectangle.setBounds(i,i1,200,200);
//                    mOverlay = new BdvOverlay() {
//                        @Override
//                        protected void draw(Graphics2D g) {
//                            g.draw(mRectangle);
//                        }
//                    };
//                    mBdvHandle.getViewerPanel().getDisplay().repaint();
//                }
//            };
        this.mBehaviour = new DragBehaviour() {
            @Override
            public void init(int i, int i1) {
                isEnd = false;
                x0 = i; x1 = i;
                y0 = i1; y1 = i1;

                mRectangle.setBounds(min(x0, x1), min(y0, y1), abs(x1 - x0), abs(y1 - y0));
                mBdvHandle.getViewerPanel().getDisplay().repaint();
            }

            @Override
            public void drag(int i, int i1) {
                isEnd = false;
                x1 = i;
                y1 = i1;
                mRectangle.setBounds(min(x0, x1), min(y0, y1), abs(x1 - x0), abs(y1 - y0));

                mBdvHandle.getViewerPanel().getDisplay().repaint();

            }

            @Override
            public void end(int i, int i1) {
                isEnd = true;
                mBdvHandle.getViewerPanel().getDisplay().repaint();
//                x0 = 0; y0 = 0; x1 = 0; y1 = 0;
//                mRectangle.setBounds(min(x0,x1),min(y0,y1),abs(x1-x0),abs(y1-y0));
//                mBdvHandle.getViewerPanel().getDisplay().repaint();
            }
        };

        this.mOverlay = new BdvOverlay() {
            @Override
            protected void draw(Graphics2D g) {
                g.draw(mRectangle);
                if (isEnd)
                    g.setColor( new Color(0,1,0,0.5f));
                else
                    g.setColor(new Color(1,1,0,0.5f));
                g.fill(mRectangle);
            }
        };
    }

    public Behaviour getBehavior() {
        return this.mBehaviour;
    }

    public BdvOverlay getOverlay() {
        return this.mOverlay;
    }

    public Rectangle getRectangle() {
        return this.mRectangle;
    }

    public void setRectangle(int x0, int y0, int w, int h) {
        this.mRectangle.setBounds(x0, y0, w, h);
    }
}
