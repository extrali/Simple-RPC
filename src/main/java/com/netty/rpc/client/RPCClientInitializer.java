package com.netty.rpc.client;

import com.netty.rpc.protocol.RPCDecoder;
import com.netty.rpc.protocol.RPCEncoder;
import com.netty.rpc.protocol.RPCRequest;
import com.netty.rpc.protocol.RPCResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class RPCClientInitializer extends ChannelInitializer<SocketChannel> {

    private RPCClientHandler handler;
    public RPCClientInitializer(RPCClientHandler handler){
        this.handler=handler;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new RPCEncoder(RPCRequest.class));
        pipeline.addLast(new RPCDecoder(RPCResponse.class));
        pipeline.addLast(handler);
    }
}
