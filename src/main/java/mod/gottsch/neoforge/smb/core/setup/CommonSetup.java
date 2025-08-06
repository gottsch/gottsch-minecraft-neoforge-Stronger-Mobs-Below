/*
 * This file is part of  Stronger Mobs Below.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package mod.gottsch.neoforge.smb.core.setup;

import mod.gottsch.neoforge.smb.SMB;
import mod.gottsch.neoforge.smb.core.config.SMBConfig;
import mod.gottsch.neoforge.smb.core.network.ModNetwork;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * 
 * @author Mark Gottschling on Jul 24, 2022
 *
 */
@EventBusSubscriber(modid = SMB.MOD_ID)
public class CommonSetup {

	public static void init(FMLCommonSetupEvent event) {
		SMBConfig.instance.addRollingFileAppender(SMB.MOD_ID);
	}

}
