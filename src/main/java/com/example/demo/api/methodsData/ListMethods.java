package com.example.demo.api.methodsData;

import lombok.Data;

import java.util.List;

@Data
public class ListMethods {
    private String  methodName;
    private List<ArgumentOfMethod> argumentOfMethods;
}
