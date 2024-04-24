package com.lartimes.media;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/4/22 12:52
 */
public class BigFileTest {

    @Test
    public void TestBigFile() throws Exception {
        File file = new File("F:\\Download\\arrrr.mp4");
        String chunkPath = "F:/chunkPath/";
        File dir = new File(chunkPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        long length = file.length();
        long splice = 1024 * 1024 * 10;
        long spliceSize = (long) Math.ceil((double) length / splice);
        byte[] bytes = new byte[1024];
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            for (long i = 0; i < spliceSize-1; i++) {
                File tmp = new File(chunkPath + i);
                if (tmp.exists()) {
                    tmp.delete();
                }
                boolean newFile = tmp.createNewFile();

                if (newFile) {
                    //向分块文件中写数据
                    try (RandomAccessFile raf_write = new RandomAccessFile(tmp, "rw")) {
                        int len = 0;
                        while ((len = randomAccessFile.read(bytes)) != -1) {
                            raf_write.write(bytes, 0, len);
                            if(i!= spliceSize-2){
                                if (raf_write.length() >= splice) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Test
    public void mergetFile() throws Exception {
        File file = new File("F:\\Download\\arrrr(2).mp4");
        if (file.exists()) {
            file.delete();
        }
        boolean newFile = file.createNewFile();
        if (!newFile) return;
        String chunkPath = "F:/chunkPath/";
        File dir = new File(chunkPath);
        File[] files = Arrays.stream(Objects.requireNonNull(dir.listFiles())).sorted(new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                return Integer.parseInt(file.getName()) - Integer.parseInt(t1.getName());
            }
        }).toList().toArray(new File[0]);
        byte[] bytes = new byte[1024];
        int length = 0;
        try (RandomAccessFile rw = new RandomAccessFile(file, "rw")) {
            for (File splice : files) {
                try (RandomAccessFile tmpFile = new RandomAccessFile(splice, "r")) {
                    while ((length = tmpFile.read(bytes)) != -1) {
                        rw.write(bytes, 0, length);
                    }
                }

            }
        }

    }
}
