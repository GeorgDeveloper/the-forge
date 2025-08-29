package ru.georgdeveloper.myapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.Position;
import ru.georgdeveloper.myapp.domain.Profession;
import ru.georgdeveloper.myapp.domain.SafetyInstruction;
import ru.georgdeveloper.myapp.repository.SafetyInstructionRepository;
import ru.georgdeveloper.myapp.service.impl.SafetyInstructionServiceImpl;

/**
 * Unit tests for the {@link SafetyInstructionService}.
 */
@ExtendWith(MockitoExtension.class)
class SafetyInstructionServiceTest {

    @Mock
    private SafetyInstructionRepository safetyInstructionRepository;

    @InjectMocks
    private SafetyInstructionServiceImpl safetyInstructionService;

    private SafetyInstruction safetyInstruction;
    private Profession profession;
    private Position position;

    @BeforeEach
    void setUp() {
        // Создаем тестовые данные
        profession = new Profession();
        profession.setId(1L);
        profession.setProfessionName("Test Profession");

        position = new Position();
        position.setId(1L);
        position.setPositionName("Test Position");

        safetyInstruction = new SafetyInstruction();
        safetyInstruction.setId(1L);
        safetyInstruction.setInstructionName("Test Instruction");
        safetyInstruction.setIntroductionDate(LocalDate.now());
        safetyInstruction.setProfession(profession);
        safetyInstruction.setPosition(position);
    }

    @Test
    void testFindOne() {
        // Подготавливаем мок
        when(safetyInstructionRepository.findWithProfessionAndPositionById(1L)).thenReturn(Optional.of(safetyInstruction));

        // Выполняем тест
        Optional<SafetyInstruction> result = safetyInstructionService.findOne(1L);

        // Проверяем результат
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getProfession().getProfessionName()).isEqualTo("Test Profession");
        assertThat(result.get().getPosition().getPositionName()).isEqualTo("Test Position");

        // Проверяем, что вызван правильный метод репозитория
        verify(safetyInstructionRepository).findWithProfessionAndPositionById(1L);
    }

    @Test
    void testFindAll() {
        // Подготавливаем мок
        Pageable pageable = PageRequest.of(0, 10);
        Page<SafetyInstruction> page = new PageImpl<>(java.util.List.of(safetyInstruction));
        when(safetyInstructionRepository.findAllWithProfessionAndPosition(pageable)).thenReturn(page);

        // Выполняем тест
        Page<SafetyInstruction> result = safetyInstructionService.findAll(pageable);

        // Проверяем результат
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(result.getContent().get(0).getProfession().getProfessionName()).isEqualTo("Test Profession");
        assertThat(result.getContent().get(0).getPosition().getPositionName()).isEqualTo("Test Position");

        // Проверяем, что вызван правильный метод репозитория
        verify(safetyInstructionRepository).findAllWithProfessionAndPosition(pageable);
    }

    @Test
    void testSave() {
        // Подготавливаем мок
        when(safetyInstructionRepository.save(any(SafetyInstruction.class))).thenReturn(safetyInstruction);

        // Выполняем тест
        SafetyInstruction result = safetyInstructionService.save(safetyInstruction);

        // Проверяем результат
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getInstructionName()).isEqualTo("Test Instruction");

        // Проверяем, что вызван метод репозитория
        verify(safetyInstructionRepository).save(safetyInstruction);
    }

    @Test
    void testUpdate() {
        // Подготавливаем мок
        when(safetyInstructionRepository.save(any(SafetyInstruction.class))).thenReturn(safetyInstruction);

        // Выполняем тест
        SafetyInstruction result = safetyInstructionService.update(safetyInstruction);

        // Проверяем результат
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getInstructionName()).isEqualTo("Test Instruction");

        // Проверяем, что вызван метод репозитория
        verify(safetyInstructionRepository).save(safetyInstruction);
    }

    @Test
    void testDelete() {
        // Выполняем тест
        safetyInstructionService.delete(1L);

        // Проверяем, что вызван метод репозитория
        verify(safetyInstructionRepository).deleteById(1L);
    }
}
