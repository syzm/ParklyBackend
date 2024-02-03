package pw.react.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponse<T> {

    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
}