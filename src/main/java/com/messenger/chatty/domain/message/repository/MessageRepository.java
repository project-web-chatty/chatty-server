package com.messenger.chatty.domain.message.repository;

import com.messenger.chatty.domain.message.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, Long> {

}
