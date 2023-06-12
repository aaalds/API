package com.heart.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送ID请求
 *
 * @author yupi
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}