package edu.trucktrack.api.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder(toBuilder = true)
public class SearchCriteriaRequest {

    private FilterBy filterBy;

    private Map<String, String> searchBy;

    private Map<String, OrderByDirection> orderBy;

    private Pageable pageable;
}
