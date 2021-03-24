package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.cv.service.statis.CoverCollectStatisService;
import com.jeeplus.modules.cv.vo.CollectionStatisVO;
import com.jeeplus.modules.cv.vo.IndexStatisVO;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "${adminPath}/bigData")
public class BigDataController  extends BaseController {
    @Autowired
    private CoverCollectStatisService coverCollectStatisService;
    @RequestMapping(value = "index")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        //管网用途数据统计
        List<CollectionStatisVO>  purposeList=coverCollectStatisService.getCoverByPurpose();
        StringBuffer nameSB=new StringBuffer();
        StringBuffer numSB=new StringBuffer();
        if(null!=purposeList&&purposeList.size()>0){
            for(CollectionStatisVO vo:purposeList){
                String purpose= vo.getPurpose();
                if(StringUtils.isNotEmpty(purpose)){
                    nameSB.append("'").append(vo.getPurpose()).append("'").append(", ");
                    numSB.append("'").append(vo.getCoverTotalNum()).append("'").append(", ");
                }

            }
        }

        String names=nameSB.toString();
        String nums=numSB.toString();
        model.addAttribute("purposeNames", names.substring(0,names.length()-1 ));
        model.addAttribute("purposeNums", nums.substring(0,nums.length()-1 ));
        //权属单位数据统计
        List<CollectionStatisVO>  ownerList=coverCollectStatisService.getNumByOwner();
        model.addAttribute("ownerList", ownerList);

        //井盖病害统计
        List<CollectionStatisVO>  damageList=coverCollectStatisService.statisByDamage();
        model.addAttribute("damageList", damageList);
        IndexStatisVO indexStatisVO=coverCollectStatisService.statisIndex();
        model.addAttribute("indexStatisVO", indexStatisVO);
        return "modules/cv/bigData/index";
    }

}
