package sandbox;

import java.io.File;
import java.util.Properties;
import java.util.stream.Stream;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * APNG disassemble then assemble with apngdis and apngasm by JNA.
 */
public class JnaMain {
    private static final String APNG_DISASSEMBLY = "apngdis";
    private static final String APNG_ASSEMBLY = "apngasm";

    public interface APngDis extends Library {
        APngDis INSTANCE = Native.load(APNG_DISASSEMBLY, APngDis.class);

        int main(int argc, String[] argv);
    }

    public interface APngAsm extends Library {
        APngAsm INSTANCE = Native.load(APNG_ASSEMBLY, APngAsm.class);

        int main(int argc, String[] argv);
    }

    public static void main(String[] args) {
        final Properties properties = System.getProperties();
        properties.setProperty("jna.library.path", "./bin");
        System.setProperties(properties);

        disAssemble();
        assemble();
    }

    private static void disAssemble() {
        final APngDis apngdis = APngDis.INSTANCE;
        final String[] commands = new String[] { APNG_DISASSEMBLY, "./sample/1.png" };
        final int exitCode = apngdis.main(commands.length, commands);
        System.out.println(exitCode);
    }

    private static void assemble() {
        new File("./output/output.png").delete();

        final APngAsm apngasm = APngAsm.INSTANCE;
        final String[] commands =
                new String[] { APNG_ASSEMBLY, "./output/output.png", "./sample/apngframe*.png" };
        final int exitCode = apngasm.main(commands.length, commands);
        System.out.println(exitCode);

        Stream.of(new File("./sample").listFiles())
              .filter(file -> file.getName().contains("apngframe"))
              .forEach(File::delete);
    }
}
