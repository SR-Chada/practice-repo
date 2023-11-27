package com.macnicagwi.core.services;

import java.util.List;

public interface ReplicationService {

    void replicateContent(List<String> payload,boolean isPublish);
}