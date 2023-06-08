package dev.putahack.module;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public enum ModuleCategory {
    COMBAT("Combat", null),
    MOVEMENT("Movement", null),
    PLAYER("Player", null),
    RENDER("Render", null);

    private final String displayName, icon;

    ModuleCategory(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }
}
