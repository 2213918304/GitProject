# SpringBoot教程-学生管理系统完整教程

## 📚 项目简介

本教程将带你从零开始构建一个完整的**学生管理系统**，包含以下功能：
- ✅ 学生信息的增删改查（CRUD）
- ✅ MySQL数据库集成
- ✅ JPA数据持久化
- ✅ RESTful API设计
- ✅ 全局异常处理
- ✅ HTML测试页面
- ✅ 详细的代码注释和说明

## 🎯 学习目标

通过本教程，你将学会：
1. SpringBoot项目的基础搭建
2. JPA实体类的设计和注解使用
3. Repository、Service、Controller三层架构
4. 数据库配置和连接
5. RESTful API的设计和实现
6. 全局异常处理的实现
7. 前端页面与后端API的交互

---

## 📋 前置知识要求

### 必备知识
- ☑️ **Java基础** - 面向对象编程、集合框架、异常处理
- ☑️ **Spring基础** - 依赖注入(DI)、控制反转(IOC)、注解使用
- ☑️ **Maven基础** - 项目结构、依赖管理、生命周期
- ☑️ **SQL基础** - 基本查询、表结构设计
- ☑️ **HTTP基础** - GET/POST/PUT/DELETE方法、状态码

### 推荐知识
- 🔄 **Git版本控制** - 代码管理和协作
- 🔄 **JSON格式** - 数据交换格式
- 🔄 **RESTful API设计** - API设计规范
- 🔄 **HTML/CSS/JavaScript** - 前端基础



## 🛠️ 环境准备

### 1. 开发工具安装

