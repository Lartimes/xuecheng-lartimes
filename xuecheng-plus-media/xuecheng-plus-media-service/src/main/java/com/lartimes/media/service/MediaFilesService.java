package com.lartimes.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.content.model.PageParams;
import com.lartimes.content.model.PageResult;
import com.lartimes.content.model.RestResponse;
import com.lartimes.media.model.dto.MediaFilesDTO;
import com.lartimes.media.model.dto.UploadFileParamsDto;
import com.lartimes.media.model.dto.UploadFileResultDto;
import com.lartimes.media.model.po.MediaFiles;

/**
 * <p>
 * 媒资信息 服务类
 * </p>
 *
 * @author itcast
 * @since 2024-04-18
 */
public interface MediaFilesService extends IService<MediaFiles> {
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath);
    public MediaFiles addMediaFilesToDb(Long companyId,String fileMd5,UploadFileParamsDto uploadFileParamsDto,String bucket,String objectName) ;

    RestResponse<Boolean> checkExists(String md5);

    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex);


    public RestResponse<Boolean> uploadChunk(String fileMd5,int chunk,byte[] bytes);
    public RestResponse<Boolean> mergechunks(Long companyId,String fileMd5,int chunkTotal,UploadFileParamsDto uploadFileParamsDto);


    PageResult<MediaFiles> getMediasByPage(PageParams params, MediaFilesDTO mediaFilesDTO);

}
