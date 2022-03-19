package loki.asm.read.clazz;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * @author zhengquan
 */
public class ReadClassFileByte {

    public static void main(String[] args) throws IOException {
        String relative = "loki/asm/read/clazz/HelloWorld.class";
        URL resource = ReadClassFileByte.class.getClassLoader().getResource(relative);
        System.out.println(resource);
        byte[] bytes = Files.readAllBytes(new File(resource.getPath()).toPath());

        System.out.println(Arrays.toString(bytes));
    }

}