#### ☕ **Java Development Kit (JDK)**
- **版本要求**: JDK 17 或更高版本
- **下载地址**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) 或 [OpenJDK](https://openjdk.org/)
- **安装验证**: 
  ```bash
  java -version
  javac -version
  ```

#### 🔧 **集成开发环境 (IDE)**
推荐以下任一IDE：

**IntelliJ IDEA (推荐)**
- **下载**: [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- **版本**: Community版本免费，Ultimate版本功能更全
- **优点**: SpringBoot支持优秀，代码提示强大

**Eclipse STS**
- **下载**: [Spring Tool Suite](https://spring.io/tools)
- **优点**: 专为Spring开发优化，免费

**Visual Studio Code**
- **下载**: [VS Code](https://code.visualstudio.com/)
- **插件**: Extension Pack for Java, Spring Boot Extension Pack

#### 🗄️ **MySQL数据库**
- **版本要求**: MySQL 8.0 或更高版本
- **下载地址**: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
- **图形化工具**: 
  - MySQL Workbench (官方)
  - Navicat (付费，功能强大)
  - DBeaver (免费)

#### 📦 **Maven (可选)**
- **说明**: 如果IDE已集成Maven，可不单独安装
- **下载**: [Apache Maven](https://maven.apache.org/download.cgi)
- **环境变量配置**: 设置 `MAVEN_HOME` 和 `PATH`

### 2. 环境配置验证

创建一个测试文件验证环境：

```java
// TestEnvironment.java
public class TestEnvironment {
    public static void main(String[] args) {
        System.out.println("Java版本: " + System.getProperty("java.version"));
        System.out.println("操作系统: " + System.getProperty("os.name"));
        System.out.println("环境配置正常！✅");
    }
}
```

### 3. MySQL数据库准备

#### 启动MySQL服务
```bash
# Windows
net start mysql80

# macOS (使用Homebrew)
brew services start mysql

# Linux
sudo systemctl start mysql
```

#### 创建数据库
```sql
-- 登录MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE student_management 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选，推荐）
CREATE USER 'student_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON student_management.* TO 'student_user'@'localhost';
FLUSH PRIVILEGES;

-- 验证数据库
SHOW DATABASES;
USE student_management;
```

---

## 🔗 项目对接指南

### 1. 前后端分离对接

#### 🌐 **API接口文档**

我们的后端提供以下RESTful API接口：

**基础URL**: `http://localhost:8080/api/students`

| 功能 | 方法 | 路径 | 请求体 | 响应 |
|------|------|------|--------|------|
| 获取所有学生 | GET | `/api/students` | 无 | 学生列表 |
| 根据ID获取学生 | GET | `/api/students/{id}` | 无 | 单个学生 |
| 添加学生 | POST | `/api/students` | Student JSON | 新增的学生 |
| 更新学生 | PUT | `/api/students/{id}` | Student JSON | 更新后的学生 |
| 删除学生 | DELETE | `/api/students/{id}` | 无 | 删除结果 |
| 分页查询 | GET | `/api/students/page` | 查询参数 | 分页结果 |
| 关键字搜索 | GET | `/api/students/search` | keyword参数 | 匹配的学生 |

#### 📡 **API调用示例**

**JavaScript/Fetch API**
```javascript
// 获取所有学生
const response = await fetch('http://localhost:8080/api/students');
const result = await response.json();

// 添加学生
const newStudent = {
    name: "张三",
    age: 20,
    gender: "MALE",
    major: "计算机科学与技术",
    className: "CS2023-1",
    email: "zhangsan@example.com"
};

const response = await fetch('http://localhost:8080/api/students', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify(newStudent)
});
```

**jQuery/Ajax**
```javascript
// 获取学生列表
$.ajax({
    url: 'http://localhost:8080/api/students',
    method: 'GET',
    success: function(data) {
        console.log('学生列表:', data.data);
    },
    error: function(xhr, status, error) {
        console.error('请求失败:', error);
    }
});

// 添加学生
$.ajax({
    url: 'http://localhost:8080/api/students',
    method: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(newStudent),
    success: function(data) {
        alert('添加成功!');
    }
});
```

**Python/Requests**
```python
import requests
import json

# 基础URL
BASE_URL = 'http://localhost:8080/api/students'

# 获取所有学生
response = requests.get(BASE_URL)
if response.status_code == 200:
    students = response.json()['data']
    print(f"共有 {len(students)} 名学生")

# 添加学生
new_student = {
    "name": "李四",
    "age": 21,
    "gender": "FEMALE",
    "major": "软件工程",
    "className": "SE2023-1",
    "email": "lisi@example.com"
}

response = requests.post(
    BASE_URL,
    headers={'Content-Type': 'application/json'},
    data=json.dumps(new_student)
)

if response.status_code == 201:
    print("学生添加成功!")
```

### 2. 跨域配置

如果前端和后端运行在不同端口，需要配置跨域：

**方法一：Controller注解（已配置）**
```java
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")  // 允许所有域名访问
public class StudentController {
    // ...
}
```

**方法二：全局配置**
```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### 3. 响应格式说明

#### 成功响应格式
```json
{
    "success": true,
    "message": "操作成功",
    "data": {
        "id": 1,
        "name": "张三",
        "age": 20,
        "gender": "MALE",
        "major": "计算机科学与技术",
        "className": "CS2023-1",
        "email": "zhangsan@example.com",
        "createTime": "2024-01-15T10:30:00",
        "updateTime": "2024-01-15T10:30:00"
    }
}
```

#### 错误响应格式
```json
{
    "status": 400,
    "error": "业务逻辑错误",
    "message": "邮箱已存在，请使用其他邮箱",
    "timestamp": "2024-01-15T10:30:00",
    "details": {
        "email": "邮箱格式不正确"
    }
}
```

### 4. 前端框架对接示例

#### 🚀 **Vue.js 对接**
```vue
<template>
  <div>
    <h2>学生管理</h2>
    <button @click="loadStudents">加载学生</button>
    <ul>
      <li v-for="student in students" :key="student.id">
        {{ student.name }} - {{ student.major }}
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      students: []
    }
  },
  methods: {
    async loadStudents() {
      try {
        const response = await axios.get('http://localhost:8080/api/students')
        this.students = response.data.data
      } catch (error) {
        console.error('加载失败:', error)
      }
    }
  }
}
</script>
```

#### ⚛️ **React 对接**
```jsx
import React, { useState, useEffect } from 'react';

function StudentList() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);

  const loadStudents = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/students');
      const result = await response.json();
      if (result.success) {
        setStudents(result.data);
      }
    } catch (error) {
      console.error('加载失败:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadStudents();
  }, []);

  return (
    <div>
      <h2>学生管理</h2>
      <button onClick={loadStudents} disabled={loading}>
        {loading ? '加载中...' : '刷新'}
      </button>
      <ul>
        {students.map(student => (
          <li key={student.id}>
            {student.name} - {student.major}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default StudentList;
```

### 5. 移动端对接

#### 📱 **Android (Java)**
```java
// 使用Retrofit库
public interface StudentService {
    @GET("students")
    Call<ApiResponse<List<Student>>> getAllStudents();
    
    @POST("students")
    Call<ApiResponse<Student>> createStudent(@Body Student student);
}

// 使用
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("http://localhost:8080/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build();

StudentService service = retrofit.create(StudentService.class);
```

#### 🍎 **iOS (Swift)**
```swift
// 使用URLSession
func loadStudents() {
    let url = URL(string: "http://localhost:8080/api/students")!
    
    URLSession.shared.dataTask(with: url) { data, response, error in
        if let data = data {
            do {
                let result = try JSONDecoder().decode(ApiResponse<[Student]>.self, from: data)
                DispatchQueue.main.async {
                    self.students = result.data
                }
            } catch {
                print("解析错误: \(error)")
            }
        }
    }.resume()
}
```

### 6. 接口测试工具（省略）

#### 🛠️ **Postman测试**
1. 下载安装 [Postman](https://www.postman.com/)
2. 创建新的Collection "学生管理系统"
3. 添加请求：
   - GET http://localhost:8080/api/students
   - POST http://localhost:8080/api/students
   - PUT http://localhost:8080/api/students/1
   - DELETE http://localhost:8080/api/students/1

#### 🌐 **curl命令测试**
```bash
# 获取所有学生
curl -X GET http://localhost:8080/api/students

# 添加学生
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "王五",
    "age": 19,
    "gender": "MALE",
    "major": "数据科学",
    "className": "DS2023-1",
    "email": "wangwu@example.com"
  }'

# 更新学生
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "王五五", "age": 20}'

# 删除学生
curl -X DELETE http://localhost:8080/api/students/1
```

---

## 第一步：创建SpringBoot项目

### 1.1 项目初始化

使用Spring Initializr创建项目：

![image-20250923132708670](C:\Users\18734\AppData\Roaming\Typora\typora-user-images\image-20250923132708670.png)

### 1.2 选择项目依赖

选择以下核心依赖：
- **Spring Web**: 用于构建web应用和RESTful API
- **Spring Data JPA**: 用于数据持久化和ORM映射
- **MySQL Driver**: MySQL数据库连接驱动

![image-20250923132824055](C:\Users\18734\AppData\Roaming\Typora\typora-user-images\image-20250923132824055.png)

### 1.3 项目结构说明

```
SpringBoot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/springboot/springboot/
│   │   │       ├── Application.java          # 主启动类
│   │   │       ├── entity/                   # 实体类包
│   │   │       ├── repository/               # 数据访问层
│   │   │       ├── service/                  # 业务逻辑层
│   │   │       ├── controller/               # 控制器层
│   │   │       └── exception/                # 异常处理
│   │   └── resources/
│   │       ├── application.properties        # 配置文件
│   │       ├── static/                       # 静态资源
│   │       └── templates/                    # 模板文件
│   └── test/                                 # 测试代码
├── pom.xml                                   # Maven依赖配置
└── README.md
```

---

## 第二步：配置应用程序属性

### 2.1 数据库配置详解

在 `src/main/resources/application.properties` 中配置数据库连接：

```properties
# ========================================
# SpringBoot 学生管理系统配置文件
# ========================================

# 应用程序名称
spring.application.name=SpringBoot-Student-Management-System

# ========================================
# 数据库配置 (MySQL)
# ========================================
# 数据库连接URL，指定数据库名为student_management
spring.datasource.url=jdbc:mysql://localhost:3306/student_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
# 数据库用户名（请根据你的MySQL配置修改）
spring.datasource.username=root
# 数据库密码（请根据你的MySQL配置修改）
spring.datasource.password=123456
# MySQL数据库驱动
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ========================================
# JPA配置
# ========================================
# 自动创建/更新数据库表结构
spring.jpa.hibernate.ddl-auto=update
# 显示SQL语句（开发阶段建议开启）
spring.jpa.show-sql=true
# 格式化SQL语句，便于阅读
spring.jpa.properties.hibernate.format_sql=true
# 数据库方言，告诉Hibernate使用MySQL的特定语法
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# ========================================
# Web配置
# ========================================
# 服务器端口号
server.port=8080

# ========================================
# 日志配置
# ========================================
# 设置日志级别
logging.level.root=INFO
logging.level.org.hibernate.SQL=DEBUG
```

### 2.2 配置说明

| 配置项 | 说明 | 重要程度 |
|--------|------|----------|
| `spring.datasource.url` | 数据库连接地址，包含数据库名 | ⭐⭐⭐ |
| `spring.jpa.hibernate.ddl-auto` | 表结构管理策略 | ⭐⭐⭐ |
| `spring.jpa.show-sql` | 是否显示执行的SQL语句 | ⭐⭐ |
| `server.port` | 应用运行端口 | ⭐⭐ |

**DDL-AUTO 选项说明：**
- `create`: 每次启动都重新创建表（会删除原有数据）
- `create-drop`: 启动时创建，关闭时删除表
- `update`: 自动更新表结构，保留数据（推荐开发使用）
- `validate`: 只验证表结构，不创建表
- `none`: 不进行任何操作

---

## 第三步：创建学生实体类（Entity）

### 3.1 实体类设计思路

实体类是JPA的核心，它代表数据库中的一张表。我们需要：
1. 使用JPA注解映射数据库表
2. 添加数据验证注解
3. 实现生命周期回调方法
4. 提供完整的getter/setter方法

### 3.2 Student实体类实现

创建 `src/main/java/com/springboot/springboot/entity/Student.java`：

```java
package com.springboot.springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 学生实体类
 * 
 * @Entity - 标识这是一个JPA实体类，会映射到数据库表
 * @Table - 指定对应的数据库表名
 */
@Entity
@Table(name = "students")
public class Student {

    /**
     * 主键ID
     * @Id - 标识这是主键字段
     * @GeneratedValue - 指定主键生成策略（数据库自增）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学生姓名
     * @Column - 指定数据库列属性
     * @NotBlank - 验证字符串不能为空
     * @Size - 验证字符串长度范围
     */
    @Column(nullable = false, length = 50)
    @NotBlank(message = "学生姓名不能为空")
    @Size(min = 2, max = 50, message = "学生姓名长度必须在2-50个字符之间")
    private String name;

    /**
     * 学生年龄
     * @NotNull - 验证不能为空
     * @Min/@Max - 数值范围验证
     */
    @Column(nullable = false)
    @NotNull(message = "年龄不能为空")
    @Min(value = 15, message = "年龄不能小于15岁")
    @Max(value = 30, message = "年龄不能大于30岁")
    private Integer age;

    /**
     * 学生性别（枚举类型）
     * @Enumerated - 枚举类型映射注解
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @NotNull(message = "性别不能为空")
    private Gender gender;

    // 其他字段...
    
    /**
     * 性别枚举
     */
    public enum Gender {
        MALE("男"), FEMALE("女");
        
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
}
```

### 3.3 JPA注解详解

| 注解 | 作用 | 示例 |
|------|------|------|
| `@Entity` | 标识JPA实体类 | `@Entity` |
| `@Table` | 指定数据库表名 | `@Table(name = "students")` |
| `@Id` | 标识主键字段 | `@Id` |
| `@GeneratedValue` | 主键生成策略 | `@GeneratedValue(strategy = GenerationType.IDENTITY)` |
| `@Column` | 列属性配置 | `@Column(nullable = false, length = 50)` |
| `@Enumerated` | 枚举类型映射 | `@Enumerated(EnumType.STRING)` |
| `@PrePersist` | 持久化前回调 | 自动设置创建时间 |
| `@PreUpdate` | 更新前回调 | 自动更新修改时间 |

### 3.4 数据验证注解

| 注解 | 作用 | 示例 |
|------|------|------|
| `@NotNull` | 不能为null | `@NotNull(message = "年龄不能为空")` |
| `@NotBlank` | 字符串不能为空或空白 | `@NotBlank(message = "姓名不能为空")` |
| `@Size` | 字符串长度限制 | `@Size(min = 2, max = 50)` |
| `@Min/@Max` | 数值范围限制 | `@Min(15) @Max(30)` |
| `@Email` | 邮箱格式验证 | `@Email(message = "邮箱格式不正确")` |

---

---

## 第四步：创建数据访问层（Repository）

### 4.1 Repository层的作用

Repository层是数据访问层，负责与数据库交互。在Spring Data JPA中，我们只需要定义接口，框架会自动生成实现类。

### 4.2 StudentRepository接口

创建 `src/main/java/com/springboot/springboot/repository/StudentRepository.java`：

```java
package com.springboot.springboot.repository;

import com.springboot.springboot.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学生数据访问层
 * 
 * @Repository - 标识数据访问层组件
 * JpaRepository<Student, Long> - 提供基本CRUD操作
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // 基本查询方法（Spring Data JPA自动实现）
    Optional<Student> findByEmail(String email);
    List<Student> findByNameContaining(String name);
    List<Student> findByClassName(String className);
    List<Student> findByMajor(String major);
    List<Student> findByGender(Student.Gender gender);
    List<Student> findByAgeBetween(Integer minAge, Integer maxAge);
    
    // 组合条件查询
    List<Student> findByClassNameAndMajor(String className, String major);
    
    // 存在性和计数查询
    boolean existsByEmail(String email);
    long countByClassName(String className);
    
    // 自定义JPQL查询
    @Query("SELECT s FROM Student s WHERE s.name LIKE %:keyword% OR s.email LIKE %:keyword%")
    List<Student> searchByKeyword(@Param("keyword") String keyword);
}
```

### 4.3 JpaRepository提供的基本方法

| 方法 | 描述 | 返回类型 |
|------|------|----------|
| `save(S entity)` | 保存或更新实体 | `S` |
| `findById(ID id)` | 根据ID查找 | `Optional<T>` |
| `findAll()` | 查找所有记录 | `List<T>` |
| `deleteById(ID id)` | 根据ID删除 | `void` |
| `count()` | 统计记录总数 | `long` |
| `existsById(ID id)` | 判断ID是否存在 | `boolean` |

### 4.4 Spring Data JPA方法命名规则

Spring Data JPA支持通过方法名自动生成查询：

| 关键字 | 示例 | JPQL片段 |
|--------|------|----------|
| `findBy` | `findByName` | `where x.name = ?1` |
| `And` | `findByNameAndAge` | `where x.name = ?1 and x.age = ?2` |
| `Or` | `findByNameOrEmail` | `where x.name = ?1 or x.email = ?2` |
| `Between` | `findByAgeBetween` | `where x.age between ?1 and ?2` |
| `LessThan` | `findByAgeLessThan` | `where x.age < ?1` |
| `GreaterThan` | `findByAgeGreaterThan` | `where x.age > ?1` |
| `Like` | `findByNameLike` | `where x.name like ?1` |
| `Containing` | `findByNameContaining` | `where x.name like %?1%` |
| `OrderBy` | `findByAgeOrderByNameAsc` | `where x.age = ?1 order by x.name asc` |

### 4.5 自定义查询

当方法命名无法满足复杂查询需求时，可以使用 `@Query` 注解：

```java
// JPQL查询
@Query("SELECT s FROM Student s WHERE s.name LIKE %:keyword%")
List<Student> searchByKeyword(@Param("keyword") String keyword);

// 原生SQL查询
@Query(value = "SELECT * FROM students WHERE age > ?1", nativeQuery = true)
List<Student> findByAgeGreaterThanNative(Integer age);
```

---

---

## 第五步：创建业务逻辑层（Service）

### 5.1 Service层的作用

Service层是业务逻辑层，负责：
1. 处理复杂的业务逻辑
2. 协调多个Repository的操作
3. 事务管理
4. 数据验证和异常处理
5. 为Controller层提供业务方法

### 5.2 StudentService实现

创建 `src/main/java/com/springboot/springboot/service/StudentService.java`：

```java
package com.springboot.springboot.service;

import com.springboot.springboot.entity.Student;
import com.springboot.springboot.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 学生业务逻辑层
 * 
 * @Service - 标识业务逻辑层组件
 * @Transactional - 事务管理
 */
@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 保存学生信息（带邮箱重复检查）
     */
    public Student saveStudent(Student student) {
        // 业务逻辑：检查邮箱是否已存在
        if (student.getId() == null && studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        return studentRepository.save(student);
    }

    /**
     * 根据ID获取学生（不存在则抛异常）
     */
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在"));
    }

    /**
     * 分页查询学生
     */
    public Page<Student> findStudentsWithPagination(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return studentRepository.findAll(pageable);
    }

    // 其他业务方法...
}
```

### 5.3 Service层关键注解

| 注解 | 作用 | 示例 |
|------|------|------|
| `@Service` | 标识业务逻辑层组件 | `@Service` |
| `@Autowired` | 自动注入依赖 | `@Autowired private StudentRepository repository;` |
| `@Transactional` | 事务管理 | `@Transactional` |
| `@Transactional(readOnly = true)` | 只读事务（优化性能） | 查询方法使用 |

### 5.4 事务管理详解

#### 事务的ACID特性
- **原子性（Atomicity）**: 事务中的所有操作要么全部成功，要么全部失败
- **一致性（Consistency）**: 事务执行前后数据库状态保持一致
- **隔离性（Isolation）**: 多个事务并发执行时互不干扰
- **持久性（Durability）**: 事务提交后数据永久保存

#### @Transactional属性

| 属性 | 描述 | 示例 |
|------|------|------|
| `readOnly` | 只读事务 | `@Transactional(readOnly = true)` |
| `rollbackFor` | 指定回滚异常 | `@Transactional(rollbackFor = Exception.class)` |
| `propagation` | 事务传播行为 | `@Transactional(propagation = Propagation.REQUIRED)` |
| `isolation` | 隔离级别 | `@Transactional(isolation = Isolation.READ_COMMITTED)` |

### 5.5 业务逻辑设计原则

1. **单一职责**: 每个方法只负责一个业务功能
2. **参数验证**: 在方法入口处验证参数
3. **异常处理**: 抛出有意义的业务异常
4. **事务边界**: 合理设置事务范围
5. **性能优化**: 避免不必要的数据库查询

### 5.6 分页查询实现

```java
public Page<Student> findStudentsWithPagination(int page, int size, String sortBy, String sortDirection) {
    // 创建排序对象
    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
    // 创建分页对象
    Pageable pageable = PageRequest.of(page, size, sort);
    // 执行分页查询
    return studentRepository.findAll(pageable);
}
```

**分页参数说明：**
- `page`: 页码（从0开始）
- `size`: 每页大小
- `sortBy`: 排序字段名
- `sortDirection`: 排序方向（ASC/DESC）

---

---

## 第六步：创建全局异常处理器

### 6.1 异常处理的重要性

在Web应用中，异常处理是保证系统稳定性和用户体验的关键：
1. 统一错误响应格式
2. 提供友好的错误信息
3. 避免敏感信息泄露
4. 便于错误追踪和调试

### 6.2 GlobalExceptionHandler实现

创建 `src/main/java/com/springboot/springboot/exception/GlobalExceptionHandler.java`：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务逻辑异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "业务逻辑错误",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        // 返回详细的验证错误信息
    }
}
```

### 6.3 自定义异常类

创建专门的异常类来处理不同的业务场景：

```java
// 资源未找到异常
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

// 数据冲突异常
public class DataConflictException extends RuntimeException {
    public DataConflictException(String message) {
        super(message);
    }
}
```

---

## 第七步：创建控制器层（Controller）

### 7.1 Controller层的作用

Controller层是Web应用的入口，负责：
1. 接收HTTP请求
2. 参数验证和数据绑定
3. 调用Service层方法
4. 返回响应结果

### 7.2 RESTful API设计原则

| HTTP方法 | 用途 | 示例 |
|----------|------|------|
| GET | 获取资源 | `GET /api/students` |
| POST | 创建资源 | `POST /api/students` |
| PUT | 更新资源 | `PUT /api/students/1` |
| DELETE | 删除资源 | `DELETE /api/students/1` |

### 7.3 StudentController实现

创建 `src/main/java/com/springboot/springboot/controller/StudentController.java`：

```java
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 添加新学生
     * POST /api/students
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Student>> createStudent(@Valid @RequestBody Student student) {
        Student savedStudent = studentService.saveStudent(student);
        ApiResponse<Student> response = new ApiResponse<>(true, "学生添加成功", savedStudent);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取所有学生
     * GET /api/students
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.findAllStudents();
        ApiResponse<List<Student>> response = new ApiResponse<>(true, "获取学生列表成功", students);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据ID获取学生
     * GET /api/students/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        ApiResponse<Student> response = new ApiResponse<>(true, "获取学生信息成功", student);
        return ResponseEntity.ok(response);
    }

    // 更多API方法...
}
```

### 7.4 Controller注解详解

| 注解 | 作用 | 示例 |
|------|------|------|
| `@RestController` | 标识RESTful控制器 | 返回JSON数据 |
| `@RequestMapping` | 类级别路径映射 | `@RequestMapping("/api/students")` |
| `@GetMapping` | 处理GET请求 | `@GetMapping("/{id}")` |
| `@PostMapping` | 处理POST请求 | `@PostMapping` |
| `@PutMapping` | 处理PUT请求 | `@PutMapping("/{id}")` |
| `@DeleteMapping` | 处理DELETE请求 | `@DeleteMapping("/{id}")` |
| `@PathVariable` | 路径变量绑定 | `@PathVariable Long id` |
| `@RequestParam` | 查询参数绑定 | `@RequestParam String name` |
| `@RequestBody` | 请求体绑定 | `@RequestBody Student student` |
| `@Valid` | 参数验证 | `@Valid @RequestBody Student student` |

---

## 第八步：创建HTML测试页面

### 8.1 测试页面的作用

HTML测试页面提供了可视化的界面来测试我们的API：
1. 直观的操作界面
2. 实时查看API响应
3. 完整的CRUD功能演示
4. 便于项目展示和调试

### 8.2 页面功能模块

创建 `src/main/resources/static/index.html`，包含以下功能：

#### 🎯 **功能模块**
1. **添加学生** - 表单输入，数据验证
2. **查询学生** - 多种查询方式
3. **学生列表** - 表格展示，删除操作
4. **统计信息** - 数据统计展示

#### 🎨 **界面特性**
- 响应式设计（Bootstrap 5）
- 美观的卡片布局
- 实时API响应显示
- 友好的用户交互

#### 📡 **API调用示例**
```javascript
// 添加学生
async function addStudent() {
    const studentData = {
        name: document.getElementById('name').value,
        age: parseInt(document.getElementById('age').value),
        gender: document.getElementById('gender').value,
        major: document.getElementById('major').value,
        className: document.getElementById('className').value,
        email: document.getElementById('email').value
    };

    const response = await fetch('/api/students', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(studentData)
    });

    const result = await response.json();
    // 显示结果...
}
```

---

## 第九步：项目测试和部署

### 9.1 启动项目

1. **启动MySQL数据库**
   ```sql
   -- 创建数据库
   CREATE DATABASE student_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **运行SpringBoot应用**
   ```bash
   mvn spring-boot:run
   ```
   或在IDE中运行 `Application.java`

3. **访问测试页面**
   ```
   http://localhost:8080/index.html
   ```

### 9.2 API测试指南

#### 📝 **添加学生** 
- **URL**: `POST /api/students`
- **Body**: 
```json
{
  "name": "张三",
  "age": 20,
  "gender": "MALE",
  "major": "计算机科学与技术",
  "className": "CS2023-1",
  "email": "zhangsan@example.com"
}
```

#### 🔍 **查询学生**
- 获取所有: `GET /api/students`
- 根据ID: `GET /api/students/1`
- 根据邮箱: `GET /api/students/email/zhangsan@example.com`
- 分页查询: `GET /api/students/page?page=0&size=10`

#### ✏️ **更新学生**
- **URL**: `PUT /api/students/1`
- **Body**: 只需包含要更新的字段

#### 🗑️ **删除学生**
- 单个删除: `DELETE /api/students/1`
- 批量删除: `DELETE /api/students/batch`

### 9.3 数据库表结构

SpringBoot启动后会自动创建 `students` 表：

```sql
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    major VARCHAR(100) NOT NULL,
    class_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);
```

---

## 🔄 结果

![image-20250923134936591](C:\Users\18734\AppData\Roaming\Typora\typora-user-images\image-20250923134936591.png)

![image-20250923134948565](C:\Users\18734\AppData\Roaming\Typora\typora-user-images\image-20250923134948565.png)

---

## 💡 项目总结

### 🏗️ **架构总览**
```
前端页面 (HTML/JS)
    ↓
控制器层 (Controller)
    ↓
业务逻辑层 (Service)
    ↓
数据访问层 (Repository)
    ↓
数据库 (MySQL)
```

### 📚 **技术栈**
- **后端**: SpringBoot 3.x + Spring Data JPA
- **数据库**: MySQL 8.0
- **前端**: HTML5 + Bootstrap 5 + JavaScript
- **工具**: Maven + IntelliJ IDEA

### ✨ **核心特性**
1. **完整的CRUD操作** - 增删改查功能齐全
2. **数据验证** - 使用Bean Validation确保数据完整性
3. **异常处理** - 全局异常处理器提供统一错误响应
4. **RESTful API** - 标准的REST接口设计
5. **响应式前端** - 美观易用的测试界面
6. **事务管理** - 确保数据一致性
7. **分页查询** - 支持大数据量处理

---

## 🔧 常见问题解决

### 1. 数据库连接问题

#### ❌ **错误：Unable to open JDBC Connection for DDL execution**
**原因**：数据库连接配置错误或数据库未启动

**解决方案**：
```properties
# 检查application.properties配置
spring.datasource.url=jdbc:mysql://localhost:3306/student_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=你的密码

# 确保MySQL服务已启动
# Windows: net start mysql80
# 确保数据库存在
# MySQL: CREATE DATABASE student_management;
```

#### ❌ **错误：Access denied for user 'root'@'localhost'**
**原因**：用户名或密码错误

**解决方案**：
1. 重置MySQL密码
2. 确认用户权限
3. 创建专用数据库用户

```sql
-- 创建新用户
CREATE USER 'student_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON student_management.* TO 'student_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. SpringBoot启动问题

#### ❌ **错误：Port 8080 was already in use**
**原因**：端口被占用

**解决方案**：
```properties
# 方法1：更改端口
server.port=8081

# 方法2：杀死占用进程
# Windows: netstat -ano | findstr :8080
#          taskkill /PID <进程ID> /F
# Linux/macOS: lsof -ti:8080 | xargs kill -9
```

#### ❌ **错误：Bean validation failed**
**原因**：实体类验证注解配置错误

**解决方案**：
1. 检查@Valid注解使用
2. 确认验证注解正确性
3. 添加validation依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 3. JPA/Hibernate问题

#### ❌ **错误：Table 'students' doesn't exist**
**原因**：表未自动创建

**解决方案**：
```properties
# 确保DDL自动执行
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# 如果还有问题，手动创建表
spring.jpa.hibernate.ddl-auto=create
```

#### ❌ **错误：Column 'xxx' cannot be null**
**原因**：实体类字段约束与数据库不匹配

**解决方案**：
1. 检查@Column(nullable = false)设置
2. 确认数据验证注解
3. 重新生成表结构

### 4. 前端调用问题

#### ❌ **错误：CORS policy blocked**
**原因**：跨域请求被阻止

**解决方案**：
```java
// 已在Controller中配置
@CrossOrigin(origins = "*")

// 或全局配置CORS
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
```

#### ❌ **错误：404 Not Found**
**原因**：API路径错误

**解决方案**：
1. 检查请求URL是否正确
2. 确认Controller的@RequestMapping路径
3. 验证HTTP方法（GET/POST/PUT/DELETE）

### 5. 性能优化建议

#### 🚀 **数据库优化**
```sql
-- 为常查询字段添加索引
CREATE INDEX idx_student_email ON students(email);
CREATE INDEX idx_student_class ON students(class_name);
CREATE INDEX idx_student_major ON students(major);
```

#### 🚀 **JPA查询优化**
```java
// 使用分页避免大量数据查询
@GetMapping("/page")
public Page<Student> getStudents(Pageable pageable) {
    return studentService.findAll(pageable);
}

// 使用@Query优化复杂查询
@Query("SELECT s FROM Student s WHERE s.major = :major ORDER BY s.name")
List<Student> findByMajorOptimized(@Param("major") String major);
```

#### 🚀 **缓存配置**
```xml
<!-- 添加缓存依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

```java
@Service
@EnableCaching
public class StudentService {
    
    @Cacheable("students")
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }
}
```

---

## 🚀 项目部署指南

### 1. 本地部署

#### 📦 **打包应用**
```bash
# 清理并打包
mvn clean package

