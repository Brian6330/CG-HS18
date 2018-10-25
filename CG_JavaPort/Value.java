package CG_JavaPort;

import java.util.Scanner;

/**
 * Allows storing data which may be accessed for read/write operations later on.
 * @param <E>
 */
public class Value<E> {

    /** the stored value. May be changed and read */
    private E value;

    public Value() {}

    /**
     * Constructs a changeable value with specified input.
     * @param v
     */
    public Value(E v) {
        this.value = v;
    }

    /**
     * @return {@link #value}
     */
    public E getValue() {
        return value;
    }

    /**
     * Overwrites the currently stored value with the specified value.
     * @param v the new value to be stored
     */
    public void setValue(E v) {
        this.value = v;
    }

    public void read(Scanner scn) {
        throw new UnsupportedOperationException("Unimplemented");
    }
}
