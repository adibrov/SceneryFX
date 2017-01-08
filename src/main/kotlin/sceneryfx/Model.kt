package sceneryfx

import graphics.scenery.scenery.Node
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by dibrov on 16/12/16.
 */

open class Model(){
    open protected var ListenerList: MutableList<Listener> = mutableListOf()
    open protected var modelNodeList: CopyOnWriteArrayList<Node> = CopyOnWriteArrayList<Node>()

    open fun notifyListener(){
        for (listener in ListenerList) {
            listener.fire()
        }
    }

    open fun registerListener(pListener:Listener) {
        this.ListenerList.add(pListener)
    }

    open fun getNodeList(): CopyOnWriteArrayList<Node> {
        return modelNodeList
    }
}