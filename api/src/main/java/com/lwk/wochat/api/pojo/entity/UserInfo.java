package com.lwk.wochat.api.pojo.entity;

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
@Table(name = "user_info")
@EntityListeners(AuditingEntityListener.class)
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", length = 128)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "gender", length = 5)
    private String gender;

    @Column(name = "avatar_url", length = 180)
    private String avatarUrl;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
