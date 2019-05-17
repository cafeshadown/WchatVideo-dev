package com.video.mapper;

import com.video.pojo.SearchRecords;
import com.video.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

    List<String> getHotwords();
}