public interface ShotVisualizer {
  public void displayShotAt(int x, int y, boolean wasHit);

  // examples of different shot hits

  public void displayNormalShotAt(int x, int y, boolean wasHit);

  public void displayBigShotAt(int x, int y, boolean wasHit);


}