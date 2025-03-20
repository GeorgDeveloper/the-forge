package ru.georgdeveloper.myapp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Task;
import ru.georgdeveloper.myapp.repository.TaskRepository;
import ru.georgdeveloper.myapp.service.TaskService;

/**
 * Service Implementation for managing {@link ru.georgdeveloper.myapp.domain.Task}.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        LOG.debug("Request to save Task : {}", task);
        return taskRepository.save(task);
    }

    @Override
    public Task update(Task task) {
        LOG.debug("Request to update Task : {}", task);
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> partialUpdate(Task task) {
        LOG.debug("Request to partially update Task : {}", task);

        return taskRepository
            .findById(task.getId())
            .map(existingTask -> {
                if (task.getTaskName() != null) {
                    existingTask.setTaskName(task.getTaskName());
                }
                if (task.getCreationDate() != null) {
                    existingTask.setCreationDate(task.getCreationDate());
                }
                if (task.getPlannedCompletionDate() != null) {
                    existingTask.setPlannedCompletionDate(task.getPlannedCompletionDate());
                }
                if (task.getStatus() != null) {
                    existingTask.setStatus(task.getStatus());
                }
                if (task.getPriority() != null) {
                    existingTask.setPriority(task.getPriority());
                }
                if (task.getBody() != null) {
                    existingTask.setBody(task.getBody());
                }

                return existingTask;
            })
            .map(taskRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> findAll(Pageable pageable) {
        LOG.debug("Request to get all Tasks");
        return taskRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Task> findOne(Long id) {
        LOG.debug("Request to get Task : {}", id);
        return taskRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }
}
