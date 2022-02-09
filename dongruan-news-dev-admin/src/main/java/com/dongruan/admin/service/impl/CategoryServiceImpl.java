package com.dongruan.admin.service.impl;

import com.dongruan.admin.mapper.CategoryMapper;
import com.dongruan.admin.service.CategoryService;
import com.dongruan.api.service.BaseService;
import com.dongruan.constant.SystemConstant;
import com.dongruan.exception.GraceException;
import com.dongruan.grace.result.ResponseStatusEnum;
import com.dongruan.pojo.Category;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author zhu
 * @date 2022/1/28 23:39:24
 * @description
 */
@Service
public class CategoryServiceImpl extends BaseService implements CategoryService {

	@Autowired
	public CategoryMapper categoryMapper;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void createCategory(Category category) {
		// 分类不会很多，所以id不需要自增，这个表的数据也不会多到几万甚至分表，数据都会集中在一起
		int result = categoryMapper.insert(category);
		if (result != 1) {
			GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
		}

		/**
		 * 不建议如下做法：
		 * 1. 查询redis中的categoryList
		 * 2. 转化categoryList为list类型
		 * 3. 在categoryList中add一个当前的category
		 * 4. 再次转换categoryList为json，并存入redis中
		 */

		// 直接使用redis删除缓存即可，用户端在查询的时候会直接查库，再把最新的数据放入到缓存中
		redis.del(SystemConstant.REDIS_ALL_CATEGORY);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void modifyCategory(Category category) {
		int result = categoryMapper.updateByPrimaryKey(category);
		if (result != 1) {
			GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
		}

		/**
		 * 不建议如下做法：
		 * 1. 查询redis中的categoryList
		 * 2. 循环categoryList中拿到原来的老的数据
		 * 3. 替换老的category为新的
		 * 4. 再次转换categoryList为json，并存入redis中
		 */

		// 直接使用redis删除缓存即可，用户端在查询的时候会直接查库，再把最新的数据放入到缓存中
		redis.del(SystemConstant.REDIS_ALL_CATEGORY);
	}

	@Override
	public boolean queryCatIsExist(String catName, String oldCatName) {
		Example example = new Example(Category.class);
		Example.Criteria catCriteria = example.createCriteria();
		catCriteria.andEqualTo("name", catName);
		if (StringUtils.isNotBlank(oldCatName)) {
			catCriteria.andNotEqualTo("name", oldCatName);
		}

		List<Category> catList = categoryMapper.selectByExample(example);

		boolean isExist = false;
		if (catList != null && !catList.isEmpty() && catList.size() > 0) {
			isExist = true;
		}

		return isExist;
	}

	@Override
	public List<Category> queryCategoryList() {
		return categoryMapper.selectAll();
	}

}
