package com.starcor.demo.enty;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultDto implements Serializable {
    private String key;
    private String value;
}
