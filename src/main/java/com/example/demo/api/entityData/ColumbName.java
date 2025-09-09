package com.example.demo.api.entityData;

import lombok.Data;

@Data
public class ColumbName {
    private String columName;
    private String dataTypes;

    public ColumbName(String columName, String dataTypes) {
        this.columName = columName;
        this.dataTypes = dataTypes;
    }
}
