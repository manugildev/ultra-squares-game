package paddle;


import com.badlogic.gdx.Game;

import helpers.AssetLoader;
import screens.SplashScreen;

public class PaddleGame extends Game {

    private ActionResolver actionresolver;


    public PaddleGame(ActionResolver actionresolver) {
        this.actionresolver = actionresolver;
    }

    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new SplashScreen(this, actionresolver));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
