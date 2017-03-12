package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 13/02/15.
 */
public class InviteButton {

    private GameWorld world;
    private Rectangle rectangle;
    private String text;
    private Sprite sprite, bonusSprite;


    private TweenManager manager;
    private Value xValue = new Value();
    private Value yValue = new Value();
    private TweenCallback cbDissapear;

    private boolean appear = false;
    private Color color;
    private Value scaleValue = new Value();
    private Value rotationValue = new Value();

    public boolean isPressed = false;

    public InviteButton(final GameWorld world, String text, float y, Color color) {
        this.text = text;
        this.world = world;
        this.color = color;
        cbDissapear = new TweenCallback() {

            @Override
            public void onEvent(int type, BaseTween<?> source) {
                xValue.setValue(-world.gameWidth);
                appear = false;
            }
        };

        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        rectangle = new Rectangle(world.gameWidth / 2 + 25, y, world.gameWidth / 2 - 75, 200);
        sprite = new Sprite(AssetLoader.square);
        sprite.setPosition(rectangle.x, rectangle.y);
        sprite.setSize(rectangle.width, rectangle.height);
        sprite.setColor(color);

        bonusSprite = new Sprite(AssetLoader.square);
        bonusSprite.setRotation(45);
        bonusSprite.setOriginCenter();
        bonusSprite.setPosition(rectangle.width - 70, rectangle.y + 42);
        bonusSprite.setSize(20, 20);
        bonusSprite.setColor(world.parseColor(Configuration.COLOR_GOLD_SQUARE,1f));

        sprite.setAlpha(0.9f);
        yValue.setValue(rectangle.y);

        xValue.setValue(rectangle.x - world.gameWidth);

        scaleValue.setValue(1f);
        Tween.to(scaleValue, -1, 0.35f).delay(0.05f).target(1.3f).repeatYoyo(10000, 0f).start(
                manager);

        rotationValue.setValue(45);
        Tween.to(rotationValue, -1, 5f).target(360 + 45).repeatYoyo(10000, 0f)
                .ease(TweenEquations.easeInOutSine).start(manager);
        bonusSprite.setOriginCenter();
    }

    public void update(float delta) {
        manager.update(delta);
        rectangle.setPosition(xValue.getValue(), yValue.getValue());

        sprite.setPosition(rectangle.getX(), rectangle.getY());
        bonusSprite.setRotation(rotationValue.getValue());

        bonusSprite.setX(rectangle.getX() + rectangle.width - 55);
        bonusSprite.setScale(scaleValue.getValue());
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontShader) {
        if (appear) {
            if (isPressed) {
                sprite.setAlpha(.5f);
                sprite.draw(batch);
            } else {
                sprite.setAlpha(1f);
                sprite.draw(batch);
            }
            sprite.draw(batch);
            bonusSprite.draw(batch);
            batch.setShader(fontShader);
            AssetLoader.font1
                    .drawWrapped(batch, text, rectangle.x, rectangle.y + rectangle.height - 15,
                            rectangle.width,
                            BitmapFont.HAlignment.CENTER);
            batch.setShader(null);
        }
    }

    public void appear() {
        appear = true;
        Tween.to(xValue, -1, .5f).target(world.gameWidth - 50 - (world.gameWidth / 2 - 75))
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void dissapear() {
        Tween.to(xValue, -1, .5f).target(rectangle.x + world.gameWidth)
                .ease(TweenEquations.easeInOutSine)
                .setCallback(cbDissapear).setCallbackTriggers(
                TweenCallback.COMPLETE).delay(0f).start(manager);
    }

    public void appearDissapear(float time) {
        appear = true;
        Timeline.createSequence()
                .push(Tween.to(xValue, -1, .5f).target(0).ease(TweenEquations.easeInOutSine))
                .pushPause(time)
                .push(Tween.to(xValue, -1, .5f).target(-rectangle.width)
                        .ease(TweenEquations.easeInOutSine)
                        .setCallback(cbDissapear).setCallbackTriggers(
                                TweenCallback.COMPLETE))
                .start(manager);
    }

    public void setText(String text) {
        this.text = text;
    }

    public Vector2 getPosition() {
        return new Vector2(rectangle.x, rectangle.y);
    }

    public boolean isTouchDown(int screenX, int screenY) {

        if (rectangle.contains(screenX, screenY)) {
            isPressed = true;
            return true;
        }
        return false;
    }

    public boolean isTouchUp(int screenX, int screenY) {
        // It only counts as a touchUp if the button is in a pressed state.
        if (rectangle.contains(screenX, screenY) && isPressed) {
            isPressed = false;
            return true;
        }
        // Whenever a finger is released, we will cancel any presses.
        isPressed = false;
        return false;
    }
}
