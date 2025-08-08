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
package mod.gottsch.neoforge.smb.core.config;

import mod.gottsch.neo.gottschcore.config.AbstractConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Mark Gottschling on Jul 25, 2022
 *
 */
public final class SMBConfig extends AbstractConfig {
	public static final String CATEGORY_DIV = "##############################";
	public static final String UNDERLINE_DIV = "------------------------------";

	public static final ModConfigSpec SERVER_SPEC;
	public static final ServerConfig SERVER;

	public static SMBConfig instance = new SMBConfig();

	static {
		final Pair<ServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder()
				.configure(ServerConfig::new);
		SERVER_SPEC = specPair.getRight();
		SERVER = specPair.getLeft();
	}

	/**
	 * For server mod config options
	 *
	 */
	public static class ServerConfig {
		public final Logging logging;

		public ServerConfig(ModConfigSpec.Builder builder) {
			builder.push("general");
			logging = new Logging(builder);
			builder.pop();
		}
	}

	@Override
	public String getLogsFolder() {
		return SERVER.logging.folder.get();
	}

	public void setLogsFolder(String folder) {
		SERVER.logging.folder.set(folder);
	}

	@Override
	public String getLoggingLevel() {
		return SERVER.logging.level.get();
	}
}
