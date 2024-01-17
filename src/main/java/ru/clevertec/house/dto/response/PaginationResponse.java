package ru.clevertec.house.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse<T> {

    private int currentPage;
    private int totalPages;
    private long totalItems;
    private List<T> data;
}
