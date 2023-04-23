package edu.trucktrack.api.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class PageResponseDTO<T> {

    private final List<T> content;

    private int page;

    private int size;
}
