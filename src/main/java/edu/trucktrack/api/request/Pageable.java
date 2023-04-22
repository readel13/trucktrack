package edu.trucktrack.api.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Pageable {

    private int page;

    private int size;
}
