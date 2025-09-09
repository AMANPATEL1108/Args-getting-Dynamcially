package com.example.demo.api.entityData;

import lombok.Data;
import java.util.List;

@Data
public class EntityClass {
    private String entityName;
    private List<ColumbName> columbNameList;
}
