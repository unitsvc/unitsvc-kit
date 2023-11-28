package com.unitsvc.kit.core.diff;

import com.unitsvc.kit.core.diff.model.DiffWrapper;
import com.unitsvc.kit.core.diff.utils.DiffUtil;

import java.util.List;

/**
 * 功能描述：中文输出复杂对象差异
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/27 18:21
 */
public class ChineseObjectDiff extends AbstractObjectDiff {

    /**
     * 自定义查询比对
     *
     * @param sourceObject 原始对象
     * @param targetObject 目标对象
     * @return 字符串
     * @throws Exception 异常
     */
    @Override
    protected String genDiffStr(Object sourceObject, Object targetObject) throws Exception {
        List<DiffWrapper> diffWrappers = generateDiff(sourceObject, targetObject);
        return DiffUtil.genDiffStr(diffWrappers);
    }

}
