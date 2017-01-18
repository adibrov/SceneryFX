package sceneryfx;

import clearvolume.renderer.ClearVolumeRendererInterface;
import clearvolume.renderer.factory.ClearVolumeRendererFactory;
import clearvolume.transferf.TransferFunctions;
import com.jogamp.newt.awt.NewtCanvasAWT;
import coremem.enums.NativeTypeEnum;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import javax.swing.*;
import java.awt.*;
import java.nio.ByteBuffer;

/**
 * Created by dibrov on 12/01/17.
 */
public class ClearVolumeJPanelWindow {
    private ClearVolumeRendererInterface mClearVolumeRenderer;
    private final Frame mJFrame;

    public ClearVolumeJPanelWindow(String pWindowName, int pWindowWidth, int pWindowHeight, NativeTypeEnum
            pNativeTypeEnum, int pMaxTextureWidth, int pMaxTextureHeight, int pNumberOfRenderLayers, boolean
                                           pUseInCanvas) {
        this.mClearVolumeRenderer = ClearVolumeRendererFactory.newBestRenderer(pWindowName, pWindowWidth,
                pWindowHeight, pNativeTypeEnum, pMaxTextureWidth, pMaxTextureWidth,
                pNumberOfRenderLayers, pUseInCanvas);

        final NewtCanvasAWT lNewtCanvasAWT = this.mClearVolumeRenderer.getNewtCanvasAWT();

        mJFrame = new JFrame("ClearVolume");
        mJFrame.setLayout(new BorderLayout());
        final Container lContainer = new Container();
        lContainer.setLayout(new BorderLayout());
        lContainer.add(lNewtCanvasAWT, BorderLayout.CENTER);
        mJFrame.setSize(new Dimension(512, 512));
        mJFrame.add(lContainer);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mJFrame.setVisible(true);
            }
        });
    }

    public ClearVolumeJPanelWindow() {
        this("ClearVolumeFrame", 512, 512,
                NativeTypeEnum.UnsignedByte, 512, 512, 1, true);
    }

    public void RenderSubstack(IntervalView pIntervalView) throws Exception {

        if (pIntervalView.numDimensions() != 3) {
            throw new Exception("Cannot render a non-3D interval");
        }

        long lIntDimX = pIntervalView.dimension(0);
        long lIntDimY = pIntervalView.dimension(1);
        long lIntDimZ = pIntervalView.dimension(2);

        // size
        long sizeInBytes = lIntDimX * lIntDimY * lIntDimZ;
        System.out.println("size in bytes is: " + sizeInBytes);

        // Writing to ByteBuffer
        byte[] lDataBuffer = new byte[(int) sizeInBytes];
        RandomAccess<UnsignedByteType> ra = pIntervalView.randomAccess();

        // reading from buffer
        for (int i = 0; i < lIntDimX; i++) {
            for (int j = 0; j < lIntDimY; j++) {
                for (int k = 0; k < lIntDimZ; k++) {
                    ra.setPosition(new int[]{i, j, k});
                    byte b = ((byte) ra.get().get());
                    lDataBuffer[(int) (i + lIntDimX * j + lIntDimX * lIntDimY * k)] = b;
                }
            }
        }

        mClearVolumeRenderer.setTransferFunction(TransferFunctions.getDefault());
        mClearVolumeRenderer.setVisible(true);

        mClearVolumeRenderer.setVolumeDataBuffer(0,
                ByteBuffer.wrap(lDataBuffer),
                lIntDimX,
                lIntDimY,
                lIntDimZ);
        mClearVolumeRenderer.requestDisplay();

        while (mClearVolumeRenderer.isShowing() && mJFrame.isVisible()) {
            Thread.sleep(100);
        }

        mClearVolumeRenderer.close();
        mJFrame.dispose();

    }

    public static void main(String[] args) {
        ClearVolumeJPanelWindow cvjw = new ClearVolumeJPanelWindow();
        try {
            Img<UnsignedByteType> bigStack = (Img<UnsignedByteType>) (new ImgOpener().openImgs("" +
                    "./sandbox/pics/WingDiskStack8bit" +
                    ".tif")
                    .get(0));


            long[] fromPoint = {200, 200, 1};
            long[] intDim = {500, 500, 55};

            // Creating a smaller view
            IntervalView<UnsignedByteType> cropStack = Views.offsetInterval(bigStack, fromPoint,
                    intDim);

          //  cvjw.RenderSubstack(cropStack);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}