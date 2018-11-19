package com.netty4.rpc.core.codec;

import com.caucho.hessian.io.Hessian2Input;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.io.StreamCorruptedException;

/**
 * @auther zhihui.kzh
 * @create 10/9/1710:01
 */
public class HessianDecoder  extends LengthFieldBasedFrameDecoder {

    /**
     * Creates a new decoder whose maximum object size is {@code 1048576}
     * bytes.  If the size of the received object is greater than
     * {@code 1048576} bytes, a {@link StreamCorruptedException} will be
     * raised.
     *
     */
    public HessianDecoder() {
        this(1048576);
    }

    /**
     * Creates a new decoder with the specified maximum object size.
     *
     * @param maxObjectSize  the maximum byte length of the serialized object.
     *                       if the length of the received object is greater
     *                       than this value, {@link StreamCorruptedException}
     *                       will be raised.
     */
    public HessianDecoder(int maxObjectSize) {
        super(maxObjectSize, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }


        Hessian2Input input = null;
        try {
            // stream closed in the finally
            input = new Hessian2Input(new ByteBufInputStream(frame, true));
            return input.readObject();

        } finally {
            try {
                ReferenceCountUtil.release(frame);
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }
}