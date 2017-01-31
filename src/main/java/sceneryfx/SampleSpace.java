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

        System.out.println("got an image of dims: " + mDimX + " " + mDimY + " " + mDimZ);

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

    public Img getSubstack(long[] pInit, long[] pDimensions){

//        long[] pInitCheck = {max(pInit[0], 0), max(pInit[1], 1), max(pInit[2], 2)};
//        long[] pFinalCheck = {min(pFinal[0], 0), min(pFinal[1], 1), min(pFinal[2], 2)};

      //  long[] dims = {pFinal[0] - pInit[0] + 1, pFinal[1] - pInit[1] + 1, pFinal[2] - pInit[2] + 1};

        Img imgOut = new ArrayImgFactory().create(pDimensions, new UnsignedByteType());
//        long[] lFinal = {pInit[0] + pDim[0] - 1, pInit[1] + pDim[1] - 1, pInit[2] + pDim[2] - 1};

//
//        System.out.println("pInit are: " + pInit[0] + " " + pInit[1] + " " + pInit[2]);
//        System.out.println("pFinal are: " + pFinal[0] + " " + pFinal[1] + " " + pFinal[2]);
//        System.out.println("dims are: " + dims[0] + " " + dims[1] + " " + dims[2]);

        IterableInterval img = Views.offsetInterval(mData, pInit, pDimensions);
        Cursor<UnsignedByteType> lCursor = img.localizingCursor();
        RandomAccess<UnsignedByteType> lRa = imgOut.randomAccess();

        System.out.println("img dims: " + img.dimension(0) + " " + img.dimension(1) + " " + img.dimension(2));
        System.out.println("imgOut dims: " + imgOut.dimension(0) + " " + imgOut.dimension(1) + " " + imgOut
                .dimension(2));

        int count = 0;
        while (lCursor.hasNext()) {
            lCursor.fwd();
            count += 1;

//            if (count < 20) {
//                System.out.println("count is:" + count);
//                System.out.println("cursor pos x y z: " + lCursor.getIntPosition(0) + " " + lCursor.getIntPosition(1) +
//                        " " + lCursor.getIntPosition(2));
//            }
            lRa.setPosition(lCursor);
          //  lCursor.get().set(lRa.get());
            lRa.get().set(lCursor.get());

        }

        return imgOut;
    }


}
