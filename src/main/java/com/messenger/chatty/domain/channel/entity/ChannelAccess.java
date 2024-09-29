package com.messenger.chatty.domain.channel.entity;

import com.messenger.chatty.domain.base.entity.BaseEntity;
import com.messenger.chatty.domain.message.entity.Message;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "channel_access")
public class ChannelAccess extends BaseEntity {

    @Id
    @Column(name = "channel_access_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workspaceJoinId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    private String lastMessageId;

    public void updateAccessTime(Message message) {
        this.lastMessageId = message.getId();
    }
}
