package edu.trucktrack.service;

import edu.trucktrack.entity.CurrencyEntity;
import edu.trucktrack.repository.jpa.CurrencyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyJpaRepository repository;

    public List<String> getAll() {
        return repository.findAll().stream().map(CurrencyEntity::getName).toList();
    }

}
