package sceneryfx

import cleargl.GLVector
import org.scijava.ui.behaviour.ClickBehaviour
import scenery.*
import scenery.backends.Renderer
import scenery.controls.InputHandler
import scenery.controls.behaviours.ArcballCameraControl
import scenery.controls.behaviours.FPSCameraControl
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.concurrent.thread

/**
 * Created by dibrov on 14/12/16.
 */

class RenderModel(pModel: Model, pRenderer: Renderer? = null, pCamera: Camera? = null, pLights:
List<PointLight>? = null) : SceneryDefaultApplication("RenderModel") {

    private val cam: Node
    private val lights: List<Node>
    private val model: Model

    private fun updateModelChildren(pUpdatedModel: CopyOnWriteArrayList<Node>){
        scene.children = pUpdatedModel
    }

    init {


        if (pModel.getList().isEmpty()) {
            throw Exception("Refuse to render an empty model!")
        } else {
            model = pModel
        }

        val updateListener = object :Listener {
            override fun notifyMe() {
                updateModelChildren(model.getList())
            }
        }

        model.registerListener(updateListener)


        if (pRenderer == null) {
            renderer = Renderer.createRenderer(applicationName, scene, 512, 512)
        } else {
            renderer = pRenderer
        }

        if (pCamera == null) {
            cam = DetachedHeadCamera()
            cam.position = GLVector(0.0f, 0.0f, 15.0f)
            cam.perspectiveCamera(50.0f, windowWidth.toFloat(), windowHeight.toFloat())
            cam.active = true
        } else {
            cam = pCamera

        }

        if (pLights == null) {
            lights = (0..2).map {
                PointLight()
            }

            lights.mapIndexed { i, light ->
                light.position = GLVector(2.0f * i, 2.0f * i, 2.0f * i)
                light.emissionColor = GLVector(1.0f, 0.0f, 1.0f)
                light.intensity = 0.2f * (i + 1);

            }
        } else {
            lights = pLights
        }

    }

    override fun init() {



        hub.add(SceneryElement.RENDERER, renderer!!)
        scene.addChild(cam)

        for (item in model.getList()) {
            scene.addChild(item)
        }

        for (light in lights) {
            scene.addChild(light)
        }

        thread {
            var t = 0
            while (true) {
                Thread.sleep(100)//
                lights.forEachIndexed { i, pointLight ->
                    pointLight.position = GLVector(0.0f, 15.0f * Math.sin(2 * i * Math.PI / 3.0f + t * Math.PI / 50).toFloat(), -15.0f * Math.cos(2 * i * Math.PI / 3.0f + t * Math.PI / 50).toFloat())
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

        inputHandler.addBehaviour("toggle_control_mode", toggleControlMode)
        inputHandler.addKeyBinding("toggle_control_mode", "C")


    }

    override fun main() {

        super.main()

    }


}
