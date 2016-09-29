package com.github.kazuhito_m.fw.custom.spring_test_dbunit;

import com.github.kazuhito_m.fw.custom.poi.XlsxDataSet;
import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * spring-test-dbunit本家にはない「Excelのデータを読み込むLoader」クラス。
 */
public class ExcelDataSetLoader extends AbstractDataSetLoader {

    private final Logger logger = LoggerFactory.getLogger(ExcelDataSetLoader.class);

    @Override
    protected IDataSet createDataSet(Resource resource) throws Exception {
        logger.trace("Load excel file : " + resource.getFile().getPath());
        return new XlsxDataSet(resource.getFile());
    }

}
