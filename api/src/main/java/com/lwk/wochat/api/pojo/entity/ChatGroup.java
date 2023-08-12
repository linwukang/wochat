package com.lwk.wochat.api.pojo.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_group")
@EntityListeners(AuditingEntityListener.class)
public class ChatGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "group_name", length = 30)
    private String groupName;

    @Column(name = "members_max_number")
    private Integer membersMaxNumber;

    @Column(name = "members_number")
    private Integer membersNumber;

    @Column(name = "avatar_url", length = 180)
    private String avatarUrl;

    @Column(name = "introduction", length = 500)
    private String introduction;

    @ElementCollection
    @Column(name = "tags", columnDefinition = "TEXT", length = 100)
    private List<String> tags;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "chat_group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Account> members;
}
