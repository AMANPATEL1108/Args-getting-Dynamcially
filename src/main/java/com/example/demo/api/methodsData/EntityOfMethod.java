package com.example.demo.api.methodsData;

import lombok.Data;

import java.util.List;

@Data
public class EntityOfMethod {
   private String entityName;
   private List<ListMethods> methodList;

}
