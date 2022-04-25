package oogasalad.controller;

import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.items.Item;
import oogasalad.model.utilities.usables.weapons.Weapon;
import oogasalad.model.utilities.winconditions.WinCondition;

public record GameData(List<Player> players,
                       List<Piece> pieces,
                       CellState[][] board,
                       Map<Player, DecisionEngine> engineMap,
                       List<WinCondition> winConditions,
                       Map<CellState, Color> cellStateColorMap,
                       List<Weapon> weapons,
                       List<IslandCell> specialIslands,
                       List<Item> powerUps,
                       Map<String,Double> playerInventory,
                       List<Usable> allUsables,
                       Integer shotsPerTurn,
                       Integer shipMovementRate,
                       Integer gold) {}
