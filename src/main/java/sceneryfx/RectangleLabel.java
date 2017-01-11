package sceneryfx;

import java.awt.*;
import java.awt.geom.Point2D;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Created by dibrov on 11/01/17.
 */
public class RectangleLabel {
    private Rectangle mRectangle;
    private Color mColor;
    private float mLineWidth;
    private int mTimePointIndex = 0;

    public RectangleLabel(int x0, int x1, int y0, int y1, Color pColor, float pLineWidth) {
        mRectangle= new Rectangle(min(x0, x1), min(y0, y1), abs(x1 - x0), abs(y1 - y0));
        mColor = pColor;
        mLineWidth = pLineWidth;
    }

    public RectangleLabel() {
        mRectangle = new Rectangle(0,0,0,0);
        mColor = new Color(0,1,0,0.5f);
        mLineWidth = 2.0f;
    }

    public void updateCoords(int x0, int x1, int y0, int y1) {
        mRectangle.setBounds(min(x0, x1), min(y0, y1), abs(x1 - x0), abs(y1 - y0));
    }

    public void setRectangle(Rectangle pRectangle) {
        this.mRectangle = pRectangle;
    }

    public Rectangle getRectangle(){
        return mRectangle;
    }
    public Color getColor(){
        return mColor;
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setColor(Color mColor) {
        this.mColor = mColor;
    }

    public void setLineWidth(float mLineWidth) {
        this.mLineWidth = mLineWidth;
    }

    public void setTimePointIndex(int mTimePointIndex) {
        this.mTimePointIndex = mTimePointIndex;
    }

    public int getmTimePointIndex() {
        return mTimePointIndex;
    }
}
