package io.dgms.unity.api;

public interface DGTrial
{
    Long getId();

    String getName();

    DGStudy getStudy();

    DGTask getTask();
}
