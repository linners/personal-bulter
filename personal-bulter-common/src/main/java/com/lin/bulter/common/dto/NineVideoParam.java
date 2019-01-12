package com.lin.bulter.common.dto;

import com.lin.bulter.common.dto.common.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NineVideoParam {

    private PageParam page;

    private Integer id;

    private String video;

    private String title;

    private String img;

}
