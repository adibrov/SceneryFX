package tools;

import io.scif.img.ImgIOException;
import net.imglib2.IterableInterval;
import net.imglib2.Iterator;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import io.scif.img.ImgOpener;
import net.imglib2.view.Views;
import sceneryfx.Imglib2SandboxKt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * Created by dibrov on 19/12/16.
 */
public class Converters {

    public static void tif2byte(String pathIn, String pathOut){
        try {

            FileOutputStream outim = new FileOutputStream(pathOut);
            double t1 = System.nanoTime();

            Img<UnsignedByteType> img = (Img<UnsignedByteType>) new ImgOpener().openImgs(pathIn)
                    .get(0);

            double t2 = System.nanoTime();

            System.out.println("opening took: " + (t2-t1));

            System.out.println("dimensions: " + img.dimension(0) + " " + img.dimension(1) + " " + img.dimension(2));

            t1 = System.nanoTime();

            for (UnsignedByteType item : img) {
                //System.out.println("float is: " + item);
                outim.write(item.get());
            }
            t2 = System.nanoTime();

            System.out.println("writing stream took: " + (t2-t1));

            outim.close();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //tif2byte("./data/149_fused.tif", "./data/WingStackRaw");

        try {


            FileInputStream inim = new FileInputStream("./data/WingStackRaw");
            byte[] arr = new byte[1976*3868*59];
            double t1 = System.nanoTime();
            inim.read(arr);
            double t2 = System.nanoTime();

            System.out.println("reading stream took: " + (t2-t1)/10E9);

            t1 = System.nanoTime();
            Img<UnsignedByteType> im = ArrayImgs.unsignedBytes(arr, 1976,3868, 59);
            t2 = System.nanoTime();

            System.out.println("wrapping img took: " + (t2-t1)/10E9);

            RandomAccessibleInterval< UnsignedByteType > viewSource = Views.offsetInterval( im,
                    new long[] { 400, 500, 0 }, new long[]{ 1000, 1000, 50 } );

            Img<UnsignedByteType> duplicate = im.factory().create(new long[] {900,900, 50}, new
                    UnsignedByteType());

            IterableInterval< UnsignedByteType > iterableTarget = Views.iterable(duplicate);

            Imglib2SandboxKt.copy(viewSource, iterableTarget);


            //ImageJFunctions.show(duplicate);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
