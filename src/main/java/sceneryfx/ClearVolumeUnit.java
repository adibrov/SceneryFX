package sceneryfx;

import clearvolume.renderer.ClearVolumeRendererInterface;
import clearvolume.renderer.factory.ClearVolumeRendererFactory;
import clearvolume.transferf.TransferFunctions;
import com.jogamp.newt.awt.NewtCanvasAWT;
import coremem.enums.NativeTypeEnum;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.TooManyListenersException;

/**
 * Created by dibrov on 06/01/17.
 */
public class ClearVolumeUnit {
    private byte[] mDataToShow;
    private int mDimX, mDimY, mDimZ;
    private DropFrame mJFrame;
    private DropTarget mDropTarget;


    public ClearVolumeRendererInterface getmCVRend() {
        return mCVRend;
    }

    private ClearVolumeRendererInterface mCVRend;
    private NativeTypeEnum mTypeEnum;

    public ClearVolumeUnit(NativeTypeEnum pTypeEnum) {

        this.mTypeEnum = pTypeEnum;

        this.mCVRend = ClearVolumeRendererFactory.newBestRenderer("ClearVolumeTest",
                512,
                512,
                mTypeEnum,
                false);
    }

    public JFrame getJFrame() {
        return mJFrame;
    }

    public ClearVolumeUnit(NativeTypeEnum pTypeEnum, boolean pUseSwing) {

        this.mTypeEnum = pTypeEnum;

        this.mCVRend = ClearVolumeRendererFactory.newBestRenderer("ClearVolumeTest",
                512,
                512,
                mTypeEnum,
                pUseSwing);
        final NewtCanvasAWT lNewtCanvasAWT = this.mCVRend.getNewtCanvasAWT();

        mJFrame = new DropFrame("DropFrame") {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                super.drop(dtde);
                System.out.println("drop!!!");
                ;
                try {
                    Img img = (Img)dtde.getTransferable().getTransferData(DataFlavor.imageFlavor);
                    mDataToShow = convert(img);
                    int x = (int)img.dimension(0);
                    int y = (int)img.dimension(1);
                    int z = (int)img.dimension(2);
                    initializeAndShowInSwingWindow(mDataToShow,x,y,z);

                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mDropTarget = new DropTarget(mJFrame, DnDConstants.ACTION_COPY_OR_MOVE, null);
        try {
            mDropTarget.addDropTargetListener(mJFrame);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }


        mJFrame.setLayout(new BorderLayout());
        final Container lContainer = new Container();
        lContainer.setLayout(new BorderLayout());
        lContainer.add(lNewtCanvasAWT, BorderLayout.CENTER);
        mJFrame.setSize(new Dimension(512, 512));
        mJFrame.add(lContainer);

    }

    public void initializeAndShowInSwingWindow(byte[] pDataToShow, int pDimX, int pDimY, int pDimZ){



        TransferHandler lTransferHandler = new TransferHandler(){

            public boolean importData(TransferHandler.TransferSupport support) {

                Transferable t = support.getTransferable();
                DataFlavor[] df = t.getTransferDataFlavors();
                try {
                    System.out.println("bla");
                    Img lImg = (Img)t.getTransferData(df[0]);
                    mDataToShow = convert(lImg);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                } catch (Exception e) {

                }

                return true;
            }

        };
        mJFrame.setTransferHandler(lTransferHandler);



        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mJFrame.setVisible(true);
            }
        });

        try {
            this.mDataToShow = pDataToShow;

            this.mDimX = pDimX;
            this.mDimY = pDimY;
            this.mDimZ = pDimZ;


            this.mCVRend.setTransferFunction(TransferFunctions.getDefault());
            this.mCVRend.setVisible(true);

            this.mCVRend.setVolumeDataBuffer(0,
                    ByteBuffer.wrap(mDataToShow),
                    mDimX,
                    mDimY,
                    mDimZ);


            this.mCVRend.requestDisplay();


        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }
    public void initializeAndShow(byte[] pDataToShow, int pDimX, int pDimY, int pDimZ) {


        try {
            this.mDataToShow = pDataToShow;

            this.mDimX = pDimX;
            this.mDimY = pDimY;
            this.mDimZ = pDimZ;


            this.mCVRend.setTransferFunction(TransferFunctions.getDefault());
            this.mCVRend.setVisible(true);

            this.mCVRend.setVolumeDataBuffer(0,
                    ByteBuffer.wrap(mDataToShow),
                    mDimX,
                    mDimY,
                    mDimZ);


            this.mCVRend.requestDisplay();


        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    public void initializeAndShow(Img pDataToShow) {


        try {
            this.mDataToShow = convert(pDataToShow);

            this.mDimX = (int)pDataToShow.dimension(0);
            this.mDimY = (int)pDataToShow.dimension(1);
            this.mDimZ = (int)pDataToShow.dimension(2);


            this.mCVRend.setTransferFunction(TransferFunctions.getDefault());
            this.mCVRend.setVisible(true);

            this.mCVRend.setVolumeDataBuffer(0,
                    ByteBuffer.wrap(mDataToShow),
                    mDimX,
                    mDimY,
                    mDimZ);

            this.mCVRend.requestDisplay();


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

    public byte[] convert(Img pNewData) {

        int lDimX = (int)pNewData.dimension(0);
        int lDimY = (int)pNewData.dimension(1);
        int lDimZ = (int)pNewData.dimension(2);

        int sizeInBytes = lDimX*lDimY*lDimZ;

        byte[] buffer = new byte[sizeInBytes];
        RandomAccess<UnsignedByteType> ra = pNewData.randomAccess();


        // reading from buffer
        for (int i = 0; i<lDimX; i++) {
            for (int j = 0; j<lDimY; j++) {
                for (int k = 0; k<lDimZ; k++) {
                    ra.setPosition(new int[] {i, j, k});
                    byte b = ((byte) ra.get().get());
                    buffer[(int) (i + lDimX * j + lDimX * lDimY * k)] = b;

                    //if (i < 0 && j<0 && k <10)

                }
            }
        }

       return buffer;
        //  mCVRend.setVolumeDataUpdateAllowed(true);

        //  mCVRend.requestVolumeCapture();
    }

    public static void main(String[] args) {
        byte[] byteArr = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] byteArr1 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ClearVolumeUnit cvu = new ClearVolumeUnit(NativeTypeEnum.UnsignedByte, true);
        cvu.initializeAndShowInSwingWindow(byteArr, 3, 3, 3);


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
