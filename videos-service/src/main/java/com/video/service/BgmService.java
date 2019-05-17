package com.video.service;

import com.video.pojo.Bgm;

import java.util.List;

public interface BgmService {
	
	/**
	 * @Description: 查询背景音乐列表
	 */
	List<Bgm> queryBgmList();
	
	/**
	 * @Description: 根据id查询bgm信息
	 */
	Bgm queryBgmById(String bgmId);
}
