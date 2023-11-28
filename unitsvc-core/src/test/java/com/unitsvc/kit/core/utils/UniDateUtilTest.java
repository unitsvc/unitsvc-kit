package com.unitsvc.kit.core.utils;

import org.junit.Test;

/**
 * 功能描述：时间工具类测试
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/22 13:30
 **/
public class UniDateUtilTest {

    /**
     * 测试日期工具类
     */
    @Test
    public void dateUtilTest() {
        UniDateUtil.YearMonthInner yearMonthInner = UniDateUtil.adjustNowYearMouthByCustom(2023, 1, -1);
        System.out.println("yearMonthInner = " + yearMonthInner);

        Long t1 = UniDateUtil.toTimestamp("2023-02-22 12:00");
        System.out.println("t1 = " + t1);

        Long t2 = UniDateUtil.toTimestamp("2023/02/22 12:00");
        System.out.println("t2 = " + t2);

        Long t3 = UniDateUtil.toTimestamp("2023/2/22 12:00");
        System.out.println("t3 = " + t3);

        Long t4 = UniDateUtil.toTimestamp("2023/02/22");
        System.out.println("t4 = " + t4);

        Long t5 = UniDateUtil.toTimestamp("2023-2-22");
        System.out.println("t5 = " + t5);

        Long t6 = UniDateUtil.toTimestamp("1677038400000");
        System.out.println("t6 = " + t6);

        Long t7 = UniDateUtil.toTimestamp("1677038400");
        System.out.println("t7 = " + t7);

        String strTime = UniDateUtil.toFmtStrTime(1677038400L, "yyyy-MM-dd HH:mm:ss");
        System.out.println("strTime = " + strTime);

    }

}
