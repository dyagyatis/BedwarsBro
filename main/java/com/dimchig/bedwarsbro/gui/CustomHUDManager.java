package com.dimchig.bedwarsbro.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import com.dimchig.bedwarsbro.stuff.SessionStatsTracker;

import java.util.ArrayList;
import java.util.List;

public class CustomHUDManager extends Gui {
    private Minecraft mc;

    public static boolean showCPS = true;
    public static boolean showArmorStatus = true;
    public static boolean showSessionStats = true;

    private static List<Long> leftClicks = new ArrayList<Long>();
    private static List<Long> rightClicks = new ArrayList<Long>();
    private static boolean wasLeftPressed = false;
    private static boolean wasRightPressed = false;

    public CustomHUDManager() {
        this.mc = Minecraft.getMinecraft();
    }

    public static void registerClick(boolean isLeft) {
        long time = System.currentTimeMillis();
        if (isLeft) {
            leftClicks.add(time);
        } else {
            rightClicks.add(time);
        }
    }

    public static int getCPS(boolean isLeft) {
        long now = System.currentTimeMillis();
        List<Long> clicks = isLeft ? leftClicks : rightClicks;
        for (int i = clicks.size() - 1; i >= 0; i--) {
            if (now - clicks.get(i) > 1000) {
                clicks.remove(i);
            }
        }
        return clicks.size();
    }

    public void updateClickState() {
        if (mc.gameSettings == null) return;
        boolean leftPressed = mc.gameSettings.keyBindAttack.isKeyDown();
        boolean rightPressed = mc.gameSettings.keyBindUseItem.isKeyDown();

        if (leftPressed && !wasLeftPressed) {
            registerClick(true);
        }
        if (rightPressed && !wasRightPressed) {
            registerClick(false);
        }
        wasLeftPressed = leftPressed;
        wasRightPressed = rightPressed;
    }

    public void drawHUD() {
        if (mc == null || mc.thePlayer == null || mc.gameSettings.showDebugInfo) return;

        updateClickState();
        FontRenderer font = mc.fontRendererObj;
        ScaledResolution sr = new ScaledResolution(mc);

        int yOffset = 10;

        // 1. Render CPS Counter
        if (showCPS) {
            int lCps = getCPS(true);
            int rCps = getCPS(false);
            String cpsStr = String.format("§bCPS: §f%d §7| §f%d", lCps, rCps);
            Gui.drawRect(5, yOffset - 2, 10 + font.getStringWidth(cpsStr), yOffset + 10, 0x80000000);
            font.drawStringWithShadow(cpsStr, 7, yOffset, 0xFFFFFF);
            yOffset += 15;
        }

        // 2. Render Session Stats Overlay
        if (showSessionStats) {
            String statsStr = String.format("§eBedwarsBro Fork §7[§aКровати: §f%d §7| §cКиллы: §f%d §7| §bВинстрик: §f%d§7]",
                    SessionStatsTracker.bedsBroken, SessionStatsTracker.kills, SessionStatsTracker.currentWinstreak);
            Gui.drawRect(5, yOffset - 2, 10 + font.getStringWidth(statsStr), yOffset + 10, 0x80000000);
            font.drawStringWithShadow(statsStr, 7, yOffset, 0xFFFFFF);
            yOffset += 15;
        }

        // 3. Render Armor Status
        if (showArmorStatus) {
            drawArmorStatus(sr);
        }
    }

    private void drawArmorStatus(ScaledResolution sr) {
        EntityPlayer player = mc.thePlayer;
        if (player == null) return;

        int x = sr.getScaledWidth() / 2 + 95;
        int y = sr.getScaledHeight() - 20;

        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();

        for (int i = 3; i >= 0; i--) {
            ItemStack stack = player.inventory.armorInventory[i];
            if (stack != null) {
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);
                x += 18;
            }
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }
}
