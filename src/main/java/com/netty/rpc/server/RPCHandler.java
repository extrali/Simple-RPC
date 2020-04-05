package com.netty.rpc.server;

import com.netty.rpc.protocol.RPCRequest;
import com.netty.rpc.protocol.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RPCHandler extends SimpleChannelInboundHandler<RPCRequest> {
    private static Logger logger= LoggerFactory.getLogger(RPCHandler.class);

    private final Map<String,Object> handlerMap;
    
    public RPCHandler(Map<String,Object> handlerMap){
        this.handlerMap=handlerMap;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest) throws Exception {
        RPCServer.submit(new Runnable() {
            @Override
            public void run() {
                String requestID = rpcRequest.getRequestID();
                RPCResponse response=new RPCResponse();
                response.setResponseID(requestID);
                handle(rpcRequest,response);
                channelHandlerContext.writeAndFlush(response);
            }
        });
        /*
        String requestID = rpcRequest.getRequestID();
        RPCResponse response=new RPCResponse();
        response.setResponseID(requestID);
        handle(rpcRequest,response);
        System.out.println(response);
        channelHandlerContext.writeAndFlush(response);*/
    }

    private void handle(RPCRequest rpcRequest,RPCResponse response){
        try {
            String className = rpcRequest.getClassName();
            Object obj = handlerMap.get(className);
            String methodName = rpcRequest.getMethodName();
            Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
            Object[] parameters = rpcRequest.getParameters();
            Method method = obj.getClass().getMethod(methodName, parameterTypes);
            Object res = method.invoke(obj,parameters);
            response.setResult(res);
        } catch (NoSuchMethodException e) {
            logger.error("No method match",e);
            response.setError(e.toString());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException",e);
            response.setError(e.toString());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            logger.error("InvocationTargetException",e);
            response.setError(e.toString());
            e.printStackTrace();
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("server caught exception",cause);
        ctx.close();
    }
}
