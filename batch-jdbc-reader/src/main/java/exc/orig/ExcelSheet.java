package exc.orig;

import org.apache.poi.ss.usermodel.*;

import java.util.LinkedList;
import java.util.List;

public class ExcelSheet {

    private final org.apache.poi.ss.usermodel.Sheet delegate;
    private final int numberOfRows;
    private final String sheetName;
    private int numberOfColumns = -1;

    public ExcelSheet(final Sheet delegatePoiSheet) {
        super();
        this.delegate = delegatePoiSheet;
        this.numberOfRows = this.delegate.getLastRowNum() + 1;
        this.sheetName = this.delegate.getSheetName();
    }

    public String[] getRow(final int rowNumber) {
        Row row = this.delegate.getRow(rowNumber);

        if (row == null) {
            return null;
        }

        List<String> cells = new LinkedList<>();
        for (int i = 0; i < getNumberOfColumns(); i++) {
            Cell cell = row.getCell(i);
            cell.setCellType(CellType.STRING);
            cells.add(cell.getStringCellValue());
        }
        return cells.toArray(new String[cells.size()]);
    }

    public int getNumberOfColumns() {
        if (numberOfColumns < 0) {
            numberOfColumns = this.delegate.getRow(0).getLastCellNum();
        }
        return numberOfColumns;
    }

    public Sheet getDelegate() {
        return delegate;
    }

    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    public String getSheetName() {
        return this.sheetName;
    }
}
