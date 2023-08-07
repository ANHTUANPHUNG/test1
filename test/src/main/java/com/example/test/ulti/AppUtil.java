package com.example.test.ulti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class AppUtil {
    public static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
    }
    public static Object getObject(HttpServletRequest request, Class clazz) {
        // tạo object bằng contructor không tham số.
        Object object;
        try {
            object = clazz.getDeclaredConstructor().newInstance();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

        java.util.Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();

            if(AppConstant.ACTION.equals(paramName)){
                continue;
            }

            System.out.println(request.getParameter(paramName));
            // Use reflection to set the parameter value to the corresponding field in the User class
            try {
                String paramValue = mapper.writeValueAsString(request.getParameter(paramName));



                // lấy value ra

                Field field = clazz.getDeclaredField(paramName);
                var fieldType = field.getType();
                field.setAccessible(true); // Set accessible, as the fields may be private


                var value = mapper.readValue(paramValue,fieldType);
                field.set(object, value);
                //set cho tung field
                // Add more type conversions as needed for other field types (e.g., boolean, double, etc.)
            } catch (NoSuchFieldException | IllegalAccessException | NumberFormatException e) {
                // Handle exceptions as needed
                System.out.println(e.getMessage());
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
            }

        }
        return object;
    }
    public static Object getObjectWithValidation(HttpServletRequest request, Class clazz, Map<String, RunnableCustom> validators){
        // tạo object bằng contructor không tham số.
        Object object;
        try {
            object = clazz.newInstance();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

        java.util.Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if(AppConstant.ACTION.equals(paramName)){
                continue;
            }

            System.out.println(request.getParameter(paramName));
            // Use reflection to set the parameter value to the corresponding field in the User class
            try {
                // lấy value ra
                String paramValue = mapper.writeValueAsString(request.getParameter(paramName));
                if(paramName.contains("_")){
                    //handle cho việc object lồng object.
                    String[] fields = paramName.split("_");
                    Field field = clazz.getDeclaredField(fields[0]);
                    field.setAccessible(true); // chuyen public
                    var fieldType = field.getType(); // class Name
                    var objectChild = fieldType.newInstance(); // tạo 1 object
                    field.set(object, objectChild);
//role_id
                    Field fieldChild = fieldType.getDeclaredField(fields[1]); // field object con
                    fieldChild.setAccessible(true);
                    var value = mapper.readValue(paramValue,fieldChild.getType());
                    fieldChild.set(objectChild, value);
                    continue;
                }
                RunnableCustom validator = validators.get(paramName);
                if(validator != null){
                    validator.setValue(request.getParameter(paramName));
                    validator.run();
                }
                Field field = clazz.getDeclaredField(paramName);
                field.setAccessible(true); // Set accessible, as the fields may be private
                Class<?> fieldType = field.getType();

                var value = mapper.readValue(paramValue,fieldType);
                field.set(object, value);
                //set cho tung field
                // Add more type conversions as needed for other field types (e.g., boolean, double, etc.)
            } catch (NoSuchFieldException | IllegalAccessException | NumberFormatException | InstantiationException e) {
                // Handle exceptions as needed
                System.out.println(e.getMessage());
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
            }

        }
        return object;
    }

    public static Object getParameterWithDefaultValue(HttpServletRequest request, String name, Object valueDefault){
        String value = request.getParameter(name);
        if(value == null)return valueDefault;
        return value;
    }
    public static <T> void saveObjectToDatabase(Connection connection, String tableName, T object, String... excludedFields) throws SQLException {
        String sql;
        Field idField;
        try {
            idField = object.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Long idValue = (Long) idField.get(object);
            if (idValue != null) {
                sql = buildUpdateSql(tableName, object, excludedFields);
            } else {
                sql = buildInsertSql(tableName, object, excludedFields);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int parameterIndex = 1;
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (fieldName.equals("serialVersionUID") || isExcluded(fieldName, excludedFields)) {
                    continue;
                }
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                try {
                    Object value = field.get(object);
                    preparedStatement.setObject(parameterIndex, value);
                    parameterIndex++;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (sql.contains("UPDATE")) {
                // Set the id value for the WHERE clause in the UPDATE statement
                preparedStatement.setLong(parameterIndex, (Long) idField.get(object));
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String buildInsertSql(String tableName, Object object, String... excludedFields) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (fieldName.equals("serialVersionUID") || fieldName.equals("id") || isExcluded(fieldName, excludedFields)) {
                continue;
            }
            sql.append(camelCaseToSnakeCase(fieldName));
            if (i < fields.length - 1) {
                sql.append(",");
            }
        }
        sql.append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (fieldName.equals("serialVersionUID") || fieldName.equals("id") || isExcluded(fieldName, excludedFields)) {
                continue;
            }
            sql.append("?");
            if (i < fields.length - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        return sql.toString();
    }
    private static String buildUpdateSql(String tableName, Object object, String... excludedFields) {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();

            if (fieldName.equals("serialVersionUID") || fieldName.equals("id") || isExcluded(fieldName, excludedFields)) {
                continue;
            }
            sql.append(camelCaseToSnakeCase(fieldName)).append("=?");
            if (i < fields.length - 1) {
                sql.append(",");
            }
        }
        sql.append(" WHERE (id = ?)");
        return sql.toString();
    }
    private static boolean isExcluded(String fieldName, String[] excludedFields) {
        for (String excludedField : excludedFields) {
            if (excludedField.equals(fieldName)) {
                return true;
            }
        }
        return false;
    }
    public static String camelCaseToSnakeCase(String camelCase) {
        return camelCase.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }
}
