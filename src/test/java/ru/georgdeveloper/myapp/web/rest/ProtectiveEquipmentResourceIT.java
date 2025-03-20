package ru.georgdeveloper.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.georgdeveloper.myapp.domain.ProtectiveEquipmentAsserts.*;
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
import ru.georgdeveloper.myapp.domain.ProtectiveEquipment;
import ru.georgdeveloper.myapp.repository.ProtectiveEquipmentRepository;

/**
 * Integration tests for the {@link ProtectiveEquipmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProtectiveEquipmentResourceIT {

    private static final String DEFAULT_EQUIPMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_ISSUANCE_FREQUENCY = 1;
    private static final Integer UPDATED_ISSUANCE_FREQUENCY = 2;

    private static final String ENTITY_API_URL = "/api/protective-equipments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProtectiveEquipmentRepository protectiveEquipmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProtectiveEquipmentMockMvc;

    private ProtectiveEquipment protectiveEquipment;

    private ProtectiveEquipment insertedProtectiveEquipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProtectiveEquipment createEntity() {
        return new ProtectiveEquipment()
            .equipmentName(DEFAULT_EQUIPMENT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .issuanceFrequency(DEFAULT_ISSUANCE_FREQUENCY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProtectiveEquipment createUpdatedEntity() {
        return new ProtectiveEquipment()
            .equipmentName(UPDATED_EQUIPMENT_NAME)
            .quantity(UPDATED_QUANTITY)
            .issuanceFrequency(UPDATED_ISSUANCE_FREQUENCY);
    }

    @BeforeEach
    public void initTest() {
        protectiveEquipment = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProtectiveEquipment != null) {
            protectiveEquipmentRepository.delete(insertedProtectiveEquipment);
            insertedProtectiveEquipment = null;
        }
    }

    @Test
    @Transactional
    void createProtectiveEquipment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProtectiveEquipment
        var returnedProtectiveEquipment = om.readValue(
            restProtectiveEquipmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(protectiveEquipment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProtectiveEquipment.class
        );

        // Validate the ProtectiveEquipment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProtectiveEquipmentUpdatableFieldsEquals(
            returnedProtectiveEquipment,
            getPersistedProtectiveEquipment(returnedProtectiveEquipment)
        );

        insertedProtectiveEquipment = returnedProtectiveEquipment;
    }

    @Test
    @Transactional
    void createProtectiveEquipmentWithExistingId() throws Exception {
        // Create the ProtectiveEquipment with an existing ID
        protectiveEquipment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProtectiveEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(protectiveEquipment)))
            .andExpect(status().isBadRequest());

        // Validate the ProtectiveEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEquipmentNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        protectiveEquipment.setEquipmentName(null);

        // Create the ProtectiveEquipment, which fails.

        restProtectiveEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(protectiveEquipment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        protectiveEquipment.setQuantity(null);

        // Create the ProtectiveEquipment, which fails.

        restProtectiveEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(protectiveEquipment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssuanceFrequencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        protectiveEquipment.setIssuanceFrequency(null);

        // Create the ProtectiveEquipment, which fails.

        restProtectiveEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(protectiveEquipment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProtectiveEquipments() throws Exception {
        // Initialize the database
        insertedProtectiveEquipment = protectiveEquipmentRepository.saveAndFlush(protectiveEquipment);

        // Get all the protectiveEquipmentList
        restProtectiveEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(protectiveEquipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentName").value(hasItem(DEFAULT_EQUIPMENT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].issuanceFrequency").value(hasItem(DEFAULT_ISSUANCE_FREQUENCY)));
    }

    @Test
    @Transactional
    void getProtectiveEquipment() throws Exception {
        // Initialize the database
        insertedProtectiveEquipment = protectiveEquipmentRepository.saveAndFlush(protectiveEquipment);

        // Get the protectiveEquipment
        restProtectiveEquipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, protectiveEquipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(protectiveEquipment.getId().intValue()))
            .andExpect(jsonPath("$.equipmentName").value(DEFAULT_EQUIPMENT_NAME))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.issuanceFrequency").value(DEFAULT_ISSUANCE_FREQUENCY));
    }

    @Test
    @Transactional
    void getNonExistingProtectiveEquipment() throws Exception {
        // Get the protectiveEquipment
        restProtectiveEquipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProtectiveEquipment() throws Exception {
        // Initialize the database
        insertedProtectiveEquipment = protectiveEquipmentRepository.saveAndFlush(protectiveEquipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the protectiveEquipment
        ProtectiveEquipment updatedProtectiveEquipment = protectiveEquipmentRepository.findById(protectiveEquipment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProtectiveEquipment are not directly saved in db
        em.detach(updatedProtectiveEquipment);
        updatedProtectiveEquipment
            .equipmentName(UPDATED_EQUIPMENT_NAME)
            .quantity(UPDATED_QUANTITY)
            .issuanceFrequency(UPDATED_ISSUANCE_FREQUENCY);

        restProtectiveEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProtectiveEquipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProtectiveEquipment))
            )
            .andExpect(status().isOk());

        // Validate the ProtectiveEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProtectiveEquipmentToMatchAllProperties(updatedProtectiveEquipment);
    }

    @Test
    @Transactional
    void putNonExistingProtectiveEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        protectiveEquipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProtectiveEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, protectiveEquipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(protectiveEquipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProtectiveEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProtectiveEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        protectiveEquipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtectiveEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(protectiveEquipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProtectiveEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProtectiveEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        protectiveEquipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtectiveEquipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(protectiveEquipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProtectiveEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProtectiveEquipmentWithPatch() throws Exception {
        // Initialize the database
        insertedProtectiveEquipment = protectiveEquipmentRepository.saveAndFlush(protectiveEquipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the protectiveEquipment using partial update
        ProtectiveEquipment partialUpdatedProtectiveEquipment = new ProtectiveEquipment();
        partialUpdatedProtectiveEquipment.setId(protectiveEquipment.getId());

        partialUpdatedProtectiveEquipment
            .equipmentName(UPDATED_EQUIPMENT_NAME)
            .quantity(UPDATED_QUANTITY)
            .issuanceFrequency(UPDATED_ISSUANCE_FREQUENCY);

        restProtectiveEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProtectiveEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProtectiveEquipment))
            )
            .andExpect(status().isOk());

        // Validate the ProtectiveEquipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProtectiveEquipmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProtectiveEquipment, protectiveEquipment),
            getPersistedProtectiveEquipment(protectiveEquipment)
        );
    }

    @Test
    @Transactional
    void fullUpdateProtectiveEquipmentWithPatch() throws Exception {
        // Initialize the database
        insertedProtectiveEquipment = protectiveEquipmentRepository.saveAndFlush(protectiveEquipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the protectiveEquipment using partial update
        ProtectiveEquipment partialUpdatedProtectiveEquipment = new ProtectiveEquipment();
        partialUpdatedProtectiveEquipment.setId(protectiveEquipment.getId());

        partialUpdatedProtectiveEquipment
            .equipmentName(UPDATED_EQUIPMENT_NAME)
            .quantity(UPDATED_QUANTITY)
            .issuanceFrequency(UPDATED_ISSUANCE_FREQUENCY);

        restProtectiveEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProtectiveEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProtectiveEquipment))
            )
            .andExpect(status().isOk());

        // Validate the ProtectiveEquipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProtectiveEquipmentUpdatableFieldsEquals(
            partialUpdatedProtectiveEquipment,
            getPersistedProtectiveEquipment(partialUpdatedProtectiveEquipment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProtectiveEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        protectiveEquipment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProtectiveEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, protectiveEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(protectiveEquipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProtectiveEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProtectiveEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        protectiveEquipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtectiveEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(protectiveEquipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProtectiveEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProtectiveEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        protectiveEquipment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProtectiveEquipmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(protectiveEquipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProtectiveEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProtectiveEquipment() throws Exception {
        // Initialize the database
        insertedProtectiveEquipment = protectiveEquipmentRepository.saveAndFlush(protectiveEquipment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the protectiveEquipment
        restProtectiveEquipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, protectiveEquipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return protectiveEquipmentRepository.count();
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

    protected ProtectiveEquipment getPersistedProtectiveEquipment(ProtectiveEquipment protectiveEquipment) {
        return protectiveEquipmentRepository.findById(protectiveEquipment.getId()).orElseThrow();
    }

    protected void assertPersistedProtectiveEquipmentToMatchAllProperties(ProtectiveEquipment expectedProtectiveEquipment) {
        assertProtectiveEquipmentAllPropertiesEquals(
            expectedProtectiveEquipment,
            getPersistedProtectiveEquipment(expectedProtectiveEquipment)
        );
    }

    protected void assertPersistedProtectiveEquipmentToMatchUpdatableProperties(ProtectiveEquipment expectedProtectiveEquipment) {
        assertProtectiveEquipmentAllUpdatablePropertiesEquals(
            expectedProtectiveEquipment,
            getPersistedProtectiveEquipment(expectedProtectiveEquipment)
        );
    }
}
