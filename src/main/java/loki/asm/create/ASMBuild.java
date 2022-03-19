package loki.asm.create;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author zhengquan
 */
public class ASMBuild implements Opcodes {



    public static byte[] build() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // jdk版本，访问修饰修饰(public class), 包路径/类名
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "simple/HelloWorld",
                null, "java/lang/Object", null);

        {// 生成构造方法
            MethodVisitor mvl = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mvl.visitCode();
            mvl.visitVarInsn(ALOAD, 0);
            mvl.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mvl.visitInsn(RETURN);
            mvl.visitMaxs(1, 1);
            mvl.visitEnd();
        }

        {// 重写 toString方法 ， 返回值返回 "This is a Hello World object."
            MethodVisitor mv2 = cw.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
            mv2.visitCode();
            mv2.visitLdcInsn("This is a Hello World object.");
            mv2.visitInsn(ARETURN);
            mv2.visitMaxs(1, 1);
            mv2.visitEnd();
        }

        cw.visitEnd();
        return cw.toByteArray();
    }

}
