package game;

import game.assets.ships.*;

/**
 * This Class contains the Functions needed to run the Game and the Function to run the whole game.
 */
public class Controller {
    /**
     * This Method checks if a Player has placed all of their Ships.
     * @param player The player that is checked.
     * @return true, if all ships have been placed, false if they still have to place some.
     */
    public static boolean checkPlayerPlacementStatus(Player player){
        if (player.getCarrierCount() == ShipType.CARRIER.getMaxCount()){
            if (player.getCruiserCount() == ShipType.CRUISER.getMaxCount()){
                if (player.getDestroyerCount() == ShipType.DESTROYER.getMaxCount()){
                    if (player.getSubmarineCount() == ShipType.SUBMARINE.getMaxCount()){
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
