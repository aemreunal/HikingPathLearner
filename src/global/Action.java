package global;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

public enum Action {
    RIGHT(0, "R: "),
    LEFT(1, "L: "),
    UP(2, "U: "),
    DOWN(3, "D: ");

    public final int index;
    public final String text;

    private Action(int index, String text) {
        this.index = index;
        this.text = text;
    }
}
