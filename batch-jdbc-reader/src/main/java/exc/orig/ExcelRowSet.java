package exc.orig;

public class ExcelRowSet {

    private final ExcelSheet sheet;
    private int currentRowIndex = -1;
    private String[] currentRow;

    public ExcelRowSet(ExcelSheet sheet) {
        this.sheet = sheet;
    }

    public boolean next() {
        currentRow = null;
        currentRowIndex++;
        if (currentRowIndex < sheet.getNumberOfRows()) {
            currentRow = sheet.getRow(currentRowIndex);
            return true;
        }
        return false;
    }

    public String getColumnValue(int idx) {
        return currentRow[idx];
    }

}
