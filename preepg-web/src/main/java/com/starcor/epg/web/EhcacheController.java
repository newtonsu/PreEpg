package com.starcor.epg.web;

import com.starcor.demo.api.RedisApi;
import com.starcor.demo.enty.ResultDto;
import com.starcor.epg.cache.ehcache.EhcacheUtil;
import com.starcor.epg.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xiaoqiang on 2017/6/27.
 */
@Controller
@RequestMapping("/ehcache")
public class EhcacheController {

//    @Autowired
//    private RedisManager redisManager;

//    @Autowired
//    private MessageService messageService;

    @Autowired
    private RedisApi redisApi;

    @ResponseBody
    @RequestMapping(value = "/getElement", method = RequestMethod.GET)
    public ModelAndView addElement(Model model, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        String key = request.getParameter("key");
        String value = EhcacheUtil.getInstance().get("userCache", key).toString();//会触发 cacheEvent的 get 事件
        ResultDto resultDto = redisApi.getRedis(key,value);
//        request.setAttribute("value",value);
//        request.setAttribute("redisKey",resultDto.getKey());
        request.setAttribute("value",resultDto.getValue());
        modelAndView.setViewName("haha");
//        redisManager.get(key);
//        try {
//            messageService.sendMsg(key,value) ;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return modelAndView;
      /*  EhcacheUtil.getInstance().put("userCache","name","qiang");//会触发 cacheEvent的 put 事件*/

/*        EhcacheUtil.getInstance().addCache("age");//会触发 cacheManagerEvent的 add 事件
        EhcacheUtil.getInstance().removeCache("age");//会触发 cacheManagerEvent的 remove 事件*/
    }

    @ResponseBody
    @RequestMapping(value = "/putElement", method = RequestMethod.GET)
    public ModelAndView putElement(Model model, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        EhcacheUtil.getInstance().put("userCache", key, value);//会触发 cacheEvent的 put 事件*/
//        System.out.println(EhcacheUtil.getInstance().get("userCache","name"));//会触发 cacheEvent的 get 事件
        modelAndView.setViewName("hello");
//        redisManager.set(key,value);
        return modelAndView;
/*        EhcacheUtil.getInstance().addCache("age");//会触发 cacheManagerEvent的 add 事件
        EhcacheUtil.getInstance().removeCache("age");//会触发 cacheManagerEvent的 remove 事件*/
    }
}
