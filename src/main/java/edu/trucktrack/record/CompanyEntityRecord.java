package edu.trucktrack.record;

import java.time.LocalDateTime;

public record CompanyEntityRecord(Long id, String name, String email, String description,
                                  String url, String address, String zipcode,
                                  LocalDateTime createdAt) {
}
