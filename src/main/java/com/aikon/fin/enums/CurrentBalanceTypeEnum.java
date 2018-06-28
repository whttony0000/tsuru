package com.aikon.fin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author haitao.wang
 */
@AllArgsConstructor
public enum CurrentBalanceTypeEnum {
    Y(1,"Y"),
    X(2, "X"),
    ;

    @Getter
    Integer code;

    @Getter
    String desc;

}
