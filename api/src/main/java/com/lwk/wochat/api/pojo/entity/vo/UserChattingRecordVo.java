package com.lwk.wochat.api.pojo.entity.vo;

import com.lwk.wochat.api.pojo.State;
import com.lwk.wochat.api.pojo.entity.UserChattingRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChattingRecordVo implements Comparable<UserChattingRecordVo> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String message;
    private Date sendTime;

    private State state;

    public UserChattingRecordVo(UserChattingRecord userChattingRecord) {
        id = userChattingRecord.getId();
        message = userChattingRecord.getMessage();
        sendTime = userChattingRecord.getSendTime();
        state = userChattingRecord.getState();
    }

    @Override
    public int compareTo(UserChattingRecordVo o) {
        return sendTime.compareTo(o.sendTime);
    }
}
