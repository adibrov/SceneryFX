import clearvolume.demo.ClearVolumeBasicDemos
import clearvolume.renderer.factory.ClearVolumeRendererFactory
import clearvolume.transferf.TransferFunctions
import coremem.enums.NativeTypeEnum
import coremem.util.Size
import net.imglib2.img.array.ArrayImg
import net.imglib2.img.array.ArrayImgFactory
import net.imglib2.type.numeric.integer.UnsignedByteType
import net.imglib2.util.Fraction
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.lang.Class

import java.lang.reflect.Member
import java.lang.reflect.Method


import java.lang.Math.toIntExact
import java.util.stream.Stream


class ClearVolumeSandbox {


   fun work() {



//        val pRessourceName = "./data/Bucky.raw"
//        val lResourceAsStream: InputStream = FileInputStream(pRessourceName)
//
//        println(lResourceAsStream.available())

    try {


        startSample("./sandbox/data/149_fused.tif",
                NativeTypeEnum.UnsignedByte,
                32,
                32,
                32)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    }





    @Throws(IOException::class, InterruptedException::class)
    private fun startSample(pRessourceName: String,
                            pNativeTypeEnum: NativeTypeEnum,
                            pSizeX: Int,
                            pSizeY: Int,
                            pSizeZ: Int) {
        //val lResourceAsStream = ClearVolumeBasicDemos::class.java.getResourceAsStream(pRessourceName)
        val lResourceAsStream = FileInputStream(pRessourceName)
        startSample(lResourceAsStream,
                pNativeTypeEnum,
                pSizeX,
                pSizeY,
                pSizeZ)
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun startSample(pInputStream: InputStream,
                            pNativeTypeEnum: NativeTypeEnum,
                            pSizeX: Int,
                            pSizeY: Int,
                            pSizeZ: Int) {



        val data = loadData(pInputStream,
                pNativeTypeEnum,
                pSizeX,
                pSizeY,
                pSizeZ)

        var imgArr: ArrayImg<UnsignedByteType, *> = ArrayImg(data, longArrayOf(32,32,32), Fraction(1,1))



        val lClearVolumeRenderer = ClearVolumeRendererFactory.newBestRenderer("ClearVolumeTest",
                512,
                512,
                pNativeTypeEnum,
                false)

        lClearVolumeRenderer.transferFunction = TransferFunctions.getDefault()
        lClearVolumeRenderer.setVisible(true)

        lClearVolumeRenderer.setVolumeDataBuffer(0,
                ByteBuffer.wrap(imgArr.update("hello") as ByteArray),
                pSizeX.toLong(),
                pSizeY.toLong(),
                pSizeZ.toLong())

        lClearVolumeRenderer.requestDisplay()

//        while (lClearVolumeRenderer.isShowing) {
//            Thread.sleep(100)
//        }

    }

    @Throws(IOException::class)
    private fun loadData(pInputStream: InputStream,
                         pNativeTypeEnum: NativeTypeEnum,
                         sizeX: Int,
                         sizeY: Int,
                         sizeZ: Int): ByteArray? {
        // Try to read the specified file
        var data: ByteArray? = null
        val fis = pInputStream
        try {
            val size = Size.of(pNativeTypeEnum) * sizeX.toLong() * sizeY.toLong() * sizeZ.toLong()
            data = ByteArray(toIntExact(size))
            fis.read(data)
        } catch (e: IOException) {
            System.err.println("Could not load input file")
            e.printStackTrace()
            return null
        }

        fis.close()
        return data
    }

    @Throws(IOException::class)
    private fun loadData(pRessourceName: String,
                         pNativeTypeEnum: NativeTypeEnum,
                         sizeX: Int,
                         sizeY: Int,
                         sizeZ: Int): ByteArray? {
        val lResourceAsStream = ClearVolumeBasicDemos::class.java.getResourceAsStream(pRessourceName)

        return loadData(lResourceAsStream,
                pNativeTypeEnum,
                sizeX,
                sizeY,
                sizeZ)
    }

}

fun main(args: Array<String>) {
    var cv = ClearVolumeSandbox()
    cv.work()
}