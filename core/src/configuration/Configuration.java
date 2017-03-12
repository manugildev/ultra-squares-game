package configuration;

/**
 * Created by ManuGil on 05/02/15.
 */
public class Configuration {

    public static final String GAME_NAME = "Ultra Squares";

    //ADMOB IDS
    public static final String AD_UNIT_ID_BANNER = "ca-app-pub-6147578034437241/5657593018";
    public static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-6147578034437241/4180859816";
    public static final float AD_FREQUENCY = 0.5f; //Number between 0 and 1



    //In APP PURCHASES IDS
    public static boolean IN_APP_PURCHASES = true;
    public static String ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArM8HVTcBHaHWHssDbVbrlzLLgysQoPkyymNE8g/ZWyp6hrhfNuOU7p689PAfxYkmkxce0XsQosvjZ4VueqdJhni01QpH7aXc69Y1cfAMmPph35+tLGW9k5QKBOOmuUquazg7fBq9WpJGNasxn6bYMSdmUuhaN+mONRghaSRGhgPS3im00aLcebqUR/Ws5kv4EL/i4uDFc57VENNak2lKZPqJmHvCsKO04Iut9cxNk8V5pDCmI77B4odBkUX+ahv4vnma1E4HZvo/rioL0dQvAC+JCg1em3iTf5ZEojSe7wyABT1XMIN7MneuuZHyy97iaBM7ff/wu26ccmdPhF3i1wIDAQAB";
    public static String PRODUCT_ID = "100coins"; //Will give 100 coins to the user

    //LEADERBOARDS
    public static final String LEADERBOARD_HIGHSCORE = "CgkIsrXBw-EOEAIQBg";
    public static final String LEADERBOARD_GAMESPLAYED = "CgkIsrXBw-EOEAIQBw";

    //ACHIEVEMENTS
    public static final String SCORE_10 = "CgkIsrXBw-EOEAIQAQ";
    public static final String SCORE_25 = "CgkIsrXBw-EOEAIQAg";
    public static final String SCORE_50 = "CgkIsrXBw-EOEAIQAw";
    public static final String SCORE_100 = "CgkIsrXBw-EOEAIQBA";
    public static final String SCORE_150 = "CgkIsrXBw-EOEAIQBQ";

    public static final String GAMESPLAYED_10 = "CgkIsrXBw-EOEAIQCA";
    public static final String GAMESPLAYED_25 = "CgkIsrXBw-EOEAIQCQ";
    public static final String GAMESPLAYED_50 = "CgkIsrXBw-EOEAIQCg";
    public static final String GAMESPLAYED_75 = "CgkIsrXBw-EOEAIQCw";
    public static final String GAMESPLAYED_100 = "CgkIsrXBw-EOEAIQDA";

    //COLORS of SQUARES
    public static final String COLOR_SQUARE_1 = "#c0392b";
    public static final String COLOR_SQUARE_2 = "#27ae60";
    public static final String COLOR_CENTER_SQUARE_1 = "#a32f23";
    public static final String COLOR_CENTER_SQUARE_2 = "#1d8f4c";
    public static final String COLOR_GOLD_SQUARE = "#f1c40f";

    //COLORS GENERAL
    public static final String COLOR_BACKGROUND = "F1F1F1";
    public static final String COLOR_FONT = "FFFFFF";
    public static final String COLOR_BANNER_WITH_SCORE = "#2980b9";
    public static final String COLOR_BANNER_WITH_GOLD_SQUARES = "#2980b9";
    public static final String COLOR_PAUSE_BUTTON = "#7f8c8d";

    //COLORS BUTTONS
    public static final String COLOR_PLAY_BUTTON = "#27ae60";
    public static final String COLOR_LEADERBOARD_BUTTON = "#c0392b";
    public static final String COLOR_ACHIEVEMENT_BUTTON = "#f1c40f";
    public static final String COLOR_RETURN_HOME_BUTTON = "#c0392b";
    public static final String COLOR_SHARE_BUTTON = "#2980b9";
    public static final String COLOR_SAVE_ME_BUTTON = "#c0392b";
    public static final String COLOR_IN_APP_PURCHASES_BUTTON = "#3b5998";

    //COLORS BANNERS
    public static final String COLOR_GAME_NAME_BANNER = "#34495e";
    public static final String COLOR_SCORE_BANNER = "#465e75";
    public static final String COLOR_HIGH_SCORE_BANNER = "#465e75";
    public static final String COLOR_GAMES_PLAYED_BANNER = "#465e75";

    //GAME VARIABLES
    public static final double MIN_TIME_BETWEEN_ENEMIES = 0.22; //TIME IN SECONDS
    public static final double MAX_TIME_BETWEEN_ENEMIES = 0.44; //TIME IN SECONDS
    public static final double GOLD_SQUARE_FREQUENCY = 0.07f; //FRECUENCY OF THE GOLD SQUARE TO APPEAR
    public static final int MAX_VELOCITY_OF_ENEMIES = 900;

    //GENERAL VARIABLES
    public static boolean DEBUG = false;


}
