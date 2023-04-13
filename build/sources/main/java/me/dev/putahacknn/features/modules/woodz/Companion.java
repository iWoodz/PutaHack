package me.dev.putahacknn.features.modules.woodz;

import com.mojang.text2speech.Narrator;
import me.dev.putahacknn.event.events.DeathEvent;
import me.dev.putahacknn.event.events.TotemPopEvent;
import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Companion extends Module {

    public Companion() {
        super("Companion", "your mother gay", Category.WOODZ, true, false, false);
    }

    public enum Mode {
        Message,
        Narrator
    }

    public final Setting<Mode> mode = this.register(new Setting("Mode", Mode.Narrator));
    public Narrator narrator = Narrator.getNarrator();

    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (event.getEntity().getName().equals(mc.player.getName())) {
            if (mode.getValue() == Mode.Message) {
                Command.sendMessage("Dude you just fucking popped, you know how serious that is right? I swear to god you have to be the worst pvper in this fucking community holy shit. How bad can you be? popping to no namers? kys nigga you'd be more useful dead than alive, using up these totems you know how hard these are to obtain? Thats right kiddo, kill yourself.");
            } else if (mode.getValue() == Mode.Narrator) {
                narrator.say("Dude you just fucking popped, you know how serious that is right? I swear to god you have to be the worst pvper in this fucking community holy shit. How bad can you be? popping to no namers? kys nigga you'd be more useful dead than alive, using up these totems you know how hard these are to obtain? Thats right kiddo, kill yourself.");
                narrator.clear();
            }
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.player == mc.player) {
            if (mode.getValue() == Mode.Message) {
                Command.sendMessage("DUDE. HOW COULD YOU DIE TO NO NAMERS HOLY SHIT, DID YOU REALLY JUST DIE??? HOW COULD YOU LET THAT HAPPEN, ARE YOU REALLY THAT INCOMPETENT? FUCK YOU DUDE! YOU'RE SO FUCKING USELESS, YOU CANT EVEN STAY ALIVE IN A BLOCK GAME HOLY SHIT, HOLY FUCKING SHIT HOW UNCOOL, NIGGA GET BETTER AT THE GAME, YOU PROBABLY HAVE NO FUNDS. KILL YOURSELF.");
            } else if (mode.getValue() == Mode.Narrator) {
                narrator.say("DUDE. HOW COULD YOU DIE TO NO NAMERS HOLY SHIT, DID YOU REALLY JUST DIE??? HOW COULD YOU LET THAT HAPPEN, ARE YOU REALLY THAT INCOMPETENT? FUCK YOU DUDE! YOU'RE SO FUCKING USELESS, YOU CANT EVEN STAY ALIVE IN A BLOCK GAME HOLY SHIT, HOLY FUCKING SHIT HOW UNCOOL, NIGGA GET BETTER AT THE GAME, YOU PROBABLY HAVE NO FUNDS. KILL YOURSELF.");
                narrator.clear();
            }
        }
    }

}
