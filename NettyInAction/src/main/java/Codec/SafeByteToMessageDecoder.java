package Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

public class SafeByteToMessageDecoder extends ByteToMessageDecoder {

    private static final int Max_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int readable = in.readableBytes();
        if(readable > Max_FRAME_SIZE) {
            in.skipBytes(readable);
            throw new TooLongFrameException("Frame too big!");
        }
    }
}
