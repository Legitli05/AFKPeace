package amerebagatelle.github.io.afkpeace.server;

import amerebagatelle.github.io.afkpeace.common.Packets;
import amerebagatelle.github.io.afkpeace.common.SettingsManager;
import amerebagatelle.github.io.afkpeace.mixin.server.CustomPayloadC2SPacketFake;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ServerNetworkHandler {
    public static final ServerNetworkHandler INSTANCE = new ServerNetworkHandler();

    public void processPacket(CustomPayloadC2SPacketFake packet, MinecraftServer server) {
        Identifier channel = packet.getChannel();
        PacketByteBuf buf = packet.getData();
        if (channel.equals(Packets.AFKPEACE_HELLO)) {
            onHello(buf, server);
        }
    }

    private void onHello(PacketByteBuf buf, MinecraftServer server) {
        ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(buf.readUuid());
        if (playerEntity != null && SettingsManager.loadBooleanSetting("disableAFKPeace")) {
            playerEntity.networkHandler.connection.send(new CustomPayloadS2CPacket(Packets.AFKPEACE_DISABLE, new PacketByteBuf(Unpooled.buffer())));
        }
    }
}