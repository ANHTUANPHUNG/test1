package com.example.test.Service;

import com.example.test.DAO.TeacherDAO;
import com.example.test.Model.Teacher;
import com.example.test.Service.dto.PageableRequest;
import com.example.test.ulti.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class TeacherService {
    private static List<Teacher> teachers;

    private static Long currentId = 0L;

    private static final TeacherService teacherService;

    private TeacherDAO teacherDAO = new TeacherDAO();

    static {
        teachers = new ArrayList<>();

        teacherService = new TeacherService();
    }

    public List<Teacher> getTeachers(PageableRequest request){
        return teacherDAO.findAll(request);
    }
    public Teacher findById(Long id){
        return teacherDAO.findById(id)
                .orElseThrow(() ->  new RuntimeException(String.format(AppConstant.ID_NOT_FOUND, "Teacher")));

    }
    public void create(Teacher teacher){
        teacherDAO.insertTeacher(teacher);
    }

    public static TeacherService getTeacherService() {
        return teacherService;
    }
    private TeacherService(){}

    public void edit(Teacher teacher) {
        teacherDAO.updateTeacher(teacher);
    }

    public boolean existById(Long id) {
        return teacherDAO.findById(id).orElse(null) != null;
    }

    public void delete(Long userId) {
        teacherDAO.deleteById(userId);

    }
}
