package helpers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import configuration.Configuration;
import gameworld.GameWorld;
import ui.SimpleButton;

public class InputHandler implements InputProcessor {

    private GameWorld world;
    private Array<SimpleButton> menuButtons;
    private float scaleFactorX;
    private float scaleFactorY;
    private int activeTouch = 0;
    private Rectangle rect;


    public InputHandler(GameWorld world, float scaleFactorX,
                        float scaleFactorY) {
        this.world = world;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
        rect = new Rectangle(0, 0, 30, 30);
        menuButtons = world.getMenu().getMenuButtons();
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Keys.R) {
            world.renewMap(0);
        } else if (keycode == Keys.SPACE) {
            world.getCenter().clickDown();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        /*if (keycode == Keys.LEFT) {
            world.getHero().noClick();
        } else if (keycode == Keys.RIGHT) {
            world.getHero().noClick();
        } else if (keycode == Keys.UP) {
            world.getHero().clickUp();
        } else if (keycode == Keys.DOWN) {
            world.getHero().clickDown();
        } else if (keycode == Keys.R) {
            world.renewMap();
        }*/
        if (keycode == Keys.SPACE) {
            world.getCenter().clickUp();
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        //Gdx.app.log("Point", screenX + " " + screenY);
        activeTouch++;

        if (world.isRunning()) {
            world.getPauseButton().isTouchDown(screenX, screenY);
            world.getCenter().clickDown();
        } else if (world.isPaused()) {
            world.getBanner().dissapearPause();
        } else if (world.isGameOver()) {
            world.renewMap(0);
        } else if (world.isMenu()) {
            checkButonsDown(screenX, screenY);
        }

        return false;
    }

    private void checkButonsDown(int screenX, int screenY) {
        for (int i = 0; i < menuButtons.size; i++) {
            menuButtons.get(i).isTouchDown(screenX, screenY);
        }
        world.getMenu().getSaveMeButton().isTouchDown(screenX, screenY);
        world.getMenu().getInviteButton().isTouchDown(screenX, screenY);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        if (world.isRunning()) {
            if (world.getPauseButton().isTouchUp(screenX, screenY)) {
                world.setPauseMode();
            } else {
                world.getCenter().clickUp();
            }
        } else if (world.isMenu()) {
            checkButonsUp(screenX, screenY);
        } else if (world.isPaused()) {
        }
        return false;
    }

    private void checkButonsUp(int screenX, int screenY) {
        if (menuButtons.get(0).isTouchUp(screenX, screenY)) {
            world.getMenu().finish();
        } else if (menuButtons.get(1).isTouchUp(screenX, screenY)) {
            world.actionResolver.shareGame(
                    "My high score at " + Configuration.GAME_NAME + " is " + AssetLoader
                            .getHighScore() + ". Can you beat me? #MatchTheSquares ");
        } else if (menuButtons.get(2).isTouchUp(screenX, screenY)) {
            world.actionResolver.showScores();
        } else if (menuButtons.get(3).isTouchUp(screenX, screenY)) {
            world.actionResolver.showAchievement();
        } else if (menuButtons.get(4).isTouchUp(screenX, screenY)) {
            world.getMenu().finishGameOver(0);
        } else if (menuButtons.get(5).isTouchUp(screenX, screenY)) {
            world.getMenu().finishGameOverStartMenu();
        } else if (menuButtons.get(6).isTouchUp(screenX, screenY)) {
            world.actionResolver.shareGame(
                    "My high score at " + Configuration.GAME_NAME + " is " + AssetLoader
                            .getHighScore() + ". Can you beat me? #UltraSquares ");
        } else if (world.getMenu().getSaveMeButton().isTouchUp(screenX, screenY)) {
            if (AssetLoader.getBonusNumber() >= 10) {
                world.getMenu().finishGameOver(world.getScore());
                AssetLoader.addBonusNumber(-10);
                world.getBonusBanner().setText(AssetLoader.getBonusNumber() + "");
            }
        } else if (world.getMenu().getInviteButton().isTouchUp(screenX, screenY)) {
            if (Configuration.IN_APP_PURCHASES) {
                world.actionResolver.IAPClick();
            } else {
                world.actionResolver.shareGame("My high score at " + Configuration.GAME_NAME + " is " + AssetLoader
                        .getHighScore() + ". Can you beat me? #UltraSquares ");
            }
        } else {
            for (int i = 0; i < menuButtons.size; i++) {
                menuButtons.get(i).isPressed = false;
            }
            world.getMenu().getSaveMeButton().isPressed = false;
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (world.gameHeight - screenY / scaleFactorY);
    }

}
