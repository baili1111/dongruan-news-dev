package com.dongruan.user.service.impl;

import com.dongruan.api.service.BaseService;
import com.dongruan.constant.SystemConstant;
import com.dongruan.enums.Sex;
import com.dongruan.pojo.AppUser;
import com.dongruan.pojo.Fans;
import com.dongruan.pojo.vo.RegionRatioVO;
import com.dongruan.user.mapper.FansMapper;
import com.dongruan.user.service.MyFansService;
import com.dongruan.user.service.UserService;
import com.dongruan.utils.PagedGridResult;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhu
 * @date 2022/1/19 23:49:53
 * @description
 */
@Service
public class MyFansServiceImpl extends BaseService implements MyFansService {

	@Autowired
	private FansMapper fansMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private Sid sid;

	@Override
	public boolean isMeFollowThisWriter(String writerId, String fanId) {

		Fans fan = new Fans();
		fan.setFanId(fanId);
		fan.setWriterId(writerId);

		int count = fansMapper.selectCount(fan);

		return count > 0 ? true : false;
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public void follow(String writerId, String fanId) {
		// 获得粉丝用户信息
		AppUser fanInfo = userService.getUser(fanId);

		String fanPkId = sid.nextShort();

		// 保存作家粉丝关联关系，字段冗余便于统计分析，并且只认成为第一次成为粉丝的数据
		Fans fan = new Fans();
		fan.setId(fanPkId);
		fan.setFanId(fanId);
		fan.setFace(fanInfo.getFace());
		fan.setWriterId(writerId);
		fan.setFanNickname(fanInfo.getNickname());
		fan.setProvince(fanInfo.getProvince());
		fan.setSex(fanInfo.getSex());

		fansMapper.insert(fan);

		// redis 作家粉丝数累加
		redis.increment(SystemConstant.REDIS_WRITER_FANS_COUNTS + ":" + writerId, 1);
		// redis 我的关注数累加
		redis.increment(SystemConstant.REDIS_MY_FOLLOW_COUNTS + ":" + fanId, 1);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void unfollow(String writerId, String fanId) {
		// 删除作家粉丝的关联关系
		Fans fan = new Fans();
		fan.setFanId(fanId);
		fan.setWriterId(writerId);
		fansMapper.delete(fan);

		// redis 作家粉丝数累减
		redis.decrement(SystemConstant.REDIS_WRITER_FANS_COUNTS + ":" + writerId, 1);
		// redis 我的关注数累减
		redis.decrement(SystemConstant.REDIS_MY_FOLLOW_COUNTS + ":" + fanId, 1);
	}

	@Override
	public PagedGridResult queryMyFansList(String writerId, Integer page, Integer pageSize) {

		Fans fan = new Fans();
		fan.setWriterId(writerId);

		PageHelper.startPage(page, pageSize);
		List<Fans> list = fansMapper.select(fan);

		return setterPageGrid(list, page);
	}

	@Override
	public Integer queryFansCounts(String writerId, Sex sex) {

		Fans fan = new Fans();
		fan.setWriterId(writerId);
		fan.setSex(sex.type);

		return fansMapper.selectCount(fan);
	}

	@Override
	public List<RegionRatioVO> queryRatioByRegion(String writerId) {
		Fans fan = new Fans();
		fan.setWriterId(writerId);

		List<RegionRatioVO> regionRatioVOList = new ArrayList<>();
		for (String region : SystemConstant.regions) {
			fan.setProvince(region);
			Integer count = fansMapper.selectCount(fan);

			RegionRatioVO regionRatioVO = new RegionRatioVO();
			regionRatioVO.setName(region);
			regionRatioVO.setValue(count);
			regionRatioVOList.add(regionRatioVO);
		}

		return regionRatioVOList;
	}


}
