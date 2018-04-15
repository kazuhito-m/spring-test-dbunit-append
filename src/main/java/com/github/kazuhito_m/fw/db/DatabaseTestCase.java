package com.github.kazuhito_m.fw.db;

import com.github.kazuhito_m.fw.category.DatabaseTests;
import com.github.kazuhito_m.fw.custom.dbunit.CustomDatabaseOperationLookup;
import com.github.kazuhito_m.fw.custom.poi.XlsxDataSetWriter;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 「データベースを使ったテスト」の際に用いるスーパークラス。
 */

// 「データベースを使ったテストだよ！」の意
@Category(DatabaseTests.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
@TransactionConfiguration
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ExcelDataSetLoader.class, databaseOperationLookup = CustomDatabaseOperationLookup.class)
public abstract class DatabaseTestCase {

	/* 定数 */

	/**
	 * エビデンス(画像)を出すディレクトリ。
	 */
	private static final String EVIDENCE_DIR = "test-evidence";

	/**
	 * ロガー(インスタンス時の)
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * ロガー(このクラスの時のみ)
	 */
	protected final Logger selfLogger = LoggerFactory
			.getLogger(DatabaseTestCase.class);

	/* テスト全体の事情と道具 */

	/**
	 * メソッド名検出用のルール。
	 */
	@Rule
	public TestName names = new TestName();

	/**
	 * エビデンスの通し番号
	 */
	private int evidenceCount;

	/**
	 * テスト用のコンテキスト
	 */
	@Autowired
	private ApplicationContext context;

	/**
	 * 共通のテスト前初期処理。
	 */
	@Before
	public void setUp() {
		evidenceCount = 0;
	}

	/**
	 * targetのディレクトリに「現在のDBデータ」をExcelファイルで出力する。
	 * 
	 * @param keyword
	 *            そのエビデンスの意味。ファイル名の一部に使われる。
	 * @param tableNames
	 *            「出力したいテーブル名」の文字列配列。
	 */
	public void getEvidence(String keyword, String... tableNames) {

		// TODO コネクションをとれないので、凍結。
		if (true) {
			return;
		}

		OutputStream os = null;
		// 内部の通し番号をインクリメント。
		evidenceCount++;

		try {
			// DBUnitが使ってるカレントのコネクションを横取り。
			IDatabaseConnection dbuConnection = getDBUnitCurrentConnection();

			// データセットの条件を作る。
			QueryDataSet dataSet = new QueryDataSet(dbuConnection);
			for (String tableName : tableNames) {
				dataSet.addTable(tableName);
			}

			// 出力ディレクトリを取得し、ファイル名をつなげる。
			File outFile = new File(makeTestMethodEvidenceDir(),
					createExcelFileName(keyword));

			// データ取得とファイル書き込み。
			selfLogger.trace("データベースのエビデンスを出力します: " + outFile.getPath());
			os = new FileOutputStream(outFile);
			(new XlsxDataSetWriter()).write(dataSet, os);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (os != null) {
				try {
					// 後始末。片っぱしから閉じる。
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * DBUnitが「現在カレントで使っている」コネクションをDBUnitの型(IDatabaseConnection)で取得する。
	 * 
	 * @return 取得できたUnitの型(IDatabaseConnection)のコネクション。
	 * @throws DatabaseUnitException
	 * @throws java.sql.SQLException
	 */
	protected IDatabaseConnection getDBUnitCurrentConnection()
			throws DatabaseUnitException, SQLException {
		// データソース取得。(プロキシかましてあるから「同一transaction」でデータ取得できる。)
		// 逆にその型じゃなければこけてくれたのむから。
		// TransactionAwareDataSourceProxy dataSource =
		// (TransactionAwareDataSourceProxy) BeanFactory.getBean("dataSource");
		// Javaのコネクション型を、DBユニットのコネクション型に
		// return new DatabaseConnection(dataSource.getConnection());
		return null;
	}

	/**
	 * 出力するExcelファイル名を作成する。
	 * 
	 * @param keyword
	 *            名前につけるサフィックス。未指定(null/空)なら数値・日付から自動的に作成される。
	 * @return 作成されたExcelファイル名。
	 */
	protected String createExcelFileName(String keyword) {
		String sufix = keyword;
		if (StringUtils.isEmpty(keyword)) {
			sufix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		}
		return "evidnece-" + String.format("%1$03d", evidenceCount) + "-"
				+ sufix + ".xlsx";
	}

	/**
	 * DBのスナップショットイメージExcelファイルを保存する「テストメソッド毎のエビデンスディレクトリ｣を返す。
	 * 
	 * @return テストメソッド毎のエビデンスのディレクトリのFileオブジェクト。
	 */
	protected File makeTestMethodEvidenceDir() {
		// リソースファイルのトップディレクトリまでを取得。
		String resourcePath = this.getClass().getResource("").getPath();
		// その「テストクラスのディレクトリ」になってるところを、エビデンス用に変える。
		String evidencePathTop = resourcePath.replaceFirst("test-classes",
				EVIDENCE_DIR);
		// 最後に、クラス名/メソッド名と付ける。
		File outDir = new File(evidencePathTop, this.getClass().getSimpleName()
				+ "/" + names.getMethodName());
		// ディレクトリを作成しておく。
		if (!outDir.exists())
			outDir.mkdirs();
		return outDir;

	}

	/**
	 * 簡易「エビデンス出力ディレクトリ名」取得。
	 * 
	 * @return 現在のテストメソッド中で、エビデンスが吐かれるディレクトリ(フォルダ)のパス名。
	 */
	protected String getEvidenceDirPath() {
		return makeTestMethodEvidenceDir().getPath();
	}

}
