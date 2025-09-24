package com.springboot.springboot.exception;

/**
 * 数据冲突异常
 * 
 * 当数据存在冲突时抛出此异常（如邮箱重复、唯一约束冲突等）
 * 
 * @author SpringBoot教程
 * @version 1.0
 */
public class DataConflictException extends RuntimeException {

    /**
     * 默认构造函数
     */
    public DataConflictException() {
        super();
    }

    /**
     * 带消息的构造函数
     * 
     * @param message 异常消息
     */
    public DataConflictException(String message) {
        super(message);
    }

    /**
     * 带消息和原因的构造函数
     * 
     * @param message 异常消息
     * @param cause 异常原因
     */
    public DataConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
