package dev.starstruck.util.trait;

import dev.starstruck.Starstruck;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public interface Printable {
    String CHAT_PREFIX = TextFormatting.DARK_PURPLE + Starstruck.getName();

    /**
     * Prints to chat client-side
     * @param content the content to print to chat
     */
    default void print(String content) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI()
                .printChatMessage(new TextComponentString(CHAT_PREFIX)
                        .setStyle(new Style().setColor(TextFormatting.GRAY))
                        .appendText(" > ")
                        .appendText(content));
    }

    /**
     * Prints to chat client-side
     * @param id the message line id for the chat GUI
     * @param content the content to print to chat
     */
    default void print(int id, String content) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI()
                .printChatMessageWithOptionalDeletion(new TextComponentString(CHAT_PREFIX)
                        .setStyle(new Style().setColor(TextFormatting.GRAY))
                        .appendText(" > ")
                        .appendText(content), id);
    }
}
