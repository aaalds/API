package com.heart.heartapiinterface.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName country
 */
@Data
public class Country implements Serializable {
    /**
     * 
     */
    private String country;

    /**
     * 
     */
    private Integer adcode;

    /**
     * 
     */
    private Integer citycode;

    private static final long serialVersionUID = 1L;
}