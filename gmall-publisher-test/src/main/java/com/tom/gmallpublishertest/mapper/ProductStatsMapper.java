package com.tom.gmallpublishertest.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductStatsMapper {
    // @Select("select sum(order_amount) from product_stats where toYYYYMMDD(stt)=#{date}")
    @Select("select sum(sku_price) from product_stats where toYYYYMMDD(stt)=#{date}")
    BigDecimal selectGmv(int date);

    /**
     *
     * ┌─tm_name─┬─order_amount─┐
     * │ 苹果    │    190531.00 │
     * │ 小米    │    139177.00 │
     * │ Redmi   │     32172.00 │
     * │ TCL     │     27897.00 │
     * │ 华为    │     26778.00 │
     * └─────────┴──────────────┘
     */
    @Select("select tm_name, sum(sku_price) order_amount from product_stats where toYYYYMMDD(stt)=#{date} " +
            "group by tm_name order by order_amount desc limit #{limit}")
    List<Map> selectGmvByTm(@Param("date") int date, @Param("limit") int limit);
}
