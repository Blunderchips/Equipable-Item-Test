package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_BUFFER_BIT;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * Base Engine. Created on 05 Jan 2018.
 *
 * @author siD
 */
public final class BaseEngine extends ApplicationAdapter {

    private Camera camera;
    private CameraInputController controller;
    private ModelBatch batch;
    private AssetManager mngr;
    private ModelInstance character;
    private ModelInstance sword;
    private AnimationController animationController;

    private boolean flag;

    public BaseEngine() {
        this.flag = true;
    }

    @Override
    public void create() {
        Gdx.gl.glClearColor(1, 1, 1, 1);

        final float width = Gdx.graphics.getWidth();
        final float height = Gdx.graphics.getHeight();
        if (width > height) {
            this.camera = new PerspectiveCamera(67f, 3f * width / height, 3f);
        } else {
            this.camera = new PerspectiveCamera(67f, 3f, 3f * height / width);
        }

        this.camera.position.set(10f, 10f, 10f);
        this.camera.lookAt(0, 0, 0);
        this.camera.update(true);
        this.camera.near = 0.1f;
        this.camera.far = 500f;

        this.controller = new CameraInputController(camera);
        Gdx.input.setInputProcessor(controller);

        this.batch = new ModelBatch();

        Gdx.app.log("Loading", "Start");
        this.mngr = new AssetManager();
        {
            this.mngr.load("character.g3dj", Model.class);
            this.mngr.load("sword.obj", Model.class);
        }
        this.mngr.finishLoading();
        Gdx.app.log("Loading", "Done");

        this.character = new ModelInstance(mngr.get("character.g3dj", Model.class));
        this.animationController = new AnimationController(character);
        this.sword = new ModelInstance(mngr.get("sword.obj", Model.class));

        displayNodes(character.nodes);
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(),
                Gdx.graphics.getBackBufferHeight());
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //<editor-fold defaultstate="uncollapsed" desc="Update">
        this.controller.update();
        this.animationController.update(Gdx.graphics.getDeltaTime());

        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            this.animationController.action(flag ? "atk 1" : "atk 2", 1, 1, null, 0.2f);
            this.flag = !flag;
        } else if (!animationController.inAction) {
            this.animationController.animate("idle", 1, 1, null, 0.2f);
        }

        Matrix4 matA = new Matrix4().set(character.transform.getRotation(new Quaternion()))
                .setTranslation(character.transform.getTranslation(new Vector3()));

        Matrix4 matB = new Matrix4().setTranslation(character.getNode(
                "mixamorig:RightHand").globalTransform.getTranslation(new Vector3()));

        Matrix4 mat = new Matrix4();
        mat.set(matA).mul(matB);

        sword.transform.set(mat);
        //</editor-fold>

        //<editor-fold defaultstate="uncollapsed" desc="Render">
        this.batch.begin(camera);
        {
            this.batch.render(character);
            this.batch.render(sword);
        }
        this.batch.end();
        //</editor-fold>
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.mngr.dispose();
    }

    private void displayNodes(Iterable<Node> nodes) {
        for (Node node : nodes) {
            Gdx.app.log("Node", node.id);
            displayNodes(node.getChildren());
        }
    }
}
