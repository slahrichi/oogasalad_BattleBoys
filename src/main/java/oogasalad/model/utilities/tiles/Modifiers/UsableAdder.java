//package oogasalad.model.utilities.tiles.Modifiers;
//
//import java.lang.reflect.Constructor;
//import java.util.Arrays;
//import java.util.function.Consumer;
//import oogasalad.controller.GameManager;
//import oogasalad.model.players.Player;
//import oogasalad.model.utilities.tiles.CellInterface;
//import oogasalad.model.utilities.usables.Usable;
//import oogasalad.model.utilities.usables.weapons.Weapon;
//
//public class UsableAdder extends Modifiers{
//  Weapon weaponToAdd;
//  final String weaponClass = "oogasalad.model.utilities.usables.weapons.";
//
//
//  public UsableAdder(Weapon weapon){
//    weaponToAdd = weapon;
//  }
//
//  public Weapon getWeapon(){
//    return weaponToAdd;
//  }
//
//  public UsableAdder(String usableCreator) {
//    String[] fields = usableCreator.split(" ");
//    String weaponName = fields[0];
//    String[] parameters  = Arrays.copyOfRange(fields, 1, fields.length);
//    try {
//      Constructor<Weapon> = (Class<Weapon>) Class.forName(weaponClass+weaponName);
//      weaponToAdd = reflectedObj.cast(new Weapon());
//    }catch(Exception e){
//
//    }
//    setMyConsumer(new PlayerConsumer() {
//      @Override
//      public void accept(Player[] players) {
//        players[0].addUsableToInventory(usableToAdd);
//      }
//    });
//  }
//
//  @Override
//  public Consumer modifierFunction(Player[] players){
//    return myConsumer;
//  }
//  @Override
//  public String toString() {
//    return "added " + usableToAdd.getMyID() + " to inventory";
//  }
//}
