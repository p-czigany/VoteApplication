package com.peterczigany.vote.validator;

import static com.peterczigany.vote.validator.SubjectValidator.SUBJECT_EMPTY;
import static com.peterczigany.vote.validator.SubjectValidator.SUBJECT_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.peterczigany.vote.VoteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SubjectValidatorTest {

  private static SubjectValidator subjectValidator;

  @BeforeAll
  static void setup() {
    subjectValidator = new SubjectValidator();
  }

  @Test
  void testSubjectCannotBeNull() {
    Exception exception =
        assertThrows(VoteException.class, () -> subjectValidator.validateSubject(null));

    assertThat(exception.getMessage()).isEqualTo(SUBJECT_NULL);
  }

  @Test
  void testSubjectCannotBeEmpty() {
    Exception exception =
        assertThrows(VoteException.class, () -> subjectValidator.validateSubject(""));

    assertThat(exception.getMessage()).isEqualTo(SUBJECT_EMPTY);
  }
}
