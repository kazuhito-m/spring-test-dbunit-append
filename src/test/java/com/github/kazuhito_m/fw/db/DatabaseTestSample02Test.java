package com.github.kazuhito_m.fw.db;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.Test;

public class DatabaseTestSample02Test extends DatabaseTestCase {

//    private IMstKeywordService sut;

    @Override
    public void setUp() {
        super.setUp();
        logger.debug("だいたい、ここでSUTの取得とか色々する。");
//        sut = MasterServiceFactory.getFactory().getMstKeywordService();
    }

    @Test
    @DatabaseSetup("./testLoad.xls")
    @ExpectedDatabase(value = "./testExpected.xls", table = "VOICE_LIST")
    public void エビデンスを吐き出させる() {

        getEvidence("VOICE_LIST");

    }

}
