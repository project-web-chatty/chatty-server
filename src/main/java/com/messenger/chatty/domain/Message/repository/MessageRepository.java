package com.messenger.chatty.domain.Message.repository;

import com.messenger.chatty.domain.Message.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, Long> {

}
