package tfar.xlpackets.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.CompressionDecoder;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.zip.Inflater;

@Mixin(CompressionDecoder.class)
public class PacketInflaterMixin {
	@Shadow(remap = false)
	private boolean validateDecompressed;

	@Shadow(remap = false)
	private int threshold;

	@Final
	@Shadow(remap = false)
	private Inflater inflater;

	/**
	 * @author RedstoneWizard08
	 * @reason Raise packet inflater limit to 2000000000
	 */
	@Overwrite(remap = false)
	protected void decode(ChannelHandlerContext p_129441_, ByteBuf p_129442_, List<Object> p_129443_) throws Exception {
		if (p_129442_.readableBytes() != 0) {
			FriendlyByteBuf friendlybytebuf = new FriendlyByteBuf(p_129442_);
			int i = friendlybytebuf.readVarInt();
			if (i == 0) {
				p_129443_.add(friendlybytebuf.readBytes(friendlybytebuf.readableBytes()));
			} else {
				if (this.validateDecompressed) {
					if (i < this.threshold) {
						throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.threshold);
					}

					if (i > 2000000000) {
						throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of 2000000000");
					}
				}

				byte[] abyte = new byte[friendlybytebuf.readableBytes()];
				friendlybytebuf.readBytes(abyte);
				this.inflater.setInput(abyte);
				byte[] abyte1 = new byte[i];
				this.inflater.inflate(abyte1);
				p_129443_.add(Unpooled.wrappedBuffer(abyte1));
				this.inflater.reset();
			}
		}
	}
}
