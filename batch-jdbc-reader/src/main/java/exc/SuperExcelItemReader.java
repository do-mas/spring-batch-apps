package exc;

import exc.exception.ExcelFileParseException;
import exc.orig.ExcelRowSet;
import exc.orig.ExcelSheet;
import exc.orig.RowMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Iterator;

public class SuperExcelItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> {

    private Log logger = LogFactory.getLog(getClass());

    private Workbook workbook;
    private InputStream workbookStream;
    private Resource resource;
    private int linesToSkip;
    private SuperRowMapper<T> rowMapper;
    private Iterator<Row> rowSet;

    public SuperExcelItemReader(Resource resource, int linesToSkip, SuperRowMapper<T> rowMapper) {
        super();
        this.resource = resource;
        this.linesToSkip = linesToSkip;
        this.rowMapper = rowMapper;
        this.setName(ClassUtils.getShortName(this.getClass()));
        this.validate();
    }

    @Override
    protected void doOpen() {

        try {

            this.openExcelFile();
            this.navigateIteratorToContent();

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

        if (rowSet.hasNext()) {

            try {
                return this.rowMapper.mapRow(rowSet.next());
            } catch (final Exception e) {
                logger.error("Error occurred during mapping " + e.getMessage());
            }

        }

        return null;

    }

    @Override
    protected void doClose() throws Exception {

        if (workbook != null) {
            this.workbook.close();
        }

        if (workbookStream != null) {
            workbookStream.close();
        }

        this.workbook = null;
        this.workbookStream = null;

    }


    private void openExcelFile() throws Exception {

        workbookStream = resource.getInputStream();
        if (!workbookStream.markSupported() && !(workbookStream instanceof PushbackInputStream)) {
            throw new IllegalStateException("InputStream MUST either support mark/reset, or be wrapped as a PushbackInputStream");
        }

        this.workbook = WorkbookFactory.create(workbookStream);
        this.workbook.setMissingCellPolicy(Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

    }

    private void navigateIteratorToContent() {

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        this.rowSet = rowIterator;

        for (int i = 0; i < this.linesToSkip; i++) {
            if (rowSet.hasNext()) {
                rowSet.next();
            }
        }

    }

    private void validate() {

        Assert.notNull(this.rowMapper, "RowMapper must be set");
        Assert.notNull(this.resource, "Input resource must be set");
        if (!this.resource.exists()) {
            throw new IllegalStateException("Resource was not found " + this.resource);
        }
        if (!this.resource.isReadable()) {
            throw new IllegalStateException("Resource is not readable " + this.resource);
        }

    }
}
