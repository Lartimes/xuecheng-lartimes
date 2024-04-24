package com.lartimes.media;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/4/22 19:25
 */
public class MinIoUploadTest {

    static MinioClient minioClient = MinioClient.builder().endpoint("http://localhost:9000").credentials("minioadmin", "minioadmin").build();

    @Test
    public void spliceUpload(){
        try {
            File file = new File("F:\\chunkPath");
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                UploadObjectArgs testbucket = UploadObjectArgs.builder().bucket("testbucket")
                        .object("chunk/" + i)
                        .filename(files[i].getAbsolutePath())
                        .build();
                minioClient.uploadObject(testbucket);
                System.out.println("上传成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }
    }

    @Test
    public void minIoMerge() throws  Exception{
        long length = new File("F:\\chunkPath").listFiles().length;
        System.out.println(length);
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i)
                .limit(length)
                .map(i -> ComposeSource.builder()
                        .bucket("testbucket")
                        .object("chunk/".concat(Integer.toString(i)))
                        .build())
                .collect(Collectors.toList());
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder().bucket("testbucket").object("merge01.mp4").sources(sources).build();
        minioClient.composeObject(composeObjectArgs);
    }

    @Test
    public void deleteSplice(){
        //合并分块完成将分块文件清除
        long length = new File("F:\\chunkPath").listFiles().length;
        List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                .limit(length)
                .map(i -> new DeleteObject("chunk/".concat(Integer.toString(i))))
                .collect(Collectors.toList());
        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket("testbucket").objects(deleteObjects).build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        results.forEach(r->{
            DeleteError deleteError = null;
            try {
                deleteError = r.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
