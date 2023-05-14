package edu.trucktrack.dao.service;

import edu.trucktrack.dao.repository.jpa.CurrencyJpaRepository;
import edu.trucktrack.dao.entity.CurrencyEntity;
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
