package sceneryfx

/**
 * Created by dibrov on 13/12/16.
 */

import cleargl.GLVector
import cleargl.GLMatrix
import com.jogamp.opengl.GLAutoDrawable
import com.sun.jna.StringArray
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler

import org.scijava.ui.behaviour.ClickBehaviour
//import sample.Main1
import graphics.scenery.scenery.*;
import graphics.scenery.scenery.backends.Renderer
import graphics.scenery.scenery.controls.InputHandler
import graphics.scenery.scenery.controls.behaviours.ArcballCameraControl
import graphics.scenery.scenery.controls.behaviours.FPSCameraControl
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.concurrent.thread






/**
 * Simple example to demonstrate the drawing of 3D lines.
 *
 * This example will draw a nicely illuminated bundle of lines using
 * the [Line] class. The line's width will oscillate while 3 lights
 * circle around the scene.
 *
 * @author Alex <dibrov@mpi-cbg.de>
 */
class RectangleExample : SceneryDefaultApplication("RectangleExample") {
    protected var lineAnimating = true
    var textContent = "Hello world!"

    var scaleFactor = 1.0f

    var list1 = CopyOnWriteArrayList<Node>()
    var list2 = CopyOnWriteArrayList<Node>()

    var flagList = "list1"

    var rect = Box(GLVector(1.5f, 1.5f, 1.5f*scaleFactor))

    fun changeScaleFactorEvent(): ChangeListener<Double>{
        return ChangeListener<Double>(){e, q, r -> scaleFactor = r.toFloat(); rect.scale = GLVector(1.0f, 1.0f, scaleFactor.toFloat()); println("abcabc" + r.toFloat()); }
    }

    override fun init() {
        renderer = Renderer.createRenderer(applicationName,
                scene,
                512,
                512)
        hub.add(SceneryElement.RENDERER, renderer!!)


        val cam: Camera = DetachedHeadCamera()
        cam.position = GLVector(0.0f, 0.0f, 15.0f)
        cam.perspectiveCamera(50.0f, windowWidth.toFloat(), windowHeight.toFloat())
        cam.active = true

        //scene.addChild(cam)


        var rect = Box(GLVector(0.5f, 0.5f, 0.5f))
        val rectMat = Material()

        rectMat.ambient = GLVector(1.0f, 0.0f, 0.0f)
        rectMat.diffuse = GLVector(0.0f, 1.0f, 0.0f)
        rectMat.specular = GLVector(1.0f, 1.0f, 1.0f)
        rect.position = GLVector(0.0f, 0.0f, 0.0f)
        rect.material = rectMat

        //scene.addChild(rect)

        val fb1 = FontBoard()
        fb1.text = "hello"

        val fbmat = Material()
        fb1.material = fbmat

        fbmat.ambient = GLVector(1.0f, 0.0f, 0.0f)
        fbmat.diffuse = GLVector(0.0f, 1.0f, 0.0f)
        fbmat.specular = GLVector(1.0f, 1.0f, 1.0f)
        fb1.position = GLVector(0.0f, 0.0f, 0.0f)
        fb1.material = rectMat
        // fb.view = GLMatrix.getIdentity()
        //scene.addChild(fb1)

        list1.add(cam)
        list1.add(rect)
        list2.add(cam)
        list2.add(fb1)
        scene.children = list1

        var colors = arrayOf(
                GLVector(1.0f, 0.0f, 0.0f),
                GLVector(0.0f, 1.0f, 0.0f),
                GLVector(0.0f, 0.0f, 1.0f)
        )


        var lights = (0..2).map {
            PointLight()
        }

        lights.mapIndexed { i, light ->
            light.position = GLVector(2.0f * i, 2.0f * i, 2.0f * i)
            light.emissionColor = GLVector(1.0f, 0.0f, 1.0f)
            light.intensity = 0.2f*(i+1);
            scene.addChild(light)
        }




        println("list1 is: " + list1.isEmpty())



        //deferredRenderer?.initializeScene(scene)

        thread {
            var t = 0
            while(true) {
//                if(lineAnimating) {
//                    line.addPoint(GLVector(10.0f * Math.random().toFloat() - 5.0f, 10.0f * Math.random().toFloat() - 5.0f, 10.0f * Math.random().toFloat() - 5.0f))
//                    line.edgeWidth = 0.03f * Math.sin(t * Math.PI / 50).toFloat() + 0.004f
//                }

                Thread.sleep(100)
//                if(rect.text != textContent)
//                    rect.text = textContent

                lights.forEachIndexed { i, pointLight ->
                    pointLight.position = GLVector(0.0f, 15.0f*Math.sin(2*i*Math.PI/3.0f+t*Math.PI/50).toFloat(), -15.0f*Math.cos(2*i*Math.PI/3.0f+t*Math.PI/50).toFloat())
                }

                t++
            }

        }




    }

    override fun inputSetup() {
        val target = GLVector(0.0f, 0.0f, 0.0f)
        val inputHandler = (hub.get(SceneryElement.INPUT) as InputHandler)
        val targetArcball = ArcballCameraControl("mouse_control", scene.findObserver(), windowWidth, windowHeight, target)
        val fpsControl = FPSCameraControl("mouse_control", scene.findObserver(), windowWidth, windowHeight)

        val toggleControlMode = object : ClickBehaviour {
            var currentMode = "fps"

            override fun click(x: Int, y: Int) {
                if (currentMode.startsWith("fps")) {
                    targetArcball.target = target

                    inputHandler.addBehaviour("mouse_control", targetArcball)
                    inputHandler.addBehaviour("scroll_arcball", targetArcball)
                    inputHandler.addKeyBinding("scroll_arcball", "scroll")

                    currentMode = "arcball"
                } else {
                    inputHandler.addBehaviour("mouse_control", fpsControl)
                    inputHandler.removeBehaviour("scroll_arcball")

                    currentMode = "fps"
                }

                System.out.println("Switched to $currentMode control")
            }
        }



        val increaseRectZSize = object: ClickBehaviour {
            override fun click(p0: Int, p1: Int) {
                scaleFactor += 0.1f
                val scaleVector = GLVector(1.0f, 1.0f, scaleFactor)
                rect.scale = GLVector(scaleVector)
            }
        }

        val decreaseRectZSize = object: ClickBehaviour {
            override fun click(p0: Int, p1: Int) {
                scaleFactor -= 0.1f
                val scaleVector = GLVector(1.0f, 1.0f, scaleFactor)
                rect.scale = GLVector(scaleVector)
            }
        }

        val toggleLineAnimation = object : ClickBehaviour {
            override fun click(x: Int, y: Int) {
                if (flagList == "list1") {
                    flagList = "list2"
                    scene.children = list2
                }
                else {
                    flagList="list1"
                    scene.children = list1
                }
            }
        }

        inputHandler.addBehaviour("toggle_control_mode", toggleControlMode)
        inputHandler.addKeyBinding("toggle_control_mode", "C")

        inputHandler.addBehaviour("toggle_line_animating", toggleLineAnimation)
        inputHandler.addKeyBinding("toggle_line_animating", "L")

        inputHandler.addBehaviour("increaseRectSize", increaseRectZSize)
        inputHandler.addKeyBinding("increaseRectSize", "B")

        inputHandler.addBehaviour("decreaseRectSize", decreaseRectZSize)
        inputHandler.addKeyBinding("decreaseRectSize", "V")


    }

    override fun main() {

        val strarr = arrayOf("1","2","3")



        // Main1.start()

        super.main()

    }


}
