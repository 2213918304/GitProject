package com.springboot.springboot.service;

import com.springboot.springboot.entity.Student;
import com.springboot.springboot.repository.StudentRepository;
import com.springboot.springboot.exception.ResourceNotFoundException;
import com.springboot.springboot.exception.DataConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 学生业务逻辑层（Service层）
 * 
 * Service层负责处理业务逻辑，协调Repository层和Controller层
 * 
 * @Service - 标识这是一个业务逻辑层组件，Spring会自动扫描并注册为Bean
 * @Transactional - 类级别事务注解，确保所有方法都在事务中执行
 * 
 * @author SpringBoot教程
 * @version 1.0
 */
@Service
@Transactional  // 开启事务支持
public class StudentService {

    /**
     * 注入StudentRepository
     * 
     * @Autowired - 自动注入依赖，Spring会自动找到StudentRepository的实现并注入
     */
    @Autowired
    private StudentRepository studentRepository;

    // ========================================
    // 基本CRUD操作
    // ========================================

    /**
     * 保存学生信息
     * 
     * 如果学生ID为null，则执行新增操作
     * 如果学生ID不为null，则执行更新操作
     * 
     * @param student 学生对象
     * @return 保存后的学生对象（包含生成的ID）
     * @throws RuntimeException 如果邮箱已存在
     */
    public Student saveStudent(Student student) {
        // 新增学生时检查邮箱是否已存在
        if (student.getId() == null && studentRepository.existsByEmail(student.getEmail())) {
            throw new DataConflictException("邮箱 " + student.getEmail() + " 已存在，请使用其他邮箱");
        }
        
        // 更新学生时检查邮箱是否被其他学生使用
        if (student.getId() != null) {
            Optional<Student> existingStudent = studentRepository.findByEmail(student.getEmail());
            if (existingStudent.isPresent() && !existingStudent.get().getId().equals(student.getId())) {
                throw new DataConflictException("邮箱 " + student.getEmail() + " 已被其他学生使用");
            }
        }
        
        return studentRepository.save(student);
    }

