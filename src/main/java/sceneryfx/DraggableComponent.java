package sceneryfx;

import net.imglib2.img.Img;
import net.imglib2.ui.TransformListener;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;

import static sceneryfx.AcquisitionUnit.getRandomStack;

/**
 * Created by dibrov on 25/01/17.
 */
public class DraggableComponent extends JPanel implements DragGestureListener, DragSourceListener {
    DragSource dragSource;
    Img mImg;

    public DraggableComponent(Img pImg) {
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        mImg = pImg;
    }

    public void setImg(Img pImg) {
        this.mImg = pImg;
    }

    public DraggableComponent() {
        dragSource = new DragSource();

        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        mImg = getRandomStack(100,100,100);

    }

    public void dragGestureRecognized(DragGestureEvent evt) {
        //Transferable t = new StringSelection("aString");
        Transferable t = new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[0];
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return true;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return mImg;
            }
        };
        dragSource.startDrag(evt, DragSource.DefaultCopyDrop, t, this);
    }

    public void dragEnter(DragSourceDragEvent evt) {
        System.out.println("enters");
    }

    public void dragOver(DragSourceDragEvent evt) {

        System.out.println("over");
    }

    public void dragExit(DragSourceEvent evt) {
        System.out.println("leaves");
    }

    public void dropActionChanged(DragSourceDragEvent evt) {
        System.out.println("changes the drag action between copy or move");
    }

    public void dragDropEnd(DragSourceDropEvent evt) {
        System.out.println("finishes or cancels the drag operation");
        getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);


    }
}