# 跳过测试打包（如果测试有问题）
mvn clean package -DskipTests

# 打包后的jar文件位于target目录
# 文件名：SpringBoot-0.0.1-SNAPSHOT.jar
```

#### ▶️ **运行应用**
```bash
# 直接运行jar包
java -jar target/SpringBoot-0.0.1-SNAPSHOT.jar

# 指定环境变量
java -jar target/SpringBoot-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

# 后台运行
nohup java -jar target/SpringBoot-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### 2. Docker部署

#### 📝 **创建Dockerfile**
```dockerfile
# Dockerfile
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制jar包
COPY target/SpringBoot-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 🐳 **构建和运行**
```bash
# 构建Docker镜像
docker build -t student-management .

# 运行容器
docker run -d \
  --name student-app \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/student_management \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  student-management

# 查看日志
docker logs -f student-app
```

#### 🐙 **Docker Compose**
```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: student-mysql
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: student_management
      MYSQL_USER: student_user
      MYSQL_PASSWORD: password123
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  app:
    build: .
    container_name: student-app
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/student_management
      SPRING_DATASOURCE_USERNAME: student_user
      SPRING_DATASOURCE_PASSWORD: password123
    restart: unless-stopped

volumes:
  mysql_data:
```

```bash
# 启动所有服务
docker-compose up -d

