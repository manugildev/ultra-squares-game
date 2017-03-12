package gameworld;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import configuration.Configuration;
import gameobjects.Enemy;
import helpers.AssetLoader;

public class GameRenderer {

    private GameWorld world;
    private final ShapeRenderer shapeRenderer;
    private ShaderProgram fontShader;
    private Sprite backSprite;
    //GAME OBJECTS
    //private Hero hero;
    private Array<Enemy> enemies;

    private GameCam camera;
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;

    BitmapFont font = new BitmapFont();
    private float angle;

    public GameRenderer(GameWorld world, int gameWidth, int gameHeight) {
        this.world = world;
        sprite = new Sprite(AssetLoader.square);
        sprite.setPosition(0, 0);
        sprite.setSize(world.worldWidth, world.worldHeight);
        camera = world.getCamera();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        initObjects();
        initFont();
    }

    private void initObjects() {
        //hero = world.getHero();
        enemies = world.getEnemies();
    }

    private void initFont() {
        fontShader = new ShaderProgram(Gdx.files.internal("font.vert"),
                Gdx.files.internal("font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader",
                    "compilation failed:\n" + fontShader.getLog());
        }
    }

    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(1,1,1, 1);
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        batch.begin();
        sprite.setColor(world.colorManager.getColor().r, world.colorManager.getColor().g,
                world.colorManager.getColor().b, 1f);
        sprite.draw(batch);
        camera.render(batch, shapeRenderer);
        for (int i = 0; i < world.getStars().size; i++) {
            world.getStars().get(i).render(batch, shapeRenderer);
        }


        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).render(batch, shapeRenderer);
        }
        world.getBonusParticle().render(batch, shapeRenderer);
        world.getCenter().render(batch, shapeRenderer);
        world.getScoreBanner().render(batch, shapeRenderer, fontShader);


        if (world.isMenu()) {
            world.getMenu().render(batch, shapeRenderer, fontShader);
        }
        world.getCenter().render(batch, shapeRenderer);
        world.getBanner().render(batch, shapeRenderer, fontShader);
        world.getBonusBanner().render(batch, shapeRenderer, fontShader);
        world.getPauseButton().draw(batch);
        if (Configuration.DEBUG) {
            font.setScale(4);
            font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(),
                    camera.getCamera().position.x - world.gameWidth / 2 + 100,
                    camera.getCamera().position.y - world.gameHeight / 2 + 100);
        }
        batch.end();

        //REMOVE THIS OUTSIDE DEBUGGING
        if (Configuration.DEBUG) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GREEN);
            for (int i = 0; i < world.getPoints().size; i++) {
                shapeRenderer.circle(world.getPoints().get(i).x, world.getPoints().get(i).y, 10);
            }
            shapeRenderer.end();
        }

    }

    private boolean cameraInsideWorld() {
        Gdx.app.log("CameraPos", camera.getCamera().position.toString());
        return false;
    }

}
