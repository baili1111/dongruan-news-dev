package com.dongruan.api.service;

import com.dongruan.utils.PagedGridResult;
import com.dongruan.utils.RedisOperator;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseService {

    @Autowired
    public RedisOperator redis;

	public PagedGridResult setterPageGrid(List<?> list, Integer page) {
		PageInfo<?> pageList = new PageInfo<>(list);
		PagedGridResult gridResult = new PagedGridResult();
		gridResult.setRows(list);
		gridResult.setPage(page);
		gridResult.setRecords(pageList.getTotal());
		gridResult.setTotal(pageList.getPages());
		return gridResult;
	}

}
