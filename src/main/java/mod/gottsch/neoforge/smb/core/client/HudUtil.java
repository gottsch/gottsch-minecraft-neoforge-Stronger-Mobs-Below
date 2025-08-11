/*
 * This file is part of Stronger Mobs Below.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Stronger Mobs Below is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Stronger Mobs Below is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Enemy Echelons.  If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.neoforge.smb.core.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.gottsch.neoforge.eechelons.core.echelon.EchelonManager;
import mod.gottsch.neoforge.smb.SMB;
import mod.gottsch.neoforge.smb.core.config.SMBConfig;
import mod.gottsch.neoforge.smb.core.event.HudEventHandler;
import mod.gottsch.neoforge.smb.core.integration.WailaIntegration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

/**
 * This class was derived from Champions by TheIllusiveC4
 * @see <a href="https://github.com/TheIllusiveC4/Champions">Champions</a>
 *
 */
public class HudUtil {
	private static final int WAILA_INTEGRATION_XOFFSET = -80;

	private static final int HUD_OFFSET_WIDTH = 32;
	private static final int MEDIUM_OFFSET_WIDTH = 64;
	private static final int HUD_OFFSET_HEIGHT = 4;
	private static final ResourceLocation HUD_BG = ResourceLocation.fromNamespaceAndPath(SMB.MOD_ID, "textures/gui/hud_bg.png");
	private static final ResourceLocation HUD_DARK_BG = ResourceLocation.fromNamespaceAndPath(SMB.MOD_ID, "textures/gui/hud_dark_bg2.png");
	private static final ResourceLocation MEDIUM_DARK_BG = ResourceLocation.fromNamespaceAndPath(SMB.MOD_ID, "textures/gui/medium_hud_dark_bg.png");

	/**
	 * 
	 * @param matrixStack
	 * @param livingEntity
	 * @return
	 */
	public static boolean renderLevelBar(GuiGraphics matrixStack, final LivingEntity livingEntity) {

		int difficulty = EchelonManager.getDifficulty(livingEntity);
		String name = EchelonManager.getDifficultyName(livingEntity).orElse("");

		// do not display is client doesn't want to show Level 0 hud.
		if (difficulty == 0 && !SMBConfig.CLIENT.showLevel0Hud.get()) {
			return false;
		}

		if (difficulty > -1) {
			Minecraft client = Minecraft.getInstance();

			// get the screen width
			int i = client.getWindow().getGuiScaledWidth();

			// calculate the text and width
			String text = !name.isEmpty() ? name : "Level " + difficulty;
			int textWidth = client.font.width(text);

			// determine what bg size to use
			ResourceLocation hudBg = textWidth > 55 ? MEDIUM_DARK_BG : HUD_DARK_BG;

			// adjust offset calculations
			// middle of the screen
			int k = i / 2 - (textWidth > 55 ? MEDIUM_OFFSET_WIDTH : HUD_OFFSET_WIDTH);
			int j = HUD_OFFSET_HEIGHT;
			
			int xOffset = SMBConfig.CLIENT.hudXOffset.get();
			int yOffset = SMBConfig.CLIENT.hudYOffset.get();

			/*
			 * only recalc offsets for integration if the config offsets are still default values
			 */
			int integrationXOffset = 0;
			int integrationYOffset = 0;
			if (xOffset == 0 && yOffset == 0) {
				if (WailaIntegration.isEnabled()) {
					integrationXOffset = WAILA_INTEGRATION_XOFFSET;
				}
			}

			// update static variable in the event handler
			HudEventHandler.startX = xOffset + k + integrationXOffset;
			HudEventHandler.startY = yOffset + 1 + integrationYOffset;

			// ----- IMPORTANT: Enable blending for transparency -----
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
//			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//			RenderSystem.setShader(GameRenderer::getPositionTexShader);
//			RenderSystem.setShaderTexture(0, hudBg);

			// 0 = startx, 0 = starty, 64 = endx, 20 = endy, 64 = width of image, 20 = height of image
			matrixStack.blit(hudBg, xOffset + k + integrationXOffset, yOffset + j + integrationYOffset, 0, 0, 64, 20, 64, 20);

			RenderSystem.disableBlend();

			// display the level text
			matrixStack.drawString(Minecraft.getInstance().font, text,
					(int)(xOffset + (float) (i / 2 - textWidth / 2) + integrationXOffset),
					(int)(yOffset + (float) (j  + client.font.lineHeight - 3) + integrationYOffset),
					Color.WHITE.getRGB());
		}

		return true;
	}
}
