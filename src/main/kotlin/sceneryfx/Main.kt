package sceneryfx

import javafxgui.GUIMain

fun main(args : Array<String>) {
  println("Hello, world!")

  //val rectangle = RectangleExample()

    val modelTest: Model = Model(5,5)
    println("list is empty? " + modelTest.getList().isEmpty())
    val rm: RenderModel = RenderModel(pModel = modelTest)

    //var gm = GUIMain(modelTest)
    GUIMain.start(modelTest)

    rm.main()
}