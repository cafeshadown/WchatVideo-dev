package com.video.mapper;

import com.video.pojo.Videos;
import com.video.pojo.vo.VideosVO;
import com.video.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideosCustomMapper extends MyMapper<Videos> {
	
	/**
	 * @Description: 条件查询所有视频列表
	 */
	List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc,
                                  @Param("userId") String userId);
	
	/**
	 * @Description: 查询关注的视频
	 */
	List<VideosVO> queryMyFollowVideos(String userId);
	
	/**
	 * @Description: 查询点赞视频
	 */
	List<VideosVO> queryMyLikeVideos(@Param("userId") String userId);
	
	/**
	 * @Description: 对视频喜欢的数量进行累加
	 */
	void addVideoLikeCount(String videoId);
	
	/**
	 * @Description: 对视频喜欢的数量进行累减
	 */
	void reduceVideoLikeCount(String videoId);
}