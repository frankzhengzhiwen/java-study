package demo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Test_00 {


    public static void main(String[] args) throws IOException {
        test1();
    }


    public static void test1() throws IOException {
        RandomAccessFile fis = new RandomAccessFile("D:\\test3.ini", "rw");
        FileChannel fci = fis.getChannel();
        ByteBuffer bbf;
        MappedByteBuffer mbb = fci.map(FileChannel.MapMode.READ_WRITE, 0,
                fis.length());

        System.out.println(mbb.position());

        byte dst[] = new byte[(int) fis.length()];
        mbb.get(dst);
        System.out.println(mbb.position());

        System.out.println(new String(dst));


        mbb.position(0);
        mbb.put("haha".getBytes());

        fis.close();

    }
}