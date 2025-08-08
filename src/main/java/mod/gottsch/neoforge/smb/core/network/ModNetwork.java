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

import mod.gottsch.neoforge.smb.SMB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

// TODO this should move back to EEchelonsAPI and the request and message
// TODO they should work on just server side since the client thread will never be called.

/**
 * @author by Mark Gottschling on 8/5/2025
 */
@EventBusSubscriber(modid = SMB.MOD_ID)
public class ModNetwork {

    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
//        final var registrar = event.registrar(SMB.MOD_ID);

//        registrar.playToClient(
//                // use the static Type object for registration
//                DifficultyMessageToClient.TYPE,
//                // the StreamCodec
//                DifficultyMessageToClient.STREAM_CODEC,
//                // the handler
//                (payload, context) -> {
//                    context.enqueueWork(() -> {
//                        // This code runs on the client thread.
//                        System.out.println("Received entity id: " + payload.entityId());
//                        DifficultyMessageToClient.handler(payload, context);
//                    });
//                }
//        );

//        registrar.playToServer(
//                DifficultyRequestToServer.TYPE,
//                DifficultyRequestToServer.STREAM_CODEC,
//                (payload, context) -> {
//                    context.enqueueWork(() -> {
//                        // this code runs on the server thread.
//                        System.out.println("requested entity id: " + payload.entityId());
//                        DifficultyRequestToServer.handler(payload, context);
//                    });
//                }
//        );
    }
}
