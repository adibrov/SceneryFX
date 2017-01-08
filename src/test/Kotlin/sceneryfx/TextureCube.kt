/**
 * Created by dibrov on 27/12/16.
 */


package sceneryfx


import org.junit.Test;
import cleargl.GLVector
import graphics.scenery.scenery.*
import graphics.scenery.scenery.backends.Renderer
import graphics.scenery.scenery.repl.REPL
import kotlin.concurrent.thread

/**
 * Created by dibrov on 13/12/16.
 */



class TexturedCubeExampleTest : SceneryDefaultApplication("TexturedCubeExample") {
    override fun init() {
        renderer = Renderer.createRenderer(applicationName, scene, 512, 512)
        hub.add(SceneryElement.RENDERER, renderer!!)

        var boxmaterial = Material()
        with(boxmaterial) {
            ambient = GLVector(1.0f, 0.0f, 0.0f)
            diffuse = GLVector(0.0f, 1.0f, 0.0f)
            specular = GLVector(1.0f, 1.0f, 1.0f)
//            textures.put("diffuse", TexturedCubeExample::class.java.getResource("textures/helix.png").file)
        }

        var box = Box(GLVector(1.0f, 1.0f, 1.0f))

        with(box) {
            box.material = boxmaterial
            scene.addChild(this)
        }

        var lights = (0..2).map {
            PointLight()
        }

        lights.mapIndexed { i, light ->
            light.position = GLVector(2.0f * i, 2.0f * i, 2.0f * i)
            light.emissionColor = GLVector(1.0f, 0.0f, 1.0f)
            light.intensity = 0.2f*(i+1);
            scene.addChild(light)
        }

        val cam: Camera = DetachedHeadCamera()
        with(cam) {
            position = GLVector(0.0f, 0.0f, 5.0f)
            perspectiveCamera(50.0f, 512.0f, 512.0f)
            active = true

            scene.addChild(this)
        }

        thread {
            while (true) {
                box.rotation.rotateByAngleY(0.01f)
                box.needsUpdate = true

                Thread.sleep(20)
            }
        }

        repl = REPL(scene, renderer!!)
        repl?.start()
        repl?.showConsoleWindow()
    }

    @Test override fun main() {
        super.main()
    }
}

