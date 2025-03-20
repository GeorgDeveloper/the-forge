package ru.georgdeveloper.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.georgdeveloper.myapp.domain.JobDescriptionAsserts.*;
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
import ru.georgdeveloper.myapp.domain.JobDescription;
import ru.georgdeveloper.myapp.repository.JobDescriptionRepository;

/**
 * Integration tests for the {@link JobDescriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JobDescriptionResourceIT {

    private static final String DEFAULT_DESCRIPTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_APPROVAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPROVAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/job-descriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobDescriptionMockMvc;

    private JobDescription jobDescription;

    private JobDescription insertedJobDescription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobDescription createEntity() {
        return new JobDescription().descriptionName(DEFAULT_DESCRIPTION_NAME).approvalDate(DEFAULT_APPROVAL_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobDescription createUpdatedEntity() {
        return new JobDescription().descriptionName(UPDATED_DESCRIPTION_NAME).approvalDate(UPDATED_APPROVAL_DATE);
    }

    @BeforeEach
    public void initTest() {
        jobDescription = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedJobDescription != null) {
            jobDescriptionRepository.delete(insertedJobDescription);
            insertedJobDescription = null;
        }
    }

    @Test
    @Transactional
    void createJobDescription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the JobDescription
        var returnedJobDescription = om.readValue(
            restJobDescriptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobDescription)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            JobDescription.class
        );

        // Validate the JobDescription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertJobDescriptionUpdatableFieldsEquals(returnedJobDescription, getPersistedJobDescription(returnedJobDescription));

        insertedJobDescription = returnedJobDescription;
    }

    @Test
    @Transactional
    void createJobDescriptionWithExistingId() throws Exception {
        // Create the JobDescription with an existing ID
        jobDescription.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobDescriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobDescription)))
            .andExpect(status().isBadRequest());

        // Validate the JobDescription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobDescription.setDescriptionName(null);

        // Create the JobDescription, which fails.

        restJobDescriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobDescription)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApprovalDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        jobDescription.setApprovalDate(null);

        // Create the JobDescription, which fails.

        restJobDescriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobDescription)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJobDescriptions() throws Exception {
        // Initialize the database
        insertedJobDescription = jobDescriptionRepository.saveAndFlush(jobDescription);

        // Get all the jobDescriptionList
        restJobDescriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobDescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].descriptionName").value(hasItem(DEFAULT_DESCRIPTION_NAME)))
            .andExpect(jsonPath("$.[*].approvalDate").value(hasItem(DEFAULT_APPROVAL_DATE.toString())));
    }

    @Test
    @Transactional
    void getJobDescription() throws Exception {
        // Initialize the database
        insertedJobDescription = jobDescriptionRepository.saveAndFlush(jobDescription);

        // Get the jobDescription
        restJobDescriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, jobDescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobDescription.getId().intValue()))
            .andExpect(jsonPath("$.descriptionName").value(DEFAULT_DESCRIPTION_NAME))
            .andExpect(jsonPath("$.approvalDate").value(DEFAULT_APPROVAL_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingJobDescription() throws Exception {
        // Get the jobDescription
        restJobDescriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJobDescription() throws Exception {
        // Initialize the database
        insertedJobDescription = jobDescriptionRepository.saveAndFlush(jobDescription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobDescription
        JobDescription updatedJobDescription = jobDescriptionRepository.findById(jobDescription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJobDescription are not directly saved in db
        em.detach(updatedJobDescription);
        updatedJobDescription.descriptionName(UPDATED_DESCRIPTION_NAME).approvalDate(UPDATED_APPROVAL_DATE);

        restJobDescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJobDescription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedJobDescription))
            )
            .andExpect(status().isOk());

        // Validate the JobDescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJobDescriptionToMatchAllProperties(updatedJobDescription);
    }

    @Test
    @Transactional
    void putNonExistingJobDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobDescription.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobDescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobDescription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobDescription))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobDescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobDescription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobDescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(jobDescription))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobDescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobDescription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobDescriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(jobDescription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobDescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobDescriptionWithPatch() throws Exception {
        // Initialize the database
        insertedJobDescription = jobDescriptionRepository.saveAndFlush(jobDescription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobDescription using partial update
        JobDescription partialUpdatedJobDescription = new JobDescription();
        partialUpdatedJobDescription.setId(jobDescription.getId());

        partialUpdatedJobDescription.descriptionName(UPDATED_DESCRIPTION_NAME);

        restJobDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobDescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJobDescription))
            )
            .andExpect(status().isOk());

        // Validate the JobDescription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobDescriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedJobDescription, jobDescription),
            getPersistedJobDescription(jobDescription)
        );
    }

    @Test
    @Transactional
    void fullUpdateJobDescriptionWithPatch() throws Exception {
        // Initialize the database
        insertedJobDescription = jobDescriptionRepository.saveAndFlush(jobDescription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jobDescription using partial update
        JobDescription partialUpdatedJobDescription = new JobDescription();
        partialUpdatedJobDescription.setId(jobDescription.getId());

        partialUpdatedJobDescription.descriptionName(UPDATED_DESCRIPTION_NAME).approvalDate(UPDATED_APPROVAL_DATE);

        restJobDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobDescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJobDescription))
            )
            .andExpect(status().isOk());

        // Validate the JobDescription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJobDescriptionUpdatableFieldsEquals(partialUpdatedJobDescription, getPersistedJobDescription(partialUpdatedJobDescription));
    }

    @Test
    @Transactional
    void patchNonExistingJobDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobDescription.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobDescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jobDescription))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobDescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobDescription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobDescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(jobDescription))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobDescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jobDescription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobDescriptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(jobDescription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobDescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJobDescription() throws Exception {
        // Initialize the database
        insertedJobDescription = jobDescriptionRepository.saveAndFlush(jobDescription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the jobDescription
        restJobDescriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobDescription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return jobDescriptionRepository.count();
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

    protected JobDescription getPersistedJobDescription(JobDescription jobDescription) {
        return jobDescriptionRepository.findById(jobDescription.getId()).orElseThrow();
    }

    protected void assertPersistedJobDescriptionToMatchAllProperties(JobDescription expectedJobDescription) {
        assertJobDescriptionAllPropertiesEquals(expectedJobDescription, getPersistedJobDescription(expectedJobDescription));
    }

    protected void assertPersistedJobDescriptionToMatchUpdatableProperties(JobDescription expectedJobDescription) {
        assertJobDescriptionAllUpdatablePropertiesEquals(expectedJobDescription, getPersistedJobDescription(expectedJobDescription));
    }
}
