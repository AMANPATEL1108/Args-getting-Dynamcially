package com.example.demo.api;

import com.example.demo.api.entityData.ColumbName;
import com.example.demo.api.entityData.EntityClass;
import com.example.demo.api.methodsData.ArgumentOfMethod;
import com.example.demo.api.methodsData.EntityOfMethod;
import com.example.demo.api.methodsData.ListMethods;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    // ------------------ PHASE 1: Define Entities ------------------
    public static List<EntityClass> add() {
        List<ColumbName> userColumns = Arrays.asList(
                new ColumbName("id", "Long"),
                new ColumbName("name", "String"),
                new ColumbName("email", "String"),
                new ColumbName("username", "String")
        );

        List<ColumbName> employeeColumns = Arrays.asList(
                new ColumbName("salary", "Long"),
                new ColumbName("name", "String"),
                new ColumbName("address", "Text")
        );

        List<ColumbName> paymentColumns = Arrays.asList(
                new ColumbName("confirm", "Boolean") // "confirm" exists here in the Payment entity
        );

        EntityClass userEntity = new EntityClass();
        userEntity.setEntityName("User");
        userEntity.setColumbNameList(userColumns);

        EntityClass employeeEntity = new EntityClass();
        employeeEntity.setEntityName("Employee");
        employeeEntity.setColumbNameList(employeeColumns);

        EntityClass paymentEntity = new EntityClass();
        paymentEntity.setEntityName("Payment");
        paymentEntity.setColumbNameList(paymentColumns);

        return Arrays.asList(userEntity, employeeEntity, paymentEntity);
    }

    // ------------------ PHASE 2 + PHASE 3: Define Methods and Auto Set DataTypes ------------------
    public static List<EntityOfMethod> initializeMethodData() {
        List<EntityClass> entityList = add();
        System.out.println("---------Start Entity List-----------------");
        System.out.println(entityList);
        System.out.println("-----------End Entity List-----------------");

        // Define methods for User
        ArgumentOfMethod userIdArg = new ArgumentOfMethod();
        userIdArg.setArgumentName("id");

        ArgumentOfMethod usernameArg = new ArgumentOfMethod();
        usernameArg.setArgumentName("username");

        ListMethods createUser = new ListMethods();
        createUser.setMethodName("createUser");
        createUser.setArgumentOfMethods(Arrays.asList(userIdArg, usernameArg));

        ArgumentOfMethod userIdArgDelete = new ArgumentOfMethod();
        userIdArgDelete.setArgumentName("id");

        ListMethods deleteUser = new ListMethods();
        deleteUser.setMethodName("deleteUser");
        deleteUser.setArgumentOfMethods(List.of(userIdArgDelete));

        EntityOfMethod userMethodEntity = new EntityOfMethod();
        userMethodEntity.setEntityName("User");
        userMethodEntity.setMethodList(Arrays.asList(createUser, deleteUser));

        // Define methods for Payment (Here, let's assume "confirm" column might be missing in a method)
        ArgumentOfMethod confirmArg1 = new ArgumentOfMethod();
        confirmArg1.setArgumentName("confirm");

        ListMethods createPayment = new ListMethods();
        createPayment.setMethodName("createPayment");
        createPayment.setArgumentOfMethods(List.of(confirmArg1));

        ArgumentOfMethod confirmArg2 = new ArgumentOfMethod();
        confirmArg2.setArgumentName("confirm");

        ListMethods confirmPayment = new ListMethods();
        confirmPayment.setMethodName("confirmPayment");
        confirmPayment.setArgumentOfMethods(List.of(confirmArg2));


        ArgumentOfMethod confirmArg3 = new ArgumentOfMethod();
        confirmArg3.setArgumentName("Yes");

        ListMethods check=new ListMethods();
        check.setMethodName("Done");
        check.setArgumentOfMethods(List.of(confirmArg3));

        EntityOfMethod paymentMethodEntity = new EntityOfMethod();
        paymentMethodEntity.setEntityName("Payment");
        paymentMethodEntity.setMethodList(Arrays.asList(createPayment, confirmPayment,check));

        List<EntityOfMethod> methodEntities = new ArrayList<>();
        methodEntities.add(userMethodEntity);
        methodEntities.add(paymentMethodEntity);

        // ------------------ PHASE 3: Match and Set Argument DataTypes ------------------
        for (EntityOfMethod methodEntity : methodEntities) {
            String entityName = methodEntity.getEntityName();

            // Find matching entity class
            EntityClass matchingEntity = entityList.stream()
                    .filter(e -> e.getEntityName().equals(entityName))
                    .findFirst()
                    .orElse(null);

            if (matchingEntity != null) {
                List<ColumbName> columns = matchingEntity.getColumbNameList();

                for (ListMethods method : methodEntity.getMethodList()) {
                    for (ArgumentOfMethod arg : method.getArgumentOfMethods()) {
                        boolean isColumnFound = false;

                        // Check if the column name exists in the entity
                        for (ColumbName column : columns) {
                            if (column.getColumName().equals(arg.getArgumentName())) {
                                arg.setArgumentDataType(column.getDataTypes());
                                isColumnFound = true;
                                break; // Found the match, no need to continue checking
                            }
                        }

                        // If column not found, set default data type as "Object"
                        if (!isColumnFound) {
                            System.out.println("Column " + arg.getArgumentName() + " not found for entity " + entityName +
                                    ". Defaulting argument data type to Object.");
                            arg.setArgumentDataType("Object");
                        }
                    }
                }
            } else {
                // If no matching entity class is found, set default data type to "Object"
                for (ListMethods method : methodEntity.getMethodList()) {
                    for (ArgumentOfMethod arg : method.getArgumentOfMethods()) {
                        System.out.println("Entity " + entityName + " not found. Defaulting argument data type to Object.");
                        arg.setArgumentDataType("Object");
                    }
                }
            }
        }

        return methodEntities;
    }

    // ------------------ MAIN ------------------
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        List<EntityOfMethod> entities = initializeMethodData();
        System.out.println("--------------------Entity Methods---------------");
        for (EntityOfMethod entity : entities) {
            System.out.println("Entity: " + entity.getEntityName());
            for (ListMethods method : entity.getMethodList()) {
                System.out.println("  Method: " + method.getMethodName());
                for (ArgumentOfMethod arg : method.getArgumentOfMethods()) {
                    System.out.println("    Arg: " + arg.getArgumentName() + " (" + arg.getArgumentDataType() + ")");
                }
            }
        }
        System.out.println("--------------------Entity Methods---------------");
    }
}
