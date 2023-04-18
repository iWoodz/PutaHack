package me.dev.putahacknn.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.putahacknn.event.events.Render2DEvent;
import me.dev.putahacknn.event.events.Render3DEvent;
import me.dev.putahacknn.features.modules.client.*;
import me.dev.putahacknn.features.modules.client.Colors;
import me.dev.putahacknn.features.modules.combat.*;
import me.dev.putahacknn.features.modules.misc.*;
import me.dev.putahacknn.features.modules.movement.*;
import me.dev.putahacknn.features.modules.player.*;
import me.dev.putahacknn.features.modules.player.Sprite;
import me.dev.putahacknn.features.modules.render.*;
import me.dev.putahacknn.features.modules.woodz.*;
import me.dev.putahacknn.util.Util;
import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.Feature;
import me.dev.putahacknn.features.gui.PutaHacknnGui;
import me.dev.putahacknn.features.modules.Module;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.apache.commons.codec.digest.DigestUtils;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModuleManager
        extends Feature {
    public ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();
    public Animation animationThread;

    public void init() {
        this.modules.add(new ItemPhysics());
        this.modules.add(new TrapHead());
        this.modules.add(new AutoReply());
        this.modules.add(new AutoGG());
        this.modules.add(new Ink());
        this.modules.add(new PortalESP());
        this.modules.add(new CsgoWatermark());
        this.modules.add(new BurrowESP());
        this.modules.add(new Colors());
        this.modules.add(new DiscordRPC());
        this.modules.add(new NameTags());
        this.modules.add(new AutoDickSuck());
        this.modules.add(new Sprite());
        this.modules.add(new StrictLimbs());
        this.modules.add(new Black());
        this.modules.add(new AntiRegear());
        this.modules.add(new ViewModel());
        this.modules.add(new HoleCampFix());
        this.modules.add(new AutoCenter());
        this.modules.add(new PvpInfo());
        this.modules.add(new Hud());
        this.modules.add(new DotGodSpammer());
        this.modules.add(new FPSFucker3000());
        this.modules.add(new AutoCrystalRewrite());
        this.modules.add(new TimeChanger());
        this.modules.add(new HealthBar());
        this.modules.add(new CrystalChams());
        this.modules.add(new AutoCompare());
        this.modules.add(new PopLag());
        this.modules.add(new PistonCrystal());
        this.modules.add(new CritRender());
        this.modules.add(new NoRender());
        this.modules.add(new AutoCookie());
        this.modules.add(new TargetHud());
        this.modules.add(new BeepBoop());
        this.modules.add(new Animations());
        this.modules.add(new Shutdown());
        this.modules.add(new AutoSelfCrash());
        this.modules.add(new AutoKit());
        this.modules.add(new LifeSaver());
        this.modules.add(new AntiLog());
        this.modules.add(new RealPlayer());
        this.modules.add(new LogAura());
        this.modules.add(new Ambience());
        this.modules.add(new Spawns());
        this.modules.add(new HideTheKids());
        this.modules.add(new GreatWallOfChina());
        this.modules.add(new AutoSwing());
        this.modules.add(new Disappointment());
        this.modules.add(new AspectRatio());
        this.modules.add(new Companion());
        this.modules.add(new DBearDetector());
        this.modules.add(new AntiDBear());
        this.modules.add(new ChatSuffix());
        this.modules.add(new Announcer());
        this.modules.add(new HoleSnap());
        this.modules.add(new ClipFlight());
        this.modules.add(new ElytraSwap());
        this.modules.add(new AutoDupe());
        this.modules.add(new Woodz());
        this.modules.add(new SilentXP());
        this.modules.add(new Strafe());
        this.modules.add(new ClickGui());
        this.modules.add(new FontMod());
        this.modules.add(new HUD());
        this.modules.add(new HoleESP());
        this.modules.add(new Replenish());
        this.modules.add(new SmallShield());
        this.modules.add(new Trajectories());
        this.modules.add(new FakePlayer());
        this.modules.add(new MCP());
        this.modules.add(new ReverseStep());
        this.modules.add(new MCF());
        this.modules.add(new PearlNotify());
        this.modules.add(new ToolTips());
        this.modules.add(new Tracker());
        this.modules.add(new PopCounter());
        this.modules.add(new Offhand());
        this.modules.add(new Surround());
        this.modules.add(new AutoTrap());
        this.modules.add(new AutoWeb());
        this.modules.add(new AutoCrystal());
        this.modules.add(new HoleFiller());
        this.modules.add(new Speed());
        this.modules.add(new Step());
        this.modules.add(new PacketFly());
        this.modules.add(new FastPlace());
        this.modules.add(new ESP());
        this.modules.add(new Selftrap());
        this.modules.add(new SelfFill());
        this.modules.add(new ArrowESP());
    }

    public String getInfo() {
        return DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getenv("os") + System.getProperty("os.name") + System.getProperty("os.arch") + System.getProperty("user.name") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS")));
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof PutaHacknnGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    private class Animation
            extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (Module module : ModuleManager.this.sortedModules) {
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            } else {
                for (String e : ModuleManager.this.sortedModulesABC) {
                    Module module = PutaHacknn.moduleManager.getModuleByName(e);
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}

