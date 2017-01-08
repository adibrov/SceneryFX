package sceneryfx

import cleargl.GLVector
import graphics.scenery.scenery.Box
import graphics.scenery.scenery.Material
import graphics.scenery.scenery.Mesh
import graphics.scenery.scenery.Node

/**
 * Created by dibrov on 15/12/16.
 */

class CursorBox(pPosition: GLVector = GLVector(0.0f, 0.0f, 0.0f), pSize: GLVector = GLVector(.5f, 0.5f, 0.5f)) {

    private var listenerList: MutableList<Listener>
    private var box: Box
    private var scaleVector: GLVector
    private var holder: Mesh



    init {
        listenerList = mutableListOf()
        box = Box(pSize)
        box.position = pPosition
        scaleVector = GLVector(1.0f, 1.0f, 1.0f)

        holder = Mesh("holder")
//
//        val hMat = Material()
//        hMat.ambient = GLVector(1.0f, 0.0f, 0.0f)
//        hMat.diffuse = GLVector(1.0f, 1.0f, 1.0f)
//        hMat.specular = GLVector(1.0f, 1.0f, 1.0f)
//        holder.material = hMat

        val cbMat = Material()
        cbMat.ambient = GLVector(1.0f, 0.0f, 0.0f)
        cbMat.diffuse = GLVector(1.0f, 1.0f, 0.0f)
        cbMat.specular = GLVector(1.0f, 1.0f, 1.0f)
        box.material = cbMat

        holder.addChild(box)
        holder.visible = true
    }

    fun registerListener(pListenerToRegister: Listener) {
        listenerList.add(pListenerToRegister)
    }

    fun notifyListeners() {
        for (listener in listenerList) {
            listener.fire()
        }
    }

    fun updateSizeX(x: Float = 1.0f) {

        box.scale.set(0, x)
        box.needsUpdate = true
        box.needsUpdateWorld = true

        println("sizeX is updated to: " + x)

    }

    fun updateSizeY(y: Float = 1.0f) {

        box.scale.set(1, y)
        box.needsUpdate = true
        box.needsUpdateWorld = true
        println("sizeY is updated to: " + y)

    }

    fun updateSizeZ(z: Float = 1.0f) {

        box.scale.set(2, z)
        box.needsUpdate = true
        box.needsUpdateWorld = true
        println("sizeZ is updated to: " + z)

    }

    fun updateX(x: Float = 0.0f) {
        holder.position.set(0,x)
        holder.needsUpdate = true
        holder.needsUpdateWorld = true
    }

    fun updateY(y: Float = 0.0f) {
        holder.position.set(1,y)
        holder.needsUpdate = true
        holder.needsUpdateWorld = true
    }

    fun updateZ(z: Float = 0.0f) {
        holder.position.set(2,z)
        holder.needsUpdate = true
        holder.needsUpdateWorld = true
    }

    fun getSize(): GLVector {
        return box.sizes
    }

    fun getPosition(): GLVector {
        return box.position
    }

    fun getNode(): Box {
        return box
    }

    fun getHolder(): Mesh {
        return holder
    }


}