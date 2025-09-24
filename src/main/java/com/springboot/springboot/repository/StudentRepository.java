package com.springboot.springboot.repository;

import com.springboot.springboot.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学生数据访问层（Repository层）
 * 
 * 这个接口继承了JpaRepository，提供了基本的CRUD操作
 * 
 * @Repository - 标识这是一个数据访问层组件
 * JpaRepository<Student, Long> - 泛型参数：实体类型和主键类型
 * 
 * @author SpringBoot教程
 * @version 1.0
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * JpaRepository已经提供的基本方法：
     * - save(S entity): 保存或更新实体
     * - findById(ID id): 根据主键查找
     * - findAll(): 查找所有记录
     * - deleteById(ID id): 根据主键删除
     * - count(): 统计总数
     * - existsById(ID id): 判断是否存在
     */

    // ========================================
    // 自定义查询方法
    // ========================================

    /**
     * 根据邮箱查找学生
     * 
     * Spring Data JPA会根据方法名自动生成查询语句
     * findBy + 属性名 + 条件
     * 
     * @param email 邮箱地址
     * @return 学生对象（可能为空）
     */
    Optional<Student> findByEmail(String email);

    /**
     * 根据姓名查找学生（模糊查询）
     * 
     * Containing表示包含，会生成LIKE查询
     * 
     * @param name 姓名关键字
     * @return 学生列表
     */
    List<Student> findByNameContaining(String name);

    /**
     * 根据班级查找学生
     * 
     * @param className 班级名称
     * @return 该班级的所有学生
     */
    List<Student> findByClassName(String className);

    /**
     * 根据专业查找学生
     * 
     * @param major 专业名称
     * @return 该专业的所有学生
     */
    List<Student> findByMajor(String major);

    /**
     * 根据性别查找学生
     * 
     * @param gender 性别枚举
     * @return 指定性别的所有学生
     */
    List<Student> findByGender(Student.Gender gender);

    /**
     * 根据年龄范围查找学生
     * 
     * Between表示范围查询
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 年龄在指定范围内的学生
     */
    List<Student> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * 根据班级和专业查找学生
     * 
     * And表示AND条件
     * 
     * @param className 班级名称
     * @param major 专业名称
     * @return 符合条件的学生列表
     */
    List<Student> findByClassNameAndMajor(String className, String major);

    /**
     * 检查邮箱是否已存在
     * 
     * Exists表示存在性查询，返回boolean
     * 
     * @param email 邮箱地址
     * @return 如果邮箱已存在返回true，否则返回false
     */
    boolean existsByEmail(String email);

    /**
     * 统计指定班级的学生数量
     * 
     * CountBy表示计数查询
     * 
     * @param className 班级名称
     * @return 该班级的学生数量
     */
    long countByClassName(String className);

    /**
     * 统计指定专业的学生数量
     * 
     * @param major 专业名称
     * @return 该专业的学生数量
     */
    long countByMajor(String major);

    // ========================================
    // 自定义JPQL查询
    // ========================================

    /**
     * 使用JPQL自定义查询
     * 
     * @Query注解用于定义自定义查询语句
     * JPQL（Java Persistence Query Language）是面向对象的查询语言
     * 
     * @param keyword 搜索关键字
     * @return 匹配的学生列表
     */
    @Query("SELECT s FROM Student s WHERE s.name LIKE %:keyword% OR s.email LIKE %:keyword%")
    List<Student> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 根据班级查询学生信息，按姓名排序
     * 
     * @param className 班级名称
     * @return 该班级学生列表（按姓名排序）
     */
    @Query("SELECT s FROM Student s WHERE s.className = :className ORDER BY s.name ASC")
    List<Student> findByClassNameOrderByName(@Param("className") String className);

    /**
     * 查询指定专业中年龄最大的学生
     * 
     * @param major 专业名称
     * @return 该专业年龄最大的学生列表
     */
    @Query("SELECT s FROM Student s WHERE s.major = :major AND s.age = (SELECT MAX(s2.age) FROM Student s2 WHERE s2.major = :major)")
    List<Student> findOldestStudentsByMajor(@Param("major") String major);

    /**
     * 统计各专业的学生数量
     * 
     * 使用原生SQL查询（当JPQL无法满足需求时使用）
     * nativeQuery = true表示使用原生SQL
     * 
     * @return 专业和对应学生数量的列表
     */
    @Query(value = "SELECT major, COUNT(*) as count FROM students GROUP BY major", nativeQuery = true)
    List<Object[]> countStudentsByMajor();

    /**
     * 根据创建时间范围查询学生
     * 
     * @param startDate 开始日期（格式：yyyy-MM-dd）
     * @param endDate 结束日期（格式：yyyy-MM-dd）
     * @return 在指定时间范围内创建的学生列表
     */
    @Query("SELECT s FROM Student s WHERE DATE(s.createTime) BETWEEN :startDate AND :endDate ORDER BY s.createTime DESC")
    List<Student> findByCreateTimeBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // ========================================
    // Spring Data JPA方法命名规则说明
    // ========================================
    
    /*
     * Spring Data JPA支持的方法命名规则：
     * 
     * 1. 查询方法：
     *    - findBy...         : 查询
     *    - readBy...         : 查询（同findBy）
     *    - queryBy...        : 查询（同findBy）
     *    - getBy...          : 查询（同findBy）
     * 
     * 2. 计数方法：
     *    - countBy...        : 计数
     * 
     * 3. 存在性查询：
     *    - existsBy...       : 判断是否存在
     * 
     * 4. 删除方法：
     *    - deleteBy...       : 删除
     *    - removeBy...       : 删除（同deleteBy）
     * 
     * 5. 条件关键字：
     *    - And               : 且
     *    - Or                : 或
     *    - Between           : 范围查询
     *    - LessThan          : 小于
     *    - LessThanEqual     : 小于等于
     *    - GreaterThan       : 大于
     *    - GreaterThanEqual  : 大于等于
     *    - After             : 晚于（用于日期）
     *    - Before            : 早于（用于日期）
     *    - IsNull            : 为空
     *    - IsNotNull         : 不为空
     *    - Like              : 模糊查询
     *    - NotLike           : 非模糊查询
     *    - Containing        : 包含
     *    - In                : 在集合中
     *    - NotIn             : 不在集合中
     *    - True              : 布尔值为真
     *    - False             : 布尔值为假
     *    - IgnoreCase        : 忽略大小写
     * 
     * 6. 排序和限制：
     *    - OrderBy...Asc     : 升序排序
     *    - OrderBy...Desc    : 降序排序
     *    - First             : 获取第一个结果
     *    - Top               : 获取前N个结果
     * 
     * 示例：
     * - findByNameAndAge(String name, Integer age)
     * - findByAgeGreaterThan(Integer age)
     * - findByEmailContainingIgnoreCase(String email)
     * - findTop10ByOrderByCreateTimeDesc()
     * - findFirstByNameOrderByAgeAsc(String name)
     */
}
