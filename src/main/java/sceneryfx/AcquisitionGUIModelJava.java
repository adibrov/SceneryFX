package sceneryfx;

import cleargl.GLVector;
import graphics.scenery.scenery.Box;
import graphics.scenery.scenery.Material;
import graphics.scenery.scenery.Node;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dibrov on 08/01/17.
 */
public class AcquisitionGUIModelJava extends ModelJava {

    private CursorBoxJava cb;
    Img<UnsignedByteType> mModelStack;

    public AcquisitionGUIModelJava(Img<UnsignedByteType> pModelStack, int x, int y) {
        cb = new CursorBoxJava();
        mModelStack = pModelStack;
        recalculate(x, y);
    }


    public void recalculate(int X, int Y) {
        CopyOnWriteArrayList<Node> listAux = new CopyOnWriteArrayList<>();
        listAux.add(cb.getHolder());
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                float rectSize = 0.5f;
                Box rect = new Box(new GLVector(rectSize, rectSize, rectSize));
                Material rectMat = new Material();
                rectMat.setAmbient(new GLVector(1.0f, 0.0f, 0.0f));
                rectMat.setDiffuse(new GLVector(0.0f, 1.0f, 0.0f));
                rectMat.setSpecular(new GLVector(1.0f, 1.0f, 1.0f));
                rect.setPosition(new GLVector(1.0f * j * rectSize - rectSize * (X / 2), 1.0f * i * rectSize - rectSize * (Y / 2),
                        0.0f));
                rect.setMaterial(rectMat);

                listAux.add(rect);
            }

        }
        modelNodeList = listAux;
        notifyListener();

    }


    public CursorBoxJava getCursorBox() {
        return cb;
    }

    public byte[] getSubStack(long x, long y) {

        long[] fromPoint = new long[] {x, y, 1};
        long[] intDim = new long[]{100, 100, 50};


        // Creating a smaller view
        IntervalView cropStack = Views.offsetInterval(mModelStack, fromPoint, intDim);
        IterableInterval ui = Views.iterable(cropStack);

        Cursor cu = ui.localizingCursor();

        // size
        int sizeInBytes = 1;
        for (long i: intDim) {
            sizeInBytes *= i;
        }


        System.out.println("size in bytes is: " + sizeInBytes);

        // Writing to ByteBuffer
        byte[] buffer = new byte[sizeInBytes];
        RandomAccess<UnsignedByteType> ra = cropStack.randomAccess();


        // characterizing ra
        System.out.println("ra dims: " + ra.numDimensions());
        //  ra.


//            for (int i = 0; i < 2; i++) {
//                System.out.println((byte)ra.get().get());
//
//            }


        // reading from buffer
        for (int i = 0; i<intDim[0]; i++) {
            for (int j = 0; j<intDim[1]; j++) {
                for (int k = 0; k<intDim[2]; k++) {
                    ra.setPosition(new int[] {i, j, k});
                    byte b = ((byte) ra.get().get());
                    buffer[(int) (i + intDim[0] * j + intDim[0] * intDim[1] * k)] = b;

                    //if (i < 0 && j<0 && k <10)

                }
            }
        }

        return buffer;
    }
}
