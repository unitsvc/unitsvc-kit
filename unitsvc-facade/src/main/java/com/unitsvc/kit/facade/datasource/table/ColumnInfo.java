package com.unitsvc.kit.facade.datasource.table;

import com.unitsvc.kit.facade.datasource.table.enums.ColumnTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * 功能描述：数据列信息
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/20 15:01
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColumnInfo implements Serializable {

    private static final long serialVersionUID = -3680391925142105542L;

    /**
     * 必填，是否主键
     * <p>
     * 说明：必须设置唯一主键
     */
    private Boolean isPrimary;

    /**
     * 必填，数据库列名
     */
    private String columnName;

    /**
     * 特殊可选，数据列别名
     * <p>
     * 说明：例如mongodb主键为_id，而实体类为id，若为主键，则别名必填。
     */
    private String columnAlias;

    /**
     * 必填，数据列类型
     */
    private ColumnTypeEnum columnType;

    /**
     * 构造方法
     *
     * @param consumer 参数变量
     * @return
     */
    public static ColumnInfo of(Consumer<ColumnInfo> consumer) {
        ColumnInfo column = new ColumnInfo();
        consumer.accept(column);
        return column;
    }

}
