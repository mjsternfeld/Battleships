package game.assets;

import game.assets.Tile;
import game.assets.ships.*;

public class Map {
    /** Variable for the sidelength of the Map. */
    private final int map_side_length = 10;
    /** A two dimensional Array containing all Tiles on the Map. */
    private Tile[][] tile_array_;

    private Ship[] ship_array_;

    private boolean is_player_map_;

    /**
     * Constructor for the Map, fills every tile with not bombed Water.
     */
    public Map(boolean is_player_map){
        Tile[][] map = new Tile[map_side_length][map_side_length];
        for (int y = 0; y < map_side_length; y++){
            for (int x = 0; x < map_side_length; x++){
                map[y][x] = new Tile(false, true, is_player_map, y * 10 + x);
            }
        }
        this.tile_array_ = map;
        this.ship_array_ = new Ship[10];
        this.is_player_map_ = is_player_map;
    }

    /**
     * Getter for the Tile-array representing the Map.
     * @return The Tile-array
     */
    public Tile[][] getTileArray(){
        return this.tile_array_;
    }

    /**
     * Getter for a specific TIle on the Map, specified by coordinates.
     * @param x x-coordinate of the Tile(1-10).
     * @param y y-coordinate of the Tile(1-10).
     * @return The Tile at the given coordinates.
     */
    public Tile getTileFromMap(int x, int y){
        Tile[][] tile_array = this.tile_array_;
        return tile_array[y - 1][x - 1];
    }

    /**
     * Getter for the Ship array in the Map
     * @return The Ship array.
     */
    public Ship[] getShipArray(){
        return this.ship_array_;
    }

    /**
     * Getter for the boolean indicating if this map is the Players map
     * @return true, if it is the Players Map.
     */
    public boolean getIsPlayerMap(){
        return this.is_player_map_;
    }

    /**
     * Setter for the Tile-Array representing the Map.
     * @param tile_array The new Tile-Array.
     */
    public void setTileArray(Tile[][] tile_array){
        this.tile_array_ = tile_array;
    }

    /**
     * This Method resets the Map to not bombed Water.
     */
    public void resetMap(boolean is_player_map){
        // iterates over the outer Tile Array.
        for (Tile[] rows : this.tile_array_){
            // iterates over the inner Tile Arrays.
            for (int i = 0; i < map_side_length; i++){
                rows[i].setIsWater(true);
                rows[i].setBombed(false);
                rows[i].setVisible(is_player_map);
            }
        }
    }

    // TODO Think about moving this to tile class
    /**
     * This Method toggles the bombed boolean of the Tile at the specified coordinates.
     * Coordinates range from 1 to 10
     * @param x x-Coordinate of the target-Tile.
     * @param y y-Coordinate of the target-Tile.
     */
    public void toggleTileBombed(int x, int y){
        boolean bombed = this.tile_array_[y - 1][x - 1].getBombed();
        this.tile_array_[y - 1][x - 1].setBombed(!bombed);
    }

    /**
     * This Method toggles the is_water boolean of the Tile at the specified coordinates.
     * Coordinates range from 1 to 10
     * @param x x-coordinate of the target-Tile.
     * @param y y-coordinate of the target-tile.
     */
    public void toggleTileWater(int x, int y){
        boolean water = this.tile_array_[y - 1][x - 1].getIsWater();
        this.tile_array_[y - 1][x - 1].setIsWater(!water);
    }

    /**
     * This Method toggles the visible boolean of the Tile at the specified coordinates.
     * Coordinates range from 1 to 10
     * @param x x-coordinate of the target-Tile.
     * @param y y-coordinate of the target-tile.
     */
    public void toggleTileVisible(int x, int y){
        boolean visible = this.tile_array_[y - 1][x - 1].getVisible();
        this.tile_array_[y - 1][x - 1].setVisible(!visible);
    }

    public void toggleMapVisible(){
        for (Tile[] tl_arr : this.tile_array_){
            for (Tile tile : tl_arr){
                tile.setVisible(true);
            }
        }
    }

    /**
     * This Method returns the Ship at the given coordinates, throws exception if there's no Ship at those coordinate.
     * @param x x-coordinate of the lookup location (1-10).
     * @param y y-coordinate of the lookup location (1-10).
     * @return The Ship at the given coordinates.
     * @throws Exception If there is no Ship at the location, an exception is thrown.
     */
    public Ship getShipAtCoordinates(int x, int y) throws Exception{
        for (Ship ship : this.ship_array_){
            for (Tile tile : ship.getShipTiles()){
                if (x - 1 == tile.getX() & y - 1 == tile.getY()) return ship;
            }
        }
        throw new Exception();
    }

