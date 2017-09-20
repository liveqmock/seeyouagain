package com.xmniao.xmn.core.manor.controller;

import com.xmniao.xmn.core.manor.entity.FlowerNode;
import com.xmniao.xmn.core.manor.service.ManorFlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 黄金庄园-花朵管理Controller
 * Created by yang.qiang on 2017/6/21.
 */
@RequestMapping("manor/flower")
@Controller
public class ManorFlowerController {
    @Autowired
    private ManorFlowerService manorFlowerService;

    @RequestMapping("")
    public String init(){
        return "golden_manor/manorFlower";
    }

    /**
     * 查询用户的初始节点 及 一级子节点
     * @param uid
     * @return
     */
    @ResponseBody
    @RequestMapping("init-node")
    public Object initNode(Integer uid ){
        FlowerNode initNode = manorFlowerService.queryInitNodeAndSubNode(uid);

        return initNode;
    }

    @ResponseBody
    @RequestMapping("all-node")
    public Object allNode(Integer uid){
        try {
            FlowerNode allNode = manorFlowerService.queryAllNode(uid);
            return allNode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("all-node-expanded")
    public Object allNodeExpanded(Integer uid){
        try {
            FlowerNode allNode = manorFlowerService.queryAllNodeEx(uid);
            return allNode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("node-info")
    public Object nodeInfo(Integer nodeId){
        return manorFlowerService.queryNodeInfo(nodeId);
    }

}
