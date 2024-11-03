package IO;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SimpleDecompressorInputStream extends InputStream {
    private InputStream in;

    public SimpleDecompressorInputStream(InputStream i){
        this.in=i;
    }

    public int Decompress(byte [] b) throws IOException {
        byte[] arr = in.readAllBytes();
        List<Byte> ans= new ArrayList<>();
        for(int t=0; t<24; t++){
            ans.add(arr[t]);
        }
        for(int i=24;i<arr.length;i++){
            if(i%2==0) {
                for (int j = 0; j < arr[i]+128; j++) {
                    ans.add((byte) 0);
                }
            }
            else{
                for (int j = 0; j < arr[i]+128; j++) {
                    ans.add((byte) 1);
                }
            }

        }
        for(int index=0;index<ans.size();index++){
            b[index]=ans.get(index);
        }
        return 0;
    }


    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b) throws IOException {
        Decompress(b);
        return 0;
    }
}

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