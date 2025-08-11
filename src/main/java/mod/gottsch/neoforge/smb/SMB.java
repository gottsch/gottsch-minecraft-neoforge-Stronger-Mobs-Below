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
package mod.gottsch.neoforge.smb;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import mod.gottsch.neoforge.eechelons.api.EnemyEchelonsApi;
import mod.gottsch.neoforge.eechelons.core.config.EchelonConfigsHolder;
import mod.gottsch.neoforge.eechelons.core.config.NameConfigsHolder;
import mod.gottsch.neoforge.eechelons.core.registry.DifficultyNameRegistry;
import mod.gottsch.neoforge.eechelons.core.registry.DifficultyNameRegistryEntry;
import mod.gottsch.neoforge.smb.core.config.SMBConfig;
import mod.gottsch.neoforge.eechelons.core.config.Config;
import mod.gottsch.neoforge.smb.core.setup.CommonSetup;
import mod.gottsch.neoforge.smb.core.setup.Registration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Many thanks go out to TheIllusiveC4 as the EchelonConfig 
 * loading code was derived from Champions.
 * @see <a href="https://github.com/TheIllusiveC4/Champions">Champions</a>
 *
 * @author Mark Gottschling on Jun 24, 2025
 *
 */
@Mod(SMB.MOD_ID)
public class SMB {
	public static final Logger LOGGER = LogManager.getLogger(SMB.MOD_ID);

	public static final String MOD_ID = "strongermobsbelow";

	private static final String MOD_CONFIG_VERSION = "1.21.1_v1";
	private static final String MOD_SUBFOLDER = "stronger_mobs_below";

	private static int configsLoaded = 0;

	/**
	 *
	 */
	public SMB(IEventBus eventBus, ModContainer modContainer) {
		// register the deferred registries
		Registration.init();
		// register the server config
		modContainer.registerConfig(ModConfig.Type.CLIENT, SMBConfig.CLIENT_SPEC, getConfigSubfolder("smb-client.toml").toString());
		modContainer.registerConfig(ModConfig.Type.COMMON, SMBConfig.COMMON_SPEC, getConfigSubfolder("smb-common.toml").toString());
		modContainer.registerConfig(ModConfig.Type.SERVER, SMBConfig.SERVER_SPEC, getConfigSubfolder("smb-server.toml").toString());

		// create the default config
		copyToServerDefaultConfig(modContainer, SMB.class, Config.ECHELONS_SPEC,
				getConfigFilename(MOD_CONFIG_VERSION));
		copyToServerDefaultConfig(modContainer, SMB.class, Config.ECHELONS_SPEC,
				getCustomConfigFilename(MOD_CONFIG_VERSION));
		copyToConfig(modContainer, SMB.class, Config.DIFFICULTY_SPEC,
				getDifficultyNamingConfigFilename(MOD_CONFIG_VERSION));

		// register 'ModSetup::init' to be called at mod setup time (server and client)
		eventBus.addListener(CommonSetup::init);
		eventBus.addListener(this::onLoadConfig);

		// TODO this will need to be moved to a Client only class - this will be remove from the server only mod version
		// register 'ClientSetup::init' to be called at mod setup time (client only)
//		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(ClientSetup::init));
	}


	// NOTE can't move to GottschCore
	public void copyToServerDefaultConfig(ModContainer modContainer, Class<?> sourceClass, ModConfigSpec spec, String resourceFilename) {

		modContainer.registerConfig(ModConfig.Type.SERVER, spec, getConfigSubfolder(resourceFilename).toString());

		// NOTE cannot have FMLPath.GAMEDIR.get() in GottschCore for some reason.
		File defaults = new File(FMLPaths.GAMEDIR.get() + "/defaultconfigs/" + getConfigSubfolder(resourceFilename));

		if (!defaults.exists()) {
			try {
				FileUtils.copyInputStreamToFile(
						Objects.requireNonNull(sourceClass.getClassLoader().getResourceAsStream(resourceFilename)),
						defaults);
			} catch (IOException e) {
				SMB.LOGGER.error("error copying to default config -> {}", resourceFilename);
			}
		}
	}

