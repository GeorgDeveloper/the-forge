package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Team;
import ru.georgdeveloper.myapp.repository.TeamRepository;
import ru.georgdeveloper.myapp.service.TeamService;

/**
 * Service Implementation for managing {@link ru.georgdeveloper.myapp.domain.Team}.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private static final Logger LOG = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team save(Team team) {
        LOG.debug("Request to save Team : {}", team);
        return teamRepository.save(team);
    }

    @Override
    public Team update(Team team) {
        LOG.debug("Request to update Team : {}", team);
        return teamRepository.save(team);
    }

    @Override
    public Optional<Team> partialUpdate(Team team) {
        LOG.debug("Request to partially update Team : {}", team);

        return teamRepository
            .findById(team.getId())
            .map(existingTeam -> {
                if (team.getTeamName() != null) {
                    existingTeam.setTeamName(team.getTeamName());
                }

                return existingTeam;
            })
            .map(teamRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Team> findAll(Pageable pageable) {
        LOG.debug("Request to get all Teams");
        return teamRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Team> findOne(Long id) {
        LOG.debug("Request to get Team : {}", id);
        return teamRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Team : {}", id);
        teamRepository.deleteById(id);
    }
}
