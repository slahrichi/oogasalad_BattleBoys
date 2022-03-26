/**
 * WaterOccupant is the most upper level hierarcy of a the backend model anything that exists on the
 * grid will be a version of a Water Occupant which is a tile unit. Example are water, ship, island, mine etc
 *
 */
public interface WaterOccupant {

  /**
   * Update method for what needs to occur if a certain tile has been hit i.e decrease life, increase points etc
   */
  public gotHit();

  /**
   * Allows for more damage than just 1 health
   * @param non 1 damage
   */
  public gotHit(int damage);

  /**
   * Generic update method for every cell
   */
  public update();

  /**
   * return how many hits the block can currently take
   */
  public int getLifeCounter();

  /**
   *
   * @return Front end and grid model needs to know if an object can be placed there or not
   */
  public boolean canCarryObject();

  /**
   * If block is moved then allows you to update the coordinates of the graph.
   * @param x
   * @param y
   */
  public updateCoordinates(int x, int y);

}