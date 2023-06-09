package dev.putahack.util.trait;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public interface Nameable {
    String getName();

    default String[] getAliases() {
        return new String[0];
    }
}
