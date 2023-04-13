package me.dev.putahacknn.features.modules.woodz;

import com.mojang.text2speech.Narrator;
import me.dev.putahacknn.features.modules.Module;

public class AutoDickSuck extends Module {

    public AutoDickSuck() {
        super("AutoDickSuck", "woodz is the best crystal pvper of all time", Category.WOODZ, true, false, false);
    }

    public Narrator narrator = Narrator.getNarrator();

    @Override
    public void onEnable() {
        narrator.say("Woods is considered by many as the best Crystal PvPer on the infamous Minecraft server, 2b2t. He has earned a reputation as a fearsome opponent due to his exceptional skills and strategic gameplay. Woods is known for his quick reflexes and ability to react to any situation with ease, which has enabled him to dominate in countless PvP battles on the server. He has an in-depth understanding of the game mechanics, and his knowledge of Crystal PvP is unparalleled. Woods's success on 2b2t is a testament to his exceptional talent and his unwavering dedication to perfecting his craft. His achievements have established him as the ultimate 2b2t Crystal PvPer, and he continues to inspire and impress players on the server and beyond.");
        this.disable();
    }

}