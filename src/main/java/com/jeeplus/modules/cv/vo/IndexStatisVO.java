package com.jeeplus.modules.cv.vo;

import java.util.List;

/**
 * 首页统计数据
 */
public class IndexStatisVO {

    Integer coverTotalNum=0;		// 总勘察数
    Integer coverTodayNum=0;		// 今日勘察数
    Integer coverNoDepartNum=0;	//ownerDepart 无权属单位

    //井盖损坏形式数据
    Integer damageGoodNum=0;		// 损坏形式-完好0
    Integer damageDefectNum=0;		// 损坏形式-井盖缺失1
    Integer damageDestroyNum=0;		// 损坏形式-井盖破坏2
    Integer damageRiftNum=0;		// 损坏形式-井周沉降、龟裂3
    Integer damageOwnerNum=0;		// 损坏形式-井筒本身破坏4
    Integer damageOtherNum=0;		//其他9


    List<CollectionStatisVO> collectionList;//最近一周采集井盖数据汇总
    List<UserCollectionVO> userCollectionList;//人员采集井盖情况汇总
}
