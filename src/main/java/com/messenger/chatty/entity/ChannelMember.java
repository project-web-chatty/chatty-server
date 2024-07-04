package com.messenger.chatty.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//object should be generated by parameterized constructor
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelMember extends BaseEntity {

    @Id
    @Column(name = "channel_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public ChannelMember(Channel channel , Member member){
        setChannel(channel);
        setMember(member);
    }

    //relation-method
    private void setChannel(Channel channel) {
        this.channel = channel;
        channel.getChannelMember().add(this);
    }

    //relation-method
    private void setMember(Member member){
        this.member =member;
        member.getChannelMembers().add(this);
    }
}
