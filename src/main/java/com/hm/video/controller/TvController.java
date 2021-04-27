package com.hm.video.controller;

import com.alibaba.fastjson.JSONObject;
import com.hm.video.dto.VideoDTO;
import com.hm.video.util.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Controller
public class TvController {
    private final String domain="https://www.hktvyb.com/";
    @GetMapping("/")
    public String index(Model model) {
        String url=domain+"vod/detail/id/947.html";
        Element body = JsoupUtil.getBody(url);
        if(body==null)
            return null;
        Elements imgs = body.select(".myui-vodlist__thumb>.lazyload");
        Element img = imgs.first();
        String src = img.attr("data-original");
        VideoDTO videoDTO=new VideoDTO();
        videoDTO.setImgUrl(src);
        Elements es = body.getElementsByClass("myui-content__list scrollbar sort-list clearfix");

        String playUrl=null;
        for (Element e : es) {
            Element li = e.selectFirst("li");
            Element a = li.selectFirst("a");
            String href = domain+a.attr("href");

           // sb.append(a.html()+"\n"+href+"\n\n");
            Element playBody = JsoupUtil.getBody(href);
            //class=""
            Elements clearfix = playBody.select(".myui-player__box>.clearfix");
            Element script = clearfix.select("script").first();
            String s = script.html().split("=")[1];
            JSONObject jsonObject= JSONObject.parseObject(s);
            playUrl=jsonObject.getString("url");
            //sb.append(jsonObject.getString("url")+"\n\n");
            if(playUrl.startsWith("https:")){
                videoDTO.setName(a.html().replace("备用",""));
                videoDTO.setPageUrl(href);
                videoDTO.setPlayUrl(playUrl);
                break;
            }
        }
        model.addAttribute("videoDTO", videoDTO);
        return "index";
    }

  /*  public static void main(String[] args) throws IOException {
        TvController controller=new TvController();
        controller.home();
    }*/

}
