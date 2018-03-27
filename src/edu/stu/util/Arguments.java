package edu.stu.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Arguments {

    /*
    @params args[0]源语言
            args[1]目标语言
            若需要翻译的内容在文件中，则文件的绝对路径保存在args[2]中
    * */
    public Map<String, String> getWords(String[] args) {
        Map<String, String> words = new HashMap<>();
        words.put("from", checkType(args[0]));
        words.put("to", checkType(args[1]));
        String query;
        if (args[2].equals("null")) {
            //args[2]代表需要翻译的内容来自于文件还是终端命令行参数，null表示来自于终端命令行参数或来自于终端重定向，其他值表示来自于文件
            query = getQueryFromTerminal(args);
            if (query.equals("")) {
                query = defaultQuery(args[0]);
            }
        } else {
            query = getQueryFromFile(args[2], checkType(args[0]));
        }
        words.put("content", query);

        return words;
    }

    private String getQueryFromFile(String pathname, String type) {
        //从文件中获取需要翻译的内容
        String query;
        try {
            FileInputStream fis = new FileInputStream(pathname);
            InputStreamReader isr = new InputStreamReader(fis);// InputStreamReader 字节流通向字符流的桥梁
            BufferedReader br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
            StringBuilder builder = new StringBuilder();
            String buffer;
            while ((buffer = br.readLine()) != null) {
                builder.append(buffer.trim());
                builder.append(" ");
            }
            br.close();
            isr.close();
            fis.close();

            query = builder.toString().trim();
        } catch (Exception e) {
            query = defaultQuery(type);
        }

        return query;
    }

    private String getQueryFromTerminal(String[] args) {
        if (args.length == 3) {
            //需要翻译的内容来自于终端的重定向
            Scanner terminalScanner = new Scanner(System.in);
            StringBuilder builder = new StringBuilder();
            if (terminalScanner.hasNextLine()) {
                while (terminalScanner.hasNextLine()) {
                    builder.append(terminalScanner.nextLine().trim());
                    builder.append(" ");
                }

                return builder.toString().trim();
            } else {
                //用户没有输入任何需要翻译的内容
                return defaultQuery(args[1]);

            }

        } else {
            //需要翻译的内容来自于终端的命令行参数
            StringBuilder builder = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                builder.append(args[i]);
                builder.append(" ");
            }
            return builder.toString().trim();
        }


    }

    private String checkType(String type) {
        //检查语言类型
        String language;
        switch (type.toLowerCase()) {
            case "zh-chs":
                language = "zh-CHS";
                break;
            case "ja":
                language = "ja";
                break;
            case "en":
                language = "EN";
                break;
            case "ko":
                language = "ko";
                break;
            case "fr":
                language = "fr";
                break;
            case "ru":
                language = "ru";
                break;
            case "pt":
                language = "pt";
                break;
            case "es":
                language = "es";
                break;
            default:
                language = "auto";
        }

        return language;

    }

    private String defaultQuery(String type) {
        String query;
        switch (type.toLowerCase()) {
            case "zh-chs":
                query = "请您输入需要翻译的内容。";
                break;
            case "ja":
                query = "翻訳の内容を入力して下さい。";
                break;
            case "en":
                query = "Please type something to translate.";
                break;
            case "ko":
                query = "번역 된 내용을 입력 해 주세요.";
                break;
            case "fr":
                query = "Voulez-vous traduire dans son contenu.";
                break;
            case "ru":
                query = "Пожалуйста, вам нужен перевод ввода контент.";
                break;
            case "pt":
                query = "Por favor, digite precisa traduzir o conteúdo.";
                break;
            case "es":
                query = "Por favor ingrese la necesidad de traducción de contenido.";
                break;
            default:
                query = "Please type something to translate.";
        }

        return query;
    }

}
