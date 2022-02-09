package com.dongruan.admin.repository;

import com.dongruan.pojo.mo.FriendLinkMO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendLinkRepository extends MongoRepository<FriendLinkMO, String> {

	List<FriendLinkMO> getAllByIsDelete(Integer isDelete);


}