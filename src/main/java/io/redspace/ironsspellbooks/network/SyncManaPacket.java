package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncManaPacket implements CustomPacketPayload {
    private float playerMana = 0;
    private int playerMaxMana = -1;
    private MagicData playerMagicData = null;
    public static final CustomPacketPayload.Type<SyncManaPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "sync_mana"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncManaPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncManaPacket::write, SyncManaPacket::new);

    public SyncManaPacket(MagicData playerMagicData, LivingEntity livingEntity) {
        //Server side only
        this.playerMagicData = playerMagicData;
        this.playerMaxMana = (int) AttributeRegistry.getMaxManaWithFallback(livingEntity);
    }

    public SyncManaPacket(FriendlyByteBuf buf) {
        playerMana = buf.readFloat();
        playerMaxMana = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(playerMagicData.getMana());
        buf.writeInt(playerMaxMana);
    }

    public static void handle(SyncManaPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientMagicData.setMana(packet.playerMana);
            ClientMagicData.setSyncedMaxMana(packet.playerMaxMana);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
