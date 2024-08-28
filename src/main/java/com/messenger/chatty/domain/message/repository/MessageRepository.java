package com.messenger.chatty.domain.message.repository;

import com.messenger.chatty.domain.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MessageRepository extends MongoRepository<Message, Long> {

    @Query(value = "{'channelId': ?0, 'sendTime': {$gt: ?1}}", sort = "{'sendTime': -1}")
    Page<Message> findMessagesByChatRoomIdAfterMemberDisconnectTime(Long channelId,
                                                                    Long accessTime,
                                                                    Pageable pageable);

}
