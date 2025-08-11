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
package mod.gottsch.neoforge.smb.core.event;

import mod.gottsch.neoforge.smb.SMB;
import mod.gottsch.neoforge.smb.core.client.HudUtil;
import mod.gottsch.neoforge.smb.core.client.MouseUtil;
import mod.gottsch.neoforge.smb.core.config.SMBConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

import java.util.Optional;

/**
 * This class was derived from Champions by TheIllusiveC4
 * @see <a href="https://github.com/TheIllusiveC4/Champions">Champions</a>
 *
 */
@EventBusSubscriber(modid = SMB.MOD_ID)
public class HudEventHandler {

//	public static boolean isRendering = false;

	// NOTE these are only used to check where the offset is set to, so other integrations can move if they overlap
	// these are NOT used in the actual echelons rendering of background and level text 
	public static int startX = 0;
	public static int startY = 0;

	/**
	 * Forge Bus Event Subscriber class
	 */


	@SubscribeEvent
	public static void renderHealthHud(final RenderGuiLayerEvent.Pre evt) {
		if (SMBConfig.SERVER.showHud.get() && SMBConfig.CLIENT.showHud.get()) {
			Minecraft mc = Minecraft.getInstance();

			Optional<LivingEntity> livingEntity;
			if (SMBConfig.SERVER.hudRangeEnabled.get()) {
				livingEntity = MouseUtil.getMouseOverEchelonMob(mc, evt.getPartialTick().getRealtimeDeltaTicks());
			} else {
				HitResult hitResult = mc.hitResult;
				if (hitResult.getType() == HitResult.Type.ENTITY
						&& ((EntityHitResult)hitResult).getEntity() instanceof LivingEntity) {
					livingEntity = Optional.of((LivingEntity)((EntityHitResult)hitResult).getEntity());
				} else {
					livingEntity = Optional.empty();
				}
			}

			livingEntity.ifPresent(entity -> {
				GuiGraphics matrixStack = evt.getGuiGraphics();
				HudUtil.renderLevelBar(matrixStack, entity);
			});
		}
	}
}
