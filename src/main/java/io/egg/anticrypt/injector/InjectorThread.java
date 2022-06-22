package io.egg.anticrypt.injector;

import io.egg.anticrypt.AgentMain;
import io.egg.anticrypt.ShittyMappingProcessor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;

import static org.objectweb.asm.Opcodes.ASM9;

public class InjectorThread extends Thread {

    @Override
    public void run() {
        System.out.println("[AntiCrypt] Looking for " + ShittyMappingProcessor.saltClass);
        while (true) {
            if (getThreadByName("ServerMain") == null) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                continue;
            }
            break;
        }
        // fuck you mojang for making me do this
        Thread mainThread = getThreadByName("ServerMain");
        ClassLoader t = mainThread.getContextClassLoader();
        Class target;
        try {
            target = t.loadClass(ShittyMappingProcessor.saltClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("[AntiCrypt] Found " + ShittyMappingProcessor.saltClass);
        // class has been loaded
        try {
            fuckShitUp(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fuckShitUp(Class target) throws ClassNotFoundException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String className = target.getName();
        String classAsPath = className.replace('.', '/') + ".class";
        InputStream stream = target.getClassLoader().getResourceAsStream(classAsPath);
        byte[] data = readStream(stream, true);
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(0);
        WriteRemover9000 modifier = new WriteRemover9000(cw, ShittyMappingProcessor.writeMethod);
        cr.accept(modifier, 0);
        ShittyHacks.writeStub(cw);
        ClassDefinition patched = new ClassDefinition(target, cw.toByteArray());

       /*
        Module base = HashMap.class.getModule();
        Module opened = this.getClass().getModule();
        HashMap<String, Set<Module>> opens = new HashMap<>();
        opens.put("java.lang", Set.of(opened));
        Set<Module> blank = Collections.emptySet();
        AgentMain.cheats.redefineModule(base, blank,  Collections.emptyMap(), opens, Collections.emptySet(), Collections.emptyMap());

        ClassLoader targetLoader = target.getClassLoader();
        Class loaderClass = ClassLoader.class;
        System.out.println(loaderClass.getModule().getName());
        Method illegal = loaderClass.getDeclaredMethod("addClass", Class.class);
        illegal.setAccessible(true);
        illegal.invoke(targetLoader, ShittyHacks.class);
        illegal.invoke(targetLoader, ShittyMappingProcessor.class);
        illegal.invoke(targetLoader, MappingProcessor.class);*/

        try {
            AgentMain.cheats.redefineClasses(patched);
        } catch (UnmodifiableClassException | java.lang.UnsupportedOperationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("[AntiCrypt] Class " + ShittyMappingProcessor.saltClass + " patched!");

    }

    public Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }

    static class WriteRemover9000 extends ClassVisitor {
        private String mName;

        public WriteRemover9000(
                ClassVisitor cv, String mName) {
            super(ASM9, cv);
            this.mName = mName;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name,
                                         String desc, String signature, String[] exceptions) {
            if (name.equals(mName) && desc.contains(ShittyMappingProcessor.byteBufClass)) {
                return null;
            }
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }
    }
    private static int computeBufferSize(InputStream inputStream) throws IOException {
        int expectedLength = inputStream.available();
        return expectedLength < 256 ? 4096 : Math.min(expectedLength, 1048576);
    }
    private static byte[] readStream(InputStream inputStream, boolean close) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        } else {
            int bufferSize = computeBufferSize(inputStream);

            byte[] var7;
            try {
                ByteArrayOutputStream outputStream;
                label142: {
                    outputStream = new ByteArrayOutputStream();

                    try {
                        byte[] data = new byte[bufferSize];

                        int bytesRead;
                        int readCount;
                        for(readCount = 0; (bytesRead = inputStream.read(data, 0, bufferSize)) != -1; ++readCount) {
                            outputStream.write(data, 0, bytesRead);
                        }

                        outputStream.flush();
                        if (readCount == 1) {
                            var7 = data;
                            break label142;
                        }

                        var7 = outputStream.toByteArray();
                    } catch (Throwable var13) {
                        try {
                            outputStream.close();
                        } catch (Throwable var12) {
                        }

                        throw var13;
                    }

                    outputStream.close();
                    return var7;
                }

                outputStream.close();
            } finally {
                if (close) {
                    inputStream.close();
                }

            }

            return var7;
        }
    }
}
