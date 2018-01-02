package com.aikon.fin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author haitao.wang
 */
@AllArgsConstructor
public enum CurrentBalanceTypeEnum {
    SINA(1,"sina"),
    DPM(2, "dpm"),
    ;

    @Getter
    Integer code;

    @Getter
    String desc;

}
