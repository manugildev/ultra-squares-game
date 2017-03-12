package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 11/02/15.
 */
public class Center {
    private GameWorld world;
    private Sprite sprite, auraSprite, bottomSprite, lineSprite, whiteSprite;
    private Vector2 position, velocity, acceleration, point;
    private int height, width;

    //VARIABLES
    private Rectangle rectangle, bottomRect;
    //TWEEN
    private TweenManager manager;
    private float angle;
    private Value angleValue = new Value();
    private Value scaleValue = new Value();
    private Value alphaValue = new Value();

    private Value rotationValue = new Value();
    private Value whiteAlpha = new Value();

    private Value yValueB = new Value();
    private Value yValueO = new Value();

    private String color = "#27ae60";
    private int type;
    private Value xValueO = new Value();
    private TweenCallback callbackAd;


    public Center(final GameWorld world, float x, float y, float width, float height) {

        this.world = world;
        this.position = new Vector2(x - (width / 2), y - (height / 2));

        //Sprite
        sprite = new Sprite(AssetLoader.square);
        sprite.setOriginCenter();
        sprite.setPosition(position.x, position.y);
        sprite.setSize(width, height);
        sprite.setColor(world.parseColor("#27ae60", 1f));

        auraSprite = new Sprite(AssetLoader.square);
        auraSprite.setOriginCenter();
        auraSprite.setPosition(position.x, position.y);
        auraSprite.setSize(width, height);
        auraSprite.setColor(world.parseColor("#27ae60", 1f));


        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        rectangle = new Rectangle(x - width / 8, y - height / 8, width / 4, height / 4);

        velocity = new Vector2();
        acceleration = new Vector2();

        angleValue.setValue(45);
        Tween.to(angleValue, -1, 5).target(765).repeatYoyo(10000, 0f)
                .ease(TweenEquations.easeInOutSine).start(manager);

        scaleValue.setValue(0);
        alphaValue.setValue(1);

        rotationValue.setValue(0);
        Tween.to(scaleValue, -1, .5f).target(1.8f).repeat(10000, 0.1f)
                .ease(TweenEquations.easeInOutSine).start(manager);
        Tween.to(alphaValue, -1, .6f).target(0f).repeat(10000, 0f)
                .ease(TweenEquations.easeInOutSine).start(manager);
        Tween.to(rotationValue, -1, 5f).target(0).repeatYoyo(10000, 0f)
                .ease(TweenEquations.easeInOutSine).start(manager);

        bottomRect = new Rectangle(-50, -50, world.gameWidth + 100,
                400);

        bottomSprite = new Sprite(AssetLoader.square);
        bottomSprite.setPosition(bottomRect.x, bottomRect.y);
        bottomSprite.setSize(bottomRect.width, bottomRect.height + 10);
        bottomSprite.setColor(world.parseColor("#27ae60", 0.6f));

        whiteSprite = new Sprite(AssetLoader.square);
        whiteSprite.setPosition(bottomRect.x, bottomRect.y);
        whiteSprite.setSize(bottomRect.width, bottomRect.height + 20);
        whiteSprite.setColor(world.parseColor("FFFFFF", 0f));

        lineSprite = new Sprite(AssetLoader.square);
        lineSprite.setPosition(bottomRect.x, bottomRect.height - 40);
        lineSprite.setSize(bottomRect.width, 10);
        lineSprite.setColor(world.parseColor("#27ae60", 0.8f));

        whiteAlpha.setValue(0);
        type = 1;
        yValueO.setValue(-300);
        yValueB.setValue(-450);

        callbackAd = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.actionResolver.viewAd(true);
            }
        };
    }

    public void update(float delta) {
        manager.update(delta);
        sprite.setY(yValueO.getValue());

        sprite.setOriginCenter();
        sprite.setRotation(angleValue.getValue());

        bottomRect.setY(yValueB.getValue());
        auraSprite.setScale(scaleValue.getValue());
        auraSprite.setY(yValueO.getValue());
        auraSprite.setOriginCenter();
        auraSprite.setRotation(angleValue.getValue());

        bottomSprite.setY(bottomRect.y);
        bottomSprite.setOriginCenter();
        bottomSprite.setRotation(rotationValue.getValue());

        whiteSprite.setY(bottomRect.y - 10);
        whiteSprite.setOriginCenter();
        whiteSprite.setRotation(rotationValue.getValue());

        lineSprite.setY(whiteSprite.getY() + whiteSprite.getHeight() - 10);
        lineSprite.setOriginCenter();
        lineSprite.setRotation(rotationValue.getValue());
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {

        bottomSprite.draw(batch);
        lineSprite.draw(batch);
        auraSprite.setAlpha(alphaValue.getValue());
        auraSprite.draw(batch);
        sprite.draw(batch);
        whiteSprite.setAlpha(whiteAlpha.getValue());
        whiteSprite.draw(batch);
        if (Configuration.DEBUG) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            shapeRenderer.rect(bottomRect.x, bottomRect.y, bottomRect.width, bottomRect.height);
            shapeRenderer.end();
            batch.begin();
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getPosition() {
        Vector2 pos = new Vector2(position.x + sprite.getWidth() / 2,
                position.y + sprite.getHeight() / 2);
        return pos;
    }

    public Rectangle getBottomRect() {
        return bottomRect;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void changeColor() {
        if (color.equals("#27ae60")) {
            color = "#c0392b";
            auraSprite.setColor(world.parseColor("#c0392b", 1f));
            sprite.setColor(world.parseColor("#c0392b", 1f));
            lineSprite.setColor(world.parseColor("#c0392b", 0.7f));
            bottomSprite.setColor(world.parseColor("#c0392b", 0.4f));
        } else {
            color = "#27ae60";
            auraSprite.setColor(world.parseColor("#27ae60", 1f));
            sprite.setColor(world.parseColor("#27ae60", 1f));
            lineSprite.setColor(world.parseColor("#27ae60", 0.7f));
            bottomSprite.setColor(world.parseColor("#27ae60", 0.4f));
        }
    }

    public void clickDown() {
        type = 0;
        color = "#c0392b";
        auraSprite.setColor(world.parseColor("FFFFFF", 1f));
        sprite.setColor(world.parseColor(Configuration.COLOR_CENTER_SQUARE_1, 1f));
        lineSprite.setColor(world.parseColor(Configuration.COLOR_SQUARE_1, 1f));
        bottomSprite.setColor(world.parseColor(Configuration.COLOR_SQUARE_1, 0.7f));

    }

    public void clickUp() {
        type = 1;
        color = "#27ae60";
        auraSprite.setColor(world.parseColor("FFFFFF", 1f));
        sprite.setColor(world.parseColor(Configuration.COLOR_CENTER_SQUARE_2, 1f));
        lineSprite.setColor(world.parseColor(Configuration.COLOR_SQUARE_2, 1f));
        bottomSprite.setColor(world.parseColor(Configuration.COLOR_SQUARE_2, 0.7f));
    }

    public int getType() {
        return type;
    }

    public void clicked() {
        whiteAlpha.setValue(0.5f);
        Tween.to(whiteAlpha, -1, .2f).target(0f).repeat(0, 0f)
                .ease(TweenEquations.easeInOutSine).start(manager);

    }

    public void start() {
        world.actionResolver.viewAd(false);
        yValueB.setValue(-400);
        Tween.to(yValueB, -1, .5f).target(-50).repeat(0, 0f)
                .ease(TweenEquations.easeInOutSine).start(manager);

        yValueO.setValue(-300);
        Tween.to(yValueO, -1, .5f).target(this.position.y).repeat(0, 0f).delay(.3f)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void finish() {
        Tween.to(yValueB, -1, .5f).target(-499).repeat(0, 0f).delay(.3f)
                .ease(TweenEquations.easeInOutSine).start(manager);

        Tween.to(yValueO, -1, .5f).target(-300).repeat(0, 0f).delay(0f)
                .ease(TweenEquations.easeInOutSine).setCallback(callbackAd).setCallbackTriggers(
                TweenCallback.COMPLETE).start(manager);
    }
}
