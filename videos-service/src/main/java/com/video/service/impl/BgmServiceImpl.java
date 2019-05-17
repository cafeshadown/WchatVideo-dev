package com.video.service.impl;

import com.video.mapper.BgmMapper;
import com.video.pojo.Bgm;
import com.video.service.BgmService;
import idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {

	@Resource
	private BgmMapper bgmMapper;
	
	@Resource
	private Sid sid;
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<Bgm> queryBgmList() {
		return bgmMapper.selectAll();
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Bgm queryBgmById(String bgmId) {
		return bgmMapper.selectByPrimaryKey(bgmId);
	}

}
