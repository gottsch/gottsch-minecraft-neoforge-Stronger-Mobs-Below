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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 
 * @author Mark Gottschling on Jul 30, 2022
 *
 */
public record DifficultyRequestToServer(int entityId, String registryName, String location) implements CustomPacketPayload {

	// The static nested Type record that holds the ResourceLocation
	public static final CustomPacketPayload.Type<DifficultyRequestToServer> TYPE =
			new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SMB.MOD_ID, "difficulty_to_server"));


	// The StreamCodec for this payload
	public static final StreamCodec<FriendlyByteBuf, DifficultyRequestToServer> STREAM_CODEC = StreamCodec.of(
			(buf, payload) -> {
				buf.writeInt(payload.entityId());
				buf.writeUtf(payload.registryName());
				buf.writeUtf(payload.location());
			},
			(buf) -> new DifficultyRequestToServer(buf.readInt(), buf.readUtf(), buf.readUtf())
	);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handler(DifficultyRequestToServer request, IPayloadContext context) {
		Level level = context.player().level();
		if (level != null) {
			Entity entity = level.getEntity(request.entityId());
			if (entity != null) {
				if (EnemyEchelonsApi.hasDifficulty(entity)) {
					// send the level back to the client
					DifficultyMessageToClient message = new DifficultyMessageToClient(entity.getId(), EnemyEchelonsApi.getDifficulty(entity), EnemyEchelonsApi.getDifficultyName(entity).orElse(null));
					PacketDistributor.sendToPlayersTrackingEntity(entity, message);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "DifficultyRequestToServer [entityId=" + entityId + ", registryName=" + registryName + ", location="
				+ location + "]";
	}

}
