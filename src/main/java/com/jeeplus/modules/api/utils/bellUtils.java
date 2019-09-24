package com.jeeplus.modules.api.utils;

import com.jeeplus.modules.cv.constant.CodeConstant;

public class bellUtils {
    /**
     *
     * @param fortificationState //设防状态： 0:已设防1：已撤防
     * @return
     */
    public static String changeDefenseStatus(Integer fortificationState){
        String defenseStatus="";
        if(fortificationState==0){
            defenseStatus= CodeConstant.DEFENSE_STATUS.FORTIFY;
        }else{
            defenseStatus=CodeConstant.DEFENSE_STATUS.REVOKE;
        }
        return defenseStatus;
    }
}
