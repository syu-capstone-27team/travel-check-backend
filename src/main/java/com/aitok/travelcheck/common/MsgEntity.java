package com.aitok.travelcheck.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 제외하고 json생성
public class MsgEntity {
    private String msg;
    private Object result;

    public MsgEntity(String msg, Object result) {
        this.msg = msg;
        this.result  = result;
    }
}
