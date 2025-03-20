package ru.georgdeveloper.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.georgdeveloper.myapp.domain.SafetyInstructionAsserts.*;
import static ru.georgdeveloper.myapp.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.georgdeveloper.myapp.IntegrationTest;
import ru.georgdeveloper.myapp.domain.SafetyInstruction;
import ru.georgdeveloper.myapp.repository.SafetyInstructionRepository;

/**
 * Integration tests for the {@link SafetyInstructionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SafetyInstructionResourceIT {

    private static final String DEFAULT_INSTRUCTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTION_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INTRODUCTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INTRODUCTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/safety-instructions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SafetyInstructionRepository safetyInstructionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSafetyInstructionMockMvc;

    private SafetyInstruction safetyInstruction;

    private SafetyInstruction insertedSafetyInstruction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SafetyInstruction createEntity() {
        return new SafetyInstruction().instructionName(DEFAULT_INSTRUCTION_NAME).introductionDate(DEFAULT_INTRODUCTION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SafetyInstruction createUpdatedEntity() {
        return new SafetyInstruction().instructionName(UPDATED_INSTRUCTION_NAME).introductionDate(UPDATED_INTRODUCTION_DATE);
    }

    @BeforeEach
    public void initTest() {
        safetyInstruction = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSafetyInstruction != null) {
            safetyInstructionRepository.delete(insertedSafetyInstruction);
            insertedSafetyInstruction = null;
        }
    }

    @Test
    @Transactional
    void createSafetyInstruction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SafetyInstruction
        var returnedSafetyInstruction = om.readValue(
            restSafetyInstructionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(safetyInstruction)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SafetyInstruction.class
        );

        // Validate the SafetyInstruction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSafetyInstructionUpdatableFieldsEquals(returnedSafetyInstruction, getPersistedSafetyInstruction(returnedSafetyInstruction));

        insertedSafetyInstruction = returnedSafetyInstruction;
    }

    @Test
    @Transactional
    void createSafetyInstructionWithExistingId() throws Exception {
        // Create the SafetyInstruction with an existing ID
        safetyInstruction.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSafetyInstructionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(safetyInstruction)))
            .andExpect(status().isBadRequest());

        // Validate the SafetyInstruction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInstructionNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        safetyInstruction.setInstructionName(null);

        // Create the SafetyInstruction, which fails.

        restSafetyInstructionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(safetyInstruction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIntroductionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        safetyInstruction.setIntroductionDate(null);

        // Create the SafetyInstruction, which fails.

        restSafetyInstructionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(safetyInstruction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSafetyInstructions() throws Exception {
        // Initialize the database
        insertedSafetyInstruction = safetyInstructionRepository.saveAndFlush(safetyInstruction);

        // Get all the safetyInstructionList
        restSafetyInstructionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(safetyInstruction.getId().intValue())))
            .andExpect(jsonPath("$.[*].instructionName").value(hasItem(DEFAULT_INSTRUCTION_NAME)))
            .andExpect(jsonPath("$.[*].introductionDate").value(hasItem(DEFAULT_INTRODUCTION_DATE.toString())));
    }

    @Test
    @Transactional
    void getSafetyInstruction() throws Exception {
        // Initialize the database
        insertedSafetyInstruction = safetyInstructionRepository.saveAndFlush(safetyInstruction);

        // Get the safetyInstruction
        restSafetyInstructionMockMvc
            .perform(get(ENTITY_API_URL_ID, safetyInstruction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(safetyInstruction.getId().intValue()))
            .andExpect(jsonPath("$.instructionName").value(DEFAULT_INSTRUCTION_NAME))
            .andExpect(jsonPath("$.introductionDate").value(DEFAULT_INTRODUCTION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSafetyInstruction() throws Exception {
        // Get the safetyInstruction
        restSafetyInstructionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSafetyInstruction() throws Exception {
        // Initialize the database
        insertedSafetyInstruction = safetyInstructionRepository.saveAndFlush(safetyInstruction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the safetyInstruction
        SafetyInstruction updatedSafetyInstruction = safetyInstructionRepository.findById(safetyInstruction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSafetyInstruction are not directly saved in db
        em.detach(updatedSafetyInstruction);
        updatedSafetyInstruction.instructionName(UPDATED_INSTRUCTION_NAME).introductionDate(UPDATED_INTRODUCTION_DATE);

        restSafetyInstructionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSafetyInstruction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSafetyInstruction))
            )
            .andExpect(status().isOk());

        // Validate the SafetyInstruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSafetyInstructionToMatchAllProperties(updatedSafetyInstruction);
    }

    @Test
    @Transactional
    void putNonExistingSafetyInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        safetyInstruction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSafetyInstructionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, safetyInstruction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(safetyInstruction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SafetyInstruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSafetyInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        safetyInstruction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSafetyInstructionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(safetyInstruction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SafetyInstruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSafetyInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        safetyInstruction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSafetyInstructionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(safetyInstruction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SafetyInstruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSafetyInstructionWithPatch() throws Exception {
        // Initialize the database
        insertedSafetyInstruction = safetyInstructionRepository.saveAndFlush(safetyInstruction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the safetyInstruction using partial update
        SafetyInstruction partialUpdatedSafetyInstruction = new SafetyInstruction();
        partialUpdatedSafetyInstruction.setId(safetyInstruction.getId());

        partialUpdatedSafetyInstruction.instructionName(UPDATED_INSTRUCTION_NAME).introductionDate(UPDATED_INTRODUCTION_DATE);

        restSafetyInstructionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSafetyInstruction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSafetyInstruction))
            )
            .andExpect(status().isOk());

        // Validate the SafetyInstruction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSafetyInstructionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSafetyInstruction, safetyInstruction),
            getPersistedSafetyInstruction(safetyInstruction)
        );
    }

    @Test
    @Transactional
    void fullUpdateSafetyInstructionWithPatch() throws Exception {
        // Initialize the database
        insertedSafetyInstruction = safetyInstructionRepository.saveAndFlush(safetyInstruction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the safetyInstruction using partial update
        SafetyInstruction partialUpdatedSafetyInstruction = new SafetyInstruction();
        partialUpdatedSafetyInstruction.setId(safetyInstruction.getId());

        partialUpdatedSafetyInstruction.instructionName(UPDATED_INSTRUCTION_NAME).introductionDate(UPDATED_INTRODUCTION_DATE);

        restSafetyInstructionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSafetyInstruction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSafetyInstruction))
            )
            .andExpect(status().isOk());

        // Validate the SafetyInstruction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSafetyInstructionUpdatableFieldsEquals(
            partialUpdatedSafetyInstruction,
            getPersistedSafetyInstruction(partialUpdatedSafetyInstruction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSafetyInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        safetyInstruction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSafetyInstructionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, safetyInstruction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(safetyInstruction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SafetyInstruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSafetyInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        safetyInstruction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSafetyInstructionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(safetyInstruction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SafetyInstruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSafetyInstruction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        safetyInstruction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSafetyInstructionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(safetyInstruction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SafetyInstruction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSafetyInstruction() throws Exception {
        // Initialize the database
        insertedSafetyInstruction = safetyInstructionRepository.saveAndFlush(safetyInstruction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the safetyInstruction
        restSafetyInstructionMockMvc
            .perform(delete(ENTITY_API_URL_ID, safetyInstruction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return safetyInstructionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected SafetyInstruction getPersistedSafetyInstruction(SafetyInstruction safetyInstruction) {
        return safetyInstructionRepository.findById(safetyInstruction.getId()).orElseThrow();
    }

    protected void assertPersistedSafetyInstructionToMatchAllProperties(SafetyInstruction expectedSafetyInstruction) {
        assertSafetyInstructionAllPropertiesEquals(expectedSafetyInstruction, getPersistedSafetyInstruction(expectedSafetyInstruction));
    }

    protected void assertPersistedSafetyInstructionToMatchUpdatableProperties(SafetyInstruction expectedSafetyInstruction) {
        assertSafetyInstructionAllUpdatablePropertiesEquals(
            expectedSafetyInstruction,
            getPersistedSafetyInstruction(expectedSafetyInstruction)
        );
    }
}
