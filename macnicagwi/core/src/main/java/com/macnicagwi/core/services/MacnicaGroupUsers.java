package com.macnicagwi.core.services;

import javax.jcr.Value;
import java.util.List;


public interface MacnicaGroupUsers {

    List<Value> getUserDetails(String id);

    List<Value> getUserEmail(String id);

    
    
}
