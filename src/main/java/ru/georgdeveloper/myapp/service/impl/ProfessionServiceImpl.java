package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Profession;
import ru.georgdeveloper.myapp.repository.ProfessionRepository;
import ru.georgdeveloper.myapp.service.ProfessionService;

/**
 * Service Implementation for managing {@link ru.georgdeveloper.myapp.domain.Profession}.
 */
@Service
@Transactional
public class ProfessionServiceImpl implements ProfessionService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionServiceImpl.class);

    private final ProfessionRepository professionRepository;

    public ProfessionServiceImpl(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    @Override
    public Profession save(Profession profession) {
        LOG.debug("Request to save Profession : {}", profession);
        return professionRepository.save(profession);
    }

    @Override
    public Profession update(Profession profession) {
        LOG.debug("Request to update Profession : {}", profession);
        return professionRepository.save(profession);
    }

    @Override
    public Optional<Profession> partialUpdate(Profession profession) {
        LOG.debug("Request to partially update Profession : {}", profession);

        return professionRepository
            .findById(profession.getId())
            .map(existingProfession -> {
                if (profession.getProfessionName() != null) {
                    existingProfession.setProfessionName(profession.getProfessionName());
                }

                return existingProfession;
            })
            .map(professionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Profession> findAll(Pageable pageable) {
        LOG.debug("Request to get all Professions");
        return professionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Profession> findOne(Long id) {
        LOG.debug("Request to get Profession : {}", id);
        return professionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Profession : {}", id);
        professionRepository.deleteById(id);
    }
}
