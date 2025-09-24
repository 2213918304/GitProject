package com.springboot.springboot.controller;

import com.springboot.springboot.entity.Student;
import com.springboot.springboot.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 学生控制器层（Controller层）
 * 
 * Controller层负责处理HTTP请求，调用Service层的方法，并返回响应
 * 
 * @RestController - 组合注解，相当于@Controller + @ResponseBody
 *                   标识这是一个RESTful控制器，所有方法返回的数据都会被序列化为JSON
 * @RequestMapping - 设置类级别的请求路径前缀
 * @CrossOrigin - 允许跨域请求（用于前后端分离项目）
 * 
 * @author SpringBoot教程
 * @version 1.0
 */
@RestController
@RequestMapping("/api/students")  // 所有接口的路径都以 /api/students 开头
@CrossOrigin(origins = "*")  // 允许所有域名跨域访问（生产环境建议指定具体域名）
public class StudentController {

    /**
     * 注入StudentService
     * 
     * @Autowired - 自动注入依赖
     */
    @Autowired
    private StudentService studentService;

    // ========================================
    // 增（Create）操作
    // ========================================

    /**
     * 添加新学生
     * 
     * @PostMapping - 处理POST请求，用于创建新资源
     * @RequestBody - 将HTTP请求体中的JSON数据绑定到方法参数
     * @Valid - 启用参数验证，会根据实体类中的验证注解进行校验
     * 
     * HTTP请求示例：
     * POST /api/students
     * Content-Type: application/json
     * {
     *   "name": "张三",
     *   "age": 20,
     *   "gender": "MALE",
     *   "major": "计算机科学与技术",
     *   "className": "CS2023-1",
     *   "email": "zhangsan@example.com"
     * }
     * 
     * @param student 学生对象（从请求体获取）
     * @return ResponseEntity包装的响应结果
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Student>> createStudent(@Valid @RequestBody Student student) {
        Student savedStudent = studentService.saveStudent(student);
        
        ApiResponse<Student> response = new ApiResponse<>(
                true,
                "学生添加成功",
                savedStudent
        );
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);  // 201 Created
    }

    /**
     * 批量添加学生
     * 
     * @param students 学生列表
     * @return 批量添加结果
     */
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<Student>>> createStudentsBatch(@Valid @RequestBody List<Student> students) {
        List<Student> savedStudents = studentService.saveAllStudents(students);
        
        ApiResponse<List<Student>> response = new ApiResponse<>(
                true,
                "批量添加成功，共添加 " + savedStudents.size() + " 名学生",
                savedStudents
        );
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ========================================
    // 查（Read）操作
    // ========================================

    /**
     * 获取所有学生
     * 
     * @GetMapping - 处理GET请求，用于获取资源
     * 
     * HTTP请求示例：
     * GET /api/students
     * 
     * @return 所有学生列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.findAllStudents();
        
        ApiResponse<List<Student>> response = new ApiResponse<>(
                true,
                "获取学生列表成功",
                students
        );
        
        return ResponseEntity.ok(response);  // 200 OK
    }

    /**
     * 根据ID获取学生
     * 
     * @PathVariable - 将URL路径中的变量绑定到方法参数
     * 
     * HTTP请求示例：
     * GET /api/students/1
     * 
     * @param id 学生ID
     * @return 学生信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        
        ApiResponse<Student> response = new ApiResponse<>(
                true,
                "获取学生信息成功",
                student
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 分页查询学生
     * 
     * @RequestParam - 获取查询参数
     * defaultValue - 设置参数默认值
     * 
     * HTTP请求示例：
     * GET /api/students/page?page=0&size=10&sortBy=name&sortDirection=ASC
     * 
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param sortBy 排序字段
     * @param sortDirection 排序方向
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<Student>>> getStudentsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Page<Student> studentsPage = studentService.findStudentsWithPagination(page, size, sortBy, sortDirection);
        
        ApiResponse<Page<Student>> response = new ApiResponse<>(
                true,
                "分页查询成功",
                studentsPage
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 根据邮箱查找学生
     * 
     * HTTP请求示例：
     * GET /api/students/email/zhangsan@example.com
     * 
     * @param email 邮箱地址
     * @return 学生信息（可能为空）
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<Student>> getStudentByEmail(@PathVariable String email) {
        Optional<Student> studentOpt = studentService.findStudentByEmail(email);
        
        if (studentOpt.isPresent()) {
            ApiResponse<Student> response = new ApiResponse<>(
                    true,
                    "根据邮箱查找学生成功",
                    studentOpt.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Student> response = new ApiResponse<>(
                    false,
                    "未找到邮箱为 " + email + " 的学生",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * 根据班级查找学生
     * 
     * HTTP请求示例：
     * GET /api/students/class/CS2023-1
     * 
     * @param className 班级名称
     * @return 该班级的学生列表
     */
    @GetMapping("/class/{className}")
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByClassName(@PathVariable String className) {
        List<Student> students = studentService.findStudentsByClassName(className);
        
        ApiResponse<List<Student>> response = new ApiResponse<>(
                true,
                "根据班级查找学生成功，共找到 " + students.size() + " 名学生",
                students
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 根据专业查找学生
     * 
     * HTTP请求示例：
     * GET /api/students/major/计算机科学与技术
     * 
     * @param major 专业名称
     * @return 该专业的学生列表
     */
    @GetMapping("/major/{major}")
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByMajor(@PathVariable String major) {
        List<Student> students = studentService.findStudentsByMajor(major);
        
        ApiResponse<List<Student>> response = new ApiResponse<>(
                true,
                "根据专业查找学生成功，共找到 " + students.size() + " 名学生",
                students
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 根据性别查找学生
     * 
     * HTTP请求示例：
     * GET /api/students/gender/MALE
     * 
     * @param gender 性别（MALE/FEMALE）
     * @return 指定性别的学生列表
     */
    @GetMapping("/gender/{gender}")
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByGender(@PathVariable Student.Gender gender) {
        List<Student> students = studentService.findStudentsByGender(gender);
        
        ApiResponse<List<Student>> response = new ApiResponse<>(
                true,
                "根据性别查找学生成功，共找到 " + students.size() + " 名学生",
                students
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 根据年龄范围查找学生
     * 
     * HTTP请求示例：
     * GET /api/students/age-range?minAge=18&maxAge=25
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 年龄在指定范围内的学生列表
     */
    @GetMapping("/age-range")
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByAgeRange(
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge) {
        
        List<Student> students = studentService.findStudentsByAgeRange(minAge, maxAge);
        
        ApiResponse<List<Student>> response = new ApiResponse<>(
                true,
                "根据年龄范围查找学生成功，共找到 " + students.size() + " 名学生",
                students
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 关键字搜索学生
     * 
     * HTTP请求示例：
     * GET /api/students/search?keyword=张
     * 
     * @param keyword 搜索关键字
     * @return 匹配的学生列表
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Student>>> searchStudents(@RequestParam String keyword) {
        List<Student> students = studentService.searchStudentsByKeyword(keyword);
        
        ApiResponse<List<Student>> response = new ApiResponse<>(
                true,
                "搜索学生成功，共找到 " + students.size() + " 名学生",
                students
        );
        
        return ResponseEntity.ok(response);
    }

    // ========================================
    // 改（Update）操作
    // ========================================

    /**
     * 更新学生信息
     * 
     * @PutMapping - 处理PUT请求，用于更新整个资源
     * 
     * HTTP请求示例：
     * PUT /api/students/1
     * Content-Type: application/json
     * {
     *   "name": "张三三",
     *   "age": 21,
     *   "email": "zhangsan_new@example.com"
     * }
     * 
     * @param id 学生ID
     * @param student 更新的学生信息
     * @return 更新后的学生信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {
        
        Student updatedStudent = studentService.updateStudent(id, student);
        
        ApiResponse<Student> response = new ApiResponse<>(
                true,
                "学生信息更新成功",
                updatedStudent
        );
        
        return ResponseEntity.ok(response);
    }

    // ========================================
    // 删（Delete）操作
    // ========================================

    /**
     * 删除学生
     * 
     * @DeleteMapping - 处理DELETE请求，用于删除资源
     * 
     * HTTP请求示例：
     * DELETE /api/students/1
     * 
     * @param id 学生ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        
        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "学生删除成功",
                null
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 批量删除学生
     * 
     * HTTP请求示例：
     * DELETE /api/students/batch
     * Content-Type: application/json
     * [1, 2, 3, 4, 5]
     * 
     * @param ids 学生ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<String>> deleteStudentsBatch(@RequestBody List<Long> ids) {
        int deletedCount = studentService.deleteStudentsByIds(ids);
        
        ApiResponse<String> response = new ApiResponse<>(
                true,
                "批量删除成功",
                "成功删除 " + deletedCount + " 名学生"
        );
        
        return ResponseEntity.ok(response);
    }

    // ========================================
    // 统计操作
    // ========================================

    /**
     * 获取学生总数
     * 
     * HTTP请求示例：
     * GET /api/students/count
     * 
     * @return 学生总数
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getStudentCount() {
        long count = studentService.countAllStudents();
        
        ApiResponse<Long> response = new ApiResponse<>(
                true,
                "获取学生总数成功",
                count
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取指定班级的学生数量
     * 
     * HTTP请求示例：
     * GET /api/students/count/class/CS2023-1
     * 
     * @param className 班级名称
     * @return 该班级的学生数量
     */
    @GetMapping("/count/class/{className}")
    public ResponseEntity<ApiResponse<Long>> getStudentCountByClassName(@PathVariable String className) {
        long count = studentService.countStudentsByClassName(className);
        
        ApiResponse<Long> response = new ApiResponse<>(
                true,
                "获取班级学生数量成功",
                count
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 检查邮箱是否已存在
     * 
     * HTTP请求示例：
     * GET /api/students/email-exists?email=test@example.com
     * 
     * @param email 邮箱地址
     * @return 是否存在
     */
    @GetMapping("/email-exists")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailExists(@RequestParam String email) {
        boolean exists = studentService.isEmailExists(email);
        
        ApiResponse<Boolean> response = new ApiResponse<>(
                true,
                exists ? "邮箱已存在" : "邮箱可用",
                exists
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 统一响应格式
     * 
     * 这个内部类定义了统一的API响应格式，确保所有接口返回的JSON结构一致
     * 
     * @param <T> 数据类型泛型
     */
    public static class ApiResponse<T> {
        private boolean success;     // 请求是否成功
        private String message;      // 响应消息
        private T data;             // 响应数据

        // 构造函数
        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Getter和Setter方法
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
