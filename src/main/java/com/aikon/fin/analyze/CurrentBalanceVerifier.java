package com.aikon.fin.analyze;

import com.aikon.fin.entity.CurrentBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haitao.wang
 */
@Slf4j
public class CurrentBalanceVerifier {

    public static void main(String[] args) {
        CurrentBalanceVerifier verifier = new CurrentBalanceVerifier();
        verifier.verify("csv/20180101/X.csv", "csv/20180101/Y.csv");
    }



    public void verify(String xFilePath, String yFilePath) {
        Analyzer xFileAnalyzer = new XFileAnalyzer(false);
        Analyzer yFileAnalyzer = new YFileAnalyzer(false);

        List<CurrentBalance> xCurrentBalances = null;
        List<CurrentBalance> yCurrentBalances = null;
        try {
            yCurrentBalances = yFileAnalyzer.analyze(Resources.getResourceAsFile(yFilePath).getPath());
            xCurrentBalances = xFileAnalyzer.analyze(Resources.getResourceAsFile(xFilePath).getPath());
        } catch (IOException e) {
            log.info("Error Load Files {}",e);
        }
        List<CurrentBalance> finalYCurrentBalances = yCurrentBalances;
        List<CurrentBalance> finalXCurrentBalances = xCurrentBalances;
        List<CurrentBalance> intersection = finalYCurrentBalances.stream().filter(xItem -> !finalXCurrentBalances.contains(xItem)).collect(Collectors.toList());
        intersection.stream().forEach(item -> log.info(item.getMemberId() + " " + item.getBalance()));
    }
}
