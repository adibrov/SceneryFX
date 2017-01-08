package sceneryfx;

import clearvolume.renderer.ClearVolumeRendererInterface;
import clearvolume.renderer.factory.ClearVolumeRendererFactory;
import clearvolume.transferf.TransferFunctions;
import coremem.enums.NativeTypeEnum;

import java.nio.ByteBuffer;

/**
 * Created by dibrov on 06/01/17.
 */
public class ClearVolumeUnit {
    private byte[] mDataToShow;
    private int mDimX, mDimY, mDimZ;

    public ClearVolumeRendererInterface getmCVRend() {
        return mCVRend;
    }

    private ClearVolumeRendererInterface mCVRend;
    private NativeTypeEnum mTypeEnum;

    public ClearVolumeUnit(byte[] pDataToShow, int pDimX, int pDimY, int pDimZ, NativeTypeEnum pTypeEnum) {

        this.mDataToShow = pDataToShow;

        this.mDimX = pDimX;
        this.mDimY = pDimY;
        this.mDimZ = pDimZ;

        this.mTypeEnum = pTypeEnum;




    }

    public void initializeAndShow() {


        try {

            mCVRend = ClearVolumeRendererFactory.newBestRenderer("ClearVolumeTest",
                    512,
                    512,
                    mTypeEnum,
                    false);


            mCVRend.setTransferFunction(TransferFunctions.getDefault());
            mCVRend.setVisible(true);

            mCVRend.setVolumeDataBuffer(0,
                    ByteBuffer.wrap(mDataToShow),
                    mDimX,
                    mDimY,
                    mDimZ);

            mCVRend.requestDisplay();


        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    public void reload(byte[] pNewData) {
        this.mDataToShow = pNewData;
        mCVRend.setVolumeDataBuffer(0,
                ByteBuffer.wrap(mDataToShow),
                mDimX,
                mDimY,
                mDimZ);

      //  mCVRend.setVolumeDataUpdateAllowed(true);

      //  mCVRend.requestVolumeCapture();
    }

    public static void main(String[] args) {
        byte[] byteArr = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] byteArr1 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ClearVolumeUnit cvu = new ClearVolumeUnit(byteArr, 3, 3, 3, NativeTypeEnum.UnsignedByte);
        cvu.initializeAndShow();


        byte[] arr = new byte[27];
        String current = "byteArr";
        while (cvu.getmCVRend().isShowing()) {



            switch (current) {
                case ("byteArr"):
                    arr = byteArr1;
                    current = "byteArr1";
                    break;
                case ("byteArr1"):
                    arr = byteArr;
                    current = "byteArr";
                    break;

            }

            System.out.println(current);
            cvu.reload(arr);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}