# 停止所有服务
docker-compose down
```

### 3. 云服务器部署

#### ☁️ **服务器环境准备**
```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装Java 17
sudo apt install openjdk-17-jdk -y

# 安装MySQL
sudo apt install mysql-server -y

# 配置MySQL
sudo mysql_secure_installation

# 创建数据库
mysql -u root -p
CREATE DATABASE student_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 🔧 **应用配置**
```properties
# application-prod.properties
# 生产环境配置
spring.profiles.active=prod

# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/student_management?useUnicode=true&characterEncoding=utf8&useSSL=true
spring.datasource.username=${DB_USERNAME:student_user}
spring.datasource.password=${DB_PASSWORD:password123}

# JPA配置
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# 日志配置
logging.level.root=WARN
logging.level.com.springboot.springboot=INFO
logging.file.name=/var/log/student-management/app.log

# 服务器配置
server.port=8080
server.servlet.context-path=/
```

#### 🚀 **系统服务配置**
```bash
# 创建系统服务文件
sudo nano /etc/systemd/system/student-management.service
```

```ini
[Unit]
Description=Student Management System
After=syslog.target network.target

[Service]
Type=simple
User=ubuntu
ExecStart=/usr/bin/java -jar /home/ubuntu/student-management/app.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

Environment=DB_USERNAME=student_user
Environment=DB_PASSWORD=password123

[Install]
WantedBy=multi-user.target
```

