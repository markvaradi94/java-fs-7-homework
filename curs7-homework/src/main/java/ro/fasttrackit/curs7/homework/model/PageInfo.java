package ro.fasttrackit.curs7.homework.model;

import lombok.Builder;

@Builder
public record PageInfo(
        int totalPages,
        int totalElements,
        int crtPage,
        int pageSize
) {
}
