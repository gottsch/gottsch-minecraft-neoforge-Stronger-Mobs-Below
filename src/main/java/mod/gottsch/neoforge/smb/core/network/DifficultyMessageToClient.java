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
package mod.gottsch.neoforge.smb.core.network;


import mod.gottsch.neoforge.eechelons.api.EnemyEchelonsApi;
import mod.gottsch.neoforge.smb.SMB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

/**
 *
 * @author Mark Gottschling on Jul 28, 2022
 *
 */
public record DifficultyMessageToClient(int entityId, int level, String name) implements CustomPacketPayload {

	// the static nested Type record that holds the ResourceLocation
	public static final Type<DifficultyMessageToClient> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(SMB.MOD_ID, "difficulty_to_client"));


	// the StreamCodec for this payload
	public static final StreamCodec<FriendlyByteBuf, DifficultyMessageToClient> STREAM_CODEC = StreamCodec.of(
			(buf, payload) -> {
				buf.writeInt(payload.entityId());
				buf.writeInt(payload.level());
				buf.writeUtf(Optional.ofNullable(payload.name()).orElse(""));
			},
			(buf) -> new DifficultyMessageToClient(buf.readInt(), buf.readInt(), buf.readUtf())
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handler(DifficultyMessageToClient message, IPayloadContext context) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level != null) {
			Entity entity = level.getEntity(message.entityId());
			if (entity != null && EnemyEchelonsApi.hasDifficulty(entity)) {
				EnemyEchelonsApi.setDifficulty(entity, message.level());
				EnemyEchelonsApi.setDifficultyName(entity, message.name());
			}
		}
	}

	@Override
	public String toString() {
		return "DifficultyMessageToClient [entityId=" + entityId + ", level=" + level + "]";
	}
}