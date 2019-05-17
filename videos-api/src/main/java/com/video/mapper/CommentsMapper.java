package com.video.mapper;

import com.video.pojo.Comments;
import com.video.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CommentsMapper extends MyMapper<Comments> {
}