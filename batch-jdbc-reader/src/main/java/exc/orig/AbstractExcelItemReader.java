package exc.orig;

import exc.exception.ExcelFileParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public abstract class AbstractExcelItemReader<T>
        extends AbstractItemCountingItemStreamItemReader<T> implements ResourceAwareItemReaderItemStream<T>, InitializingBean {

    private Log logger = LogFactory.getLog(getClass());
    private Resource resource;
    private int linesToSkip = 0;
    private int sheetNumberToRead = 0;
    private RowMapper<T> rowMapper;
    private ExcelRowSet rowSet;

    public AbstractExcelItemReader() {
        super();
        this.setName(ClassUtils.getShortName(this.getClass()));
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setLinesToSkip(int linesToSkip) {
        this.linesToSkip = linesToSkip;
    }

    public void setRowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    protected void doOpen() {
        try {
            validateInputResource();
            this.openExcelFile(this.resource);
            this.rowSet = this.getContentRowSet(this.getSheet(this.sheetNumberToRead));
            logger.debug("Opened workbook: " + this.resource.getFilename());
        } catch (Exception e) {
            throw new ExcelFileParseException("Exception parsing Excel file. " + this.resource.getFilename(), e);
        }

    }

    @Override
    protected T doRead() {

        if (this.rowSet == null) {
            return null;
        }

        if (rowSet.next()) {
            try {
                return this.rowMapper.mapRow(rowSet);
            } catch (final Exception e) {
                logger.debug("Error occurred during mapping " + e.getMessage());
            }
        }

        return null;

    }

    private ExcelRowSet getContentRowSet(ExcelSheet sheet) {

        this.rowSet = new ExcelRowSet(sheet);
        logger.debug("Opened ` " + sheet.getSheetName() + ", with " + sheet.getNumberOfRows() + " rows.");
        for (int i = 0; i < this.linesToSkip; i++) {
            rowSet.next();
        }
        return rowSet;

    }

    protected abstract ExcelSheet getSheet(int sheet);

    protected abstract void openExcelFile(Resource resource) throws Exception;

    private boolean validateInputResource() {

        Assert.notNull(this.resource, "Input resource must be set");

        if (!this.resource.exists()) {
            throw new IllegalStateException("Resource was not found " + this.resource);
        }

        if (!this.resource.isReadable()) {
            throw new IllegalStateException("Resource is not readable " + this.resource);
        }

        return false;
    }

    public void afterPropertiesSet() {
        Assert.notNull(this.rowMapper, "RowMapper must be set");
    }
}
