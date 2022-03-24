
public interface PlayerAPI {

    public void playTurn();

    public void makePurchase(int amount);

    public void moveShip(Ship s, Coordinate c);

    public void placeShips(Ship s);

    public void addGold(int amount);

}
