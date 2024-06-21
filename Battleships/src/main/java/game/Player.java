package game;

import game.assets.*;
import game.assets.ships.*;
import gui.StartGUI;

/**
 * This class represents a Player. WIP
 */
public class Player {
    /** The Players own Map where he can place ships, visible to the Player from the start.*/
    private Map player_map_;
    /** The Opponents Map, not visible from the Start. */
    private Map opponent_map_;

    private boolean my_turn_;

    public boolean isHost() {
        return isHost;
    }

    private boolean isHost;

    /**
     * Constructor for a Player, all ship counters are initialized as 0.
     */
    public Player(boolean isHost){
        this.player_map_ = new Map(true);
        this.opponent_map_ = new Map(false);
        this.my_turn_ = false;
        this.isHost = isHost;
    }

    /**
     * Getter for the Players Map.
     * @return the Players Map.
     */
    public Map getPlayerMap(){
        return this.player_map_;
    }

    /**
     * Getter for the opponents Map.
     * @return the opponents Map.
     */
    public Map getOpponentMap(){
        return this.opponent_map_;
    }

    /**
     * Getter for the bool indicating whether it is this players turn or not.
     * @return true, if it is this Players turn.
     */
    public boolean getMyTurn(){
        return this.my_turn_;
    }

    /**
     * Getter for the Carrier Count.
     * @return the Carrier count.
     */
    public int getCarrierCount(){
        int count = 0;
        for (Ship ship : this.player_map_.getShipArray()){
            if (ship == null) break;
            if (ship.getShipType() == ShipType.CARRIER) count++;
        }
        return count;
    }

    /**
     * Getter for the cruiser count.
     * @return the Cruiser count.
     */
    public int getCruiserCount(){
        int count = 0;
        for (Ship ship : this.player_map_.getShipArray()){
            if (ship == null) break;
            if (ship.getShipType() == ShipType.CRUISER) count++;
        }
        return count;
    }

    /**
     * Getter for the destroyer Count.
     * @return the destroyer count.
     */
    public int getDestroyerCount(){
        int count = 0;
        for (Ship ship : this.player_map_.getShipArray()){
            if (ship == null) break;
            if (ship.getShipType() == ShipType.DESTROYER) count++;
        }
        return count;
    }

    /**
     * Getter for the Submarine count.
     * @return the submarine count.
     */
    public int getSubmarineCount(){
        int count = 0;
        for (Ship ship : this.player_map_.getShipArray()){
            if (ship == null) break;
            if (ship.getShipType() == ShipType.SUBMARINE) count++;
        }
        return count;
    }


    public void setPlayerMap(Map player_map){
        this.player_map_ = player_map;
    }


    public void setOpponentsMap(Map op_map){
        this.opponent_map_ = op_map;
    }


    public void setMyTurn(boolean turn){
        this.my_turn_ = turn;
    }

    /**
     * This Methods toggles the boolean indicating Player turns.
     */
    public void toggleMyTurn(){
        this.setMyTurn(!this.my_turn_);
    }

    /**
     * This Method checks if a Ship of a given Type can be added or if the Player already has the Maximum amount of
     * Ships of this Type.
     * @param type The Type that needs checking.
     * @return true, if Ship can be added, false if not.
     */
    public boolean checkShipCount(ShipType type){
        int count = 0;
        for (Ship ship : this.player_map_.getShipArray()){
            if (ship == null) break;
            if (ship.getShipType() == type) count++;
        }
        return count < type.getMaxCount();
    }

    /**
     * This Method checks if a Ship of given Type can be placed at the specified Location.
     * @param type The type of the Ship that we want to place/check.
     * @param vertical whether the Ship is to be placed vertically or horizontally.
     * @param x x-coordinate of the right-/upmost Tile.
     * @param y y-coordinate of the right-/upmost Tile.
     * @return true, if the placement location is valid, false if not.
     */
    public boolean checkPlacementLocation(ShipType type, boolean vertical, int x, int y){
        Tile[] all_tiles_to_check = this.player_map_.getShipAdjacentTiles(type, vertical, x, y);
        for (Tile tile : all_tiles_to_check){
            // if there is just one Tile with a Ship on it, the placement-location is invalid.
            if (!tile.getIsWater()) {
                return false;

            }
        }
        return true;
    }

