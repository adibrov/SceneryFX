package sceneryfx

import coremem.enums.NativeTypeEnum
import io.scif.img.ImgOpener
import javafxgui.GUIMain
import net.imglib2.img.Img
import net.imglib2.type.numeric.integer.UnsignedByteType
import org.junit.Test
import sceneryfx.RenderModel
import sceneryfx.acquisitionGUIModel
import kotlin.concurrent.thread

/**
 * Created by dibrov on 27/12/16.
 */
@Test fun main(args : Array<String>) {
    println("Hello, world!")

    //val rectangle = RectangleExample()

    var bigStack: Img<UnsignedByteType> = ImgOpener().openImgs("./sandbox/pics/149_fused.tif").get(0) as
            Img<UnsignedByteType>


    val modelTest: acquisitionGUIModel = acquisitionGUIModel(X = Integer(5), Y = Integer(5), pModelStack =  bigStack)
    println("list is empty? " + modelTest.getNodeList().isEmpty())

    val cvu = ClearVolumeUnit(modelTest.getSubStack(0, 0), 100, 100, 50,
            NativeTypeEnum.UnsignedByte)

    cvu.initializeAndShow()

    var gm = GUIMain(modelTest, cvu)
    GUIMain.start(modelTest, cvu)




//    val rm = RenderModel(modelTest)
//    rm.main()



}