package dev.putahack.module;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public enum ModuleCategory {
    CLIENT("Client", null),
    COMBAT("Combat", null),
    RENDER("Render", null),
    MOVEMENT("Movement", null);

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
