package com.springboot.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 
 * 这个类用于统一处理整个应用程序中抛出的异常，提供统一的错误响应格式
 * 
 * @RestControllerAdvice - 全局异常处理注解，结合了@ControllerAdvice和@ResponseBody
 * 作用：
 * 1. 捕获全局异常
 * 2. 统一返回格式
 * 3. 减少重复的异常处理代码
 * 4. 提供友好的错误信息
 * 
 * @author SpringBoot教程
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理运行时异常
     * 
     * @ExceptionHandler - 指定要处理的异常类型
     * 当Controller层或Service层抛出RuntimeException时，会被这个方法捕获
     * 
     * @param ex 异常对象
     * @return 统一的错误响应
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
     * 
     * 当使用@Valid注解验证请求参数失败时，会抛出MethodArgumentNotValidException
     * 这个方法会捕获验证异常并返回详细的验证错误信息
     * 
     * @param ex 参数验证异常
     * @return 包含验证错误详情的响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // 收集所有验证错误
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "参数验证失败",
                "请求参数不符合要求",
                LocalDateTime.now(),
                errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理资源未找到异常
     * 
     * 自定义异常，用于处理查找资源不存在的情况
     * 
     * @param ex 资源未找到异常
     * @return 404错误响应
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "资源未找到",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理数据重复异常
     * 
     * 自定义异常，用于处理数据重复的情况（如邮箱重复）
     * 
     * @param ex 数据重复异常
     * @return 409冲突响应
     */
    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleDataConflictException(DataConflictException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "数据冲突",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * 处理非法参数异常
     * 
     * 当传入的参数不合法时抛出此异常
     * 
     * @param ex 非法参数异常
     * @return 400错误响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "参数错误",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理其他未知异常
     * 
     * 作为兜底处理，捕获所有未被特定处理器处理的异常
     * 
     * @param ex 异常对象
     * @return 500内部服务器错误响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "系统内部错误",
                "服务器发生未知错误，请稍后重试",
                LocalDateTime.now()
        );

        // 在生产环境中，应该记录完整的异常堆栈信息到日志
        ex.printStackTrace();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 统一错误响应格式
     * 
     * 这个内部类定义了统一的错误响应格式，确保所有错误返回的JSON结构一致
     */
    public static class ApiErrorResponse {
        private int status;          // HTTP状态码
        private String error;        // 错误类型
        private String message;      // 错误消息
        private LocalDateTime timestamp;  // 错误发生时间
        private Map<String, String> details;  // 详细错误信息（可选）

        // 构造函数 - 不包含详细信息
        public ApiErrorResponse(int status, String error, String message, LocalDateTime timestamp) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.timestamp = timestamp;
        }

        // 构造函数 - 包含详细信息
        public ApiErrorResponse(int status, String error, String message, LocalDateTime timestamp, Map<String, String> details) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.timestamp = timestamp;
            this.details = details;
        }

        // Getter和Setter方法
        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public Map<String, String> getDetails() {
            return details;
        }

        public void setDetails(Map<String, String> details) {
            this.details = details;
        }
    }
}
