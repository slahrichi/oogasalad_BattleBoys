/**
 * Weapon will be an abstract cass which has all the necessary information which all weapons are
 * supposed to have, as well as constructor for the setting the default setting for the weapon like
 * area of affect,weapon type etc.
 * <p>
 * It will have a number of subclasses ones that I am considering now are regular, splash damage,
 * scanning weapon etc.
 * <p>
 * all of them will implemet a method which applies respective effects according to weapon type.

public abstract interface Weapon {

  /**
   * creates a weapon and defines its area of effect, also type of effect

  public makeWeapon(Coordinate[] areaofEffect, string weaponType) {

  }

  /**
   * fires a projectile at the selected location, applies the corresponding affect to the cell(or
   * the cells around it)


  public fireAt(Coordinate impactLocation) {

  }

  /**
   * defines what the actual effect it for the projectile of the given weapon, will vary across
   * subclasses implementations
   *

  public abstract applyEffect() {

  }

}
 */