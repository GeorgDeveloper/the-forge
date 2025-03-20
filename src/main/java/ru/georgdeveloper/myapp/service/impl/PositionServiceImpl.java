package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Position;
import ru.georgdeveloper.myapp.repository.PositionRepository;
import ru.georgdeveloper.myapp.service.PositionService;

/**
 * Service Implementation for managing {@link ru.georgdeveloper.myapp.domain.Position}.
 */
@Service
@Transactional
public class PositionServiceImpl implements PositionService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionServiceImpl.class);

    private final PositionRepository positionRepository;

    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public Position save(Position position) {
        LOG.debug("Request to save Position : {}", position);
        return positionRepository.save(position);
    }

    @Override
    public Position update(Position position) {
        LOG.debug("Request to update Position : {}", position);
        return positionRepository.save(position);
    }

    @Override
    public Optional<Position> partialUpdate(Position position) {
        LOG.debug("Request to partially update Position : {}", position);

        return positionRepository
            .findById(position.getId())
            .map(existingPosition -> {
                if (position.getPositionName() != null) {
                    existingPosition.setPositionName(position.getPositionName());
                }

                return existingPosition;
            })
            .map(positionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Position> findAll(Pageable pageable) {
        LOG.debug("Request to get all Positions");
        return positionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Position> findOne(Long id) {
        LOG.debug("Request to get Position : {}", id);
        return positionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Position : {}", id);
        positionRepository.deleteById(id);
    }
}
