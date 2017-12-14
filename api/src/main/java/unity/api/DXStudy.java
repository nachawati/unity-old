package unity.api;

import java.time.Instant;

public interface DXStudy
{
    Instant getDateInstantiated();

    Instant getDateLastActivity();

    Long getId();

    String getName();

    DXStudyType getType();

    Integer getUserId();
}
