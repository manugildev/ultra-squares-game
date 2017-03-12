package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import Tweens.Value;
import Tweens.ValueAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import gameworld.GameState;
import gameworld.GameWorld;
import helpers.AssetLoader;
import sun.security.krb5.Config;
import ui.Banner;
import ui.BannerGameOver;
import ui.InviteButton;
import ui.SaveMeButton;
import ui.SimpleButton;

/**
 * Created by ManuGil on 13/02/15.
 */
public class Menu {

    private GameWorld world;
    private Sprite backSprite;
    private TweenManager manager;
    private Banner banner;

    private Value alphaValue = new Value();

    private SimpleButton playButton, shareButton, learderboardButton, achievementsButton, playButton1, restartButton, shareButton1;
    private Array<SimpleButton> menuButtons = new Array<SimpleButton>();
    private CenterMenu center;
    private BannerGameOver scoreBanner, highScoreBanner, gamesPlayedBanner;
    private SaveMeButton saveMeButton;
    private InviteButton inviteButton;

    public Menu(GameWorld world) {

        this.world = world;
        backSprite = new Sprite(AssetLoader.square);
        backSprite.setPosition(0, 0);
        backSprite.setSize(world.gameWidth, world.gameHeight);
        backSprite.setColor(Color.WHITE);

        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        alphaValue.setValue(0.0f);
        banner = new Banner(world, Configuration.GAME_NAME, world.gameHeight / 2 + 337 - 25,
                world.parseColor(Configuration.COLOR_GAME_NAME_BANNER, 1f));

        playButton = new SimpleButton(world, world.gameWidth / 2 - 375, world.gameHeight / 2 - 450,
                150, 150,
                AssetLoader.square, AssetLoader.playButtonUp, Configuration.COLOR_PLAY_BUTTON,
                .15f);

        achievementsButton = new SimpleButton(world, world.gameWidth / 2 + 225,
                world.gameHeight / 2 - 450, 150, 150,
                AssetLoader.square, AssetLoader.achieveButtonUp,
                Configuration.COLOR_ACHIEVEMENT_BUTTON, 0f);

        shareButton = new SimpleButton(world, world.gameWidth / 2 + 25, world.gameHeight / 2 - 450,
                150, 150,
                AssetLoader.square, AssetLoader.shareButtonUp, Configuration.COLOR_SHARE_BUTTON,
                .05f);

        learderboardButton = new SimpleButton(world, world.gameWidth / 2 - 175,
                world.gameHeight / 2 - 450, 150, 150,
                AssetLoader.square, AssetLoader.rankButtonUp,
                Configuration.COLOR_LEADERBOARD_BUTTON, .1f);

        //BUTTONS ON GAMEOVER
        playButton1 = new SimpleButton(world, world.gameWidth / 2 - 75 - 200,
                world.gameHeight / 2 - 95 - (85 / 2) + 150 - 200 - 250 - 25, 150, 150,
                AssetLoader.square, AssetLoader.playButtonUp, Configuration.COLOR_PLAY_BUTTON, .1f);

        restartButton = new SimpleButton(world, world.gameWidth / 2 - 75,
                world.gameHeight / 2 - 95 - (85 / 2) + 150 - 200 - 250 - 25,
                150, 150,
                AssetLoader.square, AssetLoader.rateButtonUp,
                Configuration.COLOR_RETURN_HOME_BUTTON, .05f);

        shareButton1 = new SimpleButton(world, world.gameWidth / 2 + 50 + 75,
                world.gameHeight / 2 - 95 - (85 / 2) + 150 - 200 - 250 - 25,
                150, 150,
                AssetLoader.square, AssetLoader.shareButtonUp, Configuration.COLOR_SHARE_BUTTON,
                .0f);

        menuButtons.add(playButton);
        menuButtons.add(shareButton);
        menuButtons.add(learderboardButton);
        menuButtons.add(achievementsButton);
        menuButtons.add(playButton1);
        menuButtons.add(restartButton);
        menuButtons.add(shareButton1);

        center = new CenterMenu(world, world.gameWidth / 2, world.gameHeight / 2, 150, 150);

        scoreBanner = new BannerGameOver(world, world.getScore() + "",
                world.gameHeight / 2 + 95 - (85 / 2) + 150 - 25,
                world.parseColor(
                        Configuration.COLOR_SCORE_BANNER, 1f));
        highScoreBanner = new BannerGameOver(world, AssetLoader.getHighScore() + "",
                world.gameHeight / 2 - (85 / 2) + 150 - 25,
                world.parseColor(Configuration.COLOR_HIGH_SCORE_BANNER, 1f));
        gamesPlayedBanner = new BannerGameOver(world, AssetLoader.getGamesPlayed() + "",
                world.gameHeight / 2 - 95 - (85 / 2) + 150 - 25,
                world.parseColor(Configuration.COLOR_GAMES_PLAYED_BANNER, 1f));

        saveMeButton = new SaveMeButton(world, "Save me!\n10",
                world.gameHeight / 2 - 95 - (85 / 2) + 150 - 200 - 50 - 25,
                world.parseColor(Configuration.COLOR_SAVE_ME_BUTTON, 1f));

        inviteButton = new InviteButton(world, "Get Gold\nSquares",
                world.gameHeight / 2 - 95 - (85 / 2) + 150 - 200 - 50 - 25,
                world.parseColor(Configuration.COLOR_IN_APP_PURCHASES_BUTTON, 1f));

    }

