package sceneryfx;

import clearvolume.demo.ClearVolumeBasicDemos;
import clearvolume.renderer.ClearVolumeRendererInterface;
import clearvolume.renderer.factory.ClearVolumeRendererFactory;
import clearvolume.transferf.TransferFunctions;
import coremem.enums.NativeTypeEnum;
import coremem.util.Size;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static java.lang.Math.toIntExact;

/**
 * Created by dibrov on 22/12/16.
 */
public class CVTests {

    @Test
    public void demoWithFileDatasets() {

        try {
            startSample("./data/Bucky.raw",
                    NativeTypeEnum.UnsignedByte,
                    32,
                    32,
                    32);
        } catch (final Throwable e) {
            e.printStackTrace();
        }

    }

    private static void startSample(final String pRessourceName,
                                    final NativeTypeEnum pNativeTypeEnum,
                                    final int pSizeX,
                                    final int pSizeY,
                                    final int pSizeZ) throws IOException,
            InterruptedException {
//        final InputStream lResourceAsStream =
//                CVTests.class.getResourceAsStream(pRessourceName);


        final InputStream lResourceAsStream = new FileInputStream(pRessourceName);
        startSample(lResourceAsStream,
                pNativeTypeEnum,
                pSizeX,
                pSizeY,
                pSizeZ);
    }

    private static void startSample(final InputStream pInputStream,
                                    final NativeTypeEnum pNativeTypeEnum,
                                    final int pSizeX,
                                    final int pSizeY,
                                    final int pSizeZ) throws IOException,
            InterruptedException {

        final byte[] data = loadData(pInputStream,
                pNativeTypeEnum,
                pSizeX,
                pSizeY,
                pSizeZ);

        final ClearVolumeRendererInterface lClearVolumeRenderer =
                ClearVolumeRendererFactory.newBestRenderer("ClearVolumeTest",
                        512,
                        512,
                        pNativeTypeEnum,
                        false);

        lClearVolumeRenderer.setTransferFunction(TransferFunctions.getDefault());
        lClearVolumeRenderer.setVisible(true);

        lClearVolumeRenderer.setVolumeDataBuffer(0,
                ByteBuffer.wrap(data),
                pSizeX,
                pSizeY,
                pSizeZ);

        lClearVolumeRenderer.requestDisplay();

        while (lClearVolumeRenderer.isShowing()) {
            Thread.sleep(100);
        }

    }

    private static byte[] loadData(final InputStream pInputStream,
                                   final NativeTypeEnum pNativeTypeEnum,
                                   final int sizeX,
                                   final int sizeY,
                                   final int sizeZ) throws IOException {
        // Try to read the specified file
        byte data[] = null;
        final InputStream fis = pInputStream;
        try {
            final long size = Size.of(pNativeTypeEnum) * sizeX
                    * sizeY
                    * sizeZ;
            data = new byte[toIntExact(size)];
            fis.read(data);
        } catch (final IOException e) {
            System.err.println("Could not load input file");
            e.printStackTrace();
            return null;
        }
        fis.close();
        return data;
    }

    @SuppressWarnings("unused")
    private static byte[] loadData(final String pRessourceName,
                                   final NativeTypeEnum pNativeTypeEnum,
                                   final int sizeX,
                                   final int sizeY,
                                   final int sizeZ) throws IOException {
        final InputStream lResourceAsStream =
                ClearVolumeBasicDemos.class.getResourceAsStream(pRessourceName);

        return loadData(lResourceAsStream,
                pNativeTypeEnum,
                sizeX,
                sizeY,
                sizeZ);
    }
}
