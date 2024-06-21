package game.assets.ships;

public enum ShipType {
    CARRIER(5, 1),
    CRUISER(4, 2),
    DESTROYER(3, 3),
    SUBMARINE(2, 4);

    private final int ship_length_;

    private final int max_count_;

    ShipType(int ship_length, int max_count){
        this.ship_length_ = ship_length;
        this.max_count_ = max_count;
    }

    /**
     * Getter for the length in Tiles of ships with this Type
     * @return The length as an integer
     */
    public int getShipLength(){
        return this.ship_length_;
    }

    /**
     * Getter for the Maximum amount of Ships of this Type per Player.
     * @return The Maximum amount.
     */
    public int getMaxCount(){
        return this.max_count_;
    }

    @Override
    public String toString(){
        switch (this){
            case CARRIER:
                return "ca";
            case CRUISER:
                return "cu";
            case DESTROYER:
                return "de";
            case SUBMARINE:
                return "su";
        }
        return "";
    }
}