    /**
     * This Method checks if a player placed all its Ships.
     * @return true, if placement is finished, false if not.
     */
    public boolean checkPlacementStatus(){
        if (this.getCarrierCount() == ShipType.CARRIER.getMaxCount()){
            if (this.getCruiserCount() == ShipType.CRUISER.getMaxCount()){
                if (this.getDestroyerCount() == ShipType.DESTROYER.getMaxCount()){
                    if (this.getSubmarineCount() == ShipType.SUBMARINE.getMaxCount()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This Method places a ship on the Map
     * @param ship_type The type of ship to be placed.
     * @param vertical whether the ship is placed vertically or horizontally
     * @param x x-coordinate of the right/uppermost Tile of the Ship(1 to 10).
     * @param y y-coordinate of the right/uppermost Tile of the Ship(1 to 10).
     */
    public void placeShip(ShipType ship_type, boolean vertical, int x, int y){
        if (checkShipCount(ship_type) & checkPlacementLocation(ship_type, vertical, x, y)){
            Tile[][] map = this.player_map_.getTileArray();
            Tile[] ship_tiles = new Tile[ship_type.getShipLength()];
            // adding the Tiles to a Tile Array, needed for the Ship Constructor.
            for (int i = 0; i < ship_type.getShipLength(); i++) {
                if (vertical) {
                    ship_tiles[i] = map[y + i - 1][x - 1];
                } else {
                    ship_tiles[i] = map[y - 1][x + i - 1];
                }
            }
            // adding the Ship to the arrays of Ships.
            this.player_map_.addShipToArray(new Ship(ship_type, ship_tiles));
        }
        //else throw new Exception();
    }

    /**
     * This Method removes the last Ship that was placed on the players Map.
     */
    public void undoLastShipPlacement(){
        this.player_map_.removeLastShipFromArray();
    }

    /**
     * This Method checks if this Player lost.
     * @return true if this player lost, false if not.
     */
    public boolean checkForGameLoss(){
        return !checkAllShipsIntact(this.player_map_.getShipArray());
    }

    /**
     * This Method checks if this Player won.
     * @return true if this Player won, false if not.
     */
    public boolean checkForGameWin(){
        return !checkAllShipsIntact(this.opponent_map_.getShipArray());
        // if checkAllShipsIntact returns true, the opponent still has intact Ship, so this Player has not won,
        // so we need to return false here.
    }

    /**
     * This Method bombards a Tile
     * @param x x-coordinate of the targeted Tile(1 - 10).
     * @param y y-coordinate of the targeted Tile(1 - 10).
     */
    public void attackTile(int x, int y){
        if (getMyTurn()) {
            Tile[][] op_map = this.opponent_map_.getTileArray();
            op_map[y - 1][x - 1].setBombed(true);
            op_map[y - 1][x - 1].setVisible(true);
            if (!op_map[y-1][x-1].getIsWater()) {
                try {
                    Ship attacked_ship = this.opponent_map_.getShipAtCoordinates(x, y);
                    attacked_ship.reloadDamageState();
                    // if the last tile of the Ship is hit, set adjacent Tiles to visible and bombed.
                    if (!attacked_ship.getIntact()) {
                        Tile[] adjacent_tiles = this.opponent_map_.getShipAdjacentTiles(
                                attacked_ship.getShipType(), attacked_ship.getVertical(), attacked_ship.getShipX() + 1, attacked_ship.getShipY() + 1
                        );
                        for (Tile tile : adjacent_tiles) {
                            tile.setBombed(true);
                            tile.setVisible(true);
                        }
                        if (this.checkForGameWin()) {
                            this.opponent_map_.toggleMapVisible();
                            this.setMyTurn(false);
                            if (this.isHost)
                                StartGUI.getNewHost().sendMessageFromClientToServer("Player 1 Won.");
                            else
                                StartGUI.getNewJoin().sendMessage("Player 2 Won!");
                        }
                    }
                } catch (Exception ex) {
                    // Water hit
                }
            }
            else {
                if (this.isHost)
                    StartGUI.getNewHost().sendMessageFromClientToServer("[CLIENTTURN]");
                else
                    StartGUI.getNewJoin().sendMessage("[HOSTTURN]");
            }
        }
    }

    /**
     * This Method is the counterpart to attackTile(). It
     * @param x
     * @param y
     */
    public void receiveAttack(int x, int y){
        Tile[][] player_map = this.player_map_.getTileArray();
        player_map[y - 1][x - 1].setBombed(true);
        try {
            Ship attacked_ship = this.player_map_.getShipAtCoordinates(x, y);
            attacked_ship.reloadDamageState();
            // if the last tile of our Ship is hit, all adjacent Tiles are set to bombed too.
            if (!attacked_ship.getIntact()){
                Tile[] adjacent_tiles = this.player_map_.getShipAdjacentTiles(
                        attacked_ship.getShipType(), attacked_ship.getVertical(), attacked_ship.getShipX()+1, attacked_ship.getShipY()+1
                );
                for (Tile tile : adjacent_tiles ){
                    tile.setBombed(true);
                }
                if (checkForGameLoss()){
                    this.opponent_map_.toggleMapVisible();
                    this.setMyTurn(false);
                }
            }
        }
        catch (Exception ex){
            // Water hit
        }
    }

    /**
     * This Method checks if at least one Ship in a Ship array is intact.
     * @param ship_array
     * @return true, if there is an intact ship, false if all ships are sunk.
     */
    public boolean checkAllShipsIntact(Ship[] ship_array){
        for(Ship ship : ship_array){
            if (ship.getIntact()) return true;
        }
        return false;
    }

    /**
     * This Method creates a String representing the Players Map intended for network communication.
     * @return The created String.
     */
    public String createUpdateString(){
        return this.player_map_.createShipArrayString();
    }

    static int call_count = 0;
    /**
     * This Method updates the opponents Map according to the given String.
     * @param ship_array_str String this method gets its update instructions from
     */
    public void updateOpponentMap(String ship_array_str){
        System.out.println("opponent ships bfore: " + this.opponent_map_.createShipArrayString());
        String[] string_array = ship_array_str.split(" ");
        for (String s : string_array) {
            try {
                addShipToOpponentShips(s);
            } catch (Exception ex) {
            }
        }
        System.out.println("opponent ships after: " + this.opponent_map_.createShipArrayString());
    }

    /**
     * This Method places a Ship on the opponent map.
     * @param ship_string String specifying the Ships type, adjustment and starting coordinates e.g."cuh0_2"
     */
    // TODO add exception Throw in switch default.
    public void addShipToOpponentShips(String ship_string) throws Exception{
        String s = ship_string.trim();


        // getting the type of the Ship from the String.
        ShipType type;
        switch (s.substring(0, 2)) {
            case "ca":
                type = ShipType.CARRIER;
                break;
            case "cu":
                type = ShipType.CRUISER;
                break;
            case "de":
                type = ShipType.DESTROYER;
                break;
            case "su":
                type = ShipType.SUBMARINE;
                break;
            default:
                throw new Exception();
        }

        // getting the alignment of the Ship from the String.
        char sub_alignment = s.charAt(2);
        boolean vertical;
        if (sub_alignment == 'v') vertical = true;
        else vertical = false;

        // getting the 'starting' coordinates of the Ship
        char sub_x = s.charAt(3);
        int x = sub_x - '0';
        char sub_y = s.charAt(5);
        int y = sub_y - '0';

        // creating a Tile Array, the coordinates are the actual indices this time.
        Tile[] tile_array = new Tile[type.getShipLength()];
        Tile[][] map = this.opponent_map_.getTileArray();
        for (int i = 0; i < type.getShipLength(); i++) {
            if (vertical) {
                map[y + i][x].setIsWater(false);
                tile_array[i] = map[y + i][x];
            } else {
                map[y][x + i].setIsWater(false);
                tile_array[i] = map[y][x + i];
            }
        }

        // adding the Ship to the opponents Ship array.
        //if (!this.opponent_map_.createShipArrayString().contains(s)) {
        this.opponent_map_.addShipToArray(new Ship(type, tile_array));

    }

    @Override
    public String toString(){
        return "Player Map:\n" + this.player_map_ + "\nOpponent Map:\n" + this.opponent_map_;
    }
}
