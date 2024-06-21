package game.assets;

public class Tile {
    /** This boolean indicates whether a Tile has been fired upon or not */
    private boolean bombed_;
    /** Indicates whether theres a Ship on this Tile or just Water */
    private boolean is_water_;
    /** Indicates if the Tiles information is visible to the Player */
    private boolean visible_;
    /** A unique ID that is calculated from the Tiles x and y coordinate. (10y + x). */
    private int id_;


    public Tile(boolean bombed, boolean is_water, boolean visible, int id){
        this.is_water_ = is_water;
        this.bombed_ = bombed;
        this.visible_ = visible;
        this.id_ = id;
    }

    /**
     * Getter for whether a Tile is bombed.
     * @return true if it is bombed, false else.
     */
    public boolean getBombed(){
        return this.bombed_;
    }

    /**
     * Getter for whether a Tile is Water or not.
     * @return true if the TIle is water, false else.
     */
    public boolean getIsWater(){
        return this.is_water_;
    }

    /**
     * Getter for the Visibility of a Tile
     * @return true, if the information on the Tile is visible, false else
     */
    public boolean getVisible(){
        return this.visible_;
    }

    /**
     * Getter for the ID of a TIle.
     * @return the Tiles ID
     */
    public int getID(){
        return this.id_;
    }

    /**
     * This Method calculates a Tiles x-coordinate from its ID
     * @return The x-coordinate.
     */
    public int getX(){
        return this.id_ % 10;
    }

    /**
     * This Method calculates a Tiles y-coordinate from its ID
     * @return The y-coordinate.
     */
    public int getY(){
        return (this.id_ - (this.id_ % 10)) / 10;
    }

    /**
     * Setter for whether a Tile is bombed or not
     * @param bombed if true, the Tile is bombed, else it's not.
     */
    public void setBombed(boolean bombed){
        this.bombed_ = bombed;
    }

    /**
     * Setter for whether a Tile is Water or not
     * @param water if true, is Water; else it's a ship.
     */
    public void setIsWater(boolean water){
        this.is_water_ = water;
    }

    /**
     * Setter for the visibility of a Tile.
     * @param visible true if the Tile is set to visible; false if not.
     */
    public void setVisible(boolean visible){
        this.visible_ = visible;
    }

    /**
     * creates a String representing a Tile.
     * @return The String representing Tile.
     */
    @Override
    public String toString(){
        if (!this.visible_) return "&";
        else {
            if (this.is_water_) {
                if (this.bombed_) return "o";
                else return "~";
            } else {
                if (this.bombed_) return "X";
                else return "#";
            }
        }
    }
}
