package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import configuration.Configuration;

public class AssetLoader {

    public static Texture logoTexture;
    public static TextureRegion logo;
    public static TextureRegion square;
    public static BitmapFont font, font1, font2;
    private static Preferences prefs;
    public static Sound sound;
    public static Texture bonus, buttonsT;
    public static TextureRegion squareAura;

    public static Array<Sound> sounds = new Array<Sound>();
    public static TextureRegion playButtonUp, rankButtonUp, shareButtonUp, achieveButtonUp,
            rateButtonUp, pauseButton;
    public static TextureRegion enemieshape;
    private static Texture enemieShapeT;

    public static void load() {
        logoTexture = new Texture(Gdx.files.internal("logo.png"));
        logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        logo = new TextureRegion(logoTexture, 0, 0, logoTexture.getWidth(),
                logoTexture.getHeight());
        logo.flip(false, false);

        square = new TextureRegion(new Texture(Gdx.files.internal("square.png")), 0, 0, 10, 10);
        squareAura = new TextureRegion(new Texture(Gdx.files.internal("squareAura.png")), 0, 0, 400,
                400);

        enemieShapeT = new Texture(Gdx.files.internal("enemie-shape.png"));
        enemieshape = new TextureRegion(enemieShapeT, 0, 0, enemieShapeT.getWidth(),
                enemieShapeT.getHeight());

        pauseButton = new TextureRegion(new Texture(Gdx.files.internal("pause.png")), 0, 0, 240,
                240);
        buttonsT = new Texture(Gdx.files.internal("buttons.png"));
        buttonsT.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        bonus = new Texture(Gdx.files.internal("bonus.png"));
        bonus.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        Texture tfont = new Texture(Gdx.files.internal("font.png"), true);
        tfont.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);

        // FONT
        font = new BitmapFont(Gdx.files.internal("font.fnt"),
                new TextureRegion(tfont), true);
        font.setScale(2f, -2f);
        font.setColor(parseColor(Configuration.COLOR_FONT, 1f));

        font1 = new BitmapFont(Gdx.files.internal("font.fnt"),
                new TextureRegion(tfont), true);
        font1.setScale(1.5f, -1.5f);
        font1.setColor(Color.WHITE);

        font2 = new BitmapFont(Gdx.files.internal("font.fnt"),
                new TextureRegion(tfont), true);
        font2.setScale(1.3f, -1.3f);
        font2.setColor(Color.WHITE);

        // MENU BG TEXTURE

        prefs = Gdx.app.getPreferences("UltraSquares");

        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        if (!prefs.contains("games")) {
            prefs.putInteger("games", 0);
        }

        sound = Gdx.audio.newSound(Gdx.files.internal("sound.wav"));
        sounds.add(sound);
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("sound1.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("sound2.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("sound3.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("sound4.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("sound5.wav")));

        //CROP BUTTONS
        playButtonUp = new TextureRegion(buttonsT, 0, 0, 240, 240);
        rankButtonUp = new TextureRegion(buttonsT, 240, 0, 240, 240);
        shareButtonUp = new TextureRegion(buttonsT, 720, 0, 240, 240);
        achieveButtonUp = new TextureRegion(buttonsT, 960, 0, 240, 240);
        rateButtonUp = new TextureRegion(buttonsT, 480, 0, 240, 240);


    }

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void addGamesPlayed() {
        prefs.putInteger("games", prefs.getInteger("games") + 1);
        prefs.flush();
    }

    public static int getGamesPlayed() {
        return prefs.getInteger("games");
    }

    public static void dispose() {
        font.dispose();
        font1.dispose();
        font2.dispose();
        bonus.dispose();
        logoTexture.dispose();
        sound.dispose();
        buttonsT.dispose();

    }

    public static int getBonusNumber() {
        return prefs.getInteger("bonus");
    }

    public static void addBonusNumber(int number) {
        prefs.putInteger("bonus", prefs.getInteger("bonus") + number);
        prefs.flush();
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


}
