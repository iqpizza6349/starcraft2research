package io.iqpizza.starcraft.simcity;

public record Size(int width, int height) {

    public static Size of(int width, int height) {
        return new Size(width, height);
    }

    public static final Size BIG_SIZE = Size.of(5, 5);
    public static final Size MIDDLE_SIZE = Size.of(3, 3);
    public static final Size SMALL_SIZE = Size.of(2, 2);
    public static final Size TINY_SIZE = Size.of(1, 1);
}
