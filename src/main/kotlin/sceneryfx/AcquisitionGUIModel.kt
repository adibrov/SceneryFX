package sceneryfx

import cleargl.GLVector
import io.scif.img.ImgOpener
import net.imglib2.img.Img
import net.imglib2.img.array.ArrayImg
import net.imglib2.img.array.ArrayImgFactory
import net.imglib2.img.cell.CellImgFactory
import net.imglib2.img.display.imagej.ImageJFunctions
import net.imglib2.type.numeric.integer.UnsignedByteType
import net.imglib2.type.numeric.real.FloatType
import net.imglib2.view.Views
import graphics.scenery.scenery.Box
import graphics.scenery.scenery.Material
import graphics.scenery.scenery.Node
import net.imglib2.img.ImgFactory
import java.util.concurrent.CopyOnWriteArrayList


/**
 * Created by dibrov on 14/12/16.
 */

class AcquisitionGUIModel(pModelStack:Img<UnsignedByteType>, X:Integer, Y:Integer) : Model() {

    private var cb = CursorBox()
    val bt: UnsignedByteType? = UnsignedByteType()
    val mModelStack:Img<UnsignedByteType>
//    var locX: Long
//    var locY: Long


    init {
        mModelStack = pModelStack
        recalculate(X.toInt(), Y.toInt())
//        locX = (cb.getPosition().x()*2000)
//        locY
        //notifyListener()

        println("model created")
    }

    fun recalculate(X: Int, Y: Int) {
        val listAux = CopyOnWriteArrayList<Node>()
        listAux.add(cb.getHolder())
        for (i in 0..X - 1) {
            for (j in 0..Y - 1) {
                var rectSize = 0.5f
                var rect = Box(GLVector(rectSize, rectSize, rectSize))
                val rectMat = Material()
                rectMat.ambient = GLVector(1.0f, 0.0f, 0.0f)
                rectMat.diffuse = GLVector(0.0f, 1.0f, 0.0f)
                rectMat.specular = GLVector(1.0f, 1.0f, 1.0f)
                rect.position = GLVector(1.0f * j * rectSize - rectSize * (X / 2), 1.0f * i * rectSize - rectSize * (Y / 2),
                        0.0f)
                rect.material = rectMat

                listAux.add(rect)
            }

        }
        modelNodeList = listAux
        notifyListener()

    }


    fun getCursorBox(): CursorBox {
        return cb
    }

    fun getSubStack(x:Long, y:Long):ByteArray {

        val fromPoint = longArrayOf(x, y, 1)
        val intDim = longArrayOf(100, 100, 50)


        // Creating a smaller view
        val cropStack = Views.offsetInterval<UnsignedByteType>(mModelStack, fromPoint,
                intDim)

        val ui = Views.iterable(cropStack)

        val cu = ui.localizingCursor()

        // size
        var sizeInBytes = 1
        for (i in fromPoint.indices) {
            sizeInBytes *= intDim[i].toInt()
        }


        println("size in bytes is: " + sizeInBytes)

        // Writing to ByteBuffer
        val buffer = ByteArray(sizeInBytes)
        //buffer.p


        val ra = cropStack.randomAccess()


        // characterizing ra
        println("ra dims: " + ra.numDimensions())
        //  ra.


//            for (int i = 0; i < 2; i++) {
//                System.out.println((byte)ra.get().get());
//
//            }


        // reading from buffer
        for (i in 0..intDim[0] - 1) {
            for (j in 0..intDim[1] - 1) {
                for (k in 0..intDim[2] - 1) {
                    ra.setPosition(intArrayOf(i.toInt(), j.toInt(), k.toInt()))
                    val b = ra.get().get().toByte()
                    buffer[(i + intDim[0] * j + intDim[0] * intDim[1] * k).toInt()] = b

                    //if (i < 0 && j<0 && k <10)

                }
            }
        }

        return buffer
    }



}