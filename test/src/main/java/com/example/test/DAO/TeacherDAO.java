package com.example.test.DAO;

import com.example.test.Model.Category;
import com.example.test.Model.Teacher;
import com.example.test.Service.dto.Enums.EGender;
import com.example.test.Service.dto.Enums.ESortType;
import com.example.test.Service.dto.PageableRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherDAO  extends DatabaseConnection{
    private final String SELECT_ALL_TEACHERS = "SELECT t.*,c.name category_name  FROM `teachers` t LEFT JOIN " +
            "`categorys` c on t.category_id = c.id  WHERE t.`name` like  '%s' OR t.`dob` like '%s' OR t.`hobie` like '%s' OR t.`gender` like '%s' OR t.`category_id` like '%s'  Order BY %s %s LIMIT %s OFFSET %s";
    private final String SELECT_TOTAL_RECORDS = "SELECT COUNT(1) as cnt  FROM `teachers` t LEFT JOIN " +
            "`categorys` c on t.category_id = c.id  WHERE t.`name` like '%s' OR t.`dob` like '%s' OR t.`hobie` like '%s' OR t.`gender` like '%s' OR t.`category_id` like '%s'";
    private final String INSERT_TEACHERS ="INSERT INTO `teachers` (`name`, `dob`, `hobie`, `gender`, `category_id`) VALUES ( ?, ?, ?, ?, ?)";

    private final String UPDATE_TEACHERS = "UPDATE `teachers` SET `name` = ?, `dob` = ?, `hobie` = ?, `gender` = ?, `category_id` = ? WHERE (`id` = ?)";

    private final String FIND_BY_ID = "SELECT t.*,c.name category_name  FROM " +
            "`teachers` t LEFT JOIN `categorys` c on t.category_id = c.id WHERE t.`id` = ?";
    private final String DELETE_BY_ID = "DELETE FROM `teachers` WHERE (`id` = ?)";
    public List<Teacher> findAll(PageableRequest request) {
        List<Teacher> teachers = new ArrayList<>();
        String search = request.getSearch();
        if(request.getSortField() == null){
            request.setSortField("id");
        }
        if(request.getSortType() == null){
            request.setSortType(ESortType.DESC);
        }
        if(search == null){
            search = "%%";
        }else {
            search = "%" + search + "%";
        }
        var offset = (request.getPage() - 1) * request.getLimit();
        try (Connection connection = getConnection();

             // Step 2: truyền câu lênh mình muốn chạy nằm ở trong này (SELECT_USERS)
             PreparedStatement preparedStatement = connection
                     .prepareStatement(String.format(SELECT_ALL_TEACHERS, search, search, search, search, search,
                             request.getSortField(), request.getSortType(), request.getLimit(), offset))) {

            System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                teachers.add(getUserByResultSet(rs));
            }
            PreparedStatement statementTotalRecords = connection
                    .prepareStatement(String.format(SELECT_TOTAL_RECORDS, search,  search, search, search, search));
            ResultSet resultSetOfTotalRecords = statementTotalRecords.executeQuery();
            if(resultSetOfTotalRecords.next()){
                int totalPage =
                        (int) Math.ceil(resultSetOfTotalRecords.getDouble("cnt")/request.getLimit());
                request.setTotalPage(totalPage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return teachers;
    }
    public void insertTeacher(Teacher teacher){
        try (Connection connection = getConnection();

             // Step 2: truyền câu lênh mình muốn chạy nằm ở trong này (SELECT_USERS)
             PreparedStatement preparedStatement = connection
                     .prepareStatement(INSERT_TEACHERS)) {
            System.out.println(preparedStatement);
            preparedStatement.setString(1,teacher.getName());
            preparedStatement.setString(2,teacher.getDob());
            preparedStatement.setString(3,teacher.getHobie());
            preparedStatement.setString(4,teacher.getGender().toString());
            preparedStatement.setLong(5,teacher.getCategory().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateTeacher(Teacher teacher) {
        try (Connection connection = getConnection();

             // Step 2: truyền câu lênh mình muốn chạy nằm ở trong này (SELECT_USERS)
             PreparedStatement preparedStatement = connection
                     .prepareStatement(UPDATE_TEACHERS)) {
            System.out.println(preparedStatement);
            preparedStatement.setString(1,teacher.getName());
            preparedStatement.setString(2,teacher.getDob());
            preparedStatement.setString(3,teacher.getHobie());
            preparedStatement.setString(4,teacher.getGender().toString());
            preparedStatement.setLong(5,teacher.getCategory().getId());
            preparedStatement.setLong(6,teacher.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<Teacher> findById(Long id) {
        try (Connection connection = getConnection();

             // Step 2: truyền câu lênh mình muốn chạy nằm ở trong này (SELECT_USERS)
             PreparedStatement preparedStatement = connection
                     .prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return Optional.of(getUserByResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void deleteById(Long id) {
        try (Connection connection = getConnection();

             // Step 2: truyền câu lênh mình muốn chạy nằm ở trong này (SELECT_USERS)
             PreparedStatement preparedStatement = connection
                     .prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Teacher getUserByResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String dob = rs.getString("dob");
        String hobie = rs.getString("hobie");
        String gender = rs.getString("gender");
        String categoryName = rs.getString("category_name");
        Long categoryId = rs.getLong("category_id");
        Category category = new Category(categoryId, categoryName);

        return new Teacher(id, name,  Date.valueOf(dob), hobie, EGender.valueOf(gender), category);
    }
}
