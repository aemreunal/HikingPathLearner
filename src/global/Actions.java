package global;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

public enum Actions {
    RIGHT(0),
    LEFT(1),
    UP(2),
    DOWN(3);

    public final int value;

    private Actions(int value) {
        this.value = value;
    }
}
