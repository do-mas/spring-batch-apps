package com.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;

import javax.sql.DataSource;

public class OfferDBReader extends JdbcPagingItemReader<OfferBatchBean> {

    private static Logger log = Logger.getLogger(OfferDBReader.class.getSimpleName());
    public OfferDBReader(DataSource dataSource, PagingQueryProvider queryProvider) throws Exception {

        super.setPageSize(2000);
        super.setMaxItemCount(10000000);
        super.setFetchSize(2000);
        super.setDataSource(dataSource);
        super.setQueryProvider(queryProvider);
        super.setRowMapper(new OfferBeanRowMapper());
    }

}
