package exc.exception;

import org.springframework.batch.item.ParseException;

public class ExcelFileParseException extends ParseException {

    public ExcelFileParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
