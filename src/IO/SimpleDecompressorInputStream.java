//package IO;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//public class SimpleDecompressorInputStream extends InputStream {
//    private InputStream in;
//
//    public SimpleDecompressorInputStream(InputStream in) {
//        this.in = in;
//    }
//
//    @Override
//    public int read() throws IOException {
//        throw new UnsupportedOperationException("Use read(byte[] b) for decompression.");
//    }
//
//    @Override
//    public int read(byte[] b) throws IOException {
//        int index = 0;
//        while (index < b.length) {
//            int byteValue = in.read();  // Read the byte (0 or 1)
//            int count = in.read();      // Read how many times this byte repeats
//
//            for (int i = 0; i < count && index < b.length; i++) {
//                b[index++] = (byte) byteValue;
//            }
//        }
//        return index;
//    }
//}
package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SimpleDecompressorInputStream extends InputStream {

    private InputStream in;

    public SimpleDecompressorInputStream(InputStream i) {
        this.in = i;
    }


    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b) throws IOException {
        //read all the bytes of the maze
        byte[] ans = in.readAllBytes();
        List<Byte> out = new ArrayList<>();
        //keep the size, start point and Goal point
        for (int i = 0; i < 24; i++) {
            out.add(ans[i]);
        }
        //go through the rest of the maze and decode it
        for (int i = 24; i < ans.length; i++) {
            //the number represent sequence of 0
            if (i % 2 == 0) {
                for (int j = 0; j < ans[i] + 128; j++) {
                    out.add((byte) 0);
                }
            }
            //the number represent sequence of 1
            else {
                for (int j = 0; j < ans[i] + 128; j++) {
                    out.add((byte) 1);
                }
            }
        }

        //convert it to array from list
        for (int index = 0; index < out.size(); index++) {
            b[index] = out.get(index);
        }

        return 0;
    }
}