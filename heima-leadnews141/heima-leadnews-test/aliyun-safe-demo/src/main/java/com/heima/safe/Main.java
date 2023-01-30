package com.heima.safe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.ImageSyncScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        IClientProfile profile = DefaultProfile
            .getProfile("cn-shanghai", "LTAI5tBBQNpZsVmk78pfc9Y4", "tc522gz9dtyG8rTOxSsuy01yRHPEUr");
        DefaultProfile
            .addEndpoint("cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);

        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        // 指定返回格式。
        imageSyncScanRequest.setAcceptFormat(FormatType.JSON);
        // 指定请求方法。
        imageSyncScanRequest.setMethod(MethodType.POST);
        imageSyncScanRequest.setEncoding("utf-8");
        // 支持HTTP和HTTPS。
        imageSyncScanRequest.setProtocol(ProtocolType.HTTP);

        JSONObject httpBody = new JSONObject();
        /**
         * 设置要检测的场景。
         * ocr：表示OCR图文识别和OCR卡证识别。
         */
        httpBody.put("scenes", Arrays.asList("ocr"));

        /**
         * 设置待检测的图片，一张图片对应一个检测任务。
         * 多张图片同时检测时，处理时间由最后一张处理完的图片决定。
         * 通常情况下批量检测的平均响应时间比单任务检测长，一次批量提交的图片数越多，响应时间被拉长的概率越高。
         * 代码中以单张图片检测作为示例，如果需要批量检测多张图片，请自行构建多个任务。
         */
        JSONObject task = new JSONObject();
        task.put("dataId", UUID.randomUUID().toString());

        // 设置图片链接。
        task.put("url", "https://leadnews141.oss-cn-shanghai.aliyuncs.com/material/2022/4/20220424/11111.png");
        task.put("time", new Date());
        httpBody.put("tasks", Arrays.asList(task));

        // 需要OCR卡证识别时，设置卡证类型。
        JSONObject cardExtras = new JSONObject();
        // 身份证正面识别。
//        cardExtras.put("card", "id-card-front");
        // 身份证反面。
        //cardExtras.put("card", "id-card-back");
//        httpBody.put("extras", cardExtras);
        imageSyncScanRequest.setHttpContent(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(httpBody.toJSONString()), "UTF-8", FormatType.JSON);
        /**
         * 请设置超时时间，服务端全链路处理超时时间为10秒，请据此做相应设置。
         * 如果您设置的ReadTimeout小于服务端处理的时间，程序中会获得一个read timeout异常。
         */
        imageSyncScanRequest.setConnectTimeout(3000);
        imageSyncScanRequest.setReadTimeout(10000);
        HttpResponse httpResponse = null;
        try {
            httpResponse = client.doAction(imageSyncScanRequest);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        // 服务端接收到请求，并完成处理返回的结果。
        if(httpResponse != null && httpResponse.isSuccess()){
            JSONObject scrResponse = JSON.parseObject(org.apache.commons.codec.binary.StringUtils.newStringUtf8(httpResponse.getHttpContent()));
            System.out.println(JSON.toJSONString(scrResponse));
            int requestCode = scrResponse.getIntValue("code");
            // 每一张图片的检测结果。
            JSONArray taskResults = scrResponse.getJSONArray("data");
            if (200 == requestCode) {
                for (Object taskResult : taskResults) {
                    // 单张图片的处理结果。
                    int taskCode = ((JSONObject)taskResult).getIntValue("code");
                    // 对应检测场景下图片的处理结果。如果是多个场景，则会有每个场景的结果。
                    JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
                    if(200 == taskCode){
                        for (Object sceneResult : sceneResults) {
                            String scene = ((JSONObject)sceneResult).getString("scene");
                            String suggestion = ((JSONObject)sceneResult).getString("suggestion");
                            //do something
                            // 识别出来的身份证信息。
                            if("review" .equals(suggestion) && "ocr".equals(scene)){
                                JSONObject idCardInfo =  ((JSONObject) sceneResult).getJSONObject("idCardInfo");
                                System.out.println(idCardInfo.toJSONString());
                            }
                        }
                    }else{
                        // 单张图片处理失败，原因视具体的情况详细分析。
                        System.out.println("task process fail. task response:" + JSON.toJSONString(taskResult));
                     }
                }
            } else {
                /**
                 * 表明请求整体处理失败，原因视具体的情况详细分析。
                 */
                System.out.println("the whole image scan request failed. response:" + JSON.toJSONString(scrResponse));
             }
        }
    }
}