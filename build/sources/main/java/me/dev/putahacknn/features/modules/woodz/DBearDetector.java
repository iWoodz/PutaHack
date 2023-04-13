package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DBearDetector extends Module {

    public DBearDetector() {
        super("DBearDetector", "detector bear", Category.WOODZ, true, false, false);
    }

    public List<Entity> knownPlayers = new ArrayList<>();

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        try {
            for (Entity entity : mc.world.loadedEntityList.stream().filter(entity -> entity != mc.player).collect(Collectors.toList())) {
                if (entity instanceof EntityPlayer && !knownPlayers.contains(entity)) {
                    if (entity.getName().equals("detective_bear1") || entity.getName().equals("wxxl")) {
                        Command.sendMessage("Detective Bear found on account " + entity.getName() + " at " + MathUtil.round(entity.posX, 2) + " " + MathUtil.round(entity.posY, 2) + " " + MathUtil.round(entity.posZ, 2));
                        knownPlayers.add(entity);
                    }
                }
            }
            knownPlayers.removeIf(entity -> entity instanceof EntityPlayer && !mc.world.loadedEntityList.contains(entity));
        } catch (Exception ignored) {

        }
    }

}
