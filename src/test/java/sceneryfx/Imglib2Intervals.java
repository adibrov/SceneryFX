package sceneryfx;

import clearvolume.renderer.ClearVolumeRendererInterface;
import clearvolume.renderer.factory.ClearVolumeRendererFactory;
import clearvolume.transferf.TransferFunctions;
import coremem.enums.NativeTypeEnum;
import io.scif.img.ImgOpener;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.Views;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by dibrov on 05/01/17.
 */
public class Imglib2Intervals {
    @Test
    public void IntervalToByteStream() {


        try {
            // Loading a raw stack form HDD
            Img<UnsignedByteType> bigStack = (Img<UnsignedByteType>) (new ImgOpener().openImgs("./pics/149_fused.tif")
                    .get(0));


            // ImageJFunctions.show(bigStack);
            // Defining interval

            long[] fromPoint = {570, 570, 1};
            long[] intDim = {100, 100, 55};

            // Creating a smaller view
            RandomAccessibleInterval<UnsignedByteType> cropStack = Views.offsetInterval(bigStack, fromPoint,
                    intDim);

            IterableInterval<UnsignedByteType> ui = Views.iterable(cropStack);

            Cursor<UnsignedByteType> cu = ui.localizingCursor();

            // size
            int sizeInBytes = 1;
            for (int i = 0; i < fromPoint.length; i++) {
                sizeInBytes *= intDim[i];
            }


            System.out.println("size in bytes is: " + sizeInBytes);

            // Writing to ByteBuffer
            byte[] buffer = new byte[sizeInBytes];
            //buffer.p



            RandomAccess<UnsignedByteType> ra = cropStack.randomAccess();



            // characterizing ra
            System.out.println("ra dims: " + ra.numDimensions());
          //  ra.





//            for (int i = 0; i < 2; i++) {
//                System.out.println((byte)ra.get().get());
//
//            }


            // reading from buffer
            for (int i = 0; i < intDim[0]; i++) {
                for (int j = 0; j < intDim[1]; j++) {
                    for (int k = 0; k < intDim[2]; k++) {
                        ra.setPosition(new int[]{i,j,k});
                        byte b = (byte) ra.get().get();
                        buffer[i + (int)intDim[0]*j +(int)intDim[0]*(int)intDim[1]*k] = b;

                        //if (i < 0 && j<0 && k <10)

                    }
                }
            }

//            for (int i = 0; i < sizeInBytes; i++) {
//                ra.setPosition(cu);
//
//                buffer[i] = (byte) ra.get().get();
//
//                if (i < 100) {
//                  //  System.out.println("unsigned byte #" + i + "is: " + a);
//                    System.out.println("byte #" + i + "is: " + buffer[i]);
//                }
//                cu.fwd();
//            }

            ClearVolumeRendererInterface cvrend = ClearVolumeRendererFactory.newBestRenderer("ClearVolumeTest",
                    512,
                    512,
                    NativeTypeEnum.UnsignedByte,
                    false);


            cvrend.setTransferFunction(TransferFunctions.getDefault());
            cvrend.setVisible(true);

            cvrend.setVolumeDataBuffer(0,
                    ByteBuffer.wrap(buffer),
                    intDim[0],
                    intDim[1],
                    intDim[2]);

            cvrend.requestDisplay();

            while (cvrend.isShowing()) {
                Thread.sleep(100);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
