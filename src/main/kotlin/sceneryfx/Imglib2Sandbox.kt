package sceneryfx

import ij.IJ
import ij.ImagePlus
import io.scif.img.ImgOpener
import net.imglib2.img.Img
import net.imglib2.img.array.ArrayImgFactory
import net.imglib2.img.display.imagej.ImageJFunctions
import net.imglib2.type.numeric.integer.UnsignedByteType
import net.imglib2.type.numeric.real.FloatType
import net.imglib2.view.Views

import net.imglib2.RandomAccessibleInterval



import net.imglib2.IterableInterval
import net.imglib2.RandomAccessible
import net.imglib2.img.ImagePlusAdapter
import net.imglib2.img.array.ArrayImg
import net.imglib2.type.Type
import net.imglib2.util.Fraction


/**
 * Created by dibrov on 18/12/16.
 */

fun <T : Type<T>> copy(source: RandomAccessible<T>,
                       target: IterableInterval<T>) {
    // create a cursor that automatically localizes itself on every move
    val targetCursor = target.localizingCursor()
    val sourceRandomAccess = source.randomAccess()

    // iterate over the input cursor
    while (targetCursor.hasNext()) {
        // move input cursor forward
        targetCursor.fwd()

        // set the output cursor to the position of the input cursor
        sourceRandomAccess.setPosition(targetCursor)

        // set the value of this pixel of the output image, every Type supports T.set( T type )
        targetCursor.get().set(sourceRandomAccess.get())
    }
}

fun main(args: Array<String>) {
    val image = ArrayImgFactory<UnsignedByteType>().create(longArrayOf(400, 320), UnsignedByteType())
//
//    var img: Img<FloatType> = ImgOpener().openImg("./data/149_fused.tif",
//            ArrayImgFactory<FloatType>()) as Img<FloatType>;
    var imp: ImagePlus = IJ.openImage("./data/DrosophilaWing.tif")



    // var img1 =  ImageJFunctions.wrap< UnsignedByteType >(imp);
    //   ImageJFunctions.show(img1)
    var img1: Img<FloatType> = ImgOpener().openImgs("./data/DrosophilaWing.tif").get(0) as Img<FloatType>

    // img1.t

    //   var out: FileOutputStream = FileOutputStream("imgOut")

    var x = shortArrayOf(1,2,3)


    var imgArr: ArrayImg<UnsignedByteType, *> = ArrayImgFactory<UnsignedByteType>().create(longArrayOf(1,1,
            1), UnsignedByteType() )
    var y = imgArr.update(null)
  //  println("array is : " + y.get(0) + y.get(1) + y.get(2))




// use a View to define an interval (min and max coordinate, inclusive) to display
    val view = Views.interval(img1, longArrayOf(0, 0, 0), longArrayOf(1000, 1000, 50))

    val viewSource = Views.offsetInterval(img1, longArrayOf(100, 100), longArrayOf(250, 150))
    val duplicate = img1.factory().create(longArrayOf(100,100), FloatType())

    val iterableTarget = Views.iterable(duplicate)

//    copy(viewSource, iterableTarget)


    ImageJFunctions.show(duplicate)
}
