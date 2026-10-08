package com.dimchig.bedwarsbro.gui;

import com.dimchig.bedwarsbro.Main;
import com.dimchig.bedwarsbro.gui.GuiMinimap.MyBed;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class BedDefenseHighlighter {
    private Minecraft mc;
    public static boolean enabled = true;

    public BedDefenseHighlighter() {
        this.mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!enabled || mc == null || mc.theWorld == null || mc.thePlayer == null) return;
        if (Main.minimap == null || Main.minimap.bedsFound == null || Main.minimap.bedsFound.size() == 0) return;

        EntityPlayerSP player = mc.thePlayer;
        World world = mc.theWorld;

        float partialTicks = event.partialTicks;
        double px = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
        double py = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
        double pz = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0f);

        GL11.glTranslated(-px, -py, -pz);

        ArrayList<MyBed> beds = new ArrayList<MyBed>(Main.minimap.bedsFound);
        for (MyBed bed : beds) {
            BlockPos bpos = new BlockPos(bed.part1_posX, bed.part1_posY, bed.part1_posZ);

            // Scan 3x3x3 box surrounding bed
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -1; dy <= 2; dy++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        BlockPos checkPos = bpos.add(dx, dy, dz);
                        Block block = world.getBlockState(checkPos).getBlock();

                        if (block == Blocks.air || block == Blocks.bed) continue;

                        float r = 1.0f, g = 1.0f, b = 1.0f;
                        if (block == Blocks.planks) {
                            r = 1.0f; g = 0.7f; b = 0.0f; // Wood -> Gold
                        } else if (block == Blocks.end_stone) {
                            r = 1.0f; g = 1.0f; b = 0.2f; // Endstone -> Yellow
                        } else if (block == Blocks.stained_glass || block == Blocks.glass) {
                            r = 0.0f; g = 0.8f; b = 1.0f; // Glass -> Cyan
                        } else if (block == Blocks.obsidian) {
                            r = 0.6f; g = 0.0f; b = 1.0f; // Obsidian -> Purple
                        } else if (block == Blocks.wool) {
                            r = 0.8f; g = 0.8f; b = 0.8f; // Wool -> Light Gray
                        } else {
                            continue;
                        }

                        AxisAlignedBB aabb = new AxisAlignedBB(
                                checkPos.getX(), checkPos.getY(), checkPos.getZ(),
                                checkPos.getX() + 1, checkPos.getY() + 1, checkPos.getZ() + 1
                        );

                        GL11.glColor4f(r, g, b, 0.8f);
                        RenderGlobal.drawSelectionBoundingBox(aabb);
                    }
                }
            }
        }

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
