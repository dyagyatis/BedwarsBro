package com.dimchig.bedwarsbro.stuff;

import com.dimchig.bedwarsbro.ChatSender;
import com.dimchig.bedwarsbro.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class AdvancedSoundAlerts {
    private Minecraft mc;
    public static boolean enabled = true;

    private long lastInvisSoundTime = 0;
    private long lastPearlSoundTime = 0;

    public AdvancedSoundAlerts() {
        this.mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!enabled || event.phase != TickEvent.Phase.START) return;
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;

        EntityPlayerSP player = mc.thePlayer;
        long now = System.currentTimeMillis();

        // 1. Check Invisible Players nearby
        if (now - lastInvisSoundTime > 2000) {
            List<EntityPlayer> players = mc.theWorld.playerEntities;
            for (EntityPlayer p : players) {
                if (p.equals(player) || player.isOnSameTeam(p)) continue;
                if (p.isInvisible() && player.getDistanceToEntity(p) < 12.0f) {
                    mc.theWorld.playSound(player.posX, player.posY, player.posZ, "note.bassattack", 1.5f, 0.8f, false);
                    ChatSender.addText(Main.PREF + "§c§lВНИМАНИЕ! §eНевидимый игрок рядом (§f" + (int) player.getDistanceToEntity(p) + "м§e)!");
                    lastInvisSoundTime = now;
                    break;
                }
            }
        }

        // 2. Check Ender Pearls & Fireballs heading towards player
        if (now - lastPearlSoundTime > 1500) {
            List<Entity> loadedEntities = mc.theWorld.loadedEntityList;
            for (Entity entity : loadedEntities) {
                if (entity instanceof EntityEnderPearl || entity instanceof EntityLargeFireball) {
                    double dist = player.getDistanceToEntity(entity);
                    if (dist < 20.0) {
                        String typeStr = (entity instanceof EntityEnderPearl) ? "§5Эндер-жемчуг" : "§cФаербол";
                        mc.theWorld.playSound(player.posX, player.posY, player.posZ, "note.pling", 2.0f, 1.5f, false);
                        ChatSender.addText(Main.PREF + "§c§lОПАСНОСТЬ! " + typeStr + " §eблизко (§f" + (int) dist + "м§e)!");
                        lastPearlSoundTime = now;
                        break;
                    }
                }
            }
        }
    }
}
