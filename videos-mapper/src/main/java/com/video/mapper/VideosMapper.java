package com.video.mapper;

import com.video.pojo.Videos;
import com.video.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideosMapper extends MyMapper<Videos> {


}