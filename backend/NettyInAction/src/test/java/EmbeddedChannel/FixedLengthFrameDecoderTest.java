package EmbeddedChannel;

import Codec.FixedLengthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(
                new FixedLengthFrameDecoder(3)
        );

        assertTrue(channel.writeInbound(input.retain()));

        assertTrue(channel.finish());

        ByteBuf read = (ByteBuf)channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        // release buf
        read.release();
        
    }


}
