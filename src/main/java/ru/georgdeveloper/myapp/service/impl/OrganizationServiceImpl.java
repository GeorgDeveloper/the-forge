package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Organization;
import ru.georgdeveloper.myapp.repository.OrganizationRepository;
import ru.georgdeveloper.myapp.service.OrganizationService;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization save(Organization organization) {
        LOG.debug("Сохранение организации: {}", organization);
        return organizationRepository.save(organization);
    }

    @Override
    public Organization update(Organization organization) {
        LOG.debug("Обновление организации: {}", organization);
        return organizationRepository.save(organization);
    }

    @Override
    public Optional<Organization> partialUpdate(Organization organization) {
        return organizationRepository
            .findById(organization.getId())
            .map(existing -> {
                if (organization.getFullName() != null) existing.setFullName(organization.getFullName());
                if (organization.getShortName() != null) existing.setShortName(organization.getShortName());
                if (organization.getActivityAreas() != null) existing.setActivityAreas(organization.getActivityAreas());
                if (organization.getTagline() != null) existing.setTagline(organization.getTagline());
                if (organization.getLegalAddress() != null) existing.setLegalAddress(organization.getLegalAddress());
                if (organization.getActualAddress() != null) existing.setActualAddress(organization.getActualAddress());
                if (organization.getInn() != null) existing.setInn(organization.getInn());
                if (organization.getKpp() != null) existing.setKpp(organization.getKpp());
                if (organization.getOgrn() != null) existing.setOgrn(organization.getOgrn());
                if (organization.getOkpo() != null) existing.setOkpo(organization.getOkpo());
                if (organization.getBankName() != null) existing.setBankName(organization.getBankName());
                if (organization.getBankBik() != null) existing.setBankBik(organization.getBankBik());
                if (organization.getBankCorrAccount() != null) existing.setBankCorrAccount(organization.getBankCorrAccount());
                if (organization.getBankSettlementAccount() != null) existing.setBankSettlementAccount(
                    organization.getBankSettlementAccount()
                );
                if (organization.getPhoneMain() != null) existing.setPhoneMain(organization.getPhoneMain());
                if (organization.getPhoneSales() != null) existing.setPhoneSales(organization.getPhoneSales());
                if (organization.getPhoneSupport() != null) existing.setPhoneSupport(organization.getPhoneSupport());
                if (organization.getEmailMain() != null) existing.setEmailMain(organization.getEmailMain());
                if (organization.getEmailPartners() != null) existing.setEmailPartners(organization.getEmailPartners());
                if (organization.getEmailSupport() != null) existing.setEmailSupport(organization.getEmailSupport());
                if (organization.getWebsite() != null) existing.setWebsite(organization.getWebsite());
                if (organization.getFoundedYear() != null) existing.setFoundedYear(organization.getFoundedYear());
                if (organization.getEmployeesCountRange() != null) existing.setEmployeesCountRange(organization.getEmployeesCountRange());
                if (organization.getKeyPersons() != null) existing.setKeyPersons(organization.getKeyPersons());
                if (organization.getProducts() != null) existing.setProducts(organization.getProducts());
                if (organization.getPartners() != null) existing.setPartners(organization.getPartners());
                return existing;
            })
            .map(organizationRepository::save);
    }

    @Override
    public void delete(Long id) {
        organizationRepository.deleteById(id);
    }
}
