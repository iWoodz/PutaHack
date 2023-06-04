package dev.putahack.util.trait;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public interface INameable {
    String getName();

    default String[] getAliases() {
        return new String[0];
    }
}
