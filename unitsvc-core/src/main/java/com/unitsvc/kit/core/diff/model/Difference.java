package com.unitsvc.kit.core.diff.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 功能描述：不同值
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/27 18:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Difference implements Serializable {

    private static final long serialVersionUID = 5978761572695905177L;

    /**
     * 新值
     */
    private Object oldValue;
    /**
     * 旧值
     */
    private Object newValue;

}
