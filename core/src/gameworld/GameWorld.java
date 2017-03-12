package gameworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
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
import gameobjects.BonusParticle;
import gameobjects.Center;
import gameobjects.Enemy;
import gameobjects.Menu;
import gameobjects.Star;
import helpers.AssetLoader;
import helpers.ColorManager;
import helpers.Rumble;
import paddle.ActionResolver;
import paddle.PaddleGame;
import ui.Banner;
import ui.BonusBanner;
import ui.Button;
import ui.ScoreBanner;

public class GameWorld {

    //GENERAL VARIABLES
    public float gameWidth;
    public float gameHeight;
    public float worldWidth;
    public float worldHeight;

    public ActionResolver actionResolver;
    public PaddleGame game;
    public GameWorld world = this;


    //GAME OBJECTS
    //private Hero hero;
    private Menu menu;
    private Center center;
    private GameCam camera;
    public GameState gameState;
    public ColorManager colorManager;
    private Button pauseButton;
    private final int numberOfStars;
    private final int numberOfEnemies;
    private Array<Star> stars = new Array<Star>();
    private Array<Enemy> enemies = new Array<Enemy>();
    private Array<Vector2> points = new Array<Vector2>();
    private BonusParticle bonusParticle;


    public Banner banner;
    public ScoreBanner scoreBanner;
    public BonusBanner bonusBanner;

    private int score;

    //TWEENS
    private TweenManager manager;
    private Value secondValue = new Value();
    private Value centerValue = new Value();

    public Rumble rumble;
    public Vector2 emptyVector = new Vector2();
    TweenCallback centerCallback, cbSecond;
    Tween centerTween;
    public Tween squaresTween;


    public GameWorld(PaddleGame game, ActionResolver actionResolver, float gameWidth,
                     float gameHeight, float worldWidth, float worldHeight) {

        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.game = game;
        this.actionResolver = actionResolver;
        this.numberOfStars = 0;
        this.numberOfEnemies = 100;

        menu = new Menu(this);
        menu.start();
        colorManager = new ColorManager();
        camera = new GameCam(world, worldWidth / 2, worldHeight / 2, worldWidth, worldHeight);
        center = new Center(this, worldWidth / 2, 170, 100, 100);

        createPoints(20);
        gameState = GameState.MENU;

        for (int i = 0; i < numberOfStars; i++) {
            stars.add(new Star(world));
        }

        enemies.clear();
        for (int i = 0; i < numberOfEnemies; i++) {
            int place = (int) Math.floor(Math.random() * points.size);
            enemies.add(new Enemy(world, points.get(place).x, points.get(place).y, 50, 50));
        }

        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();


        this.rumble = new Rumble(this);

        banner = new Banner(this, "Pause", world.gameHeight / 2, parseColor("#000000", 1f));
        //banner.appear();
        scoreBanner = new ScoreBanner(this, score + "", world.gameHeight - 275,
                parseColor(Configuration.COLOR_BANNER_WITH_SCORE, 1f));
        bonusBanner = new BonusBanner(this, AssetLoader.getBonusNumber() + "",
                world.gameHeight - 275,
                parseColor(Configuration.COLOR_BANNER_WITH_GOLD_SQUARES, 1f));
        bonusBanner.appear();

        int place = (int) Math.floor(Math.random() * points.size);
        bonusParticle = new BonusParticle(world, points.get(place).x, points.get(place).y, 30, 30);
        pauseButton = new Button(this, 50, world.gameHeight - 150,
                100, 100,
                AssetLoader.square, AssetLoader.pauseButton, Configuration.COLOR_PAUSE_BUTTON,
                .05f);
        squaresTween = Tween.to(secondValue, -1, 0);
    }

