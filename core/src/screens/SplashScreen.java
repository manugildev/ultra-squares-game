package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Tweens.SpriteAccessor;
import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import helpers.AssetLoader;
import paddle.ActionResolver;
import paddle.PaddleGame;

public class SplashScreen implements Screen {

    private TweenManager manager;
    private SpriteBatch batcher;
    private Sprite sprite;
    private PaddleGame game;

    private Value r1 = new Value();
    float height;
    private ActionResolver actionResolver;

    public SplashScreen(PaddleGame game, ActionResolver actionResolver) {
        this.game = game;
        this.actionResolver = actionResolver;
    }

    @Override
    public void show() {
        sprite = new Sprite(AssetLoader.logo);
        sprite.setColor(1, 1, 1,0);

        float width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        float desiredWidth = width * .39f;
        float scale = desiredWidth / sprite.getWidth();

        sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
        sprite.setPosition((width / 2) - (sprite.getWidth() / 2), (height / 2)
                - (sprite.getHeight() / 2));
        sprite.setAlpha(0);
        r1.setValue(height);
        setupTween();
        batcher = new SpriteBatch();
    }

    private void setupTween() {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();

        TweenCallback cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new GameScreen(game, actionResolver));
            }
        };

        Tween.to(sprite, SpriteAccessor.ALPHA, 1.2f).target(1)
                .ease(TweenEquations.easeInOutQuad).repeatYoyo(1, .4f)
                .setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);
        Tween.to(r1, -1, .5f).target((height / 2) - (sprite.getHeight() / 2))
                .ease(TweenEquations.easeInOutQuad).repeatYoyo(0, .4f)
                .delay(.8f).start(manager);
        // Tween.to(r1, -1, 1f).target(0 - sprite.getHeight())
        // .ease(TweenEquations.easeInOutQuad).delay(1.6f).start(manager);
    }

    @Override
    public void render(float delta) {
        manager.update(delta);
        sprite.setY(r1.getValue());
        Gdx.gl.glClearColor(0.945f,0.945f,0.945f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        sprite.draw(batcher);
        batcher.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}