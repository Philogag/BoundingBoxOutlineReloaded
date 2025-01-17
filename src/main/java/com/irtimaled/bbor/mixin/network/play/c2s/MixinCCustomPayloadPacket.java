package com.irtimaled.bbor.mixin.network.play.c2s;

import com.irtimaled.bbor.common.interop.CommonInterop;
import com.irtimaled.bbor.common.messages.PayloadReader;
import com.irtimaled.bbor.common.messages.SubscribeToServer;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CustomPayloadC2SPacket.class)
public class MixinCCustomPayloadPacket {
    @Shadow
    private Identifier channel;

    @Redirect(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/listener/ServerPlayPacketListener;onCustomPayload(Lnet/minecraft/network/packet/c2s/play/CustomPayloadC2SPacket;)V"))
    private void processPacket(ServerPlayPacketListener netHandlerPlayServer, CustomPayloadC2SPacket packet) {
        String channelName = channel.toString();
        if (channelName.startsWith("bbor:")) {
            PayloadReader reader = new PayloadReader(packet.getData());
            switch (channelName) {
                case SubscribeToServer.NAME -> {
                    CommonInterop.playerSubscribed(((ServerPlayNetworkHandler) netHandlerPlayServer).player);
                }
            }
        } else {
            netHandlerPlayServer.onCustomPayload(packet);
        }
    }
}
