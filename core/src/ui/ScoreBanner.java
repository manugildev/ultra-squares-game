package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 13/02/15.
 */
public class ScoreBanner {

    private GameWorld world;
    private Rectangle rectangle;
    private String text;
    private Sprite sprite;


    private TweenManager manager;
    private Value xValue = new Value();
    private Value yValue = new Value();
    private TweenCallback cbDissapear;

    private boolean appear = false;
    private Color color;

    public ScoreBanner(final GameWorld world, String text, float y, Color color) {
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
        rectangle = new Rectangle(-200, y, 200, 80);
        sprite = new Sprite(AssetLoader.square);
        sprite.setPosition(rectangle.x, rectangle.y);
        sprite.setSize(rectangle.width, rectangle.height);
        sprite.setColor(color);
        sprite.setAlpha(1f);
        yValue.setValue(rectangle.y);
        xValue.setValue(rectangle.x);


    }

    public void update(float delta) {
        manager.update(delta);
        rectangle.setPosition(xValue.getValue(), yValue.getValue());
        sprite.setPosition(rectangle.getX(), rectangle.getY());
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontShader) {
        if (appear) {
            sprite.draw(batch);
            batch.setShader(fontShader);
            AssetLoader.font2
                    .drawWrapped(batch, text, rectangle.x, rectangle.y + rectangle.height-5,
                            rectangle.width,
                            BitmapFont.HAlignment.CENTER);
            batch.setShader(null);
        }
    }

    public void appear() {
        appear = true;
        Tween.to(xValue, -1, .5f).target(0).ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void dissapear() {
        Tween.to(xValue, -1, .5f).target(-rectangle.width).ease(TweenEquations.easeInOutSine)
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
}
