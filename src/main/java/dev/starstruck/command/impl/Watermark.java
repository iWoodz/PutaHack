package dev.starstruck.command.impl;

import dev.starstruck.command.Command;
import dev.starstruck.command.exceptions.InvalidSyntaxException;
import dev.starstruck.module.render.HUD;
import org.lwjgl.opengl.Display;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class Watermark extends Command {
    public Watermark() {
        super(new String[]{"watermark", "wm"},
                "Sets the client watermark displayed in the HUD and on the window title",
                "[watermark]");
    }

    @Override
    public String dispatch(String[] args) throws InvalidSyntaxException {
        if (args.length == 0) throw new InvalidSyntaxException();

        if (mc.getSession().getUsername()
                .toLowerCase().contains("sable")) return "no";

        HUD.WATERMARK = String.join(" ", args).trim();
        Display.setTitle(HUD.WATERMARK);

        return "Set the HUD watermark";
    }
}
