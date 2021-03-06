package sceneryfx;

import cleargl.GLVector;
import graphics.scenery.scenery.*;
import graphics.scenery.scenery.backends.Renderer;
import graphics.scenery.scenery.controls.InputHandler;
import graphics.scenery.scenery.controls.behaviours.ArcballCameraControl;
import graphics.scenery.scenery.controls.behaviours.FPSCameraControl;
import org.scijava.ui.behaviour.ClickBehaviour;

import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dibrov on 08/01/17.
 */
public class RenderModelJava extends SceneryDefaultApplication {

    private Node cam;
    private LinkedList<Node> lights;
    private AcquisitionGUIModelJava mModel;
    private Camera mCamera;
    private Renderer mRenderer;
    private LinkedList<PointLight> mLights;


    public RenderModelJava(AcquisitionGUIModelJava pModel, Renderer pRenderer, Camera pCamera, LinkedList<PointLight>
            pLights) throws Exception {
        super("RenderModelJava", 512, 512);
        this.mCamera = pCamera;
        this.mLights = pLights;
        this.mRenderer = pRenderer;
        this.mModel = pModel;

        if (mModel.getNodeList().isEmpty()) {
            throw new Exception("Refuse to render an empty model!");
        } else {
            mModel = pModel;
        }

        Listener updateListener = new Listener() {
            @Override
            public void fire() {
                updateModelChildren(mModel.getNodeList());
            }
        };

        mModel.registerListener(updateListener);


        if (pRenderer == null) {
            mRenderer = Renderer.Factory.createRenderer(this.getApplicationName(), this.getScene(), 512, 512);
        } else {
            mRenderer = pRenderer;
        }

        if (mCamera == null) {
            mCamera = new DetachedHeadCamera();
            mCamera.setPosition(new GLVector(0.0f, 0.0f, 15.0f));
            mCamera.perspectiveCamera(50.0f, (float) this.getWindowWidth(), (float) this.getWindowHeight(), 0.1f,
                    1000.0f);
            mCamera.setActive(true);
        } else {
            mCamera = pCamera;

        }

        if (pLights == null) {
            mLights = new LinkedList<PointLight>();

            for (int i = 0; i < 2; i++) {
                PointLight pl = new PointLight();
                pl.setPosition(new GLVector(2.0f * i, 2.0f * i, 2.0f * i));
                pl.setEmissionColor(new GLVector(1.0f, 0.0f, 1.0f));
                pl.setIntensity(0.2f * (i + 1));
                mLights.add(new PointLight());
            }

        } else {
            mLights = pLights;
        }


    }

    private void updateModelChildren(CopyOnWriteArrayList<Node> pUpdatedModel) {

        this.getScene().setChildren(pUpdatedModel);
    }

    @Override
    public void init() {
        super.init();
        this.getHub().add(SceneryElement.RENDERER, mRenderer);
        this.getScene().addChild(mCamera);

        for (Node item : mModel.getNodeList()) {
            this.getScene().addChild(item);
        }

        for (PointLight light : mLights) {
            this.getScene().addChild(light);
        }


        Thread th = new Thread() {
            @Override
            public void run() {
                super.run();
                int t = 0;
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int counter = 0;
                    for (PointLight pl : mLights) {
                        pl.setPosition(new GLVector(0.0f, 15.0f * (float) Math.sin(2 * counter* Math.PI / 3.0f + t *
                                Math
                                .PI / 50), -15.0f * (float)Math.cos(2 * counter * Math.PI / 3.0f + t * Math.PI / 50)));

                        counter++;
                    }

                    t++;
                }
            }
        };
        th.run();
    }


    @Override
    public void inputSetup() {
        super.inputSetup();

        GLVector target = new GLVector(0.0f, 0.0f, 0.0f);
        InputHandler inputHandler = (InputHandler) (this.getHub().get(SceneryElement.INPUT));
        ArcballCameraControl targetArcball = new ArcballCameraControl("mouse_control", this.getScene().findObserver
                (),
                this.getWindowWidth(), this.getWindowHeight(),
                target);
        FPSCameraControl fpsControl = new FPSCameraControl("mouse_control", this.getScene().findObserver(), this
                        .getWindowWidth(),
                this.getWindowHeight());

        ClickBehaviour toggleControlMode = new ClickBehaviour() {
            String currentMode = "fps";

            @Override
            public void click(int x, int y) {
                if (currentMode.startsWith("fps")) {
                    targetArcball.setTarget(target);

                    inputHandler.addBehaviour("mouse_control", targetArcball);
                    inputHandler.addBehaviour("scroll_arcball", targetArcball);
                    inputHandler.addKeyBinding("scroll_arcball", "scroll");

                    currentMode = "arcball";
                } else {
                    inputHandler.addBehaviour("mouse_control", fpsControl);
                    inputHandler.removeBehaviour("scroll_arcball");

                    currentMode = "fps";
                }

                System.out.println("Switched to $currentMode control");
            }


        };

        inputHandler.addBehaviour("toggle_control_mode", toggleControlMode);
        inputHandler.addKeyBinding("toggle_control_mode", "C");


    }

    public void main() {
        System.out.println("calling main of RenderModel");
        super.main();

    }


}

