package ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import gameworld.GameWorld;

/**
 * Created by ManuGil on 15/01/15.
 */
public class Button {

    private final TweenCallback cbDissapear;
    private float x, y, width, height;

    private TextureRegion buttonUp;
    private TextureRegion buttonDown;

    private Rectangle bounds;
    private Sprite sprite, iconSprite;

    private GameWorld world;
    public boolean isPressed = false;
    private TweenManager manager;
    private Value xValue = new Value();

    private float retard1;

    public Button(final GameWorld world, final float x, float y, float width, float height,
                        TextureRegion buttonUp, TextureRegion buttonDown, String color,
                        float retard1) {

        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonUp = buttonUp;
        this.buttonDown = buttonDown;
        this.retard1 = retard1;

        xValue.setValue(x - world.gameWidth);
        bounds = new Rectangle(x - world.gameWidth, y, width, height);

        iconSprite = new Sprite(buttonDown);
        iconSprite.setBounds(bounds.x + bounds.width / 2,
                bounds.y,
                bounds.width / 1f, bounds.height / 1f);

        sprite = new Sprite(buttonUp);
        sprite.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        sprite.setColor(world.parseColor(color, 1f));

        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        cbDissapear = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                xValue.setValue(x - world.gameWidth);
            }
        };

    }

    public void start() {
        Tween.to(xValue, -1, .5f).target(xValue.getValue() + world.gameWidth)
                .ease(TweenEquations.easeInOutSine).delay(retard1).start(manager);
    }

    public void finish() {
        xValue.setValue(x);
        Tween.to(xValue, -1, .5f).target(xValue.getValue() - world.gameWidth).ease(
                TweenEquations.easeInOutSine)
                .setCallback(cbDissapear).delay(retard1).setCallbackTriggers(
                TweenCallback.COMPLETE).start(manager);
    }

    public boolean isClicked(int screenX, int screenY) {
        return bounds.contains(screenX, screenY);
    }

    public void draw(SpriteBatch batcher) {
        if (isPressed) {
            sprite.setAlpha(.5f);
            sprite.draw(batcher);
            iconSprite.draw(batcher);
        } else {
            sprite.setAlpha(1f);
            sprite.draw(batcher);
            iconSprite.draw(batcher);
        }
    }

    public boolean isTouchDown(int screenX, int screenY) {

        if (bounds.contains(screenX, screenY)) {
            isPressed = true;
            return true;
        }
        return false;
    }

    public boolean isTouchUp(int screenX, int screenY) {
        // It only counts as a touchUp if the button is in a pressed state.
        if (bounds.contains(screenX, screenY) && isPressed) {
            isPressed = false;
            return true;
        }
        // Whenever a finger is released, we will cancel any presses.
        isPressed = false;
        return false;
    }

    public void update(float delta) {
        manager.update(delta);
        bounds.setX(xValue.getValue());
        sprite.setX(xValue.getValue());
        iconSprite.setX(xValue.getValue());
    }
}