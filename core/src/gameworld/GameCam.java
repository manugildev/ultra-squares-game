package gameworld;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

/**
 * Created by ManuGil on 11/02/15.
 */
public class GameCam {

    private OrthographicCamera camera;
    private GameWorld world;

    public GameCam(GameWorld world, float x, float y, float width, float hegiht) {
        camera = new OrthographicCamera(world.gameWidth, world.gameHeight);
        camera.position.set(world.worldWidth / 2, world.worldHeight / 2, 0);
        camera.update();
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }


}
