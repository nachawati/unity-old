package io.dgms.unity.api;

import java.time.Instant;

public interface DGStudy
{
    Instant getDateInstantiated();

    Instant getDateLastActivity();

    Long getId();

    String getName();

    DGStudyType getType();

    Integer getUserId();
}
