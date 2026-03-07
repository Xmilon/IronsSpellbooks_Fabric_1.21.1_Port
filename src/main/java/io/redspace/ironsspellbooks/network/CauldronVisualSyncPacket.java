package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronTile;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CauldronVisualSyncPacket implements CustomPacketPayload {
    public static final Type<CauldronVisualSyncPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "cauldron_visual_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CauldronVisualSyncPacket> STREAM_CODEC = CustomPacketPayload.codec(CauldronVisualSyncPacket::write, CauldronVisualSyncPacket::new);

    private final BlockPos pos;
    private final CompoundTag tag;

    public CauldronVisualSyncPacket(BlockPos pos, CompoundTag tag) {
        this.pos = pos;
        this.tag = tag == null ? new CompoundTag() : tag.copy();
    }

    public CauldronVisualSyncPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        CompoundTag readTag = buf.readNbt();
        this.tag = readTag == null ? new CompoundTag() : readTag;
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeNbt(tag);
    }

    public static void handle(CauldronVisualSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var mc = Minecraft.getInstance();
            if (mc.level == null) {
                return;
            }
            if (mc.level.getBlockEntity(packet.pos) instanceof AlchemistCauldronTile tile) {
                tile.handleUpdateTag(packet.tag, mc.level.registryAccess());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

