package io.egg.anticrypt;

import proguard.obfuscate.MappingProcessor;

public class ShittyMappingProcessor implements MappingProcessor {
    public static String saltClass;
    public static String writeMethod;

    public static String byteBufClass;
    public static String writeLongMethod;
    public static String writeByteArrayMethod;
    @Override
    public boolean processClassMapping(String className, String newClassName) {
        if (className.equals("net.minecraft.util.Crypt$SaltSignaturePair")) {
            saltClass = newClassName;
            return true;
        } else if (className.equals("net.minecraft.network.FriendlyByteBuf")) {
            byteBufClass = newClassName;
            return true;
        }
        return false;
    }

    @Override
    public void processFieldMapping(String className, String fieldType, String fieldName, String newClassName, String newFieldName) {

    }

    @Override
    public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber, String methodReturnType, String methodName, String methodArguments, String newClassName, int newFirstLineNumber, int newLastLineNumber, String newMethodName) {
        if (className.equals("net.minecraft.util.Crypt$SaltSignaturePair") && methodName.equals("write")) {
            writeMethod = newMethodName;
        } else if (className.equals("net.minecraft.network.FriendlyByteBuf")) {
            if (methodName.equals("writeLong")) {
                writeLongMethod = newMethodName;
            } else if (methodName.equals("writeByteArray")) {
                writeByteArrayMethod = newMethodName;
            }
        }
    }
    public static void debug() {

    }
}
