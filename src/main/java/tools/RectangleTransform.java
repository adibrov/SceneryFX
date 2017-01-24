package tools;

import net.imglib2.realtransform.AffineTransform3D;

import java.awt.*;

/**
 * Created by dibrov on 24/01/17.
 */
public class RectangleTransform {

    public static Rectangle transformRectangle(Rectangle pRectangle, AffineTransform3D pAffine3D) {
        Rectangle lRectangle = new Rectangle(pRectangle);
        double[] lRectOldInit = {pRectangle.x, pRectangle.y, 0};
        double[] lRectOldFinal = {pRectangle.x + pRectangle.width, pRectangle.y + pRectangle.height, 0};
        double[] lRectNewInit = new double[3];
        double[] lRectNewFinal = new double[3];

        pAffine3D.apply(lRectOldInit, lRectNewInit);
        pAffine3D.apply(lRectOldFinal, lRectNewFinal);

        // should do sth with this casts....
        lRectangle.setBounds((int) lRectNewInit[0], (int) lRectNewInit[1], (int) (lRectNewFinal[0] - lRectNewInit[0]),
                (int) (lRectNewFinal[1] - lRectNewInit[1]));

        return lRectangle;

    }
}
