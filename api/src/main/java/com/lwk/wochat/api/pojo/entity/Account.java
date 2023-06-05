package com.lwk.wochat.api.pojo.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@Entity
@Table(name = "user_account")
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "u_account")
    private String account;

    @Column(name = "u_password")
    private String password;

    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;
}
