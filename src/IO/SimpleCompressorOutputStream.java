//package IO;
//
//import java.io.IOException;
//import java.io.OutputStream;
//
//public class SimpleCompressorOutputStream extends OutputStream {
//    private OutputStream out;
//
//    public SimpleCompressorOutputStream(OutputStream out) {
//        this.out = out;
//    }
//
//    @Override
//    public void write(int b) throws IOException {
//        throw new UnsupportedOperationException("Use write(byte[] b) for compression.");
//    }
//
//    @Override
//    public void write(byte[] b) throws IOException {
//        int count = 1;
//        byte currentByte = b[0];
//
//        for (int i = 1; i < b.length; i++) {
//            if (b[i] == currentByte) {
//                count++;
//            } else {
//                // Write the current byte and the count
//                out.write(currentByte);
//                out.write(count);
//                currentByte = b[i];
//                count = 1;
//            }
//        }
//
//        // Write the last byte and its count
//        out.write(currentByte);
//        out.write(count);
//        out.flush();
//    }
//}
package IO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SimpleCompressorOutputStream extends OutputStream {

    private OutputStream out;

    public SimpleCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        this.out.write(b);
    }

    /**
     * going through the array and compress the maze to bytes using a simple method to count the number of times each
     * sequence of 0 and 1 appears
     *
     * @param b the data.
     */
    @Override
    public void write(byte[] b) throws IOException {
        //the size of the maze, the start point and the Goal point will stay the same
        for (int i = 0; i < 24; i++) {
            write(b[i]);
        }
        int i = 24;
        //go through the rest of the array, checks the amount of 0 and 1 that appear in a row and write them to the stream
        int counterFor0 = 0;
        int counterFor1 = 0;
        List<Byte> ans = new ArrayList<>();
        //checks if the first byte is 1 to put number for the 0
        if (b[i] == 1) {
            ans.add((byte) -128);
        }
        while (i < b.length) {
            //if the sequence is 0
            if (b[i] == 0) {
                counterFor0++;
                //check if the last byte was 1:
                if (counterFor1 > 0) {
                    //write the sequence of 1 and reset it to 0
                    writeCounter(ans, counterFor1);
                    counterFor1 = 0;
                }
            }
            //if the sequence is 1
            else if (b[i] == 1) {
                counterFor1++;
                //check if the last byte was 0:
                if (counterFor0 > 0) {
                    //write the sequence of 0 and reset it to 0
                    writeCounter(ans, counterFor0);
                    counterFor0 = 0;
                }
            }
            i++;
        }
        //add the last counter to the list
        if (counterFor0 > 0) {
            writeCounter(ans, counterFor0);
        }
        if (counterFor1 > 0) {
            writeCounter(ans, counterFor1);
        }

        //convert the list to array
        byte[] output = new byte[ans.size()];
        for (int j = 0; j < output.length; j++) {
            output[j] = ans.get(j);
        }
        for (byte t : output) {
            write(t);
        }
    }

    //the function add the counters to the byte list
    //checks the amount of the sequence
    // if greater than 255, add the number then add 0 and then again add the number till it would be less than 255
    public void writeCounter(List<Byte> arr, int counter) {
        while (counter > 255) {
            arr.add((byte) (counter - 127));
            arr.add((byte) -128);
            counter = counter - 255;
        }
        counter = counter - 128;
        arr.add((byte) (counter));
    }
}