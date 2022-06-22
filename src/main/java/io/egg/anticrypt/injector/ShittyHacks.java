package io.egg.anticrypt.injector;

import io.egg.anticrypt.ShittyMappingProcessor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

public class ShittyHacks {
    public static void wtf(Object byteBuf, Object saltSigPair) {
        Method writeLong;
        Method writeByteArray;
        try {
            writeLong = byteBuf.getClass().getDeclaredMethod(ShittyMappingProcessor.writeLongMethod, Long.class);
            writeByteArray = byteBuf.getClass().getDeclaredMethod(ShittyMappingProcessor.writeByteArrayMethod, byte[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        try {
            writeLong.invoke(byteBuf, 0L);
            writeByteArray.invoke(byteBuf, ((Object) new byte[0]));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }
    /*public static void writeStub(ClassWriter w) {
        MethodVisitor methodVisitor;
        methodVisitor = w.visitMethod(ACC_PUBLIC | ACC_STATIC, ShittyMappingProcessor.writeMethod, "(L" + ShittyMappingProcessor.byteBufClass + ";L" + ShittyMappingProcessor.saltClass  +";)V", null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "io/egg/fuck/injector/ShittyHacks", "wtf", "(Ljava/lang/Object;Ljava/lang/Object;)V", false);
        Label label1 = new Label();
        methodVisitor.visitLabel(label1);
        methodVisitor.visitInsn(RETURN);
        Label label2 = new Label();
        methodVisitor.visitLabel(label2);
        methodVisitor.visitLocalVariable("$$0", "L" + ShittyMappingProcessor.byteBufClass +";", null, label0, label2, 0);
        methodVisitor.visitLocalVariable("$$1", "L" + ShittyMappingProcessor.saltClass +";", null, label0, label2, 1);
        methodVisitor.visitMaxs(2, 2);
        methodVisitor.visitEnd();
    }*/
    public static void writeStub(ClassWriter w) {
        String bb = ShittyMappingProcessor.byteBufClass.replace('.', '/');
        String s = ShittyMappingProcessor.saltClass;
        MethodVisitor methodVisitor;
        methodVisitor = w.visitMethod(ACC_PUBLIC | ACC_STATIC, ShittyMappingProcessor.writeMethod, "(L" + ShittyMappingProcessor.byteBufClass + ";L" + ShittyMappingProcessor.saltClass  +";)V", null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitLineNumber(226, label0);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitInsn(LCONST_0);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, bb, ShittyMappingProcessor.writeLongMethod, "(J)Lio/netty/buffer/ByteBuf;", false);
        methodVisitor.visitInsn(POP);
        Label label1 = new Label();
        methodVisitor.visitLabel(label1);
        methodVisitor.visitLineNumber(227, label1);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitInsn(ICONST_0);
        methodVisitor.visitIntInsn(NEWARRAY, T_BYTE);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, bb, ShittyMappingProcessor.writeByteArrayMethod, "([B)L" + bb + ";", false);
        methodVisitor.visitInsn(POP);
        Label label2 = new Label();
        methodVisitor.visitLabel(label2);
        methodVisitor.visitLineNumber(228, label2);
        methodVisitor.visitInsn(RETURN);
        Label label3 = new Label();
        methodVisitor.visitLabel(label3);
        methodVisitor.visitLocalVariable("$$0", "L" + bb +";", null, label0, label3, 0);
        methodVisitor.visitLocalVariable("$$1", "L" + s + ";", null, label0, label3, 1);
        methodVisitor.visitMaxs(3, 2);
        methodVisitor.visitEnd();
    }
}