    private void startFalling() {
        secondValue.setValue(0);
        cbSecond = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                int point = (int) Math.floor(Math.random() * points.size);
                int point1;
                do {
                    point = (int) Math.floor(Math.random() * points.size);
                    point1 = generateRandomPoint(point);
                } while (point == point1);

                int type1 = Math.random() < 0.5f ? 0 : 1;
                if (Math.random() < 0.2f) {
                    moveEnemy(point, type1);
                    moveEnemy(point1, type1);
                } else {
                    moveEnemy(point, type1);
                }
                if (Math.random() < Configuration.GOLD_SQUARE_FREQUENCY) {
                    point = (int) Math.floor(Math.random() * points.size);
                    moveParticle(point);
                }
                float rTime = (float) (Math
                        .random() * (Configuration.MAX_TIME_BETWEEN_ENEMIES - Configuration.MIN_TIME_BETWEEN_ENEMIES)
                        + Configuration.MIN_TIME_BETWEEN_ENEMIES);
                squaresTween = Tween.to(secondValue, -1, rTime).target(1).repeatYoyo(0, 0)
                        .ease(TweenEquations.easeInOutSine).setCallbackTriggers(
                                TweenCallback.END).setCallback(cbSecond).start(manager);
            }
        };
        squaresTween.kill();
        squaresTween = Tween.to(secondValue, -1, 0.3f).target(1).repeatYoyo(0, 0)
                .ease(TweenEquations.easeInOutSine).setCallbackTriggers(
                        TweenCallback.END).setCallback(cbSecond).delay(.0f).start(manager);
    }


    private int generateRandomPoint(int point) {
        int number;
        do {
            number = (int) Math.floor(Math.random() * points.size);
        }
        while (number == point && number >= point + 1 && (number >= 0 && number < points.size) && number <= point - 1);
        return number;
    }

    public void update(float delta) {
        manager.update(delta);
        colorManager.update(delta);

        menu.update(delta);
        center.update(delta);
        scoreBanner.update(delta);
        bonusBanner.update(delta);
        pauseButton.update(delta);
        banner.update(delta);
        if (!isPaused()) {
            for (int i = 0; i < enemies.size; i++) {
                enemies.get(i).update(delta);
            }
            bonusParticle.update(delta);
            for (int i = 0; i < numberOfStars; i++) {
                stars.get(i).update(delta);
            }
        }
        if (isRunning()) {
            collisions();
        } else if (isMenu()) {
        } else if (isGameOver()) {
        } else if (isPaused()) {
        }


    }

    private void collisions() {
        //BONUS PARTICLE COLLISION
        if (bonusParticle.getEnemyState() == BonusParticle.EnemyState.DEAD) {
            bonusParticle.setPosition(new Vector2(0, world.gameHeight - 100));
            bonusParticle.setVelocity(emptyVector);
            bonusParticle.setEnemyState(BonusParticle.EnemyState.WAITING);
        } else if (bonusParticle.getEnemyState() == BonusParticle.EnemyState.MOVING) {
            if (Intersector.overlaps(bonusParticle.getRectangle(),
                    center.getBottomRect())) {
                bonusParticle.setAcceleration(emptyVector);
                //en.setVelocity(new Vector2(0, (float) (-200 - Math.random() * 200)));
                //center.clicked();
                bonusParticle.dissapear();
                bonusParticle.setEnemyState(BonusParticle.EnemyState.FINISH);
                bonusParticle.bounce();
                AssetLoader.sounds.get(
                        1)
                        .play();
                AssetLoader.addBonusNumber(1);
                bonusBanner.setText(AssetLoader.getBonusNumber() + "");
                //rumble.rumble(5f, .1f);
            } else {

            }
        }


        for (int i = 0; i < enemies.size; i++) {
            Enemy en = enemies.get(i);
            if (en.getEnemyState() == Enemy.EnemyState.DEAD) {
                en.setPosition(points.get(0));
                en.setVelocity(emptyVector);
                en.setEnemyState(Enemy.EnemyState.WAITING);
                en.randomType();
            } else if (en.getEnemyState() == Enemy.EnemyState.MOVING) {
                if (Intersector.overlaps(en.getRectangle(),
                        center.getBottomRect())) {
                    en.setAcceleration(emptyVector);
                    //en.setVelocity(new Vector2(0, (float) (-200 - Math.random() * 200)));
                    center.clicked();
                    en.dissapear();
                    enemies.get(i).setEnemyState(Enemy.EnemyState.FINISH);
                    if (en.getType() != center.getType()) {
                        gameState = GameState.GAMEOVER;
                        finish();
                    } else {
                        en.bounce();
                        if (en.getType() == 0) {
                            AssetLoader.sounds.get(4)
                                    .play();
                        } else {
                            AssetLoader.sounds.get(3).play();
                        }
                        score++;
                        scoreBanner.setText(score + "");
                        //rumble.rumble(5f, .1f);
                    }
                }
            } else if (en.getEnemyState() == Enemy.EnemyState.MOVING_TO_CENTER) {
                if (Intersector.overlaps(en.getRectangle(), center.getRectangle())) {
                    en.setVelocity(emptyVector);
                    en.setAcceleration(emptyVector);
                    en.setVelsetted(false);
                    en.setEnemyState(Enemy.EnemyState.CENTER);
                    en.waitInCenter();
                }
            }

        }

    }

    private void finish() {
        //banner.appearDissapear(.8f);
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).dissapearFinish();
        }
        saveScoresLogic();
        menu.startGameOver();
        scoreBanner.dissapear();
        bonusParticle.dissapearFinish();
        center.finish();
        squaresTween.kill();
        pauseButton.finish();
    }

    private void saveScoresLogic() {
        AssetLoader.addGamesPlayed();
        int gamesPlayed = AssetLoader.getGamesPlayed();
        // GAMES PLAYED ACHIEVEMENTS!
        actionResolver.submitScore(score);
        actionResolver.submitGamesPlayed(gamesPlayed);

        if (score > AssetLoader.getHighScore()) {
            AssetLoader.setHighScore(score);
        }

        checkAchievements();
    }

    private void checkAchievements() {
        if (actionResolver.isSignedIn()) {
            if (score >= 10)
                actionResolver.unlockAchievementGPGS(Configuration.SCORE_10);
            if (score >= 25)
                actionResolver.unlockAchievementGPGS(Configuration.SCORE_25);
            if (score >= 50)
                actionResolver.unlockAchievementGPGS(Configuration.SCORE_50);
            if (score >= 100)
                actionResolver.unlockAchievementGPGS(Configuration.SCORE_100);
            if (score >= 150)
                actionResolver.unlockAchievementGPGS(Configuration.SCORE_150);

            int gamesPlayed = AssetLoader.getGamesPlayed();
            // GAMES PLAYED
            if (gamesPlayed >= 10)
                actionResolver.unlockAchievementGPGS(Configuration.GAMESPLAYED_10);
            if (gamesPlayed >= 25)
                actionResolver.unlockAchievementGPGS(Configuration.GAMESPLAYED_25);
            if (gamesPlayed >= 50)
                actionResolver.unlockAchievementGPGS(Configuration.GAMESPLAYED_50);
            if (gamesPlayed >= 75)
                actionResolver.unlockAchievementGPGS(Configuration.GAMESPLAYED_75);
            if (gamesPlayed >= 100)
                actionResolver.unlockAchievementGPGS(Configuration.GAMESPLAYED_100);
        }
    }


    public void pause() {
        banner.appear();
        gameState = GameState.PAUSE;
    }

    public Center getCenter() {
        return center;
    }

    public ColorManager getColorManager() {
        return colorManager;
    }

    public boolean isRunning() {
        return gameState == GameState.RUNNING;
    }

    public void renewMap(int i) {
        score = i;
        center.clickUp();
        //enemies.clear();

        scoreBanner.appear();
        scoreBanner.setText(score + "");
        center.start();
        pauseButton.start();
        startFalling();
        /*for (int i = 0; i < numberOfEnemies; i++) {
            int place = (int) Math.floor(Math.random() * points.size);
            enemies.add(new Enemy(world, points.get(place).x, points.get(place).y, 50, 50));
        }*/
    }

    public static Color parseColor(String hex, float alpha) {
        String hex1 = hex;
        if (hex1.indexOf("#") != -1) {
            hex1 = hex1.substring(1);
            // Gdx.app.log("Hex", hex1);
        }
        Color color = Color.valueOf(hex1);
        color.a = alpha;
        return color;
    }

    public void createPoints(int numOfS) {
        for (int i = 0; i < numOfS; i++) {
            float margin = 80;
            float distance = ((world.gameWidth - (margin * 2)) / (numOfS - 1));
            points.add(new Vector2(margin + (distance * i),
                    (float) (worldHeight + 199 - (Math.random() * 100))));
        }

    }

    public Array<Vector2> getPoints() {
        return points;
    }

    public Array<Star> getStars() {
        return stars;
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public void addScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public boolean isPaused() {
        return gameState == GameState.PAUSE;
    }

    public void moveEnemy() {
        boolean first = false;
        for (int i = 0; i < numberOfEnemies; i++) {

            if ((enemies.get(i).getEnemyState() == Enemy.EnemyState.DEAD || enemies.get(i)
                    .getEnemyState() == Enemy.EnemyState.WAITING) && !first) {
                enemies.get(i).randomType();
                enemies.get(i).setEnemyState(Enemy.EnemyState.MOVING);
                enemies.get(i)
                        .setPosition(
                                points.get((int) Math.floor(Math.random() * points.size)).cpy());
                enemies.get(i).setVelocity(new Vector2((float) (Math.random() * 0), -200).cpy());
                enemies.get(i).velocity.x *= Math.random() < 0.5f ? 1 : -1;
                enemies.get(i).setAcceleration(new Vector2(0, -500).cpy());
                first = true;
            }
        }
    }

    private void moveEnemy(int point, int type) {
        boolean first = false;
        for (int i = 0; i < numberOfEnemies; i++) {

            if (enemies.get(i).getEnemyState() == Enemy.EnemyState.WAITING && !first) {
                enemies.get(i).randomType();
                enemies.get(i).setEnemyState(Enemy.EnemyState.MOVING);
                if (point < points.size - 1 && point >= 0) {
                    enemies.get(i).setPosition(points.get(point).cpy());
                } else {
                    enemies.get(i).setPosition(
                            points.get((int) Math.floor(Math.random() * points.size)).cpy());
                }
                enemies.get(i).setVelocity(new Vector2((float) (Math.random() * 0), -200).cpy());
                enemies.get(i).velocity.x *= Math.random() < 0.5f ? 1 : -1;
                enemies.get(i).setAcceleration(new Vector2(0, -500).cpy());
                enemies.get(i).setType(type);
                first = true;
            }
        }
    }

    private void moveParticle(int point) {
        if (bonusParticle.getEnemyState() == BonusParticle.EnemyState.WAITING) {
            bonusParticle.setEnemyState(BonusParticle.EnemyState.MOVING);
            bonusParticle
                    .setPosition(
                            points.get((int) Math.floor(Math.random() * points.size)).cpy());
            bonusParticle.setVelocity(new Vector2((float) (Math.random() * 0), -500).cpy());
            bonusParticle.velocity.x *= Math.random() < 0.5f ? 1 : -1;
            bonusParticle.setAcceleration(new Vector2(0, -500).cpy());
        }

    }

    public GameCam getCamera() {
        return camera;
    }

    public Banner getBanner() {
        return banner;
    }

    public ScoreBanner getScoreBanner() {
        return scoreBanner;
    }

    public boolean isGameOver() {
        return gameState == GameState.GAMEOVER;
    }

    public boolean isMenu() {
        return gameState == GameState.MENU;
    }


    public Menu getMenu() {
        return menu;
    }

    public BonusBanner getBonusBanner() {
        return bonusBanner;
    }

    public BonusParticle getBonusParticle() {
        return bonusParticle;
    }

    public boolean isTransition() {
        return gameState == GameState.TRANSITION;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public void setPauseMode() {
        if (gameState == GameState.RUNNING) {
            gameState = GameState.PAUSE;
            getBanner().appearPause();
            squaresTween.pause();
        }
    }
}