    /**
     * This Method returns an Array of a Ships adjacent Tiles and the Ships Tiles itself.
     * @param type The type of the Ship whose Adjacent tiles we want to get.
     * @param vertical whether the Ship is placed vertically or horizontally
     * @param x the x-coordinate of the first tile of the Ship.
     * @param y the y-coordinate of the first tile of the Ship.
     * @return The Array of a Ships and its adjacent Tiles.
     */
    public Tile[] getShipAdjacentTiles(ShipType type, boolean vertical, int x, int y){
        Tile[][] map = this.tile_array_;
        Tile[] temp = new Tile[22];
        int index = 0;

        for (int i = 0; i < type.getShipLength(); i++) {

            if (vertical) {
                temp[index] = map[y - 1 + i][x - 1];
                index++;
                if (x > 1) {
                    temp[index] = map[y - 1 + i][x - 2];
                    index++;
                }
                if (x < 10) {
                    temp[index] = map[y - 1 + i][x];
                    index++;
                }
            }
            // if !vertical
            else {
                temp[index] = map[y - 1][x - 1 + i];
                index++;
                if (y > 1) {
                    temp[index] = map[y - 2][x - 1 + i];
                    index++;
                }
                if (y < 10) {
                    temp[index] = map[y][x - 1 + i];
                    index++;
                }
            }
        }
        // at this point the ships Tiles are in the Array
        if (vertical){
            if (y > 1){
                temp[index] = map[y - 2][x -1];
                index++;
                if (x > 1) {
                    temp[index] = map[y - 2][x - 2];
                    index++;
                }
                if (x < 10){
                    temp[index] = map[y - 2][x];
                    index++;
                }
            }
            if (y + type.getShipLength() - 1 < 10){
                temp[index] = map[y + type.getShipLength() - 1][x - 1];
                index++;
                if (x > 1) {
                    temp[index] = map[y + type.getShipLength() - 1][x - 2];
                    index++;
                }
                if (x < 10){
                    temp[index] = map[y + type.getShipLength() - 1][x];
                    index++;
                }
            }
        }
        else{
            if (x > 1){
                temp[index] = map[y - 1][x - 2];
                index++;
                if (y > 1){
                    temp[index] = map[y - 2][x - 2];
                    index++;
                }
                if (y < 10){
                    temp[index] = map[y][x - 2];
                    index++;
                }
            }
            if (x + type.getShipLength() - 1 < 10){
                temp[index] = map[y - 1][x + type.getShipLength() - 1];
                index++;
                if (y > 1){
                    temp[index] = map[y - 2][x + type.getShipLength() - 1];
                    index++;
                }
                if (y < 10){
                    temp[index] = map[y][x + type.getShipLength() - 1];
                    index++;
                }
            }
        }

        // Cutting all null-values from the end of the Array.
        int tile_count = 0;
        while(temp[tile_count] != null){
            tile_count++;
        }
        Tile[] adjacent_tiles = new Tile[tile_count];
        for(int i = 0; i < tile_count; i++){
            adjacent_tiles[i] = temp[i];
        }
        return adjacent_tiles;
    }

    /**
     * This Method adds a Ship to the Maps ship array.
     * @param ship The ship we want to add.
     */
    public void addShipToArray(Ship ship){
        int i = 0;
        while(this.ship_array_[i] != null){
            i++;
        }
        if (i <= 9) {
            this.ship_array_[i] = ship;
            for (Tile tile : ship.getShipTiles()){
                tile.setIsWater(false);
            }
        }
        //else throw new Exception();
    }

    /**
     * This Method removes the last Ship in the Ship array and resets all the Tiles to Water.
     */
    public void removeLastShipFromArray(){
        if (this.ship_array_[0] != null) {
            int index = 0;
            for (Ship ship : this.ship_array_) {
                if (ship == null) break;
                index++;
            }
            index--;
            Ship last_ship = this.ship_array_[index];
            if (last_ship != null) {
                // setting all Ship Tiles to water.
                for (Tile tile : last_ship.getShipTiles()) {
                    tile.setIsWater(true);
                }
                this.ship_array_[index] = null;
            }
            //else throw new Exception();
        }
        //else throw new Exception();
    }

    /**
     * This Method creates a String representing a ShipArray intended for Server communication.
     * e.g. cah2_0 cuv3_7 cuh 1_9 ...
     * @return The created String
     */
    public String createShipArrayString(){
        String s = "";
        for (Ship ship : this.ship_array_){
            if (ship == null) break;
            s += ship + " ";
        }
        return s;
    }

    /**
     * creates a String representing the Map. each state a Tile can have is represented with a different Char
     * like this:
     * Tile[
     *    Tile[~~~~~~~~~~]
     *    TIle[~~~~~~~~~~]
     *    ...
     * ]
     * @return The String representing a Map.
     */
    @Override
    public String toString(){
        String s = "";
        for (Tile[] outer_array : this.tile_array_){
            for (Tile tile : outer_array){
                s += tile;
            }
            s += "\n";
        }
        return s;
    }
}