    /**
     * 根据ID查找学生
     * 
     * @param id 学生ID
     * @return 学生对象（可能为空）
     */
    public Optional<Student> findStudentById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * 根据ID获取学生（如果不存在则抛出异常）
     * 
     * @param id 学生ID
     * @return 学生对象
     * @throws RuntimeException 如果学生不存在
     */
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID为 " + id + " 的学生不存在"));
    }

    /**
     * 查找所有学生
     * 
     * @return 所有学生列表
     */
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * 分页查询学生
     * 
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向（ASC或DESC）
     * @return 分页结果
     */
    public Page<Student> findStudentsWithPagination(int page, int size, String sortBy, String sortDirection) {
        // 创建排序对象
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        // 创建分页对象
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return studentRepository.findAll(pageable);
    }

    /**
     * 更新学生信息
     * 
     * @param id 学生ID
     * @param updatedStudent 更新的学生信息
     * @return 更新后的学生对象
     * @throws RuntimeException 如果学生不存在
     */
    public Student updateStudent(Long id, Student updatedStudent) {
        Student existingStudent = getStudentById(id);
        
        // 更新字段（只更新非空字段）
        if (updatedStudent.getName() != null) {
            existingStudent.setName(updatedStudent.getName());
        }
        if (updatedStudent.getAge() != null) {
            existingStudent.setAge(updatedStudent.getAge());
        }
        if (updatedStudent.getGender() != null) {
            existingStudent.setGender(updatedStudent.getGender());
        }
        if (updatedStudent.getMajor() != null) {
            existingStudent.setMajor(updatedStudent.getMajor());
        }
        if (updatedStudent.getClassName() != null) {
            existingStudent.setClassName(updatedStudent.getClassName());
        }
        if (updatedStudent.getEmail() != null) {
            // 检查邮箱是否被其他学生使用
            Optional<Student> studentWithEmail = studentRepository.findByEmail(updatedStudent.getEmail());
            if (studentWithEmail.isPresent() && !studentWithEmail.get().getId().equals(id)) {
                throw new DataConflictException("邮箱 " + updatedStudent.getEmail() + " 已被其他学生使用");
            }
            existingStudent.setEmail(updatedStudent.getEmail());
        }
        
        return studentRepository.save(existingStudent);
    }

    /**
     * 删除学生
     * 
     * @param id 学生ID
     * @throws RuntimeException 如果学生不存在
     */
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("ID为 " + id + " 的学生不存在，无法删除");
        }
        studentRepository.deleteById(id);
    }

    /**
     * 批量删除学生
     * 
     * @param ids 学生ID列表
     * @return 成功删除的学生数量
     */
    public int deleteStudentsByIds(List<Long> ids) {
        int deletedCount = 0;
        for (Long id : ids) {
            if (studentRepository.existsById(id)) {
                studentRepository.deleteById(id);
                deletedCount++;
            }
        }
        return deletedCount;
    }

    // ========================================
    // 查询操作
    // ========================================

    /**
     * 根据邮箱查找学生
     * 
     * @param email 邮箱地址
     * @return 学生对象（可能为空）
     */
    public Optional<Student> findStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    /**
     * 根据姓名模糊查询学生
     * 
     * @param name 姓名关键字
     * @return 匹配的学生列表
     */
    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContaining(name);
    }

    /**
     * 根据班级查找学生
     * 
     * @param className 班级名称
     * @return 该班级的所有学生
     */
    public List<Student> findStudentsByClassName(String className) {
        return studentRepository.findByClassName(className);
    }

    /**
     * 根据专业查找学生
     * 
     * @param major 专业名称
     * @return 该专业的所有学生
     */
    public List<Student> findStudentsByMajor(String major) {
        return studentRepository.findByMajor(major);
    }

    /**
     * 根据性别查找学生
     * 
     * @param gender 性别
     * @return 指定性别的所有学生
     */
    public List<Student> findStudentsByGender(Student.Gender gender) {
        return studentRepository.findByGender(gender);
    }

    /**
     * 根据年龄范围查找学生
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 年龄在指定范围内的学生
     */
    public List<Student> findStudentsByAgeRange(Integer minAge, Integer maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    /**
     * 多条件组合查询
     * 
     * @param className 班级名称
     * @param major 专业名称
     * @return 符合条件的学生列表
     */
    public List<Student> findStudentsByClassAndMajor(String className, String major) {
        return studentRepository.findByClassNameAndMajor(className, major);
    }

    /**
     * 关键字搜索（搜索姓名和邮箱）
     * 
     * @param keyword 搜索关键字
     * @return 匹配的学生列表
     */
    public List<Student> searchStudentsByKeyword(String keyword) {
        return studentRepository.searchByKeyword(keyword);
    }

    // ========================================
    // 统计操作
    // ========================================

    /**
     * 统计学生总数
     * 
     * @return 学生总数
     */
    public long countAllStudents() {
        return studentRepository.count();
    }

    /**
     * 统计指定班级的学生数量
     * 
     * @param className 班级名称
     * @return 该班级的学生数量
     */
    public long countStudentsByClassName(String className) {
        return studentRepository.countByClassName(className);
    }

    /**
     * 统计指定专业的学生数量
     * 
     * @param major 专业名称
     * @return 该专业的学生数量
     */
    public long countStudentsByMajor(String major) {
        return studentRepository.countByMajor(major);
    }

    /**
     * 检查邮箱是否已存在
     * 
     * @param email 邮箱地址
     * @return 如果邮箱已存在返回true，否则返回false
     */
    public boolean isEmailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    /**
     * 检查学生是否存在
     * 
     * @param id 学生ID
     * @return 如果学生存在返回true，否则返回false
     */
    public boolean isStudentExists(Long id) {
        return studentRepository.existsById(id);
    }

    // ========================================
    // 业务逻辑方法
    // ========================================

    /**
     * 获取班级学生列表（按姓名排序）
     * 
     * @param className 班级名称
     * @return 该班级学生列表（按姓名排序）
     */
    public List<Student> getClassStudentsOrderByName(String className) {
        return studentRepository.findByClassNameOrderByName(className);
    }

    /**
     * 获取专业中年龄最大的学生
     * 
     * @param major 专业名称
     * @return 该专业年龄最大的学生列表
     */
    public List<Student> getOldestStudentsByMajor(String major) {
        return studentRepository.findOldestStudentsByMajor(major);
    }

    /**
     * 获取各专业的学生数量统计
     * 
     * @return 专业和对应学生数量的列表
     */
    public List<Object[]> getStudentCountByMajor() {
        return studentRepository.countStudentsByMajor();
    }

    /**
     * 根据时间范围查询学生
     * 
     * @param startDate 开始日期（格式：yyyy-MM-dd）
     * @param endDate 结束日期（格式：yyyy-MM-dd）
     * @return 在指定时间范围内创建的学生列表
     */
    public List<Student> findStudentsByDateRange(String startDate, String endDate) {
        return studentRepository.findByCreateTimeBetween(startDate, endDate);
    }

    /**
     * 批量保存学生
     * 
     * @param students 学生列表
     * @return 保存的学生列表
     * @throws RuntimeException 如果有重复邮箱
     */
    @Transactional  // 确保批量操作的事务性
    public List<Student> saveAllStudents(List<Student> students) {
        // 检查是否有重复邮箱
        for (Student student : students) {
            if (studentRepository.existsByEmail(student.getEmail())) {
                throw new DataConflictException("邮箱 " + student.getEmail() + " 已存在，批量保存失败");
            }
        }
        
        return studentRepository.saveAll(students);
    }
}
