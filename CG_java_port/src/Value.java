import java.util.Scanner;

/**
 * Allows storing data which may be accessed for read/write operations later on.
 *
 * @param <E>
 */
public class Value<E> {

    /**
     * the stored value. May be read and writte to
     */
    private E value;

    public Value() {
    }

    /**
     * Constructs a changeable value with specified input.
     *
     * @param v
     */
    public Value(E v) {
        this.value = v;
    }

    /**
     * @return {@link #value}
     */
    public E get() {
        return value;
    }

    /**
     * Overwrites the currently stored value with the specified value.
     *
     * @param v the new value to be stored
     */
    public void set(E v) {
        this.value = v;
    }

    /**
     * Reads the next value from the specified Stream to this {@link #value}.
     *
     * @param scn the scanner to be read
     * @throws UnsupportedOperationException always
     */
    public void read(Scanner scn) {
        throw new UnsupportedOperationException("Unimplemented");
    }
}
