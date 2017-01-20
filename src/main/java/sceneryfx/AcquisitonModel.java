package sceneryfx;

import javafx.util.Pair;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dibrov on 18/01/17.
 */
public class AcquisitonModel {
    private int mDimX;
    private int mDimY;
    private int mDimZ;
    private Img mData;
    private ArrayList<Rectangle>[] mGeometry;
    private HashMap<Rectangle, AcquisitionUnit> mLabelRectangleMap;

    public ArrayList<Rectangle>[] getGeometry() {
        return mGeometry;
    }

    public Img getData() {
        return mData;
    }


    public HashMap<Rectangle, AcquisitionUnit> getLabelRectangleMap() {
        return mLabelRectangleMap;
    }

    public AcquisitonModel(int pDimX, int pDimY, int pDimZ) {
        this.mData = new ArrayImgFactory().create(new long[]{pDimX, pDimY, pDimZ}, new UnsignedByteType());
        this.mGeometry = new ArrayList[pDimZ];
        for (int i = 0; i < pDimZ; i++) {

            mGeometry[i] = new ArrayList<>();
            mGeometry[i].add(new Rectangle(0,0,0,0));
        }
        this.mLabelRectangleMap = new HashMap<>();
    }

    public void addAcquisitionUnitWithLabel(AcquisitionUnit pAcquisitionUnit, Rectangle pRectangleLabel, int[]
            pLocation, int plastLabelZ) {
        mLabelRectangleMap.put(pRectangleLabel, pAcquisitionUnit);
        for (int i = pLocation[2]; i < plastLabelZ + 1; i++) {
            mGeometry[i].add(new Rectangle(pRectangleLabel));
        }

    }

    public static void main(String[] args) {
        AcquisitonModel lAcquisiitonModel = new AcquisitonModel(1000, 1000, 1000);
        AcquisitionUnit lAcquisitionUnit1 = new AcquisitionUnit();
        AcquisitionUnit lAcquisitionUnit2 = new AcquisitionUnit(new int[]{10, 10, 10});

        lAcquisiitonModel.addAcquisitionUnitWithLabel(lAcquisitionUnit1, new Rectangle(lAcquisitionUnit1.getLocation()
                        [0], lAcquisitionUnit1.getLocation()[1], lAcquisitionUnit1.getDimX(), lAcquisitionUnit1.getDimY()),
                lAcquisitionUnit1.getLocation(),
                lAcquisitionUnit1.getLocation()[2]);

        lAcquisiitonModel.addAcquisitionUnitWithLabel(lAcquisitionUnit2, new Rectangle(lAcquisitionUnit2.getLocation()
                        [0], lAcquisitionUnit2.getLocation()[1], lAcquisitionUnit2.getDimX(),
                        lAcquisitionUnit2.getDimY()),
                lAcquisitionUnit2.getLocation(),
                lAcquisitionUnit2.getLocation()[2]);


        HashMap<Rectangle, AcquisitionUnit> am = new HashMap<>();
        am = lAcquisiitonModel.getLabelRectangleMap();

        System.out.println(am);
    }
}
