package com.netty.rpc.server;

import com.netty.rpc.protocol.RPCDecoder;
import com.netty.rpc.protocol.RPCEncoder;
import com.netty.rpc.protocol.RPCRequest;
import com.netty.rpc.protocol.RPCResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

public class RPCServerInitializer extends ChannelInitializer<SocketChannel> {
    private final Map<String,Object> handlerMap;

    public RPCServerInitializer(Map<String,Object> handlerMap){
        this.handlerMap=handlerMap;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new RPCDecoder(RPCRequest.class));
        pipeline.addLast(new RPCEncoder(RPCResponse.class));
        pipeline.addLast(new RPCHandler(handlerMap));
    }
}
