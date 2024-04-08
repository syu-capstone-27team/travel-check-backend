package com.aitok.travelcheck.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgEntity {
    private String msg;
    private Object result;

    public MsgEntity(String msg, Object result) {
        this.msg = msg;
        this.result  = result;
    }
}
