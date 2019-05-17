package com.video.mapper;

import com.video.pojo.Comments;
import com.video.pojo.vo.CommentsVO;
import com.video.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface CommentsCustomMapper extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
}