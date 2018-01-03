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
        verifier.verify("csv/20180102/dpm.csv", "csv/20180102/sina.csv");
    }



    public void verify(String dpmFilePath, String sinaFilePath) {
        Analyzer dpmFileAnalyzer = new DpmFileAnalyzer(false);
        Analyzer sinaFileAnalyzer = new SinaFileAnalyzer(false);

        List<CurrentBalance> dpmCurrentBalances = null;
        List<CurrentBalance> sinaCurrentBalances = null;
        try {
            sinaCurrentBalances = sinaFileAnalyzer.analyze(Resources.getResourceAsFile(sinaFilePath).getPath());
            dpmCurrentBalances = dpmFileAnalyzer.analyze(Resources.getResourceAsFile(dpmFilePath).getPath());
        } catch (IOException e) {
            log.info("Error Load Files {}",e);
        }
        List<CurrentBalance> finalSinaCurrentBalances = sinaCurrentBalances;
        List<CurrentBalance> finalDpmCurrentBalances = dpmCurrentBalances;
        List<CurrentBalance> intersection = finalSinaCurrentBalances.stream().filter(dpmItem -> !finalDpmCurrentBalances.contains(dpmItem)).collect(Collectors.toList());
        intersection.stream().forEach(item -> log.info(item.getMemberId() + " " + item.getBalance()));
    }
}
