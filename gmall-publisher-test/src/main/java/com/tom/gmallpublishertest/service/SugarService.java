package com.tom.gmallpublishertest.service;

import java.math.BigDecimal;
import java.util.Map;

public interface SugarService {
    BigDecimal getGmv(int date);

    Map getGmvTm(int date, int limit);
}
