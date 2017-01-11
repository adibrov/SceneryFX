package sceneryfx;

import bdv.util.BdvHandle;
import bdv.util.BdvOverlay;
import bdv.viewer.animate.OverlayAnimator;
import org.scijava.ui.behaviour.Behaviour;
import org.scijava.ui.behaviour.ClickBehaviour;

import java.awt.*;

/**
 * Created by dibrov on 09/01/17.
 */
public class AnimatorOverlayBehaviour {
    private Behaviour mBehaviour;
    private OverlayAnimator mOverlayAnimator;
    private Rectangle mRectangle = new Rectangle(0, 0, 200, 200);
    private BdvHandle mBdvHandle;

    public AnimatorOverlayBehaviour(BdvHandle pBdvHandle) {
        this.mBdvHandle = pBdvHandle;

        this.mBehaviour = new ClickBehaviour() {
            @Override
            public void click(int i, int i1) {
                System.out.println("click!");
                mRectangle.setBounds(i, i1, 200, 200);
            }
        };

        this.mOverlayAnimator = new OverlayAnimator() {
            @Override
            public void paint(Graphics2D graphics2D, long l) {
                graphics2D.draw(mRectangle);
            }

            @Override
            public boolean isComplete() {
                return false;
            }

            @Override
            public boolean requiresRepaint() {
                return false;
            }
        };
//            this.mBehaviour = new DragBehaviour() {
//                @Override
//                public void init(int i, int i1) {
//                    mRectangle = new Rectangle(i,i1,0,0);
//                    mOverlay = new BdvOverlay() {
//                        @Override
//                        protected void draw(Graphics2D g) {
//                            g.draw(mRectangle);
//                        }
//                    };
//                }
//
//                @Override
//                public void drag(int i, int i1) {
//                    mRectangle.setBounds(mRectangle.x, mRectangle.y, i, i1);
//                    mOverlay = new BdvOverlay() {
//                        @Override
//                        protected void draw(Graphics2D g) {
//                            g.draw(mRectangle);
//                        }
//                    };
//                }
//
//                @Override
//                public void end(int i, int i1) {
//                    mRectangle.setBounds(0,0,0,0);
//                    mOverlay = new BdvOverlay() {
//                        @Override
//                        protected void draw(Graphics2D g) {
//                            g.draw(mRectangle);
//                        }
//                    };
//                }
//            };


    }

    public Behaviour getBehavior() {
        return this.mBehaviour;
    }

    public OverlayAnimator getOverlayAnimator() {
        return this.mOverlayAnimator;
    }

    public Rectangle getRectangle() {
        return this.mRectangle;
    }

    public void setRectangle(int x0, int y0, int w, int h) {
        this.mRectangle.setBounds(x0, y0, w, h);
    }
}
