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
import mod.gottsch.neoforge.smb.SMB;
import net.neoforged.fml.common.EventBusSubscriber;
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

	public static final ModConfigSpec COMMON_SPEC;
	public static final CommonConfig COMMON;
	
	public static final ModConfigSpec CLIENT_SPEC;
	public static final ClientConfig CLIENT;

	public static SMBConfig instance = new SMBConfig();
	
	static {
		final Pair<ServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder()
				.configure(ServerConfig::new);
		SERVER_SPEC = specPair.getRight();
		SERVER = specPair.getLeft();
		
		final Pair<CommonConfig, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder()
				.configure(CommonConfig::new);
		COMMON_SPEC = commonSpecPair.getRight();
		COMMON = commonSpecPair.getLeft();

		final Pair<ClientConfig, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder()
				.configure(ClientConfig::new);
		CLIENT_SPEC = clientSpecPair.getRight();
		CLIENT = clientSpecPair.getLeft();
	}

	public static class CommonConfig {
		public final Logging logging;
		public CommonConfig(ModConfigSpec.Builder builder) {
			logging = new Logging(builder);
		}
	}
	
	public static class ClientConfig {
		public final ModConfigSpec.BooleanValue showHud;
		public final ModConfigSpec.BooleanValue showLevel0Hud;
		public final ModConfigSpec.IntValue hudXOffset;
		public final ModConfigSpec.IntValue hudYOffset;
		public final ModConfigSpec.BooleanValue useDarkHud;
		
		public final ModConfigSpec.BooleanValue enableWailaIntegration;
		public final ModConfigSpec.BooleanValue enableChampionsIntegration;

		public ClientConfig(ModConfigSpec.Builder builder) {
			builder.push("hud");

			// show the hud, but only if server.show=true. ie, if server hud=false, client cannot override and display hud.
		  showHud = builder
							.comment(" Enable HUD display.")
							.define("showHud", true);

		  showLevel0Hud = builder
				  .comment(" Enable HUD display when mob is at Level 0")
				  .define("showLevel0Hud", true);

				hudXOffset = builder
					.comment(" The HUD x-offset.")
					.defineInRange("hudXOffset", 0, -1000, 1000);

			hudYOffset = builder
					.comment(" The HUD y-offset.")
					.defineInRange("hudYOffset", 0, -1000, 1000);

			useDarkHud = builder
					.comment(" Use dark theme HUD.")
					.define("useDarkThemeHud", true);
			
			enableWailaIntegration =
					builder.comment(" Moves the Stronger Mobs Below HUD beside (to the left) of the WAILA HUD.",
							" This setting is ignored if hudXOffset or hudYOffset are set (not 0).")
					.define("enableWailaIntegration", true);

			enableChampionsIntegration =
					builder.comment(" Moves the Stronger Mobs Below HUD beside (to the left) of the Champions HUD.",
							" This setting will supercede enableWailaIntegration.",
							" This setting is ignored if hudXOffset or hudYOffset are set (not 0).")
					.define("enableChampionsIntegration", true);
			
			builder.pop();
		}
	}

	/**
	 * For server mod config options
	 *
	 */
	public static class ServerConfig {
		public final ModConfigSpec.BooleanValue showHud;
		public final ModConfigSpec.BooleanValue hudRangeEnabled;
		public final ModConfigSpec.IntValue hudRange;

		public ServerConfig(ModConfigSpec.Builder builder) {
			builder.push("general");
			builder.pop();

			builder.push("hud");

			// showHud remains in server config so server admin can determine if users are able to see the level or not.
			showHud = builder
					.comment(" Enable HUD display.")
					.define("showHud", true);

			hudRangeEnabled = builder
					.comment(" Enable custom HUD range.",
							" NOTE enabling is more computationally expensive on the client side.")
					.define("hudRangeEnabled", false);

			hudRange = builder
					.comment(" The distance that the HUD can be seen from (in blocks).",
							" Vanilla default = 3.")
					.defineInRange("hudRange", 3, 0, 100);

			builder.pop();
		}
	}

	@Override
	public String getLogsFolder() {
		return COMMON.logging.folder.get();
	}
	
	public void setLogsFolder(String folder) {
		COMMON.logging.folder.set(folder);
	}
	
	@Override
	public String getLoggingLevel() {
		return COMMON.logging.level.get();
	}
}
