package com.aikon.fin.analyze;

import com.aikon.fin.entity.CurrentBalance;
import com.google.common.base.Splitter;
import com.aikon.fin.enums.CurrentBalanceTypeEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author haitao.wang
 */
public class SinaFileAnalyzer extends AbstractFileAnalyzer{
    public SinaFileAnalyzer(boolean ifSave) {
        super(ifSave);
    }

    public CurrentBalance handleLine(String line) {
        List<String> words = Splitter.on(",").splitToList(line);
        BigDecimal balance = new BigDecimal(words.get(1));
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        String memberId = words.get(0);
        BigDecimal profit = new BigDecimal(words.get(4));
        CurrentBalance currentBalance = new CurrentBalance();
        currentBalance.setMemberId(memberId);
        currentBalance.setBalance(balance.add(profit));
        currentBalance.setProfit(profit);
        currentBalance.setType(CurrentBalanceTypeEnum.SINA.getCode());

        saveCurrentBalance(currentBalance);
        return currentBalance;
    }

}
