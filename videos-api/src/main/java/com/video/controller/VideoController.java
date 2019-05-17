package com.video.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.cofig.AuthService;
import com.video.enums.VideoStatusEnum;
import com.video.pojo.Bgm;
import com.video.pojo.Comments;
import com.video.pojo.Videos;
import com.video.service.BgmService;
import com.video.service.VideoService;
import com.video.utils.*;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;


@RestController
@Api(value = "视频相关业务的接口", tags = {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {
    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private BgmService bgmService;

    @Resource
    private VideoService videoService;

    @ApiOperation(value = "上传视频", notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "背景音乐播放长度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public KJSONResult upload(String userId,
                              String bgmId, double videoSeconds,
                              int videoWidth, int videoHeight,
                              String desc,
                              @ApiParam(value = "短视频", required = true)
                                          MultipartFile file) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return KJSONResult.errorMsg("用户id不能为空...");
        }

        // 文件保存的命名空间
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";
        String coverPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalVideoPath = "";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                // abc.mp4
                String arrayFilenameItem[] = fileName.split("\\.");
                String fileNamePrefix = "";
                for (int i = 0; i < arrayFilenameItem.length - 1; i++) {
                    fileNamePrefix += arrayFilenameItem[i];
                }
                // fix bug: 解决小程序端OK，PC端不OK的bug，原因：PC端和小程序端对临时视频的命名不同
//				String fileNamePrefix = fileName.split("\\.")[0];

                if (StringUtils.isNotBlank(fileName)) {

                    finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);
                    coverPathDB = coverPathDB + "/" + fileNamePrefix + ".jpg";

                    File outFile = new File(finalVideoPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return KJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return KJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        // 判断bgmId是否为空，如果不为空，
        // 那就查询bgm的信息，并且合并视频，生产新的视频
        if (StringUtils.isNotBlank(bgmId)) {
            Bgm bgm = bgmService.queryBgmById(bgmId);
            String mp3InputPath = FILE_SPACE + bgm.getPath();

            MergeVideoMp3 tool = new MergeVideoMp3(FFMPEG_EXE);
            outMusic om = new outMusic(FFMPEG_EXE);
            String videoInputPath = finalVideoPath;

            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            String videoMusicOutName = UUID.randomUUID().toString() + ".mp4";
            uploadPathDB = "/" + userId + "/video" + "/" + videoOutputName;
            String outMusicName =  "/" + userId + "/video" + "/" + videoMusicOutName;
            finalVideoPath = FILE_SPACE + uploadPathDB;
            String outMusic1 = FILE_SPACE + outMusicName;
            om.convertor(videoInputPath,outMusic1);
            tool.convertor(outMusic1, mp3InputPath, videoSeconds, finalVideoPath);
        }
        System.out.println("uploadPathDB=" + uploadPathDB);
        System.out.println("finalVideoPath=" + finalVideoPath);

        // 对视频进行截图
        FetchVideoCover videoInfo = new FetchVideoCover(FFMPEG_EXE);
        videoInfo.getCover(finalVideoPath, FILE_SPACE + coverPathDB);

        // 保存视频信息到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float) videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoDesc(desc);
        video.setVideoPath(uploadPathDB);
        video.setCoverPath(coverPathDB);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());

        String videoId = videoService.saveVideo(video);

        return KJSONResult.ok(videoId);
    }

    @ApiOperation(value = "上传封面", notes = "上传封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoId", value = "视频主键id", required = true,
                    dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/uploadCover", headers = "content-type=multipart/form-data")
    public KJSONResult uploadCover(String userId,
                                   String videoId,
                                   @ApiParam(value = "视频封面", required = true)
                                               MultipartFile file) throws Exception {

        if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
            return KJSONResult.errorMsg("视频主键id和用户id不能为空...");
        }

        // 文件保存的命名空间
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalCoverPath = "";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {

                    finalCoverPath = FILE_SPACE + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);

                    File outFile = new File(finalCoverPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return KJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return KJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        videoService.updateVideo(videoId, uploadPathDB);

        return KJSONResult.ok();
    }

    /**
     * @Description: 分页和搜索查询视频列表
     * isSaveRecord：1 - 需要保存
     * 0 - 不需要保存 ，或者为空的时候
     */
    @PostMapping(value = "/showAll")
    public KJSONResult showAll(@RequestBody Videos video, Integer isSaveRecord,
                               Integer page, Integer pageSize) {

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedResult result = videoService.getAllVideos(video, isSaveRecord, page, pageSize);
        return KJSONResult.ok(result);
    }

    /**
     * @Description: 我关注的人发的视频
     */
    @PostMapping("/showMyFollow")
    public KJSONResult showMyFollow(String userId, Integer page) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return KJSONResult.ok();
        }

        if (page == null) {
            page = 1;
        }

        int pageSize = 6;

        PagedResult videosList = videoService.queryMyFollowVideos(userId, page, pageSize);

        return KJSONResult.ok(videosList);
    }

    /**
     * @Description: 我收藏(点赞)过的视频列表
     */
    @PostMapping("/showMyLike")
    public KJSONResult showMyLike(String userId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return KJSONResult.ok();
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 6;
        }

        PagedResult videosList = videoService.queryMyLikeVideos(userId, page, pageSize);

        return KJSONResult.ok(videosList);
    }

    @PostMapping(value = "/hot")
    public KJSONResult hot() throws Exception {
        return KJSONResult.ok(videoService.getHotwords());
    }

    @PostMapping(value = "/userLike")
    public KJSONResult userLike(String userId, String videoId, String videoCreaterId)
            throws Exception {
        videoService.userLikeVideo(userId, videoId, videoCreaterId);
        return KJSONResult.ok();
    }

    @PostMapping(value = "/userUnLike")
    public KJSONResult userUnLike(String userId, String videoId, String videoCreaterId) throws Exception {
        videoService.userUnLikeVideo(userId, videoId, videoCreaterId);
        return KJSONResult.ok();
    }

    @PostMapping("/saveComment")
    public KJSONResult saveComment(@RequestBody Comments comment,
                                   String fatherCommentId, String toUserId) throws Exception {

        String url = "https://aip.baidubce.com/rest/2.0/antispam/v2/spam?access_token=" + AuthService.getAuth();
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("content", comment.getComment());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        String data = restTemplate.postForObject(url, r, String.class);
        JSONObject jsonObject = JSONObject.parseObject(data);
        jsonObject = JSON.parseObject(jsonObject.get("result").toString());
        System.out.println(jsonObject.get("spam"));
        if (Integer.parseInt(jsonObject.get("spam").toString())!=0) {
//            System.out.println(jsonObject);
//            jsonObject = JSON.parseObject(jsonObject.get("reject").toString());
//            System.out.println(jsonObject);
//            if (!jsonObject.get("label").equals("0")) {
                return KJSONResult.errorMsg("评论中含有敏感词汇,禁止评论");
//            }
        }
        comment.setFatherCommentId(fatherCommentId);
        comment.setToUserId(toUserId);
        videoService.saveComment(comment);
        return KJSONResult.ok();
    }

    @PostMapping("/getVideoComments")
    public KJSONResult getVideoComments(String videoId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(videoId)) {
            return KJSONResult.ok();
        }

        // 分页查询视频列表，时间顺序倒序排序
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PagedResult list = videoService.getAllComments(videoId, page, pageSize);

        return KJSONResult.ok(list);
    }

}
