package oogasalad.model.utilities.usables.weapons;

public class MineShot extends Weapon {
  /**
   * If it hits ship or island it acts normally, but if it misses and lands in water, it places a Mine that will
   * destroy a ship if it trys to move into that spot
   */
  int radius;
  int dmg;
  public MineShot(String id, int gold, int radius, int damage){
    super(id, gold);
    this.radius = radius;
    this.dmg = damage;
  }

  @Override
  protected void makeWeaponFunction() {

  }
}
