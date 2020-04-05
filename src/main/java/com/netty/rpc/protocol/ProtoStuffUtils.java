package com.netty.rpc.protocol;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProtoStuffUtils {
    //避免每次序列化都申请Buffer空间
    private static LinkedBuffer buffer=LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    private static Map<Class<?>, Schema<?>> schemaCache=new ConcurrentHashMap<>();

    public static <T> byte[] serialize(T  obj){
        Class<T> clazz= (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(clazz);
        byte[] data;
        try{
            data= ProtostuffIOUtil.toByteArray(obj,schema,buffer);
        }finally {
            buffer.clear();
        }
        return data;
    }

    public static <T> T deSerialize(byte[] data,Class<T> clazz){
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data,obj,schema);
        return obj;
    }

    private static <T> Schema<T> getSchema(Class<T> clazz){
        Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
        if(schema==null){
            schema= RuntimeSchema.getSchema(clazz);
            if(schema!=null){
                schemaCache.put(clazz,schema);
            }
        }
        return schema;
    }
}
