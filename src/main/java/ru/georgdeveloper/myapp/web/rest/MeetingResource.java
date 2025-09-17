package ru.georgdeveloper.myapp.web.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.georgdeveloper.myapp.domain.Meeting;
import ru.georgdeveloper.myapp.repository.MeetingRepository;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/meetings")
public class MeetingResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeetingRepository meetingRepository;

    public MeetingResource(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    @PostMapping("")
    public ResponseEntity<Meeting> create(@Valid @RequestBody Meeting meeting) throws URISyntaxException {
        Meeting result = meetingRepository.save(meeting);
        return ResponseEntity.created(new URI("/api/meetings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "meeting", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meeting> update(@PathVariable Long id, @Valid @RequestBody Meeting meeting) {
        meeting.setId(id);
        Meeting result = meetingRepository.save(meeting);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "meeting", result.getId().toString()))
            .body(result);
    }

    @GetMapping("")
    public List<Meeting> list() {
        return meetingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meeting> get(@PathVariable Long id) {
        Optional<Meeting> entity = meetingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        meetingRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, "meeting", id.toString()))
            .build();
    }
}
