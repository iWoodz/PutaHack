package me.dev.putahacknn.util;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.util.UUIDTypeAdapter;
import me.dev.putahacknn.features.command.Command;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PlayerUtil implements Util {
    private static final JsonParser PARSER = new JsonParser();

    public static String getNameFromUUID(UUID uuid) {
        try {
            lookUpName process = new lookUpName(uuid);
            Thread thread = new Thread(process);
            thread.start();
            thread.join();
            return process.getName();
        } catch (Exception e) {
            return null;
        }
    }

    public static int getItemCount(final Item item) {
        if (mc.player == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static float getDamageInPercent(ItemStack stack) {
        float green = ((float) stack.getMaxDamage() - (float) stack.getItemDamage()) / (float) stack.getMaxDamage();
        float red = 1.0f - green;
        return 100 - (int) (red * 100.0f);
    }

    public static String convertToCoords(EnumFacing direction) {
        switch (direction) {
            case WEST:
                return "-X";
            case EAST:
                return "+X";
            case SOUTH:
                return "+Z";
            case NORTH:
                return "-Z";
            default:
                return "INVALID";
        }
    }

    public static int findObiInHotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block instanceof BlockEnderChest)
                    return i;
                else if (block instanceof BlockObsidian)
                    return i;
            }
        }
        return -1;
    }

    public static void centerPlayer(Vec3d centeredBlock) {
        double xDeviation = Math.abs(centeredBlock.x - mc.player.posX);
        double zDeviation = Math.abs(centeredBlock.z - mc.player.posZ);
        if (xDeviation <= 0.1 && zDeviation <= 0.1) {
            double newX = -2;
            double newZ = -2;
            int xRel = (mc.player.posX < 0 ? -1 : 1);
            int zRel = (mc.player.posZ < 0 ? -1 : 1);
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)).getBlock() instanceof BlockAir) {
                if (Math.abs((mc.player.posX % 1)) * 1E2 <= 30) {
                    newX = Math.round(mc.player.posX - (0.3 * xRel)) + 0.5 * -xRel;
                } else if (Math.abs((mc.player.posX % 1)) * 1E2 >= 70) {
                    newX = Math.round(mc.player.posX + (0.3 * xRel)) - 0.5 * -xRel;
                }
                if (Math.abs((mc.player.posZ % 1)) * 1E2 <= 30) {
                    newZ = Math.round(mc.player.posZ - (0.3 * zRel)) + 0.5 * -zRel;
                } else if (Math.abs((mc.player.posZ % 1)) * 1E2 >= 70) {
                    newZ = Math.round(mc.player.posZ + (0.3 * zRel)) - 0.5 * -zRel;
                }
            }
            if (newX == -2)
                if (mc.player.posX > Math.round(mc.player.posX)) {
                    newX = Math.round(mc.player.posX) + 0.5;
                }
                else if (mc.player.posX < Math.round(mc.player.posX)) {
                    newX = Math.round(mc.player.posX) - 0.5;
                } else {
                    newX = mc.player.posX;
                }
            if (newZ == -2)
                if (mc.player.posZ > Math.round(mc.player.posZ)) {
                    newZ = Math.round(mc.player.posZ) + 0.5;
                } else if (mc.player.posZ < Math.round(mc.player.posZ)) {
                    newZ = Math.round(mc.player.posZ) - 0.5;
                } else {
                    newZ = mc.player.posZ;
                }
            mc.player.connection.sendPacket(new CPacketPlayer.Position(newX, mc.player.posY, newZ, true));
            mc.player.setPosition(newX, mc.player.posY, newZ);
        }
    }

    public static BlockPos getPlayerPosFloored() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static String getNameFromUUID(String uuid) {
        try {
            lookUpName process = new lookUpName(uuid);
            Thread thread = new Thread(process);
            thread.start();
            thread.join();
            return process.getName();
        } catch (Exception e) {
            return null;
        }
    }

    public static UUID getUUIDFromName(String name) {
        try {
            lookUpUUID process = new lookUpUUID(name);
            Thread thread = new Thread(process);
            thread.start();
            thread.join();
            return process.getUUID();
        } catch (Exception e) {
            return null;
        }
    }

    public static String requestIDs(String data) {
        try {
            String query = "https://api.mojang.com/profiles/minecraft";
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes(StandardCharsets.UTF_8));
            os.close();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String res = convertStreamToString(in);
            in.close();
            conn.disconnect();
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = (new Scanner(is)).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "/";
    }

    public static List<String> getHistoryOfNames(UUID id) {
        try {
            JsonArray array = getResources(new URL("https://api.mojang.com/user/profiles/" + getIdNoHyphens(id) + "/names"), "GET").getAsJsonArray();
            List<String> temp = Lists.newArrayList();
            for (JsonElement e : array) {
                JsonObject node = e.getAsJsonObject();
                String name = node.get("name").getAsString();
                long changedAt = node.has("changedToAt") ? node.get("changedToAt").getAsLong() : 0L;
                temp.add(name + "Â§8" + (new Date(changedAt)).toString());
            }
            Collections.sort(temp);
            return temp;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String getIdNoHyphens(UUID uuid) {
        return uuid.toString().replaceAll("-", "");
    }

    private static JsonElement getResources(URL url, String request) throws Exception {
        return getResources(url, request, null);
    }

    private static JsonElement getResources(URL url, String request, JsonElement element) throws Exception {
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(request);
            connection.setRequestProperty("Content-Type", "application/json");
            if (element != null) {
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                output.writeBytes(AdvancementManager.GSON.toJson(element));
                output.close();
            }
            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
                builder.append('\n');
            }
            scanner.close();
            String json = builder.toString();
            JsonElement data = PARSER.parse(json);
            return data;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    public static class lookUpUUID implements Runnable {
        private final String name;
        private volatile UUID uuid;

        public lookUpUUID(String name) {
            this.name = name;
        }

        public void run() {
            NetworkPlayerInfo profile;
            try {
                ArrayList<NetworkPlayerInfo> infoMap = new ArrayList<>(Objects.requireNonNull(mc.getConnection()).getPlayerInfoMap());
                profile = infoMap.stream().filter(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getName().equalsIgnoreCase(this.name)).findFirst().orElse(null);
                assert profile != null;
                this.uuid = profile.getGameProfile().getId();
            } catch (Exception e) {
                profile = null;
            }
            if (profile == null) {
                Command.sendMessage("Player isn't online. Looking up UUID..");
                String s = PlayerUtil.requestIDs("[\"" + this.name + "\"]");
                if (s == null || s.isEmpty()) {
                    Command.sendMessage("Couldn't find player ID. Are you connected to the internet? (0)");
                } else {
                    JsonElement element = (new JsonParser()).parse(s);
                    if (element.getAsJsonArray().size() == 0) {
                        Command.sendMessage("Couldn't find player ID. (1)");
                    } else {
                        try {
                            String id = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                            this.uuid = UUIDTypeAdapter.fromString(id);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Command.sendMessage("Couldn't find player ID. (2)");
                        }
                    }
                }
            }
        }

        public UUID getUUID() {
            return this.uuid;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class lookUpName implements Runnable {
        private final String uuid;
        private final UUID uuidID;
        private volatile String name;

        public lookUpName(String input) {
            this.uuid = input;
            this.uuidID = UUID.fromString(input);
        }

        public lookUpName(UUID input) {
            this.uuidID = input;
            this.uuid = input.toString();
        }

        public void run() {
            this.name = lookUpName();
        }

        public String lookUpName() {
            EntityPlayer player = null;
            if (mc.world != null) {
                player = mc.world.getPlayerEntityByUUID(this.uuidID);
            }
            if (player == null) {
                final String url = "https://api.mojang.com/user/profiles/" + this.uuid.replace("-", "") + "/names";
                try {
                    final String nameJson = IOUtils.toString(new URL(url));
                    if (nameJson.contains(",")) {
                        List<String> names = Arrays.asList(nameJson.split(","));
                        Collections.reverse(names);
                        return names.get(1).replace("{\"name\":\"", "").replace("\"", "");
                    } else {
                        return nameJson.replace("[{\"name\":\"", "").replace("\"}]", "");
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                    return null;
                }
            }
            return player.getName();
        }

        public String getName() {
            return this.name;
        }
    }
}
