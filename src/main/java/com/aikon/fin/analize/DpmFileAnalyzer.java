package com.aikon.fin.analize;

import com.aikon.fin.entity.CurrentBalance;
import com.google.common.base.Splitter;
import com.aikon.fin.enums.CurrentBalanceTypeEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author haitao.wang
 */
public class DpmFileAnalyzer extends AbstractFileAnalyzer {
    public DpmFileAnalyzer(boolean ifSave) {
        super(ifSave);
    }

    public CurrentBalance handleLine(String line) {
        List<String> words = Splitter.on(",").splitToList(line);

        BigDecimal balance = new BigDecimal(words.get(0));
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        String memberId = words.get(1);
        CurrentBalance currentBalance = new CurrentBalance();
        currentBalance.setMemberId(memberId);
        currentBalance.setBalance(balance);
        currentBalance.setType(CurrentBalanceTypeEnum.DPM.getCode());

        saveCurrentBalance(currentBalance);
        return currentBalance;
    }



}

