package com.springboot.springboot.exception;

/**
 * 资源未找到异常
 * 
 * 当查找的资源（如学生）不存在时抛出此异常
 * 
 * @author SpringBoot教程
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 默认构造函数
     */
    public ResourceNotFoundException() {
        super();
    }

    /**
     * 带消息的构造函数
     * 
     * @param message 异常消息
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * 带消息和原因的构造函数
     * 
     * @param message 异常消息
     * @param cause 异常原因
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
