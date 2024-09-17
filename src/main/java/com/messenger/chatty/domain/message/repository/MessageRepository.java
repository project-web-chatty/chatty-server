package com.messenger.chatty.domain.message.repository;

import com.messenger.chatty.domain.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface MessageRepository extends MongoRepository<Message, Long> {

    @Query(value = "{'channelId': ?0, 'sendTime': {$gt: ?1}}", sort = "{'sendTime': -1}")
    Page<Message> findMessagesByChatRoomIdAfterMemberDisconnectTime(Long channelId,
                                                                    Long accessTime,
                                                                    Pageable pageable);

    @Query(value = "{'channelId': ?0, 'sendTime': {$gt: ?1}}", count = true)
    long countByChatRoomIdAndSendTimeAfter(Long channelId, long lastAccessTime);

    @Query(value = "{'channelId' : ?0}")
    Page<Message> findMessages(Long channelId, Pageable pageable);

    @Query(value = "{'channelId': ?0, 'sendTime': {$lt: ?1}}", sort = "{'sendTime': -1}")
    Optional<Message> findLastMessageBeforeTime(Long channelId, Long time);

}
