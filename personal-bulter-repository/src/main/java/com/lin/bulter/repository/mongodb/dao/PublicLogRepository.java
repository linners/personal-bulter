package com.lin.bulter.repository.mongodb.dao;

import com.lin.bulter.repository.mongodb.entity.PublicLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PublicLogRepository extends MongoRepository<PublicLog, String> {

}
