package ru.georgdeveloper.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.georgdeveloper.myapp.domain.TrainingAsserts.*;
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
import ru.georgdeveloper.myapp.domain.Training;
import ru.georgdeveloper.myapp.repository.TrainingRepository;

/**
 * Integration tests for the {@link TrainingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainingResourceIT {

    private static final String DEFAULT_TRAINING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRAINING_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_TRAINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_TRAINING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_VALIDITY_PERIOD = 1;
    private static final Integer UPDATED_VALIDITY_PERIOD = 2;

    private static final LocalDate DEFAULT_NEXT_TRAINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_TRAINING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/trainings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingMockMvc;

    private Training training;

    private Training insertedTraining;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Training createEntity() {
        return new Training()
            .trainingName(DEFAULT_TRAINING_NAME)
            .lastTrainingDate(DEFAULT_LAST_TRAINING_DATE)
            .validityPeriod(DEFAULT_VALIDITY_PERIOD)
            .nextTrainingDate(DEFAULT_NEXT_TRAINING_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Training createUpdatedEntity() {
        return new Training()
            .trainingName(UPDATED_TRAINING_NAME)
            .lastTrainingDate(UPDATED_LAST_TRAINING_DATE)
            .validityPeriod(UPDATED_VALIDITY_PERIOD)
            .nextTrainingDate(UPDATED_NEXT_TRAINING_DATE);
    }

    @BeforeEach
    public void initTest() {
        training = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTraining != null) {
            trainingRepository.delete(insertedTraining);
            insertedTraining = null;
        }
    }

    @Test
    @Transactional
    void createTraining() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Training
        var returnedTraining = om.readValue(
            restTrainingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(training)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Training.class
        );

        // Validate the Training in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTrainingUpdatableFieldsEquals(returnedTraining, getPersistedTraining(returnedTraining));

        insertedTraining = returnedTraining;
    }

    @Test
    @Transactional
    void createTrainingWithExistingId() throws Exception {
        // Create the Training with an existing ID
        training.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(training)))
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrainingNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        training.setTrainingName(null);

        // Create the Training, which fails.

        restTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(training)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastTrainingDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        training.setLastTrainingDate(null);

        // Create the Training, which fails.

        restTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(training)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValidityPeriodIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        training.setValidityPeriod(null);

        // Create the Training, which fails.

        restTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(training)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrainings() throws Exception {
        // Initialize the database
        insertedTraining = trainingRepository.saveAndFlush(training);

        // Get all the trainingList
        restTrainingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(training.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingName").value(hasItem(DEFAULT_TRAINING_NAME)))
            .andExpect(jsonPath("$.[*].lastTrainingDate").value(hasItem(DEFAULT_LAST_TRAINING_DATE.toString())))
            .andExpect(jsonPath("$.[*].validityPeriod").value(hasItem(DEFAULT_VALIDITY_PERIOD)))
            .andExpect(jsonPath("$.[*].nextTrainingDate").value(hasItem(DEFAULT_NEXT_TRAINING_DATE.toString())));
    }

    @Test
    @Transactional
    void getTraining() throws Exception {
        // Initialize the database
        insertedTraining = trainingRepository.saveAndFlush(training);

        // Get the training
        restTrainingMockMvc
            .perform(get(ENTITY_API_URL_ID, training.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(training.getId().intValue()))
            .andExpect(jsonPath("$.trainingName").value(DEFAULT_TRAINING_NAME))
            .andExpect(jsonPath("$.lastTrainingDate").value(DEFAULT_LAST_TRAINING_DATE.toString()))
            .andExpect(jsonPath("$.validityPeriod").value(DEFAULT_VALIDITY_PERIOD))
            .andExpect(jsonPath("$.nextTrainingDate").value(DEFAULT_NEXT_TRAINING_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTraining() throws Exception {
        // Get the training
        restTrainingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTraining() throws Exception {
        // Initialize the database
        insertedTraining = trainingRepository.saveAndFlush(training);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the training
        Training updatedTraining = trainingRepository.findById(training.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTraining are not directly saved in db
        em.detach(updatedTraining);
        updatedTraining
            .trainingName(UPDATED_TRAINING_NAME)
            .lastTrainingDate(UPDATED_LAST_TRAINING_DATE)
            .validityPeriod(UPDATED_VALIDITY_PERIOD)
            .nextTrainingDate(UPDATED_NEXT_TRAINING_DATE);

        restTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTraining.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTraining))
            )
            .andExpect(status().isOk());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrainingToMatchAllProperties(updatedTraining);
    }

    @Test
    @Transactional
    void putNonExistingTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, training.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(training))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(training))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(training)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingWithPatch() throws Exception {
        // Initialize the database
        insertedTraining = trainingRepository.saveAndFlush(training);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the training using partial update
        Training partialUpdatedTraining = new Training();
        partialUpdatedTraining.setId(training.getId());

        partialUpdatedTraining.lastTrainingDate(UPDATED_LAST_TRAINING_DATE).nextTrainingDate(UPDATED_NEXT_TRAINING_DATE);

        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTraining))
            )
            .andExpect(status().isOk());

        // Validate the Training in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTraining, training), getPersistedTraining(training));
    }

    @Test
    @Transactional
    void fullUpdateTrainingWithPatch() throws Exception {
        // Initialize the database
        insertedTraining = trainingRepository.saveAndFlush(training);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the training using partial update
        Training partialUpdatedTraining = new Training();
        partialUpdatedTraining.setId(training.getId());

        partialUpdatedTraining
            .trainingName(UPDATED_TRAINING_NAME)
            .lastTrainingDate(UPDATED_LAST_TRAINING_DATE)
            .validityPeriod(UPDATED_VALIDITY_PERIOD)
            .nextTrainingDate(UPDATED_NEXT_TRAINING_DATE);

        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTraining))
            )
            .andExpect(status().isOk());

        // Validate the Training in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainingUpdatableFieldsEquals(partialUpdatedTraining, getPersistedTraining(partialUpdatedTraining));
    }

    @Test
    @Transactional
    void patchNonExistingTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, training.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(training))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(training))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(training)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTraining() throws Exception {
        // Initialize the database
        insertedTraining = trainingRepository.saveAndFlush(training);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the training
        restTrainingMockMvc
            .perform(delete(ENTITY_API_URL_ID, training.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trainingRepository.count();
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

    protected Training getPersistedTraining(Training training) {
        return trainingRepository.findById(training.getId()).orElseThrow();
    }

    protected void assertPersistedTrainingToMatchAllProperties(Training expectedTraining) {
        assertTrainingAllPropertiesEquals(expectedTraining, getPersistedTraining(expectedTraining));
    }

    protected void assertPersistedTrainingToMatchUpdatableProperties(Training expectedTraining) {
        assertTrainingAllUpdatablePropertiesEquals(expectedTraining, getPersistedTraining(expectedTraining));
    }
}