    public void update(float delta) {
        manager.update(delta);
        backSprite.setColor(world.colorManager.getColor());
        backSprite.setAlpha(alphaValue.getValue());
        banner.update(delta);
        for (int i = 0; i < menuButtons.size; i++) {
            menuButtons.get(i).update(delta);
        }
        center.update(delta);
        scoreBanner.update(delta);
        highScoreBanner.update(delta);
        gamesPlayedBanner.update(delta);
        saveMeButton.update(delta);
        inviteButton.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontshader) {
        backSprite.draw(batch);
        banner.render(batch, shapeRenderer, fontshader);
        playButton.draw(batch);
        achievementsButton.draw(batch);
        learderboardButton.draw(batch);
        shareButton.draw(batch);
        playButton1.draw(batch);
        restartButton.draw(batch);
        shareButton1.draw(batch);
        center.render(batch, shapeRenderer);
        scoreBanner.render(batch, shapeRenderer, fontshader);
        highScoreBanner.render(batch, shapeRenderer, fontshader);
        gamesPlayedBanner.render(batch, shapeRenderer, fontshader);
        saveMeButton.render(batch, shapeRenderer, fontshader);
        inviteButton.render(batch, shapeRenderer, fontshader);
    }

    public void finish() {
        TweenCallback cbFinish = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.gameState = GameState.RUNNING;
            }
        };
        Tween.to(alphaValue, -1, .9f).target(0).ease(TweenEquations.easeInOutSine)
                .setCallback(cbFinish).delay(0f).setCallbackTriggers(
                TweenCallback.COMPLETE).start(manager);
        banner.dissapear();
        world.renewMap(0);

        for (int i = 0; i < menuButtons.size - 3; i++) {
            menuButtons.get(i).finish();
        }
        center.finish();
    }

    public void start() {
        alphaValue.setValue(0);
        banner.appear();
        TweenCallback cbstart = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.gameState = GameState.MENU;

            }
        };
        Tween.to(alphaValue, -1, .9f).target(0.5f).ease(TweenEquations.easeInOutSine)
                .setCallback(cbstart).setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);
        world.gameState = GameState.MENU;

        for (int i = 0; i < menuButtons.size - 3; i++) {
            menuButtons.get(i).start();
        }
        center.start();
    }

    public void startGameOver() {
        alphaValue.setValue(0);
        banner.appear();
        TweenCallback cbstart = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.gameState = GameState.MENU;
                if (Math.random() < Configuration.AD_FREQUENCY) {
                    world.actionResolver.showOrLoadInterstital();
                }
            }
        };
        Tween.to(alphaValue, -1, .8f).target(0.5f).ease(TweenEquations.easeInOutSine)
                .setCallback(cbstart).setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);

        world.gameState = GameState.MENU;
        playButton1.start();
        restartButton.start();
        shareButton1.start();
        scoreBanner.setText("Score: " + world.getScore() + "");
        highScoreBanner.setText("High Score: " + AssetLoader.getHighScore() + "");
        gamesPlayedBanner.setText("Games Played: " + AssetLoader.getGamesPlayed() + "");
        scoreBanner.appear();
        highScoreBanner.appear();
        gamesPlayedBanner.appear();
        saveMeButton.appear();
        inviteButton.appear();

    }

    public void finishGameOver(int i) {
        TweenCallback cbFinish = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.gameState = GameState.RUNNING;
            }
        };
        Tween.to(alphaValue, -1, .9f).target(0).ease(TweenEquations.easeInOutSine)
                .setCallback(cbFinish).delay(0f).setCallbackTriggers(
                TweenCallback.COMPLETE).start(manager);
        banner.dissapear();
        world.renewMap(i);
        playButton1.finish();
        restartButton.finish();
        shareButton1.finish();
        scoreBanner.dissapear();
        highScoreBanner.dissapear();
        gamesPlayedBanner.dissapear();
        saveMeButton.dissapear();
        inviteButton.dissapear();
    }

    public void finishGameOverStartMenu() {
        playButton1.finish();
        restartButton.finish();
        shareButton1.finish();
        for (int i = 0; i < menuButtons.size - 3; i++) {
            menuButtons.get(i).start();
        }
        center.start();
        scoreBanner.dissapear();
        gamesPlayedBanner.dissapear();
        highScoreBanner.dissapear();
        saveMeButton.dissapear();
        inviteButton.dissapear();
    }


    public Array<SimpleButton> getMenuButtons() {
        return menuButtons;
    }


    /**
     * Created by ManuGil on 15/02/15.
     */
    public static class CenterMenu {
        private GameWorld world;
        private Sprite sprite, auraSprite;
        private Vector2 position;

        //TWEEN
        private TweenManager manager;
        private float angle;
        private Value angleValue = new Value();
        private Value scaleValue = new Value();
        private Value alphaValue = new Value();

        private Value rotationValue = new Value();
        private Value whiteAlpha = new Value();

        private String color = "#27ae60";
        private int type;
        private Value xValueO = new Value();


        public CenterMenu(GameWorld world, float x, float y, float width, float height) {

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


            whiteAlpha.setValue(0);
            type = 1;

        }

        public void update(float delta) {
            manager.update(delta);

            sprite.setX(xValueO.getValue());
            sprite.setOriginCenter();
            sprite.setRotation(angleValue.getValue());

            auraSprite.setX(xValueO.getValue());
            auraSprite.setScale(scaleValue.getValue());
            auraSprite.setOriginCenter();
            auraSprite.setRotation(angleValue.getValue());

        }

        public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {

            auraSprite.setAlpha(alphaValue.getValue());
            auraSprite.draw(batch);
            sprite.draw(batch);
            if (Configuration.DEBUG) {
                batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
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


        public void clickDown() {
            type = 0;
            color = "#c0392b";
            auraSprite.setColor(world.parseColor("FFFFFF", 1f));
            sprite.setColor(world.parseColor("#c0392b", 1f));

        }

        public void clickUp() {
            type = 1;
            color = "#27ae60";
            auraSprite.setColor(world.parseColor("FFFFFF", 1f));
            sprite.setColor(world.parseColor("#27ae60", 1f));
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
            xValueO.setValue(position.x - world.gameWidth);
            Tween.to(xValueO, -1, 0.5f).target(position.x).delay(.1f).ease(
                    TweenEquations.easeInOutSine)
                    .start(manager);
        }

        public void finish() {
            xValueO.setValue(position.x);
            Tween.to(xValueO, -1, 0.5f).target(position.x + world.gameWidth)
                    .ease(TweenEquations.easeInOutSine).start(manager);
        }
    }

    public SaveMeButton getSaveMeButton() {
        return saveMeButton;
    }

    public InviteButton getInviteButton() {
        return inviteButton;
    }
}
