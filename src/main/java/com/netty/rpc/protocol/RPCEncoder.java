package com.netty.rpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RPCEncoder extends MessageToByteEncoder{

    private Class<?> genericClass;
    public RPCEncoder(Class<?> genericClass){
        this.genericClass=genericClass;
    }


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if(genericClass.isInstance(o)) {
            byte[] data = ProtoStuffUtils.serialize(o);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
