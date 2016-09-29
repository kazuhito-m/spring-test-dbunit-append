package com.github.kazuhito_m.fw.db;

import org.junit.Test;

/**
 * DBを使ったテストのサンプル。
 */

// DBテストでは、必ず"DatabaseTestCase"クラスを継承してください。
public class DatabaseTestSample01Test extends DatabaseTestCase {

    /** テスト対象のオブジェクト。 */
//    private Object sut;

    @Override
    public void setUp() {
        // 定型文(おやくそく)
        super.setUp();
        // 個々テストクラス独自の処理
        logger.debug("だいたい、ここでSUTの取得とか色々する。");
//        sut = sut.getService();
    }

    @Test
//    @DatabaseSetup("deviceType001.xlsx")
    public void 参照系で共有テストDBを変更しない例() {
        // act
//        List actual = sut.getAll();
        // assertion
//        assertThat(actual, is(notNullValue()));
//        assertThat(actual.size(), is(4));
        // after (この場合はエビデンス取り)
        getEvidence("所属の全件", "organization");

    }

    @Test
    public void エビデンスを出力する例() {
        // 第一引数で「任意の名前」を付けられます。
        // (省略時は数字と日付で自動命名です。)
        getEvidence("結構", "organization");
        getEvidence("連打しても", "organization","openjdk_people");
        getEvidence("大丈夫だと思います", "openjdk_people");
        getEvidence("", "openjdk_people");
        getEvidence(null, "organization");

        logger.debug("上記で指定したエビデンスは、'" + this.getEvidenceDirPath() + "' に吐かれます。" );

    }

}
