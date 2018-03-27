package edu.stu.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class Print {
    private String jsonString;

    public Print(String jsonString) {
        this.jsonString = jsonString;
    }

    public int printf() {
        JSONObject result = new JSONObject(jsonString);
        String errorCode = result.getString("errorCode");//错误码
        if (!errorCode.equals("0")) {
            //从有道API处查词失败
            System.out.println("There is an internet error. Please try it latter!");
            return -1;
        }

        String query = "";//源内容，即需要翻译的内容
        try {
            query = result.getString("query");//源内容，即需要翻译的内容
        } catch (JSONException e) {
            //有道API对源内容翻译失败，即有道API翻译不出源内容，一般是用户输入的源内容拼写错误
            System.out.println("No such words, please check your spelling.");
            return 1;
        }

        JSONArray translation = result.getJSONArray("translation");//翻译结果
        System.out.println("************************************");
        System.out.println("查询：" + query);

        String lang = result.getString("l");//查询时指定的源语言和目标语言，无论查询成功与否该字符串一定存在
        System.out.println("目标：" + languageFrom2To(lang));

        JSONObject basic;//基本词典,查词而不是查句子时才有
        try {
            basic = result.getJSONObject("basic");
        } catch (JSONException e) {
            //System.out.println("[金圣叹曰：妈的！basic为空]");
            basic = null;
        }

        if (basic != null) {
            String phonetic;//默认音标，默认是英式音标，英文查词成功，一定存在
            try {
                phonetic = basic.getString("phonetic");
            } catch (JSONException e) {
                phonetic = null;
            }
            if (phonetic != null) {
                System.out.println("英式发音：" + phonetic);
            }

            JSONArray explains = basic.getJSONArray("explains");//查单词时的基本词义
            System.out.println("基本解释：");
            for (Object obj : explains.toList()) {
                System.out.print("\t\"" + (String) obj + "\"\n");
            }

        }

        System.out.println("详细解释：");
        for (Object obj : translation.toList()) {
            System.out.print("\t\"" + (String) obj + "\"  ");
        }

        System.out.println();
        return 0;
    }

    private String languageFrom2To(String lang) {
        String hint = "英文翻中文";
        for (int i = 0; i < 64; i++) {
            if (lang.equals(Print.lang1[i])) {
                return Print.lang2[i];
            }
        }

        return hint;
    }

    private static String[] lang1 = new String[64];
    private static String[] lang2 = new String[64];

    static {
        Map<String, String> map = new TreeMap<>();//TreeMap<>在map集合中插入新元素时，会将该新元素有序地插入
        map.put("zh-CHS", "中文");
        map.put("ja", "日文");
        map.put("EN", "英文");
        map.put("ko", "韩文");
        map.put("fr", "法文");
        map.put("ru", "俄文");
        map.put("pt", "葡萄牙文");
        map.put("es", "西班牙文");

        int k = 0;
        for (Map.Entry<String, String> entry1 : map.entrySet()) {
            for (Map.Entry<String, String> entry2 : map.entrySet()) {
                lang1[k] = entry1.getKey() + "2" + entry2.getKey();
                lang2[k] = entry1.getValue() + "转" + entry2.getValue();
                ++k;
            }
        }
    }
}
