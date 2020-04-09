package com.dadalong.autotest.model.user;

import lombok.Data;

import java.util.List;

@Data
public class ApiDTO {
    private String name;
    private List<ListDTO> list;
}
