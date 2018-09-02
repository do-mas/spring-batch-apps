package exc;

import org.apache.poi.ss.usermodel.Row;

public interface SuperRowMapper<T> {
    T mapRow(Row rs) throws Exception;
}
