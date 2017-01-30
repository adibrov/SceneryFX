package sceneryfx;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by dibrov on 30/01/17.
 */
public class SampleSpace {
    private int mPadding;
    private Img mData;
    private long mDimX;
    private long mDimY;
    private long mDimZ;



    public long[] getDimensions() {
        long[] dims ={mDimX, mDimY, mDimZ};
        return dims;
    }
    public SampleSpace(int pPadding, Img pDataImg) {
        this.mPadding = pPadding;

        long[] lDimOfFullSpace = {pDataImg.dimension(0) + 2*mPadding, pDataImg.dimension(1) +
                2*mPadding, pDataImg.dimension(2) + 2*mPadding};
        mDimX = lDimOfFullSpace[0];
        mDimY = lDimOfFullSpace[1];
        mDimZ = lDimOfFullSpace[2];

        this.mData = new ArrayImgFactory().create(lDimOfFullSpace, new UnsignedByteType());
        RandomAccess<UnsignedByteType> lRandIn = pDataImg.randomAccess();
        RandomAccess<UnsignedByteType> lRandOut = mData.randomAccess();

        Random lRandInt = new Random();
        System.out.println("extended image dims: " + lDimOfFullSpace[0] + " " + lDimOfFullSpace[1] + " " +
                lDimOfFullSpace[2]);


        System.out.println("original image dims: " + pDataImg.dimension(0) + " " + pDataImg.dimension(1) + " " +
                pDataImg.dimension(2));


        for (int i = 0; i < mDimX; i++) {
            for (int j = 0; j < mDimY; j++) {
                for (int k = 0; k < mDimZ; k++) {
                    int[] lPos = {i,j,k};
                    lRandOut.setPosition(lPos);
                    if ((i < mPadding || (mDimX-i) <= mPadding) || (j < mPadding || (mDimY-j) <= mPadding) || (k <
                            mPadding || (mDimZ-k) <= mPadding)) {
                        lRandOut.get().set(lRandInt.nextInt());
                    }
                    else
                    {
                        long lImgX = lRandOut.getIntPosition(0) - mPadding;
                        long lImgY = lRandOut.getIntPosition(1) - mPadding;
                        long lImgZ = lRandOut.getIntPosition(2) - mPadding;
                        long[] lImgPos = {lImgX, lImgY, lImgZ};
                        lRandIn.setPosition(lImgPos);
                        lRandOut.get().set(lRandIn.get());
                    }
                }

            }
        }
    }

    public static SampleSpace createStandardSampleSpaceFromFile(String pPathToFile) {
        try {
            Img img = (Img<UnsignedByteType>) (new ImgOpener().openImgs(pPathToFile).get
                    (0));



            return new SampleSpace(0, img);
        } catch (ImgIOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Img getSubstack(long[] pInit, long[] pFinal){

//        long[] pInitCheck = {max(pInit[0], 0), max(pInit[1], 1), max(pInit[2], 2)};
//        long[] pFinalCheck = {min(pFinal[0], 0), min(pFinal[1], 1), min(pFinal[2], 2)};

        long[] dims = {pFinal[0] - pInit[0] + 1, pFinal[1] - pInit[1] + 1, pFinal[2] - pInit[2] + 1};

        Img imgOut = new ArrayImgFactory().create(dims, new UnsignedByteType());

        IterableInterval img = Views.interval(mData, pInit, pFinal);
        Cursor<UnsignedByteType> lCursor = imgOut.localizingCursor();
        RandomAccess<UnsignedByteType> lRa = mData.randomAccess();

        while (lCursor.hasNext()) {
            lCursor.fwd();
            lRa.setPosition(lCursor);
            lCursor.get().set(lRa.get());

        }

        return imgOut;
    }


}
