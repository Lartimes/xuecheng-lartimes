package com.lartimes.media.controller;

import com.lartimes.content.model.RestResponse;
import com.lartimes.media.model.dto.UploadFileParamsDto;
import com.lartimes.media.service.MediaFilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/4/23 10:49
 */
@Tag(name = "BigFileController", description = "大文件处理controller")
@RestController
public class BigFileController {

    private final MediaFilesService mediaFilesService;


    public BigFileController(MediaFilesService mediaFilesService) {
        this.mediaFilesService = mediaFilesService;
    }

    @Operation(summary = "文件上传前检查文件")
    @PostMapping("/upload/checkfile")
    @Parameters({@Parameter(name = "fileMd5", description = "mediaFiles-ID")})
    public RestResponse<Boolean> checkfile(@RequestParam("fileMd5") String fileMd5) throws Exception {
        return mediaFilesService.checkExists(fileMd5);
    }


    @Operation(summary = "分块文件上传前的检测")
    @PostMapping("/upload/checkchunk")
    @Parameters({@Parameter(name = "fileMd5", description = "mediaFiles-ID"), @Parameter(name = "chunk", description = "分片下标")})
    public RestResponse<Boolean> checkChunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") int chunk) throws Exception {
        return mediaFilesService.checkChunk(fileMd5, chunk);
    }

    @Operation(summary = "上传分块文件")
    @PostMapping("/upload/uploadchunk")
    @Parameters({@Parameter(name = "fileMd5", description = "mediaFiles-ID"), @Parameter(name = "file", description = "上传的小分片文件"), @Parameter(name = "chunk", description = "分片下标")})
    public RestResponse<Boolean> uploadChunk(@RequestParam("file") MultipartFile file, @RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") int chunk) throws Exception {

        return mediaFilesService.uploadChunk(fileMd5, chunk, file.getBytes());
    }

    @Operation(summary = "合并文件")
    @PostMapping("/upload/mergechunks")
    @Parameters({@Parameter(name = "fileMd5", description = "mediaFiles-ID"),
            @Parameter(name = "fileName", description = "上传的文件名字"),
            @Parameter(name = "chunkTotal", description = "分片总数")})
    public RestResponse<Boolean> mergeChunks(
            @RequestParam("fileMd5") String fileMd5,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkTotal") int chunkTotal) throws Exception {
        Long companyId = 1232141425L;
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFileType("001002");
        uploadFileParamsDto.setTags("课程视频");
        uploadFileParamsDto.setRemark("");
        uploadFileParamsDto.setFilename(fileName);
        return mediaFilesService.mergechunks(companyId, fileMd5, chunkTotal, uploadFileParamsDto);

    }


}
