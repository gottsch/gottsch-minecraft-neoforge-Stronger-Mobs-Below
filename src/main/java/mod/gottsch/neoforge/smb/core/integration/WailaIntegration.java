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
package mod.gottsch.neoforge.smb.core.integration;


import mod.gottsch.neoforge.smb.core.config.SMBConfig;
import net.neoforged.fml.ModList;

/**
 * 
 * @author Mark Gottschling on Aug 1, 2022
 *
 */
public class WailaIntegration {
	private static boolean jadeLoaded = false;
	private static boolean wthitLoaded = false;
	
	public static void init() {
		ModList modList = ModList.get();

		if (modList.isLoaded("jade")) {
			jadeLoaded = true;
		}
		else if (modList.isLoaded("wthit")) {
			wthitLoaded = true;
		}
	}

	public static boolean isEnabled() {
		return SMBConfig.CLIENT.enableWailaIntegration.get()
				&& (jadeLoaded || wthitLoaded);
	}
	
	public static boolean isJadeLoaded() {
		return jadeLoaded;
	}

	public static boolean isWthitLoaded() {
		return wthitLoaded;
	}

}
