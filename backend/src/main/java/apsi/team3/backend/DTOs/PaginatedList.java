package apsi.team3.backend.DTOs;

import java.util.List;

import lombok.Value;

@Value
public class PaginatedList<T>{
    public List<T> items;
    public int pageIndex;
    public Long totalItems;
    public int totalPages;
}
