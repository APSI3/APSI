package apsi.team3.backend.helpers;

import apsi.team3.backend.exceptions.ApsiValidationException;

import java.time.LocalDate;

public class PaginationValidator {
    public static void validatePaginationArgs(LocalDate from, LocalDate to, int pageIndex) throws ApsiValidationException{
        if (from == null)
            throw new ApsiValidationException("Należy podać datę początkową", "from");
        if (to == null)
            throw new ApsiValidationException("Należy podać datę końcową", "to");
        if (from.isAfter(to))
            throw new ApsiValidationException("Data końcowa nie może być mniejsza niż początkowa", "to");
        if (pageIndex < 0)
            throw new ApsiValidationException("Indeks strony nie może być ujemny", "pageIndex");
    }
}
