package game.assets.ships;

import game.assets.Tile;
import game.assets.ships.ShipType;

/**
 * This class introduces the Ship objects, which contains a ShipType and the Ships Tiles.
 */
public class Ship {
    /** The Type of the Ship */
    private ShipType ship_type_;
    /** The Tiles the ship is placed on */
    private Tile[] ship_tiles_;

    private boolean intact_;

    /**
     * Constructor for a Ship
     * @param ship_type The Type of the Ship
     *
     */
    public Ship(ShipType ship_type, Tile[] ship_tiles){
        this.ship_type_ = ship_type;
        this.ship_tiles_ = ship_tiles;
        this.intact_ = true;
    }



    /**
     * Constructor for Ship only needing ShipType as an Argument, creates empty Array for Tiles
     * @param type The Ships Type.
     */
    public Ship(ShipType type){
        this(type, new Tile[type.getShipLength()]);
    }


    public int getLength(){
        return this.ship_type_.getShipLength();
    }


    public Tile[] getShipTiles() {
        return this.ship_tiles_;
    }


    public ShipType getShipType() {
        return this.ship_type_;
    }


    public boolean getIntact() {
        return this.intact_;
    }


    public boolean getVertical(){
        int x1 = this.getShipX();
        int x2 = this.ship_tiles_[1].getX();
        // if the x-coordinates of two different Tiles are equal, the Ship has to be vertical.
        if (x1 == x2) return true;
        else return false;
    }


    public int getShipX(){
        return this.ship_tiles_[0].getX();
    }


    public int getShipY(){
        return this.ship_tiles_[0].getY();
    }

    public void setShipTiles(Tile[] ship_tiles) {
        this.ship_tiles_ = ship_tiles;
    }


    public void setIntact(boolean intact){
        this.intact_ = intact;
    }

    /**
     * This Method checks, if at least one Tile of the Ship has not been bombed yet and sets the intact-bool accordingly.
      */
    public void reloadDamageState(){
        boolean new_intact = false;
        for (Tile tile : this.ship_tiles_){
            if (!tile.getBombed()) new_intact = true;
        }
        this.setIntact(new_intact);
    }

    /**
     * This Method creates a String intended to exchange Information via the server.
     * e.g. "cah2_0" -> CARRIER, horizontal, starting Tile is map[2][0]
     * @return The created String.
     */
    @Override
    public String toString(){
        String s = "";
        s += this.ship_type_;
        if (getVertical()) s += "v";
        else s+= "h";
        s += getShipX() + "_" + getShipY();
        return s;
    }

}
