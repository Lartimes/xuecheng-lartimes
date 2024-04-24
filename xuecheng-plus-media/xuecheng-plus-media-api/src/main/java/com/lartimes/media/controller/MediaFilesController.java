package com.lartimes.media.controller;

import com.lartimes.content.model.PageParams;
import com.lartimes.content.model.PageResult;
import com.lartimes.media.model.dto.MediaFilesDTO;
import com.lartimes.media.model.dto.UploadFileParamsDto;
import com.lartimes.media.model.dto.UploadFileResultDto;
import com.lartimes.media.model.po.MediaFiles;
import com.lartimes.media.service.MediaFilesService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 媒资信息 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class MediaFilesController {

    private final MediaFilesService mediaFilesService;

    public MediaFilesController(MediaFilesService mediaFilesService) {
        this.mediaFilesService = mediaFilesService;
    }


    @Operation(summary = "分页查询medias")
    @PostMapping("/files")
    public PageResult<MediaFiles> pageMedias(PageParams params, @RequestBody(required = false) MediaFilesDTO mediaFilesDTO) {
        return mediaFilesService.getMediasByPage(params , mediaFilesDTO);
    }

    @Operation(summary = "上传图片")
    @PostMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultDto uploadPictures(@RequestPart("filedata") MultipartFile upload, @RequestParam(value = "folder", required = false) String folder, @RequestParam(value = "objectName", required = false) String objectName) throws IOException {
        String originalFilename = upload.getOriginalFilename();
        long size = upload.getSize();
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFilename(originalFilename);
        uploadFileParamsDto.setFileSize(size);
        uploadFileParamsDto.setFileType("001001");
        Long companyId = 1232141425L;
        //创建临时文件
        File tempFile = File.createTempFile("minio", "temp");
        //上传的文件拷贝到临时文件
        upload.transferTo(tempFile);
        //文件路径
        String absolutePath = tempFile.getAbsolutePath();
        //上传文件
        return mediaFilesService.uploadFile(companyId, uploadFileParamsDto, absolutePath);
    }


}
