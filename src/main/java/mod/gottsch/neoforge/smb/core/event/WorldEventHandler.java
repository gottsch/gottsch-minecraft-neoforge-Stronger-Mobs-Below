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

import mod.gottsch.neo.gottschcore.world.WorldInfo;
import mod.gottsch.neoforge.eechelons.api.EnemyEchelonsApi;
import mod.gottsch.neoforge.smb.SMB;
import mod.gottsch.neoforge.smb.core.network.DifficultyRequestToServer;
import mod.gottsch.neoforge.smb.core.network.ModNetwork;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;


/**
 *
 * @author Mark Gottschling on Jul 31, 2022
 *
 */
@EventBusSubscriber(modid = SMB.MOD_ID)
public class WorldEventHandler {

	/**
	 * @param event
	 */
	@SubscribeEvent
	public static void onJoin(EntityJoinLevelEvent event) {

		Entity entity = event.getEntity();

		if (EnemyEchelonsApi.isValidEntity(entity)) {
//				EEchelons.LOGGER.debug("entity joining world -> {} : {}", entity.getName().getString(), entity.getId());
			/*
			 * if on the client, request an update from the server
			 */
			if (WorldInfo.isClientSide(event.getEntity().level())) {
				// get cap, ensure that level hasn't already been set.
				if (EnemyEchelonsApi.getDifficulty(entity) == -1) {
					DifficultyRequestToServer message = new DifficultyRequestToServer(entity.getId(), entity.level().dimension().location().toString(),
							entity.level().dimension().location().toString());
					PacketDistributor.sendToServer(message);
				}
			} else {
				Mob mob = (Mob) entity;
				// give the mob persistent data since there isn't an event to auto add data attachments in NeoForge.
				EnemyEchelonsApi.setDifficulty(mob, -1);
				EnemyEchelonsApi.apply(mob);
			}
		}
	}

}
