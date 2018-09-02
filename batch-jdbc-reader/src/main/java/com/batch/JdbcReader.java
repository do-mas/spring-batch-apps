package com.batch;

import components.exception.InvalidJobParametersException;
import components.readers.offer.OfferBatchBean;
import components.readers.offer.OfferBeanRowMapper;
import components.readers.offer.OfferDBReader;
import components.readers.query.sql.OfferSQLQueries;
import enums.offer.OfferType;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JdbcReader extends JdbcPagingItemReader<OfferBatchBean> {

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");


    public JdbcReader(DataSource dataSource,
                      String companyId,
                      String divisionId,
                      String offerType) throws Exception {

        super.setPageSize(2000);
        super.setMaxItemCount(10000000);
        super.setFetchSize(2000);
        super.setDataSource(dataSource);
        PagingQueryProvider queryProvider = getPagingQueryProvider(dataSource, companyId, divisionId, offerType);
        super.setQueryProvider(queryProvider);
        super.setRowMapper(new OfferBeanRowMapper());
    }

    public PagingQueryProvider getPagingQueryProvider(DataSource dataSource,
                                                      String companyId,
                                                      String divisionId,
                                                      String offerType) throws Exception {
        SqlPagingQueryProviderFactoryBean pagingProvider = new SqlPagingQueryProviderFactoryBean();
        pagingProvider.setFromClause("from InvoiceOfferQueue");
        pagingProvider.setSelectClause("SELECT marketOfferId, invoiceId, sellingCompanyId, sellingDivisionId, buyingCompanyId, buyingDivisionId, invoiceCode, discount, APR, offerType, invoiceAmount, invoiceNetAmount");
        pagingProvider.setSortKey("Id");

        String query = String.format("WHERE offerType = '%s' " +
                "AND buyingCompanyId='%s' " +
                "AND buyingDivisionId='%s' " +
                "AND status='valid' " +
                "AND subStatus='cancel'", offerType.toString().toLowerCase(), companyId, divisionId);
        pagingProvider.setWhereClause(query);
        pagingProvider.setDataSource(dataSource);

        PagingQueryProvider queryProvider = null;
        try {
            queryProvider = pagingProvider.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryProvider;
    }

}
