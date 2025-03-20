package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.ProtectiveEquipment;
import ru.georgdeveloper.myapp.repository.ProtectiveEquipmentRepository;
import ru.georgdeveloper.myapp.service.ProtectiveEquipmentService;

/**
 * Service Implementation for managing {@link ru.georgdeveloper.myapp.domain.ProtectiveEquipment}.
 */
@Service
@Transactional
public class ProtectiveEquipmentServiceImpl implements ProtectiveEquipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(ProtectiveEquipmentServiceImpl.class);

    private final ProtectiveEquipmentRepository protectiveEquipmentRepository;

    public ProtectiveEquipmentServiceImpl(ProtectiveEquipmentRepository protectiveEquipmentRepository) {
        this.protectiveEquipmentRepository = protectiveEquipmentRepository;
    }

    @Override
    public ProtectiveEquipment save(ProtectiveEquipment protectiveEquipment) {
        LOG.debug("Request to save ProtectiveEquipment : {}", protectiveEquipment);
        return protectiveEquipmentRepository.save(protectiveEquipment);
    }

    @Override
    public ProtectiveEquipment update(ProtectiveEquipment protectiveEquipment) {
        LOG.debug("Request to update ProtectiveEquipment : {}", protectiveEquipment);
        return protectiveEquipmentRepository.save(protectiveEquipment);
    }

    @Override
    public Optional<ProtectiveEquipment> partialUpdate(ProtectiveEquipment protectiveEquipment) {
        LOG.debug("Request to partially update ProtectiveEquipment : {}", protectiveEquipment);

        return protectiveEquipmentRepository
            .findById(protectiveEquipment.getId())
            .map(existingProtectiveEquipment -> {
                if (protectiveEquipment.getEquipmentName() != null) {
                    existingProtectiveEquipment.setEquipmentName(protectiveEquipment.getEquipmentName());
                }
                if (protectiveEquipment.getQuantity() != null) {
                    existingProtectiveEquipment.setQuantity(protectiveEquipment.getQuantity());
                }
                if (protectiveEquipment.getIssuanceFrequency() != null) {
                    existingProtectiveEquipment.setIssuanceFrequency(protectiveEquipment.getIssuanceFrequency());
                }

                return existingProtectiveEquipment;
            })
            .map(protectiveEquipmentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProtectiveEquipment> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProtectiveEquipments");
        return protectiveEquipmentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProtectiveEquipment> findOne(Long id) {
        LOG.debug("Request to get ProtectiveEquipment : {}", id);
        return protectiveEquipmentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProtectiveEquipment : {}", id);
        protectiveEquipmentRepository.deleteById(id);
    }
}
