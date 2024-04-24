package com.lartimes.media;

import io.minio.*;
import org.junit.jupiter.api.Test;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/4/18 15:07
 */
public class MinIoTest {
    static MinioClient minioClient = MinioClient.builder().endpoint("http://localhost:9000").credentials("minioadmin", "minioadmin").build();

    @Test
    public void delete(){
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket("testbucket")
                            .object("001/test001.mp4")
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void test() {
        try {

            UploadObjectArgs testbucket = UploadObjectArgs.builder().bucket("testbucket")
//                    .object("test001.mp4")
                    .object("001/test001.mp4")//添加子目录
                    .filename("F:\\Download\\M3U8在线下载工具.mp4").contentType("video/mp4")//默认根据扩展名确定文件内容类型，也可以指定
                    .build();
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket("testbucket").build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("testbucket").build());
            }
            minioClient.uploadObject(testbucket);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }

    }
}
