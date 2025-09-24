package com.springboot.springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 学生实体类
 * 
 * 这个类使用了JPA注解来映射数据库表，SpringBoot会根据这个类自动创建数据库表
 * 
 * @Entity - 标识这是一个JPA实体类，会映射到数据库表
 * @Table - 指定对应的数据库表名
 * @author SpringBoot教程
 * @version 1.0
 */
@Entity
@Table(name = "students")  // 指定数据库表名为 students
public class Student {

    /**
     * 主键ID
     * 
     * @Id - 标识这是主键字段
     * @GeneratedValue - 指定主键生成策略
     * GenerationType.IDENTITY - 使用数据库的自增长策略（AUTO_INCREMENT）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学生姓名
     * 
     * @Column - 指定数据库列的属性
     * nullable = false - 不允许为空
     * length = 50 - 最大长度50个字符
     * 
     * @NotBlank - Bean Validation注解，验证字符串不能为空且不能只包含空白字符
     * @Size - 验证字符串长度范围
     */
    @Column(nullable = false, length = 50)
    @NotBlank(message = "学生姓名不能为空")
    @Size(min = 2, max = 50, message = "学生姓名长度必须在2-50个字符之间")
    private String name;

    /**
     * 学生年龄
     * 
     * @Min/@Max - 数值范围验证注解
     */
    @Column(nullable = false)
    @NotNull(message = "年龄不能为空")
    @Min(value = 15, message = "年龄不能小于15岁")
    @Max(value = 30, message = "年龄不能大于30岁")
    private Integer age;

    /**
     * 学生性别
     * 
     * @Enumerated - 枚举类型映射注解
     * EnumType.STRING - 将枚举以字符串形式存储到数据库
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @NotNull(message = "性别不能为空")
    private Gender gender;

    /**
     * 专业
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "专业不能为空")
    @Size(max = 100, message = "专业名称不能超过100个字符")
    private String major;

    /**
     * 班级
     */
    @Column(nullable = false, length = 50)
    @NotBlank(message = "班级不能为空")
    @Size(max = 50, message = "班级名称不能超过50个字符")
    private String className;

    /**
     * 邮箱
     * 
     * @Email - 邮箱格式验证注解
     */
    @Column(nullable = false, length = 100, unique = true)  // unique = true 表示邮箱唯一
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 创建时间
     * 
     * @CreationTimestamp - Hibernate注解，自动设置创建时间
     */
    @Column(nullable = false, updatable = false)  // updatable = false 表示不允许更新
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 
     * @UpdateTimestamp - Hibernate注解，自动更新修改时间
     */
    @Column(nullable = false)
    private LocalDateTime updateTime;

    /**
     * 性别枚举
     * 定义在内部类中，方便管理
     */
    public enum Gender {
        MALE("男"),
        FEMALE("女");

        private final String displayName;

        Gender(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * JPA生命周期回调方法
     * 
     * @PrePersist - 在实体持久化之前执行
     */
    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    /**
     * @PreUpdate - 在实体更新之前执行
     */
    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }

    // ========================================
    // 构造函数
    // ========================================

    /**
     * 默认构造函数
     * JPA要求实体类必须有无参构造函数
     */
    public Student() {
    }

    /**
     * 全参构造函数（除了id和时间字段）
     */
    public Student(String name, Integer age, Gender gender, String major, String className, String email) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.major = major;
        this.className = className;
        this.email = email;
    }

    // ========================================
    // Getter和Setter方法
    // ========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    // ========================================
    // toString, equals, hashCode方法
    // ========================================

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", major='" + major + '\'' +
                ", className='" + className + '\'' +
                ", email='" + email + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    /**
     * equals方法：基于id和email判断两个学生对象是否相等
     * 因为email是唯一的，所以可以作为判断依据
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return id != null && id.equals(student.id);
    }

    /**
     * hashCode方法：基于id生成哈希码
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
