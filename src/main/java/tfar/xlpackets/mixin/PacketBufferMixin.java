package tfar.xlpackets.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(FriendlyByteBuf.class)
public abstract class PacketBufferMixin {
    @Shadow(remap = false)
    public abstract CompoundTag readNbt(NbtAccounter p_130082_);

    /**
     * @author RedstoneWizard08
     * @reason Raise packet buffer limit
     */
    @Nullable
    @Overwrite(remap = false)
    public CompoundTag readNbt() {
        return this.readNbt(new NbtAccounter(2_000_000_000L));
    }
}
