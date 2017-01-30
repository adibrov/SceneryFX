package sceneryfx;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class SwingSandbox {
    public static void main(String[] argv) throws Exception {

        JComponent com = new DraggableComponent();

        JFrame f = new JFrame();

        f.add(com);
        f.setSize(300, 300);
        f.setVisible(true);
    }
}



   