package ru.georgdeveloper.myapp.web.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.georgdeveloper.myapp.domain.Task;
import ru.georgdeveloper.myapp.repository.TaskRepository;

@RestController
@RequestMapping("/api/calendar-tasks")
public class CalendarTaskResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaskResource.class);

    private final TaskRepository taskRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CalendarTaskResource(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<Task>> getAllTasksForCalendar() {
        LOG.debug("REST request to get all Tasks for calendar");
        List<Task> tasks = taskRepository.findAll();
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/events") // Полный путь: /api/calendar/events
    public ResponseEntity<List<Task>> getAllCalendarEvents() {
        return ResponseEntity.ok().body(null);
    }
}