```bash
# 启用并启动服务
sudo systemctl daemon-reload
sudo systemctl enable student-management
sudo systemctl start student-management

# 查看服务状态
sudo systemctl status student-management

# 查看日志
sudo journalctl -u student-management -f
```

### 4. Nginx反向代理

#### 🌐 **Nginx配置**
```nginx
# /etc/nginx/sites-available/student-management
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 静态文件缓存
    location ~* \.(css|js|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

```bash
# 启用网站
sudo ln -s /etc/nginx/sites-available/student-management /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### 5. SSL证书配置

#### 🔒 **Let's Encrypt免费证书**
```bash
# 安装Certbot
sudo apt install certbot python3-certbot-nginx -y

# 获取证书
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo crontab -e
# 添加：0 12 * * * /usr/bin/certbot renew --quiet
```

---

## 📊 监控和维护

### 1. 应用监控

#### 📈 **Spring Boot Actuator**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```properties
# 监控配置
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

访问监控端点：
- 健康检查：`http://localhost:8080/actuator/health`
- 应用信息：`http://localhost:8080/actuator/info`
- 指标数据：`http://localhost:8080/actuator/metrics`

### 2. 日志管理

#### 📝 **Logback配置**
```xml
<!-- src/main/resources/logback-spring.xml -->
<configuration>
    <springProfile name="prod">
        <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>/var/log/student-management/app.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>/var/log/student-management/app.%d{yyyy-MM-dd}.log</fileNamePattern>
                <maxHistory>30</maxHistory>
            </rollingPolicy>
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
        
        <root level="INFO">
            <appender-ref ref="FILE"/>
        </root>
    </springProfile>
</configuration>
```

