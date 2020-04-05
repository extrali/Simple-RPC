package com.netty.rpc.client;

import com.netty.rpc.protocol.RPCRequest;
import com.netty.rpc.protocol.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class RPCClientHandler extends SimpleChannelInboundHandler<RPCResponse> implements Callable {
    private static Logger logger= LoggerFactory.getLogger(RPCClientHandler.class);

    private ChannelHandlerContext context;

    private RPCRequest rpcRequest;

    private Object result;

    public void setRpcRequest(RPCRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context=ctx;
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, RPCResponse response) throws Exception {
       result=response.getResult();
       notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("RPCClientHandler exception",cause);
        ctx.close();
    }

    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(rpcRequest);
        wait();
        return result;
    }
}
