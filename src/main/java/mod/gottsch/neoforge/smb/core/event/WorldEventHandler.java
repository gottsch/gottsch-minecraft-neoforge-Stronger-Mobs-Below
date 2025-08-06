/*
 * This file is part of  Stronger Mobs Below.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Stronger Mobs Below is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Stronger Mobs Below is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Stronger Mobs Below.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.neoforge.smb.core.event;

import mod.gottsch.neo.gottschcore.world.WorldInfo;
import mod.gottsch.neoforge.eechelons.api.EnemyEchelonsApi;
import mod.gottsch.neoforge.smb.SMB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;


/**
 * 
 * @author Mark Gottschling on Jul 31, 2022
 *
 */
public class WorldEventHandler {

	/**
	 * Forge Bus Event Subscriber class
	 */
	@EventBusSubscriber(modid = SMB.MOD_ID)
	public static class ForgeBusSubscriber {

		/**
		 * @param event
		 */
		@SubscribeEvent
		public static void onJoin(EntityJoinLevelEvent event) {

			Entity entity = event.getEntity();

			/*
			 * if on the server then apply modifications to the entity
			 */
			if (WorldInfo.isServerSide(event.getEntity().level())) {
				if (EnemyEchelonsApi.isValidEntity(entity)) {
					SMB.LOGGER.debug("entity joining world -> {} : {}", entity.getName().getString(), entity.getId());
					Mob mob = (Mob) entity;
					EnemyEchelonsApi.apply(mob);
				}
			}
		}
	}
}
