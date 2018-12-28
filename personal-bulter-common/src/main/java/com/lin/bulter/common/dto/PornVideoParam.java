package com.lin.bulter.common.dto;

import com.lin.bulter.common.dto.common.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PornVideoParam implements Serializable {

    private PageParam page;

}
