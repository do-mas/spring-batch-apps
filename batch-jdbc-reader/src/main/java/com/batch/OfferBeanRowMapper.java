package com.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfferBeanRowMapper implements RowMapper<OfferBatchBean> {

    @Override
    public OfferBatchBean mapRow(ResultSet resultSet, int i) throws SQLException {
        OfferBatchBean batchBean = new OfferBatchBean();
        batchBean.setInvoiceId(resultSet.getLong("invoiceId"));
        batchBean.setInvoiceCode(resultSet.getString("invoiceCode"));
        batchBean.setDiscount(resultSet.getBigDecimal("discount"));
        batchBean.setDiscountAmount(resultSet.getBigDecimal("discountAmount"));
        batchBean.setApr(resultSet.getBigDecimal("APR"));
        batchBean.setOfferId(resultSet.getLong("id"));
        batchBean.setOfferType(resultSet.getString("offerType"));
        batchBean.setSellingCompanyId(resultSet.getString("sellingCompanyId"));
        batchBean.setSellingDivisionId(resultSet.getString("sellingDivisionId"));
        batchBean.setBuyingCompanyId(resultSet.getString("buyingCompanyId"));
        batchBean.setBuyingDivisionId(resultSet.getString("buyingDivisionId"));
        batchBean.setInvoiceAmount(resultSet.getBigDecimal("invoiceAmount"));
        batchBean.setInvoiceNetAmount(resultSet.getBigDecimal("invoiceNetAmount"));
        return batchBean;
    }
}
