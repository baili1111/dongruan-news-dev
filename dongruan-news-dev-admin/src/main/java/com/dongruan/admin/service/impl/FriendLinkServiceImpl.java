package com.dongruan.admin.service.impl;

import com.dongruan.admin.repository.FriendLinkRepository;
import com.dongruan.admin.service.FriendLinkService;
import com.dongruan.enums.YesOrNo;
import com.dongruan.pojo.mo.FriendLinkMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhu
 * @date 2022/1/28 22:56:38
 * @description
 */
@Service
public class FriendLinkServiceImpl implements FriendLinkService {

	@Autowired
	private FriendLinkRepository friendLinkRepository;

	@Override
	public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO) {
		friendLinkRepository.save(friendLinkMO);
	}

	@Override
	public List<FriendLinkMO> queryAllFriendLinkList() {

		//分页
		//Pageable pageable = PageRequest.of(1, 10);
		//friendLinkRepository.findAll(pageable);

		return friendLinkRepository.findAll();
	}

	@Override
	public void delete(String linkId) {
		friendLinkRepository.deleteById(linkId);
	}

	@Override
	public List<FriendLinkMO> queryPortalFriendLinkList() {
		return friendLinkRepository.getAllByIsDelete(YesOrNo.NO.type);
	}
}
