package com.lwk.wochat.api.pojo.entity;

import javax.persistence.*;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_account")
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 18)
    private String username;

    @Column(name = "password", length = 32)
    private String password;

    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    @ManyToMany(mappedBy = "members")
    private List<ChatGroup> groups;
}
