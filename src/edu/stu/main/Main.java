package edu.stu.main;

import edu.stu.util.Arguments;
import edu.stu.util.Print;
import edu.stu.util.YouDaoTemplate;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> words = new Arguments().getWords(args);//获得用户输入的参数
        YouDaoTemplate youdao = new YouDaoTemplate();
        String api = "http://openapi.youdao.com/api";//有道API地址
        try {
            String resultJson = youdao.requestForHttp(api, youdao.prepareArg(words));//从有道API处返回的查询结果
            Print p = new Print(resultJson);//用于打印结果的对象
            int status = p.printf();
            if (status == -1) {
                System.out.println("Or maybe can not translate \"" + words.get("content") + "\"");
            }
        } catch (Exception e) {
            //网络异常
            System.out.println("No Internet connection, please check your network status.");
        }
    }
}
