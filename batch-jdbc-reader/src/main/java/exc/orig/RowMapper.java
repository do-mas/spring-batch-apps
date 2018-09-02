package exc.orig;

public interface RowMapper<T> {
    T mapRow(ExcelRowSet rs) throws Exception;
}
