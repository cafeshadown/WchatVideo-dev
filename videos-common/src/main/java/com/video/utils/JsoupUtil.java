//package com.video.utils;
//
//import org.apache.commons.lang.StringEscapeUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Entities;
//import org.jsoup.safety.Whitelist;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author zhouchao
// * Created on 2018-11-16
// * 描述: 过滤和转义html标签和属性中的敏感字符
// */
//@Slf4j
//public class JsoupUtil {
//
//    /**
//     * 标签白名单
//     * relaxed() 允许的标签:
//     *  a, b, blockquote, br, caption, cite, code, col, colgroup, dd, dl, dt, em, h1, h2, h3, h4,
//     *  h5, h6, i, img, li, ol, p, pre, q, small, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul。
//     *  结果不包含标签rel=nofollow ，如果需要可以手动添加。
//     */
//    static Whitelist WHITELIST = Whitelist.relaxed();
//
//    /**
//     * 配置过滤化参数,不对代码进行格式化
//     */
//    static Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings()
//            .prettyPrint(false);
//
//    /**
//     * 设置自定义的标签和属性
//     */
//    static {
//        /**
//         * addTags() 设置白名单标签
//         * addAttributes()  设置标签需要保留的属性 ,[:all]表示所有
//         * preserveRelativeLinks()  是否保留元素的URL属性中的相对链接，或将它们转换为绝对链接,默认为false. 为false时将会把baseUri和元素的URL属性拼接起来
//         */
//        WHITELIST.addAttributes(":all", "style");
//        WHITELIST.addAttributes("p", "style", "font-family");
//        WHITELIST.preserveRelativeLinks(true);
//    }
//
//    public static String clean(String s) {
//        //        return s;
//        /**
//         * baseUri ,非空
//         * 如果baseUri为空字符串或者不符合Http://xx类似的协议开头,属性中的URL链接将会被删除,如<a href='xxx'/>会变成<a/>
//         * 如果WHITELIST.preserveRelativeLinks(false), 会将baseUri和属性中的URL链接进行拼接
//         */
//        Entities.EscapeMode.base.getMap().clear();
//        //        log.info("[xss过滤标签和属性] [原字符串为] : {}", s);
//        //        s = s.replaceAll("\\\\", "");
//        String r = Jsoup.clean(s, "http://base.uri", WHITELIST, OUTPUT_SETTINGS);
//        //        log.info("[xss过滤标签和属性] [过滤后的字符串为] : {}", r);
//        return r;
//    }
//
//    /**
//     * 处理Json类型的Html标签,进行xss过滤
//     */
//    public static String cleanJson(String s) {
//        //先处理双引号的问题
//        clean(s);
//        return StringEscapeUtils.unescapeXml(s);
//    }
//}
