package sceneryfx

import cleargl.GLVector
import scenery.Box
import scenery.Material
import scenery.Node
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by dibrov on 14/12/16.
 */

class Model {
    private var xSize = 5
    private var ySize = 5
    private var ListenerList: CopyOnWriteArrayList<Listener> = CopyOnWriteArrayList()
    private var cb = CursorBox()




    private var modelList = CopyOnWriteArrayList<Node>()

    constructor (X:Int, Y:Int) {
        modelList.add(cb.getHolder())
        for (i in 0..X-1) {
            for (j in 0..Y-1) {
                var rectSize = 0.5f
                var rect = Box(GLVector(rectSize, rectSize, rectSize))
                val rectMat = Material()
                rectMat.ambient = GLVector(1.0f, 0.0f, 0.0f)
                rectMat.diffuse = GLVector(0.0f, 1.0f, 0.0f)
                rectMat.specular = GLVector(1.0f, 1.0f, 1.0f)
                rect.position = GLVector(1.0f * j * rectSize, 1.0f * i*rectSize, 0.0f)
                rect.material = rectMat
                modelList.add(rect)
            }

        }

        notifyListener()

    }

    fun recalculate (X:Int, Y:Int) {
        val listAux = CopyOnWriteArrayList<Node>()
        listAux.add(cb.getHolder())
        for (i in 0..X-1) {
            for (j in 0..Y-1) {
                var rectSize = 0.5f
                var rect = Box(GLVector(rectSize, rectSize, rectSize))
                val rectMat = Material()
                rectMat.ambient = GLVector(1.0f, 0.0f, 0.0f)
                rectMat.diffuse = GLVector(0.0f, 1.0f, 0.0f)
                rectMat.specular = GLVector(1.0f, 1.0f, 1.0f)
                rect.position = GLVector(1.0f * j * rectSize, 1.0f * i*rectSize, 0.0f)
                rect.material = rectMat
                listAux.add(rect)
            }

        }
        modelList = listAux
        notifyListener()

    }

    fun changeX(newXSize:Int){
        for (i in 0..ySize-1) {
            for (j in xSize..newXSize - 1) {
                var rectSize = 0.5f
                var rect = Box(GLVector(rectSize, rectSize, rectSize))
                val rectMat = Material()
                rectMat.ambient = GLVector(1.0f, 0.0f, 0.0f)
                rectMat.diffuse = GLVector(0.0f, 1.0f, 0.0f)
                rectMat.specular = GLVector(1.0f, 1.0f, 1.0f)
                rect.position = GLVector(1.0f * j * rectSize, 1.0f * i*rectSize, 0.0f)
                rect.material = rectMat
                modelList.add(rect)
            }

        }
        xSize = newXSize
        notifyListener()
    }

    fun changeY(newYSize:Int){
        for (i in ySize..newYSize-1){
            for (j in 0..xSize-1){
                var rectSize = 0.5f
                var rect = Box(GLVector(rectSize, rectSize, rectSize))
                val rectMat = Material()
                rectMat.ambient = GLVector(1.0f, 0.0f, 0.0f)
                rectMat.diffuse = GLVector(0.0f, 1.0f, 0.0f)
                rectMat.specular = GLVector(1.0f, 1.0f, 1.0f)
                rect.position = GLVector(1.0f*j*rectSize, 1.0f*i*rectSize, 0.0f)
                rect.material = rectMat
                modelList.add(rect)
            }

        }
        ySize = newYSize
        notifyListener()
    }



    fun getList():CopyOnWriteArrayList<Node>{
        return modelList
    }

    fun notifyListener(){
        for (item in ListenerList) {
            item.notifyMe()
        }
    }

    fun registerListener(sth:Listener) {
        this.ListenerList.add(sth)
    }

    fun getCursorBox(): CursorBox{
        return cb
    }


}