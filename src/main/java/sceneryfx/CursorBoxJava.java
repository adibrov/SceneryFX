package sceneryfx;

import cleargl.GLVector;
import graphics.scenery.scenery.Box;
import graphics.scenery.scenery.Material;
import graphics.scenery.scenery.Mesh;

import java.util.LinkedList;

/**
 * Created by dibrov on 08/01/17.
 */
public class CursorBoxJava {

    private LinkedList<Listener> mListenerList;
    private Box box;
    private GLVector scaleVector;
    private Mesh holder;

    public CursorBoxJava(){
        this(new GLVector(0.0f, 0.0f, 0.0f), new GLVector(0.5f, 0.5f, 0.5f));
    }

    public CursorBoxJava(GLVector pPosition, GLVector pSize){
        this.mListenerList = new LinkedList<>();
        box = new Box(pSize);
        box.setPosition(pPosition);
        scaleVector = new GLVector(1.0f, 1.0f, 1.0f);

        holder = new Mesh("holder");

        Material cbMat = new Material();
        cbMat.setAmbient(new GLVector(1.0f, 0.0f, 0.0f));
        cbMat.setDiffuse(new GLVector(1.0f, 1.0f, 0.0f));
        cbMat.setSpecular(new GLVector(1.0f, 1.0f, 1.0f));
        box.setMaterial(cbMat);

        holder.addChild(box);
        holder.setVisible(true);
    }

    public void registerListener(Listener pListenerToRegister) {
        mListenerList.add(pListenerToRegister);
    }

    public void notifyListeners() {
        for (Listener listener: mListenerList) {
            listener.fire();
        }
    }


    public void updateSizeX(Float x) {

        box.getScale().set(0, x);
        box.setNeedsUpdate(true);
        box.setNeedsUpdateWorld(true);

        System.out.println("sizeX is updated to: " + x);

    }

    public void updateSizeY(Float y) {

        box.getScale().set(1, y);
        box.setNeedsUpdate(true);
        box.setNeedsUpdateWorld(true);

        System.out.println("sizY is updated to: " + y);
    }

    public void updateSizeZ(Float z) {

        box.getScale().set(2, z);
        box.setNeedsUpdate(true);
        box.setNeedsUpdateWorld(true);

        System.out.println("sizeZ is updated to: " + z);

    }

    public void updateX(Float x) {
        holder.getPosition().set(0,x);
        holder.setNeedsUpdate(true);
        holder.setNeedsUpdateWorld(true);
    }

    public void updateY(Float y) {
        holder.getPosition().set(1,y);
        holder.setNeedsUpdate(true);
        holder.setNeedsUpdateWorld(true);
    }

    public void updateZ(Float z) {
        holder.getPosition().set(2,z);
        holder.setNeedsUpdate(true);
        holder.setNeedsUpdateWorld(true);
    }

    public GLVector getSize(){
        return box.getSizes();
    }

    public GLVector getPosition() {
        return box.getPosition();
    }

    public Box getNode() {
        return box;
    }

    public Mesh getHolder() {
        return holder;
    }

}
