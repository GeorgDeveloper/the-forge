package ru.georgdeveloper.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.georgdeveloper.myapp.domain.AdditionalTrainingAsserts.*;
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
import ru.georgdeveloper.myapp.domain.AdditionalTraining;
import ru.georgdeveloper.myapp.repository.AdditionalTrainingRepository;

/**
 * Integration tests for the {@link AdditionalTrainingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdditionalTrainingResourceIT {

    private static final String DEFAULT_TRAINING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRAINING_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRAINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRAINING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_VALIDITY_PERIOD = 1;
    private static final Integer UPDATED_VALIDITY_PERIOD = 2;

    private static final LocalDate DEFAULT_NEXT_TRAINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_TRAINING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/additional-trainings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdditionalTrainingRepository additionalTrainingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdditionalTrainingMockMvc;

    private AdditionalTraining additionalTraining;

    private AdditionalTraining insertedAdditionalTraining;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdditionalTraining createEntity() {
        return new AdditionalTraining()
            .trainingName(DEFAULT_TRAINING_NAME)
            .trainingDate(DEFAULT_TRAINING_DATE)
            .validityPeriod(DEFAULT_VALIDITY_PERIOD)
            .nextTrainingDate(DEFAULT_NEXT_TRAINING_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdditionalTraining createUpdatedEntity() {
        return new AdditionalTraining()
            .trainingName(UPDATED_TRAINING_NAME)
            .trainingDate(UPDATED_TRAINING_DATE)
            .validityPeriod(UPDATED_VALIDITY_PERIOD)
            .nextTrainingDate(UPDATED_NEXT_TRAINING_DATE);
    }

    @BeforeEach
    public void initTest() {
        additionalTraining = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAdditionalTraining != null) {
            additionalTrainingRepository.delete(insertedAdditionalTraining);
            insertedAdditionalTraining = null;
        }
    }

    @Test
    @Transactional
    void createAdditionalTraining() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AdditionalTraining
        var returnedAdditionalTraining = om.readValue(
            restAdditionalTrainingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalTraining)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdditionalTraining.class
        );

        // Validate the AdditionalTraining in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAdditionalTrainingUpdatableFieldsEquals(
            returnedAdditionalTraining,
            getPersistedAdditionalTraining(returnedAdditionalTraining)
        );

        insertedAdditionalTraining = returnedAdditionalTraining;
    }

    @Test
    @Transactional
    void createAdditionalTrainingWithExistingId() throws Exception {
        // Create the AdditionalTraining with an existing ID
        additionalTraining.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdditionalTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalTraining)))
            .andExpect(status().isBadRequest());

        // Validate the AdditionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrainingNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        additionalTraining.setTrainingName(null);

        // Create the AdditionalTraining, which fails.

        restAdditionalTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalTraining)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTrainingDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        additionalTraining.setTrainingDate(null);

        // Create the AdditionalTraining, which fails.

        restAdditionalTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalTraining)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValidityPeriodIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        additionalTraining.setValidityPeriod(null);

        // Create the AdditionalTraining, which fails.

        restAdditionalTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalTraining)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdditionalTrainings() throws Exception {
        // Initialize the database
        insertedAdditionalTraining = additionalTrainingRepository.saveAndFlush(additionalTraining);

        // Get all the additionalTrainingList
        restAdditionalTrainingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(additionalTraining.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingName").value(hasItem(DEFAULT_TRAINING_NAME)))
            .andExpect(jsonPath("$.[*].trainingDate").value(hasItem(DEFAULT_TRAINING_DATE.toString())))
            .andExpect(jsonPath("$.[*].validityPeriod").value(hasItem(DEFAULT_VALIDITY_PERIOD)))
            .andExpect(jsonPath("$.[*].nextTrainingDate").value(hasItem(DEFAULT_NEXT_TRAINING_DATE.toString())));
    }

    @Test
    @Transactional
    void getAdditionalTraining() throws Exception {
        // Initialize the database
        insertedAdditionalTraining = additionalTrainingRepository.saveAndFlush(additionalTraining);

        // Get the additionalTraining
        restAdditionalTrainingMockMvc
            .perform(get(ENTITY_API_URL_ID, additionalTraining.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(additionalTraining.getId().intValue()))
            .andExpect(jsonPath("$.trainingName").value(DEFAULT_TRAINING_NAME))
            .andExpect(jsonPath("$.trainingDate").value(DEFAULT_TRAINING_DATE.toString()))
            .andExpect(jsonPath("$.validityPeriod").value(DEFAULT_VALIDITY_PERIOD))
            .andExpect(jsonPath("$.nextTrainingDate").value(DEFAULT_NEXT_TRAINING_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAdditionalTraining() throws Exception {
        // Get the additionalTraining
        restAdditionalTrainingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdditionalTraining() throws Exception {
        // Initialize the database
        insertedAdditionalTraining = additionalTrainingRepository.saveAndFlush(additionalTraining);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the additionalTraining
        AdditionalTraining updatedAdditionalTraining = additionalTrainingRepository.findById(additionalTraining.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdditionalTraining are not directly saved in db
        em.detach(updatedAdditionalTraining);
        updatedAdditionalTraining
            .trainingName(UPDATED_TRAINING_NAME)
            .trainingDate(UPDATED_TRAINING_DATE)
            .validityPeriod(UPDATED_VALIDITY_PERIOD)
            .nextTrainingDate(UPDATED_NEXT_TRAINING_DATE);

        restAdditionalTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdditionalTraining.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAdditionalTraining))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdditionalTrainingToMatchAllProperties(updatedAdditionalTraining);
    }

    @Test
    @Transactional
    void putNonExistingAdditionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalTraining.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdditionalTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, additionalTraining.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(additionalTraining))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdditionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalTraining.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(additionalTraining))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdditionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalTraining.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalTrainingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalTraining)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdditionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdditionalTrainingWithPatch() throws Exception {
        // Initialize the database
        insertedAdditionalTraining = additionalTrainingRepository.saveAndFlush(additionalTraining);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the additionalTraining using partial update
        AdditionalTraining partialUpdatedAdditionalTraining = new AdditionalTraining();
        partialUpdatedAdditionalTraining.setId(additionalTraining.getId());

        partialUpdatedAdditionalTraining.trainingDate(UPDATED_TRAINING_DATE).nextTrainingDate(UPDATED_NEXT_TRAINING_DATE);

        restAdditionalTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdditionalTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdditionalTraining))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalTraining in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdditionalTrainingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdditionalTraining, additionalTraining),
            getPersistedAdditionalTraining(additionalTraining)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdditionalTrainingWithPatch() throws Exception {
        // Initialize the database
        insertedAdditionalTraining = additionalTrainingRepository.saveAndFlush(additionalTraining);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the additionalTraining using partial update
        AdditionalTraining partialUpdatedAdditionalTraining = new AdditionalTraining();
        partialUpdatedAdditionalTraining.setId(additionalTraining.getId());

        partialUpdatedAdditionalTraining
            .trainingName(UPDATED_TRAINING_NAME)
            .trainingDate(UPDATED_TRAINING_DATE)
            .validityPeriod(UPDATED_VALIDITY_PERIOD)
            .nextTrainingDate(UPDATED_NEXT_TRAINING_DATE);

        restAdditionalTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdditionalTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdditionalTraining))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalTraining in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdditionalTrainingUpdatableFieldsEquals(
            partialUpdatedAdditionalTraining,
            getPersistedAdditionalTraining(partialUpdatedAdditionalTraining)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAdditionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalTraining.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdditionalTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, additionalTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(additionalTraining))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdditionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalTraining.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(additionalTraining))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdditionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalTraining.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalTrainingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(additionalTraining)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdditionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdditionalTraining() throws Exception {
        // Initialize the database
        insertedAdditionalTraining = additionalTrainingRepository.saveAndFlush(additionalTraining);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the additionalTraining
        restAdditionalTrainingMockMvc
            .perform(delete(ENTITY_API_URL_ID, additionalTraining.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return additionalTrainingRepository.count();
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

    protected AdditionalTraining getPersistedAdditionalTraining(AdditionalTraining additionalTraining) {
        return additionalTrainingRepository.findById(additionalTraining.getId()).orElseThrow();
    }

    protected void assertPersistedAdditionalTrainingToMatchAllProperties(AdditionalTraining expectedAdditionalTraining) {
        assertAdditionalTrainingAllPropertiesEquals(expectedAdditionalTraining, getPersistedAdditionalTraining(expectedAdditionalTraining));
    }

    protected void assertPersistedAdditionalTrainingToMatchUpdatableProperties(AdditionalTraining expectedAdditionalTraining) {
        assertAdditionalTrainingAllUpdatablePropertiesEquals(
            expectedAdditionalTraining,
            getPersistedAdditionalTraining(expectedAdditionalTraining)
        );
    }
}
