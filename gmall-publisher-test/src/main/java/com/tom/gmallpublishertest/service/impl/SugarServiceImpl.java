package com.tom.gmallpublishertest.service.impl;

import com.tom.gmallpublishertest.mapper.ProductStatsMapper;
import com.tom.gmallpublishertest.service.SugarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SugarServiceImpl implements SugarService {
    @Autowired
    private ProductStatsMapper productStatsMapper;

    @Override
    public BigDecimal getGmv(int date) {
        return productStatsMapper.selectGmv(date);
    }

    @Override
    public Map getGmvTm(int date, int limit) {
        // 查询数据
        List<Map> mapList = productStatsMapper.selectGmvByTm(date, limit);

        // 创建Map用于存放结果数据
        HashMap<String, BigDecimal> result = new HashMap<>();

        // 遍历mapList，将数据取出放入result Map["tm_name" -> "苹果"), (order_amount->190531.00)
        for (Map map : mapList){
            result.put((String) map.get("tm_name"), (BigDecimal) map.get("order_amount"));
        }

        // 返回结果集合
        return result;
    }
}
