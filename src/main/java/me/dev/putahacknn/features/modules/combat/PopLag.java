package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.TotemPopEvent;
import me.dev.putahacknn.features.modules.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PopLag extends Module {

    public PopLag() {
        super("PopLag", "dbear will 100% call you a robot, no doubt", Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (!PutaHacknn.friendManager.isFriend(event.getEntity())) {
            mc.player.connection.sendPacket(new CPacketChatMessage("/msg " + event.getEntity().getName() + " \u6C63\u2525\uB9A2\uC384\u7E3A\u2C37\u84B9\uA225\u84B9\u3AC3\u317E\u2C36\u84B9\uA225\u84B9\u3AC3\u347E\u2C37\u84B9\u2673\u7340\u7465\u2220\uB9A2\uC384\u773D\u656B\u3459\u6F4D\u5262\u4A30\u7156\u6173\u374F\u7257\u4439\u4954\u7A42\u4336\u2063\u5546\u644B\u675A\u6970\u3347\u3151\u5838\u756C\u6A6E\u3268\u4439\u4579\u5076\u4C74\u7835\u4840\u536D\u2266\u250A\uB9A2\uC384\u7E3A\u3935\u3535\u2525\uB9A2\uC384\u7E3A\u3331\u4439\u2525\uB9A2\uC384\u7E3A\u2C32\u3535\uAE25\u4450\u475A\u765F\u2525\uB9A2\uC384\u7E3A\u3535\u4439\u2525\uB9A2\uC384\u7E3A\u3832\u4439\u2225\uC3C3\u3DAA\uA225\u84B9\u3AC3\u327E\u2C39\u2531\u4439\u84B9\u3AC3\u357E\u2C34\u2531\uA225\u84B9\u3AC3\u367E\u312C\u2525\uB9A2\uC384\u7E3A\u3934\u84B9\u84B9\uB9A2\uC384\u7E3A\u3335\u4439\u2525\uB9A2\uC384\u7E3A\u2C37\u84B9\uA225\u84B9\u3AC3\u317E\u2C36\u84B9\uA225\u84B9\u3AC3\u347E\u2C37\u84B9\uA225\u84B9\u3AC3\u337E\u2C30\u6F4D\uA225\u84B9\u3AC3\u367E\u2C32\u3535\uA225\u84B9\u3AC3\u347E\u2C33\u2531\uA225\u84B9\u3AC3\u337E\u2C37\u3535\uA225\u84B9\u3AC3\u6F4D\u2525\uB9A2\uC384\u7E3A\u3732\u4439\u2525\uA842\u7685\uBB7A\u2543\uA225\u84B9\u3AC3\u337E\u312C\u2525\uB9A2\uC384\u7E3A\u3034\u6F4D\u2525\uB9A2\uC384\u7E3A\u3634\u4439\u2525\uB9A2\uC384\u7E3A\u3434\u6F4D\u2525\uB9A2\uC384\u7E3A\u3632\u6F4D\u2525\uB9A2\uC384\u7E3A\u3333\u6F4D\u2525\uB9A2\uC384\u7E3A"));
        }
    }

}
