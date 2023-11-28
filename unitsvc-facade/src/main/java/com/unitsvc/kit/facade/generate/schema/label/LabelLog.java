package com.unitsvc.kit.facade.generate.schema.label;

import com.jpardus.spider.sccs.Log;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 功能描述：标签刷新记录
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:29
 **/
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LabelLog extends Log {
    /**
     * 标签编号
     */
    private String label;
}

