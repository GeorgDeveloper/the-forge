package ru.georgdeveloper.myapp.web.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.georgdeveloper.myapp.domain.OtherEvent;
import ru.georgdeveloper.myapp.repository.OtherEventRepository;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/other-events")
public class OtherEventResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OtherEventRepository otherEventRepository;

    public OtherEventResource(OtherEventRepository otherEventRepository) {
        this.otherEventRepository = otherEventRepository;
    }

    @PostMapping("")
    public ResponseEntity<OtherEvent> create(@Valid @RequestBody OtherEvent other) throws URISyntaxException {
        OtherEvent result = otherEventRepository.save(other);
        return ResponseEntity.created(new URI("/api/other-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "otherEvent", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OtherEvent> update(@PathVariable Long id, @Valid @RequestBody OtherEvent other) {
        other.setId(id);
        OtherEvent result = otherEventRepository.save(other);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "otherEvent", result.getId().toString()))
            .body(result);
    }

    @GetMapping("")
    public List<OtherEvent> list() {
        return otherEventRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OtherEvent> get(@PathVariable Long id) {
        Optional<OtherEvent> entity = otherEventRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        otherEventRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, "otherEvent", id.toString()))
            .build();
    }
}
