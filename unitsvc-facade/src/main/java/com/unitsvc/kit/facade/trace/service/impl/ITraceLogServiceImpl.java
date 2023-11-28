package com.unitsvc.kit.facade.trace.service.impl;

import com.unitsvc.kit.facade.trace.service.pojo.BizTraceLog;
import com.jpardus.db.jmongo.MongoInserter;
import com.jpardus.db.jmongo.MongoUpdater;
import com.unitsvc.kit.facade.trace.service.ITraceLogService;
import com.unitsvc.kit.facade.trace.utils.TraceConfigUtil;
import org.springframework.stereotype.Service;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/27 13:28
 **/
@Service
public class ITraceLogServiceImpl implements ITraceLogService {

    /**
     * 新增链路日志
     *
     * @param traceLog 链路日志
     */
    @Override
    public void insert(BizTraceLog traceLog) {
        MongoInserter.insertObject(TraceConfigUtil.getTraceLogTableName(), traceLog);
    }

    /**
     * 更新链路日志
     *
     * @param traceLog 链路日志
     */
    @Override
    public void upsert(BizTraceLog traceLog) {
        MongoUpdater.upsert(TraceConfigUtil.getTraceLogTableName(), traceLog);
    }

}