	public void copyToConfig(ModContainer modContainer, Class<?> sourceClass, ModConfigSpec spec, String resourceFilename) {
		modContainer.registerConfig(ModConfig.Type.COMMON, spec, getConfigSubfolder(resourceFilename).toString());

		File defaults = FMLPaths.CONFIGDIR.get().resolve(getConfigSubfolder(resourceFilename)).toFile();

		if (!defaults.exists()) {
			try {
				FileUtils.copyInputStreamToFile(
						Objects.requireNonNull(sourceClass.getClassLoader().getResourceAsStream(resourceFilename)),
						defaults);
            } catch (IOException e) {
				LOGGER.error("Error creating common config for " + resourceFilename);
			}
		}
	}

	/**
	 * On a config event.
	 * @param event
	 */
	private void onLoadConfig(final ModConfigEvent.Loading event) {
		if (event.getConfig().getModId().equals(MOD_ID)) {
			if (event.getConfig().getType() == ModConfig.Type.COMMON) {
				IConfigSpec spec = event.getConfig().getSpec();

				// TODO remove spec check, but use isNamingLoaded flag instead.
				if (spec == Config.DIFFICULTY_SPEC) {
					// get the toml config data
					CommentedConfig commentedConfig = event.getConfig().getLoadedConfig().config();
					List<NameConfigsHolder.NameConfig> configs = Config.transformNameConfigs(commentedConfig);
					List<DifficultyNameRegistryEntry> entries = configs.stream().map(DifficultyNameRegistryEntry::new).toList();
					DifficultyNameRegistry.register(entries);

					// TODO enable to load multiple naming configs
				}
			}

			if (event.getConfig().getType() == ModConfig.Type.SERVER) {
				IConfigSpec spec = event.getConfig().getSpec();
				// get the toml config data
				CommentedConfig commentedConfig = event.getConfig().getLoadedConfig().config();

				if (spec == Config.ECHELONS_SPEC) {
					// clear the EchelonManager.
					// NOTE only SMB should do this,
					// all other mods should only add to the manager.
//					if (configsLoaded == 0) {
//						EchelonManager.REGISTRY.clear();
//					}

					// TODO all echelon configs should be converted to objects first, then sorted by load order, then register()/build()

					configsLoaded++;
					// transform/copy the toml into the config
					List<EchelonConfigsHolder.Config> configs = Config.transformEchelonConfigs(commentedConfig);

					// TODO make this an API call
					// TODO review loading of config file. ALL configs need to be registered separately
					// and the mobs need to be updated

					// pass the EchelonConfigs to the build
//					EchelonManager.REGISTRY.register(configs);
					EnemyEchelonsApi.register(configs);

					// TODO don't like this way of controlling when to load configs.
					if (configsLoaded == 2) {
						loadAdditionalConfigs(event);
					}
				}
			}
		}
	}

	private void loadAdditionalConfigs(final ModConfigEvent event) {
		Path folder = event.getConfig().getFullPath().getParent();

		// Get all files recursively
		Collection<File> files = FileUtils.listFiles(folder.toFile(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		files.stream()
				.filter(File::isFile) // filter out directories, keep only files - this might be redundant with the FileUtils.
				.filter(file -> file.getName().endsWith(".toml")) // Filter by extension (.txt in this example)
				.filter(f -> !f.getName().equals(getConfigFilename(MOD_CONFIG_VERSION))
						&& !f.getName().equals(getCustomConfigFilename(MOD_CONFIG_VERSION)))
				.forEach(f -> {
					System.out.println(f.getAbsolutePath());
					// create a CommentedFileConfig instance
					CommentedFileConfig configData = CommentedFileConfig.builder(f)
							.sync() // Automatically synchronize changes to file
							.autosave() // Automatically save changes
							.build();
					configData.load(); // Load the file content

					EchelonConfigsHolder holder = new ObjectConverter().toObject(configData, EchelonConfigsHolder::new);
					// build/register config file
//					EchelonManager.REGISTRY.register(holder.configs);
					EnemyEchelonsApi.register(holder.configs);
				});
	}

	private String getConfigFilename(String version) {
		return "smb_difficulty_config_" + version + ".toml";
	}

	private String getCustomConfigFilename(String version) {
		return "smb_custom_difficulty_config_" + version + ".toml";
	}

	private String getDifficultyNamingConfigFilename(String version) {
		return "smb_naming_config_" + version + ".toml";
	}

	private Path getConfigSubfolder(String filename) {
		return Paths.get(MOD_SUBFOLDER).resolve(filename);
	}

}
