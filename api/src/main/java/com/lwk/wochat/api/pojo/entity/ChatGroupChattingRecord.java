package com.lwk.wochat.api.pojo.entity;

import com.lwk.wochat.api.pojo.State;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_group_chatting_record")
@EntityListeners(AuditingEntityListener.class)
public class ChatGroupChattingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "sender_id")
    private long senderId;

    @Column(name = "receiver_id")
    private long receiverId;

    @Column(name = "message")
    private String message;

    @Column(name = "send_time")
    private Date sendTime;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;
}
