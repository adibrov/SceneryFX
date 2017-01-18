package sceneryfx;

import net.imglib2.img.Img;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dibrov on 18/01/17.
 */
public class AcquisitonModel {
    private Img mData;
    private ArrayList<ArrayList<Rectangle>> mGeometry;
    private HashMap<AcquisitionUnit, Rectangle> mLabelRectangleMap;
}
