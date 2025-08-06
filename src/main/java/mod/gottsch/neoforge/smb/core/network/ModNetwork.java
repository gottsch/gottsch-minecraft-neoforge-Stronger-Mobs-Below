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
        final var registrar = event.registrar(SMB.MOD_ID);

        registrar.playToClient(
                // use the static Type object for registration
                DifficultyMessageToClient.TYPE,
                // the StreamCodec
                DifficultyMessageToClient.STREAM_CODEC,
                // the handler
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        // This code runs on the client thread.
                        System.out.println("Received entity id: " + payload.entityId());
                        // TODO finish
                    });
                }
        );

        registrar.playToServer(
                DifficultyRequestToServer.TYPE,
                DifficultyRequestToServer.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        // this code runs on the server thread.
                        System.out.println("requested entity id: " + payload.entityId());
                        DifficultyRequestToServer.handler(payload, context);
                    });
                }
        );
    }
}
