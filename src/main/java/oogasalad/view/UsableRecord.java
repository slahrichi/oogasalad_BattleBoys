package oogasalad.view;

/**
 * This record's purpose is to be used for displaying players' weapons/items in their inventory. We
 * can show the user the id of each weapon/item, get the image associated with each weapon/item from
 * a properties file using the className, and show the user the quantity of each weapon/item in
 * their inventory.
 *
 * @author Minjun Kwak
 */
public record UsableRecord(String id, String className, double quantity) {

}
