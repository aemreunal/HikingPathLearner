package global;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

public enum TerrainType {
    GROUND(-1),
    HILL(-4);

    public final int reward;

    private TerrainType(int reward) {
        this.reward = reward;
    }
}
