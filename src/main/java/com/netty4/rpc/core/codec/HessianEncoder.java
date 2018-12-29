package com.netty4.rpc.core.codec;

import com.caucho.hessian.io.Hessian2Output;
import com.netty4.rpc.core.protocol.RpcBaseModel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.io.Serializable;

/**
 * @auther zhihui.kzh
 * @create 10/9/1710:01
 */
@Sharable
public class HessianEncoder extends MessageToByteEncoder<RpcBaseModel> {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcBaseModel msg, ByteBuf out) throws Exception {
        int startIdx = out.writerIndex();

        ByteBufOutputStream bout = new ByteBufOutputStream(out);
        Hessian2Output oout = null;
        try {
            bout.write(LENGTH_PLACEHOLDER);

            oout = new Hessian2Output(bout);

            oout.writeObject(msg);
            oout.flush();
        } finally {
            try {
                if (oout != null) {
                    oout.close();
                } else {
                    bout.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        int endIdx = out.writerIndex();

        out.setInt(startIdx, endIdx - startIdx - 4);
    }
}
