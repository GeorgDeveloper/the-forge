package ru.georgdeveloper.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SafetyInstructionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSafetyInstructionAllPropertiesEquals(SafetyInstruction expected, SafetyInstruction actual) {
        assertSafetyInstructionAutoGeneratedPropertiesEquals(expected, actual);
        assertSafetyInstructionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSafetyInstructionAllUpdatablePropertiesEquals(SafetyInstruction expected, SafetyInstruction actual) {
        assertSafetyInstructionUpdatableFieldsEquals(expected, actual);
        assertSafetyInstructionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSafetyInstructionAutoGeneratedPropertiesEquals(SafetyInstruction expected, SafetyInstruction actual) {
        assertThat(actual)
            .as("Verify SafetyInstruction auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSafetyInstructionUpdatableFieldsEquals(SafetyInstruction expected, SafetyInstruction actual) {
        assertThat(actual)
            .as("Verify SafetyInstruction relevant properties")
            .satisfies(a -> assertThat(a.getInstructionName()).as("check instructionName").isEqualTo(expected.getInstructionName()))
            .satisfies(a -> assertThat(a.getIntroductionDate()).as("check introductionDate").isEqualTo(expected.getIntroductionDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSafetyInstructionUpdatableRelationshipsEquals(SafetyInstruction expected, SafetyInstruction actual) {
        assertThat(actual)
            .as("Verify SafetyInstruction relationships")
            .satisfies(a -> assertThat(a.getProfession()).as("check profession").isEqualTo(expected.getProfession()))
            .satisfies(a -> assertThat(a.getPosition()).as("check position").isEqualTo(expected.getPosition()));
    }
}
