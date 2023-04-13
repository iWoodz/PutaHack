package me.dev.putahacknn.features.modules.woodz;

import com.mojang.text2speech.Narrator;
import me.dev.putahacknn.features.modules.Module;

public class AutoCookie extends Module {

    public AutoCookie() {
        super("AutoCookie", "dbear will never find something like this ong", Category.WOODZ, true, false, false);
    }

    public Narrator narrator = Narrator.getNarrator();

    @Override
    public void onEnable() {
        narrator.say("I went downstairs to the kitchen and I found a cookie, best believe i gobbled that shit up");
        this.disable();
    }

}
