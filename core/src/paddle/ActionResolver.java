package paddle;

public interface ActionResolver {
    public void showOrLoadInterstital();

    public void signIn();

    public void signOut();

    public void rateGame();

    public void submitScore(long score);

    public void showScores();

    public boolean isSignedIn();

    public boolean shareGame(String msg);

    public void unlockAchievementGPGS(String string);

    public void showAchievement();

    public void submitGamesPlayed(long score);

    public void viewAd(boolean view);

    public void facebookButtonClick();

    public void IAPClick();

}
