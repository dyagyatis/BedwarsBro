package com.dimchig.bedwarsbro.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TargetHUDManager extends Gui {
    private Minecraft mc;
    public static boolean enabled = true;

    public TargetHUDManager() {
        this.mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (!enabled || event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (mc == null || mc.thePlayer == null || mc.pointedEntity == null) return;
        if (!(mc.pointedEntity instanceof EntityPlayer)) return;

        EntityPlayer target = (EntityPlayer) mc.pointedEntity;
        if (target.isDead || target.isInvisibleToPlayer(mc.thePlayer)) return;

        ScaledResolution sr = new ScaledResolution(mc);
        FontRenderer font = mc.fontRendererObj;

        int width = 160;
        int height = 50;
        int x = (sr.getScaledWidth() - width) / 2;
        int y = sr.getScaledHeight() / 2 + 30;

        // Background card
        Gui.drawRect(x, y, x + width, y + height, 0xCC000000);
        Gui.drawRect(x, y, x + width, y + 2, 0xFF00AAFF);

        // Name & Distance
        String nameStr = target.getName();
        float dist = mc.thePlayer.getDistanceToEntity(target);
        String distStr = String.format("§7[§e%.1fm§7]", dist);
        font.drawStringWithShadow(nameStr, x + 10, y + 8, 0xFFFFFF);
        font.drawStringWithShadow(distStr, x + width - font.getStringWidth(distStr) - 10, y + 8, 0xFFFF55);

        // Health Bar
        float hp = target.getHealth();
        float maxHp = target.getMaxHealth();
        float hpPercent = Math.min(1.0f, Math.max(0.0f, hp / maxHp));

        int barX = x + 10;
        int barY = y + 22;
        int barWidth = width - 20;
        int barHeight = 8;

        Gui.drawRect(barX, barY, barX + barWidth, barY + barHeight, 0xFF333333);
        int color = hpPercent > 0.5f ? 0xFF00FF00 : (hpPercent > 0.25f ? 0xFFFFFF00 : 0xFFFF0000);
        Gui.drawRect(barX, barY, barX + (int) (barWidth * hpPercent), barY + barHeight, color);

        String hpText = String.format("§f%.1f / %.1f HP", hp, maxHp);
        font.drawStringWithShadow(hpText, barX + (barWidth - font.getStringWidth(hpText)) / 2, barY, 0xFFFFFF);

        // Armor items
        int armorX = x + 10;
        int armorY = y + 32;

        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        RenderHelper.enableGUIStandardItemLighting();

        for (int i = 3; i >= 0; i--) {
            ItemStack stack = target.inventory.armorInventory[i];
            if (stack != null) {
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, armorX, armorY);
                armorX += 16;
            }
        }
        if (target.getHeldItem() != null) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItem(), armorX + 10, armorY);
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }
}
