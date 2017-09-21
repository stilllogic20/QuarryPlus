package com.yogpc.qp.packet.enchantment;

import java.io.IOException;

import com.yogpc.qp.BlockData;
import com.yogpc.qp.packet.IMessage;
import com.yogpc.qp.tile.TileBasic;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * To server only.
 */
public class EnchantmentMessage implements IMessage {

    Type type;
    BlockPos pos;
    int dim;
    Enchantment enchantment;
    BlockData data;

    public static EnchantmentMessage create(TileBasic tile, Type type, Enchantment enchantment, BlockData data) {
        EnchantmentMessage message = new EnchantmentMessage();
        message.pos = tile.getPos();
        message.dim = tile.getWorld().provider.getDimension();
        message.type = type;
        message.enchantment = enchantment;
        message.data = data;
        return message;
    }

    @Override
    public void fromBytes(PacketBuffer buffer) throws IOException {
        pos = buffer.readBlockPos();
        type = buffer.readEnumValue(Type.class);
        enchantment = Enchantment.getEnchantmentByLocation(buffer.readString(Short.MAX_VALUE));
        data = BlockData.of(buffer.readCompoundTag());
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void toBytes(PacketBuffer buffer) {
        buffer.writeBlockPos(pos).writeEnumValue(type).writeString(enchantment.getRegistryName().toString()).writeCompoundTag(data.toNBT());
    }

    @Override
    public IMessage onRecieve(IMessage message, MessageContext ctx) {
        World world = ctx.getServerHandler().playerEntity.world;
        if (world.provider.getDimension() == dim) {
            TileBasic tile = (TileBasic) world.getTileEntity(pos);
            assert tile != null;
            if (type == Type.Toggle) {
                if (enchantment == Enchantments.FORTUNE) {
                    tile.fortuneInclude = !tile.fortuneInclude;
                } else if (enchantment == Enchantments.SILK_TOUCH) {
                    tile.silktouchInclude = !tile.silktouchInclude;
                }
            } else if (type == Type.Remove) {
                if (enchantment == Enchantments.FORTUNE)
                    tile.fortuneList.remove(data);
                else if (enchantment == Enchantments.SILK_TOUCH)
                    tile.silktouchList.remove(data);
            }
        }
        return null;
    }

    public enum Type {Toggle, Remove}
}