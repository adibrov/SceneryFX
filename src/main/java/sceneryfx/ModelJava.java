package sceneryfx;

import graphics.scenery.scenery.Node;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dibrov on 08/01/17.
 */
public abstract class ModelJava {
     protected LinkedList<Listener> mListenerList = new LinkedList<>();
     protected CopyOnWriteArrayList<Node> modelNodeList = new CopyOnWriteArrayList<Node>();

    protected void notifyListener(){
        for (Listener listener: mListenerList) {
            listener.fire();
        }
    }

    protected void registerListener(Listener pListener) {
        this.mListenerList.add(pListener);
    }

    protected CopyOnWriteArrayList<Node> getNodeList()  {
        return modelNodeList;
    }
}
