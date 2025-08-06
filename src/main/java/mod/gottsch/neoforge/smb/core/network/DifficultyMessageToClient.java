/*
 * This file is part of  Enemy Echelons API.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 *
 * All rights reserved.
 *
 * Enemy Echelons API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Enemy Echelons API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Enemy Echelons API.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.neoforge.smb.core.network;


import mod.gottsch.neoforge.smb.SMB;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Jul 28, 2022
 *
 */
public record DifficultyMessageToClient(int entityId, int level, String name) implements CustomPacketPayload {

	// The static nested Type record that holds the ResourceLocation
	public static final Type<DifficultyMessageToClient> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(SMB.MOD_ID, "difficulty_to_client"));


	// The StreamCodec for this payload
	public static final StreamCodec<FriendlyByteBuf, DifficultyMessageToClient> STREAM_CODEC = StreamCodec.of(
			(buf, payload) -> {
				buf.writeInt(payload.entityId());
						buf.writeInt(payload.level());
						buf.writeUtf(payload.name());
			},
			(buf) -> new DifficultyMessageToClient(buf.readInt(), buf.readInt(), buf.readUtf())
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

//	private static void processMessage(Context ctx, DifficultyMessageToClient msg) {
//		ClientLevel world = Minecraft.getInstance().level;
//		if (world != null) {
//			Entity entity = world.getEntity(msg.entityId);
////			EEchelons.LOGGER.debug("handling client message to entity -> {} for level -> {}", entity.getName().getString(), msg.level);
//			entity.getCapability(ModCapabilities.DIFFICULTY_CAPABILITY).ifPresent(cap -> {
////				EEchelons.LOGGER.debug("setting the level on the client entity");
//				cap.setDifficulty(msg.level);
//				cap.setName(msg.name);
//			});
//		}
//	}
//
	@Override
	public String toString() {
		return "DifficultyMessageToClient [entityId=" + entityId + ", level=" + level + "]";
	}
}
