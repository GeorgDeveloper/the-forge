package ru.georgdeveloper.myapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.domain.Position;
import ru.georgdeveloper.myapp.domain.Profession;
import ru.georgdeveloper.myapp.domain.SafetyInstruction;

/**
 * Integration tests for the {@link SafetyInstructionRepository}.
 */
@DataJpaTest
@Transactional
class SafetyInstructionRepositoryTest {

    @Autowired
    private SafetyInstructionRepository safetyInstructionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private SafetyInstruction safetyInstruction;
    private Profession profession;
    private Position position;

    @BeforeEach
    void setUp() {
        // Создаем тестовые данные
        profession = new Profession();
        profession.setProfessionName("Test Profession");
        profession = entityManager.persistAndFlush(profession);

        position = new Position();
        position.setPositionName("Test Position");
        position = entityManager.persistAndFlush(position);

        safetyInstruction = new SafetyInstruction();
        safetyInstruction.setInstructionName("Test Instruction");
        safetyInstruction.setIntroductionDate(LocalDate.now());
        safetyInstruction.setProfession(profession);
        safetyInstruction.setPosition(position);
    }

    @Test
    void testFindWithProfessionAndPositionById() {
        // Сохраняем сущность
        SafetyInstruction saved = safetyInstructionRepository.saveAndFlush(safetyInstruction);
        entityManager.clear(); // Очищаем кэш

        // Тестируем новый метод
        Optional<SafetyInstruction> found = safetyInstructionRepository.findWithProfessionAndPositionById(saved.getId());

        assertThat(found).isPresent();
        SafetyInstruction foundInstruction = found.get();

        // Проверяем, что связанные объекты загружены
        assertThat(foundInstruction.getProfession()).isNotNull();
        assertThat(foundInstruction.getProfession().getProfessionName()).isEqualTo("Test Profession");
        assertThat(foundInstruction.getPosition()).isNotNull();
        assertThat(foundInstruction.getPosition().getPositionName()).isEqualTo("Test Position");
    }

    @Test
    void testFindAllWithProfessionAndPosition() {
        // Сохраняем сущность
        safetyInstructionRepository.saveAndFlush(safetyInstruction);
        entityManager.clear(); // Очищаем кэш

        // Тестируем новый метод с пагинацией
        Page<SafetyInstruction> page = safetyInstructionRepository.findAllWithProfessionAndPosition(PageRequest.of(0, 10));

        assertThat(page.getContent()).isNotEmpty();
        SafetyInstruction foundInstruction = page.getContent().get(0);

        // Проверяем, что связанные объекты загружены
        assertThat(foundInstruction.getProfession()).isNotNull();
        assertThat(foundInstruction.getProfession().getProfessionName()).isEqualTo("Test Profession");
        assertThat(foundInstruction.getPosition()).isNotNull();
        assertThat(foundInstruction.getPosition().getPositionName()).isEqualTo("Test Position");
    }

    @Test
    void testFindByIdWithoutRelations() {
        // Сохраняем сущность
        SafetyInstruction saved = safetyInstructionRepository.saveAndFlush(safetyInstruction);
        entityManager.clear(); // Очищаем кэш

        // Тестируем стандартный метод (должен работать как раньше)
        Optional<SafetyInstruction> found = safetyInstructionRepository.findById(saved.getId());

        assertThat(found).isPresent();
        SafetyInstruction foundInstruction = found.get();

        // Проверяем, что связанные объекты НЕ загружены (ленивая загрузка)
        assertThat(foundInstruction.getProfession()).isNotNull(); // Прокси объект
        assertThat(foundInstruction.getPosition()).isNotNull(); // Прокси объект
    }
}
