package ru.georgdeveloper.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.georgdeveloper.myapp.domain.ProfessionAsserts.*;
import static ru.georgdeveloper.myapp.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import ru.georgdeveloper.myapp.domain.Profession;
import ru.georgdeveloper.myapp.repository.ProfessionRepository;

/**
 * Integration tests for the {@link ProfessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfessionResourceIT {

    private static final String DEFAULT_PROFESSION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/professions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionRepository professionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionMockMvc;

    private Profession profession;

    private Profession insertedProfession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profession createEntity() {
        return new Profession().professionName(DEFAULT_PROFESSION_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profession createUpdatedEntity() {
        return new Profession().professionName(UPDATED_PROFESSION_NAME);
    }

    @BeforeEach
    public void initTest() {
        profession = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProfession != null) {
            professionRepository.delete(insertedProfession);
            insertedProfession = null;
        }
    }

    @Test
    @Transactional
    void createProfession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Profession
        var returnedProfession = om.readValue(
            restProfessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profession)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Profession.class
        );

        // Validate the Profession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProfessionUpdatableFieldsEquals(returnedProfession, getPersistedProfession(returnedProfession));

        insertedProfession = returnedProfession;
    }

    @Test
    @Transactional
    void createProfessionWithExistingId() throws Exception {
        // Create the Profession with an existing ID
        profession.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profession)))
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProfessionNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profession.setProfessionName(null);

        // Create the Profession, which fails.

        restProfessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profession)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessions() throws Exception {
        // Initialize the database
        insertedProfession = professionRepository.saveAndFlush(profession);

        // Get all the professionList
        restProfessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profession.getId().intValue())))
            .andExpect(jsonPath("$.[*].professionName").value(hasItem(DEFAULT_PROFESSION_NAME)));
    }

    @Test
    @Transactional
    void getProfession() throws Exception {
        // Initialize the database
        insertedProfession = professionRepository.saveAndFlush(profession);

        // Get the profession
        restProfessionMockMvc
            .perform(get(ENTITY_API_URL_ID, profession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profession.getId().intValue()))
            .andExpect(jsonPath("$.professionName").value(DEFAULT_PROFESSION_NAME));
    }

    @Test
    @Transactional
    void getNonExistingProfession() throws Exception {
        // Get the profession
        restProfessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfession() throws Exception {
        // Initialize the database
        insertedProfession = professionRepository.saveAndFlush(profession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profession
        Profession updatedProfession = professionRepository.findById(profession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfession are not directly saved in db
        em.detach(updatedProfession);
        updatedProfession.professionName(UPDATED_PROFESSION_NAME);

        restProfessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProfession.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProfession))
            )
            .andExpect(status().isOk());

        // Validate the Profession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionToMatchAllProperties(updatedProfession);
    }

    @Test
    @Transactional
    void putNonExistingProfession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profession.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profession.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profession))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profession))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profession)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionWithPatch() throws Exception {
        // Initialize the database
        insertedProfession = professionRepository.saveAndFlush(profession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profession using partial update
        Profession partialUpdatedProfession = new Profession();
        partialUpdatedProfession.setId(profession.getId());

        partialUpdatedProfession.professionName(UPDATED_PROFESSION_NAME);

        restProfessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfession))
            )
            .andExpect(status().isOk());

        // Validate the Profession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfession, profession),
            getPersistedProfession(profession)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionWithPatch() throws Exception {
        // Initialize the database
        insertedProfession = professionRepository.saveAndFlush(profession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profession using partial update
        Profession partialUpdatedProfession = new Profession();
        partialUpdatedProfession.setId(profession.getId());

        partialUpdatedProfession.professionName(UPDATED_PROFESSION_NAME);

        restProfessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfession))
            )
            .andExpect(status().isOk());

        // Validate the Profession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionUpdatableFieldsEquals(partialUpdatedProfession, getPersistedProfession(partialUpdatedProfession));
    }

    @Test
    @Transactional
    void patchNonExistingProfession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profession.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profession))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profession))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(profession)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfession() throws Exception {
        // Initialize the database
        insertedProfession = professionRepository.saveAndFlush(profession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profession
        restProfessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, profession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionRepository.count();
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

    protected Profession getPersistedProfession(Profession profession) {
        return professionRepository.findById(profession.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionToMatchAllProperties(Profession expectedProfession) {
        assertProfessionAllPropertiesEquals(expectedProfession, getPersistedProfession(expectedProfession));
    }

    protected void assertPersistedProfessionToMatchUpdatableProperties(Profession expectedProfession) {
        assertProfessionAllUpdatablePropertiesEquals(expectedProfession, getPersistedProfession(expectedProfession));
    }
}