### 3. 数据备份

#### 💾 **MySQL备份脚本**
```bash
#!/bin/bash
# backup.sh

# 配置
DB_NAME="student_management"
DB_USER="student_user"
DB_PASS="password123"
BACKUP_DIR="/backup/mysql"
DATE=$(date +%Y%m%d_%H%M%S)

# 创建备份目录
mkdir -p $BACKUP_DIR

# 执行备份
mysqldump -u$DB_USER -p$DB_PASS $DB_NAME > $BACKUP_DIR/backup_$DATE.sql

# 压缩备份文件
gzip $BACKUP_DIR/backup_$DATE.sql

# 删除7天前的备份
find $BACKUP_DIR -name "backup_*.sql.gz" -mtime +7 -delete

echo "备份完成：backup_$DATE.sql.gz"
```

```bash
# 设置定时备份
crontab -e
# 每天凌晨2点备份
0 2 * * * /path/to/backup.sh
```

---

## 💡 学习要点

### 实体类设计原则
1. **单一职责**: 每个实体类只负责一个业务概念
2. **完整性**: 包含必要的验证和约束
3. **可维护性**: 清晰的注释和合理的命名
4. **性能考虑**: 合适的字段类型和长度限制

### JPA最佳实践
1. 总是使用`@Column`注解明确字段属性
2. 为字符串字段设置合理的长度限制
3. 使用枚举类型提高数据一致性
4. 利用生命周期回调自动管理时间戳