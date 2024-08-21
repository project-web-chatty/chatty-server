package com.messenger.chatty.domain.channel.entity;

import com.messenger.chatty.domain.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    private LocalDateTime accessTime;

    public void updateAccessTime() {
        this.accessTime = LocalDateTime.now();
    }
}
