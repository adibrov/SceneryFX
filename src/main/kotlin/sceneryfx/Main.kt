package sceneryfx

import io.scif.img.ImgOpener
import javafxgui.JavaFXGUIControlPanelKotlin
import net.imglib2.img.Img
import net.imglib2.type.numeric.integer.UnsignedByteType

fun main(args : Array<String>) {
  println("Hello, world!")

  //val rectangle = RectangleExample()

    var bigStack: Img<UnsignedByteType> = ImgOpener().openImgs("./data/149_fused.tif").get(0) as Img<UnsignedByteType>
    val modelTest: AcquisitionGUIModel = AcquisitionGUIModel(X = Integer(5),Y = Integer(5),  pModelStack = bigStack)
    println("list is empty? " + modelTest.getNodeList().isEmpty())
    val rm: RenderModel = RenderModel(pModel = modelTest)

//    var gm = JavaFXGUIControlPanelKotlin(modelTest)
//    JavaFXGUIControlPanelKotlin.start(modelTest)

    rm.main()
}