package com.hm.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.hm.video.util.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TvController {
    private final String domain="https://www.hktvyb.com/";
    @GetMapping("/home")
    public String home() {
        String url=domain+"vod/detail/id/947.html";
        Element body = JsoupUtil.getBody(url);
        if(body==null)
            return null;
        Elements es = body.getElementsByClass("myui-content__list scrollbar sort-list clearfix");
        for (Element e : es) {
            Element li = e.selectFirst("li");
            Element a = li.selectFirst("a");
            String href = domain+a.attr("href");
            System.out.println(a.html()+"\n"+href);
            Element playBody = JsoupUtil.getBody(href);
            //class=""
            Elements clearfix = playBody.select(".myui-player__box>.clearfix");
            Element script = clearfix.select("script").first();
            String s = script.html().split("=")[1];
            JSONObject jsonObject= JSONObject.parseObject(s);
            System.out.println(jsonObject.getString("url"));
        }
        return body.text();
    }

    public static void main(String[] args) throws IOException {
        TvController controller=new TvController();
        controller.home();
    }

}
