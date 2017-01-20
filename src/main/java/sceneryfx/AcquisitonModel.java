package sceneryfx;

import javafx.util.Pair;
import net.imglib2.*;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.Views;

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

    public void addAcquisitionUnitWithLabel(AcquisitionUnit pAcquisitionUnit, Rectangle pRectangleLabel, long[]
            pLocation, int plastLabelZ) {
        mLabelRectangleMap.put(pRectangleLabel, pAcquisitionUnit);
        for (int i = (int)pLocation[2]; i < plastLabelZ + 1; i++) {
            mGeometry[i].add(new Rectangle(pRectangleLabel));
        }

    }


    public void setSubstack(long[] pInitLoc, Img pSubstack) {
        System.out.println("setting substack");
        System.out.println("substack dim0: " + pSubstack.dimension(0));
        System.out.println("substack dim1: " + pSubstack.dimension(1));
        System.out.println("substack dim2: " + pSubstack.dimension(2));
        long[] pFinalLoc = new long[]{pInitLoc[0] + pSubstack.dimension(0) -1 , pInitLoc[1] + pSubstack.dimension
                (1) -1 ,
                pInitLoc[2] + pSubstack
                .dimension(2)-1};

        IterableInterval<UnsignedByteType> lSubstackToFill = Views.iterable(Views.interval(this.getData(), pInitLoc,
                pFinalLoc));
        System.out.println("init loc: " + pInitLoc[0] + " " + pInitLoc[1] + " " + pInitLoc[2]);
        System.out.println("final loc: " + pFinalLoc[0] + " " + pFinalLoc[1] + " " + pFinalLoc[2]);
        net.imglib2.Cursor<UnsignedByteType> lTargetCursor = lSubstackToFill.localizingCursor();
        RandomAccess<UnsignedByteType> lSourceRA = pSubstack.randomAccess();

        while (lTargetCursor.hasNext()) {
            lTargetCursor.fwd();

            lSourceRA.setPosition(new long[]{lTargetCursor.getIntPosition(0) - pInitLoc[0], lTargetCursor
                    .getIntPosition
                    (1) -
                    pInitLoc[1], lTargetCursor.getIntPosition(2) - pInitLoc[2]});
            //System.out.println("ltargetcursor: " + lTargetCursor.getIntPosition(0));
            lTargetCursor.get().set(lSourceRA.get());
           // lTargetCursor.get().set(100);
        }
    }
    public static void main(String[] args) {
        AcquisitonModel lAcquisiitonModel = new AcquisitonModel(1000, 1000, 1000);
        AcquisitionUnit lAcquisitionUnit1 = new AcquisitionUnit();
        AcquisitionUnit lAcquisitionUnit2 = new AcquisitionUnit(new long[]{10, 10, 10});

        lAcquisiitonModel.addAcquisitionUnitWithLabel(lAcquisitionUnit1, new Rectangle(
                        (int)lAcquisitionUnit1.getLocation()
                        [0], (int)lAcquisitionUnit1.getLocation()[1], lAcquisitionUnit1.getDimX(),
                        lAcquisitionUnit1.getDimY()),
                lAcquisitionUnit1.getLocation(),
                (int)lAcquisitionUnit1.getLocation()[2]);

        lAcquisiitonModel.addAcquisitionUnitWithLabel(lAcquisitionUnit2, new Rectangle((int)
                        lAcquisitionUnit2.getLocation()
                        [0], (int)lAcquisitionUnit2.getLocation()[1], lAcquisitionUnit2.getDimX(),
                        lAcquisitionUnit2.getDimY()),
                lAcquisitionUnit2.getLocation(),
                (int)lAcquisitionUnit2.getLocation()[2]);


        HashMap<Rectangle, AcquisitionUnit> am = new HashMap<>();
        am = lAcquisiitonModel.getLabelRectangleMap();

        System.out.println(am);
    }
}
