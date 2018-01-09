package com.shiwen.shares.entity;

import lombok.Data;

/**
 * 股票实体对象
 * <p>
 * 该对象来源：0：”大秦铁路”，股票名字；
 * 1：”27.55″，今日开盘价；
 * 2：”27.25″，昨日收盘价；
 * 3：”26.91″，当前价格；
 * 4：”27.55″，今日最高价；
 * 5：”26.20″，今日最低价；
 * 6：”26.91″，竞买价，即“买一”报价；
 * 7：”26.92″，竞卖价，即“卖一”报价；
 * 8：”22114263″，成交的股票数，由于股票交易以一百股为基本单位，所以在使用时，通常把该值除以一百；
 * 9：”589824680″，成交金额，单位为“元”，为了一目了然，通常以“万元”为成交金额的单位，所以通常把该值除以一万；
 * 10：”4695″，“买一”申请4695股，即47手；
 * 11：”26.91″，“买一”报价；
 * 12：”57590″，“买二”
 * 13：”26.90″，“买二”
 * 14：”14700″，“买三”
 * 15：”26.89″，“买三”
 * 16：”14300″，“买四”
 * 17：”26.88″，“买四”
 * 18：”15100″，“买五”
 * 19：”26.87″，“买五”
 * 20：”3100″，“卖一”申报3100股，即31手；
 * 21：”26.92″，“卖一”报价
 * (22, 23), (24, 25), (26,27), (28, 29)分别为“卖二”至“卖四的情况”
 * 30：”2008-01-11″，日期；
 * 31：”15:05:32″，时间；
 */
@Data
public class ShareEntity {

    /**
     * 股票名字
     */
    private String shareName;


    /**
     * 今日开盘价
     */
    private String todayOpeningPrice;

    /**
     * 昨日收盘价
     */
    private String yesterdayClosingPrice;

    /**
     * 当前价格
     */
    private String currentPrice;
    /**
     * 今日最高价
     */
    private String todayHighPrice;

    /**
     * 今日最低价
     */
    private String todayLowPrice;

    /**
     * 买一报价
     */
    private String buyOnePrice0;

    /**
     * 卖一报价
     */
    private String sellOnePrice0;


    /**
     * 成交数量
     */
    private String dealNum;

    /**
     * 成交金额（单位：万元）
     */
    private String dealMoney;

    /**
     * 买一报价
     */
    private String buyOnePrice;

    /**
     * 买一的数量
     */
    private String buyOneNum;


    /**
     * 买二的价格
     */
    private String byTowPrice;

    /**
     * 买二的数量
     */
    private String buyTowNum;

    /**
     * 买三的价格
     */
    private String byThreePrice;

    /**
     * 买三的数量
     */
    private String buyThreeNum;

    /**
     * 买四的价格
     */
    private String byFourPrice;

    /**
     * 买四的数量
     */
    private String buyFourNum;

    /**
     * 买五的价格
     */
    private String byFivePrice;

    /**
     * 买五的数量
     */
    private String buyFiveNum;


    /**
     * 卖一报价
     */
    private String sellOnePrice;

    /**
     * 卖一的数量
     */
    private String sellOneNum;


    /**
     * 卖二报价
     */
    private String sellTwoPrice;

    /**
     * 卖二的数量
     */
    private String sellTwoNum;

    /**
     * 卖三报价
     */
    private String sellThreePrice;
    /**
     * 卖三的数量
     */
    private String sellThreeNum;

    /**
     * 卖四报价
     */
    private String sellFourPrice;

    /**
     * 卖四的数量
     */
    private String sellFourNum;

    /**
     * 卖五报价
     */
    private String sellFivePrice;

    /**
     * 卖五的数量
     */
    private String sellFiveNum;

    /**
     * 日期
     */
    private String date;

    /**
     * 时间
     */
    private String time;

    public static ShareEntity buildShareEntity(String result) {
        ShareEntity shareEntity = new ShareEntity();
        String[] values = result.split(",");
        shareEntity = setEntityValue(shareEntity, values);
        return shareEntity;

    }

    /**
     * 该对象来源：
     * 0：”大秦铁路”，股票名字；
     * 1：”27.55″，今日开盘价；
     * 2：”27.25″，昨日收盘价；
     * 3：”26.91″，当前价格；
     * 4：”27.55″，今日最高价；
     * 5：”26.20″，今日最低价；
     * 6：”26.91″，竞买价，即“买一”报价；
     * 7：”26.92″，竞卖价，即“卖一”报价；
     * 8：”22114263″，成交的股票数，由于股票交易以一百股为基本单位，所以在使用时，通常把该值除以一百；
     * 9：”589824680″，成交金额，单位为“元”，为了一目了然，通常以“万元”为成交金额的单位，所以通常把该值除以一万；
     * 10：”4695″，“买一”申请4695股，即47手；
     * 11：”26.91″，“买一”报价；
     * 12：”57590″，“买二”
     * 13：”26.90″，“买二”
     * 14：”14700″，“买三”
     * 15：”26.89″，“买三”
     * 16：”14300″，“买四”
     * 17：”26.88″，“买四”
     * 18：”15100″，“买五”
     * 19：”26.87″，“买五”
     * 20：”3100″，“卖一”申报3100股，即31手；
     * 21：”26.92″，“卖一”报价
     * (22, 23), (24, 25), (26,27), (28, 29)分别为“卖二”至“卖四的情况”
     * 30：”2008-01-11″，日期；
     * 31：”15:05:32″，时间；
     */
    private static ShareEntity setEntityValue(ShareEntity shareEntity, String[] values) {
        if (values == null) return shareEntity;
        shareEntity.shareName = values[0];
        shareEntity.todayOpeningPrice = values[1];
        shareEntity.yesterdayClosingPrice = values[2];
        shareEntity.currentPrice = values[3];
        shareEntity.todayHighPrice = values[4];
        shareEntity.todayLowPrice = values[5];
        shareEntity.buyOnePrice0 = values[6];
        shareEntity.sellOnePrice0 = values[7];
        shareEntity.dealNum = values[8];
        shareEntity.dealMoney = values[9];
        shareEntity.buyOnePrice = values[10];
        shareEntity.buyOneNum = values[11];
        shareEntity.byTowPrice = values[12];
        shareEntity.buyTowNum = values[13];
        shareEntity.byThreePrice = values[14];
        shareEntity.buyThreeNum = values[15];
        shareEntity.byFourPrice = values[16];
        shareEntity.buyFourNum = values[17];
        shareEntity.byFivePrice = values[18];
        shareEntity.buyFiveNum = values[19];
        shareEntity.sellOnePrice = values[20];
        shareEntity.sellOneNum = values[21];
        shareEntity.sellTwoPrice = values[22];
        shareEntity.sellTwoNum = values[23];
        shareEntity.sellThreePrice = values[24];
        shareEntity.sellThreeNum = values[25];
        shareEntity.sellFourPrice = values[26];
        shareEntity.sellFourNum = values[27];
        shareEntity.sellFivePrice = values[28];
        shareEntity.sellFiveNum = values[19];
        shareEntity.date = values[30];
        shareEntity.time = values[31];
        return shareEntity;
    }

}
