package com.chanlee.crpc.v3.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo工具类，提供静态方法进行序列化和反序列化
 */
public class KryoSerializer {
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo(); //为当前线程创建一个新的 Kryo 实例
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    public static byte[] serialize(Object obj){
        Kryo kryo = kryoThreadLocal.get();
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Output output = new Output(baos)) {
            kryo.writeClassAndObject(output, obj);
            output.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Kryo serialize error", e);
        }
    }

    public static Object deserialize(byte[] bytes) {
        Kryo kryo = kryoThreadLocal.get();
        try(ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Input input = new Input(bais)){
            return kryo.readClassAndObject(input);
        } catch (IOException e) {
            throw new RuntimeException("Kryo deserialize error", e);
        }
    }
}
