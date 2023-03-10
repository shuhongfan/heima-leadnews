package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustException;
import com.heima.model.common.constants.message.NewsAutoScanConstants;
import com.heima.model.common.constants.message.NewsUpOrDownConstants;
import com.heima.model.common.constants.message.PublishArticleConstants;
import com.heima.model.common.constants.wemedia.WemediaConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.threadlocal.WmThreadLocalUtils;
import com.heima.model.wemedia.dtos.NewsAuthDTO;
import com.heima.model.wemedia.dtos.WmNewsDTO;
import com.heima.model.wemedia.dtos.WmNewsPageReqDTO;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.model.wemedia.vos.WmNewsVO;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mrchen
 * @date 2022/4/25 11:07
 */
@Service
@Slf4j
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Value("${file.oss.web-site}")
    private String webSite;

    @Override
    public ResponseResult findList(WmNewsPageReqDTO dto) {
        // 1. ????????????   ??????   ??????
        dto.checkParam();
        WmUser user = WmThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 2. ??????????????????
        // 2.1  ????????????
        Page<WmNews> pageReq = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmNews> queryWrapper = Wrappers.lambdaQuery();
        // 2.2  ???????????????????????????title
//        if(StringUtils.isNotBlank(dto.getKeyword())){
//        }
        queryWrapper.like(StringUtils.isNotBlank(dto.getKeyword()),WmNews::getTitle,dto.getKeyword());
        // 2.3 ???????????????id  ??????channel_id
        queryWrapper.eq(dto.getChannelId()!=null,WmNews::getChannelId,dto.getChannelId());
        // 2.4 ?????????status   ??????????????????
        queryWrapper.eq(dto.getStatus()!=null,WmNews::getStatus,dto.getStatus());
        // 2.5 ?????????????????????  ?????? > = ????????????
        queryWrapper.ge(dto.getBeginPubDate()!=null,WmNews::getPublishTime,dto.getBeginPubDate());
        // 2.6 ?????????????????????  ?????? < = ????????????
        queryWrapper.le(dto.getEndPubDate()!=null,WmNews::getPublishTime,dto.getEndPubDate());
        // 2.7 ??????????????????id??????
        queryWrapper.eq(WmNews::getUserId,user.getId());
        // 2.8 ????????????????????????
        queryWrapper.orderByDesc(WmNews::getPublishTime);
        // 3. ??????????????????????????????   code msg data  host: ????????????????????????
        IPage<WmNews> pageResult = this.page(pageReq, queryWrapper);
        PageResponseResult result = new PageResponseResult(dto.getPage(), dto.getSize(), pageResult.getTotal(), pageResult.getRecords());
        result.setHost(webSite);
        return result;
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult submitNews(WmNewsDTO dto) {
        // 1.  ???????????? ??? dto ????????? wmNews??????
        if(StringUtils.isBlank(dto.getContent())||StringUtils.isBlank(dto.getTitle())){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"???????????? ??? ?????? ????????????");
        }
        WmUser user = WmThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto,wmNews);
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            // ???????????????type??????????????????????????????-1 ???????????? ???????????????null
            wmNews.setType(null);
        }
        List<String> images = dto.getImages();
        if(!CollectionUtils.isEmpty(images)){
            // ??????????????????????????????
            wmNews.setImages(imagesToStr(dto.getImages()));
        }
        // ?????????????????????
        wmNews.setUserId(user.getId());
        // 2.  ???????????????  wmNews
        saveWmNews(wmNews);
        if(WemediaConstants.WM_NEWS_DRAFT_STATUS.equals(dto.getStatus())){
            // ??????????????? ???????????????????????????
            return ResponseResult.okResult();
        }
        // 3. ????????????????????????????????????????????????????????????????????????
        // 3.1   ????????? ????????????????????????????????????
        List<String> urlList = parseContentImages(dto.getContent());
        // 3.2   ?????????????????? ??? ?????????????????????
        if(!CollectionUtils.isEmpty(urlList)){
            saveRelativeInfo(urlList,wmNews.getId(),WemediaConstants.WM_CONTENT_REFERENCE);
        }
        // 3.3 ?????????????????? ??? ?????????????????????
        saveRelativeInfoForCover(urlList,wmNews,dto);

        // 3.4 ?????????????????????
        rabbitTemplate.convertAndSend(NewsAutoScanConstants.WM_NEWS_AUTO_SCAN_QUEUE,wmNews.getId());
        log.info("????????????????????????????????????????????????id???: {}",wmNews.getId());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult findWmNewsById(Integer id) {
        //1 ????????????
        if (id == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2 ????????????
        WmNews wmNews = getById(id);
        if (wmNews == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //3 ????????????
        ResponseResult result = ResponseResult.okResult(wmNews);
        result.setHost(webSite);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult delNews(Integer id) {
        //1.????????????
        if(id == null){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"??????Id????????????");
        }
        //2.????????????
        WmNews wmNews = getById(id);
        if(wmNews == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"???????????????");
        }
        //3.???????????????????????????  status==9  enable == 1
        if(wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())
                && wmNews.getEnable().equals(WemediaConstants.WM_NEWS_UP)){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"??????????????????????????????");
        }
        //4.??????????????????????????????
        wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,wmNews.getId()));
        //5.????????????
        removeById(wmNews.getId());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult downOrUp(WmNewsDTO dto) {
        //1.????????????
        if(dto == null || dto.getId() == null){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID);
        }
        Short enable = dto.getEnable();
        if(enable == null ||
                (!WemediaConstants.WM_NEWS_UP.equals(enable)&&!WemediaConstants.WM_NEWS_DOWN.equals(enable))){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"?????????????????????");
        }
        //2.????????????
        WmNews wmNews = getById(dto.getId());
        if(wmNews == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"???????????????");
        }
        //3.????????????????????????
        if(!wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"????????????????????????????????????????????????");
        }
        //4.??????????????????????????????app??????????????????TODO
        update(Wrappers.<WmNews>lambdaUpdate().eq(WmNews::getId,dto.getId())
                .set(WmNews::getEnable,dto.getEnable()));

        if(WemediaConstants.WM_NEWS_UP.equals(dto.getEnable())){
            // ??????
            rabbitTemplate.convertAndSend(
                    NewsUpOrDownConstants.NEWS_UP_OR_DOWN_EXCHANGE,
                    NewsUpOrDownConstants.NEWS_UP_ROUTE_KEY,
                    wmNews.getArticleId()
            );
            log.info("?????????????????? ????????????   ??????id: {}",wmNews.getArticleId());
        }else {
            // ??????
            rabbitTemplate.convertAndSend(
                    NewsUpOrDownConstants.NEWS_UP_OR_DOWN_EXCHANGE,
                    NewsUpOrDownConstants.NEWS_DOWN_ROUTE_KEY,
                    wmNews.getArticleId()
            );
            log.info("?????????????????? ????????????   ??????id: {}",wmNews.getArticleId());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }




    @Autowired
    WmNewsMapper wmNewsMapper;
    /**
     * ??????????????????
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findList(NewsAuthDTO dto) {
        //1.????????????
        dto.checkParam();
        //???????????????
        int currentPage = dto.getPage();
        //???????????????
        dto.setPage((dto.getPage()-1)*dto.getSize());

        if(StringUtils.isNotBlank(dto.getTitle())){
            //  concat(%,?,%)
            dto.setTitle("%"+dto.getTitle()+"%");
        }

        //2.????????????
        List<WmNewsVO> wmNewsVoList = wmNewsMapper.findListAndPage(dto);
        //?????????????????????
        long count = wmNewsMapper.findListCount(dto);

        //3.????????????
        ResponseResult result = new PageResponseResult(currentPage, dto.getSize(), count, wmNewsVoList);
        result.setHost(webSite);
        return result;
    }


    @Autowired
    WmUserMapper wmUserMapper;

    /**
     * ??????????????????
     * @param id
     * @return
     */
    @Override
    public ResponseResult findWmNewsVo(Integer id) {
        //1????????????
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.??????????????????
        WmNews wmNews = getById(id);
        if(wmNews == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //3.????????????
        WmUser wmUser = null;
        if(wmNews.getUserId() != null){
            wmUser = wmUserMapper.selectById(wmNews.getUserId());
        }

        //4.??????vo????????????
        WmNewsVO wmNewsVo = new WmNewsVO();
        BeanUtils.copyProperties(wmNews,wmNewsVo);
        if(wmUser != null){
            wmNewsVo.setAuthorName(wmUser.getName());
        }
        ResponseResult responseResult = ResponseResult.okResult(wmNewsVo);
        responseResult.setHost(webSite);
        return responseResult;
    }




    /**
     * ???????????? ??? ?????????????????????
     * @param urlList
     * @param wmNews
     * @param dto
     */
    private void saveRelativeInfoForCover(List<String> urlList, WmNews wmNews, WmNewsDTO dto) {
        // 1. ??????dto??? ?????????????????????
        List<String> images = dto.getImages();
        // 2. ??????dto??????type??????????????? -1
        if(WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())){
            //     2.1  ?????????-1  ????????????????????????
            //     2.2  ???????????????????????????????????????
            int size = urlList.size();
            if(size > 0 && size <=2){
                //         ?????????????????????????????? 0   ?????? ??????2   ??????????????????
                images = urlList.stream().limit(1).collect(Collectors.toList());
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
            }else if (size > 2) {
                //         ?????????????????????????????? 0   ??????  2   ??????????????????
                images = urlList.stream().limit(3).collect(Collectors.toList());
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
            }else {
                //         ??????????????????????????? 0      ??????????????????
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }
            //         ????????????wmNews
            if(!CollectionUtils.isEmpty(images)){
                wmNews.setImages(imagesToStr(images));
            }
            updateById(wmNews);
        }
        // 3. ???????????? ?????? ??? ?????????????????????
        if(!CollectionUtils.isEmpty(images)){
            // ??????????????????????????????????????????????????????????????? ?????????????????? ???????????????
            images = images.stream().map(url -> url.replaceAll(webSite,"")).collect(Collectors.toList());
            saveRelativeInfo(images,wmNews.getId(),WemediaConstants.WM_IMAGE_REFERENCE);
        }
    }

    /**
     * "[{type:text,value:??????},{type:image,value:url??????},{},{}]"
     * @param content
     * @return
     */
    private List<String> parseContentImages(String content) {
        List<Map> contentMapList = JSON.parseArray(content, Map.class);
        return contentMapList.stream()
                        // ?????? type ??? image ?????????
                        .filter(m -> WemediaConstants.WM_NEWS_TYPE_IMAGE.equals(m.get("type")))
                        // ?????? type???image ??????map?????? ??????value?????? ?????????????????????
                        .map(m -> m.get("value").toString())
                        // ????????? ????????????website
                        .map(url -> url.replaceAll(webSite,""))
                        // ???????????????????????????
                        .distinct()
                        // ???????????????????????????
                        .collect(Collectors.toList());
    }


    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    /**
     * ?????????????????????????????????
     * @param urlList  ??????????????????
     * @param newsId   ??????id
     * @param type   ??????:   0 ????????????      1  ????????????
     */
    public void saveRelativeInfo(List<String> urlList,Integer newsId,Short type){
        // 1. ??????????????????url??????   ????????????????????????id??????
//        List<WmMaterial> wmMaterials = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery()
//                .eq(WmMaterial::getUserId, WmThreadLocalUtils.getUser().getId())
//                .in(WmMaterial::getUrl, urlList)
//                .select(WmMaterial::getId)
//        );
        List<Integer> ids = wmMaterialMapper.selectRelationsIds(urlList, WmThreadLocalUtils.getUser().getId());
        // 2. ?????? id??????  ??????  ???????????? ??????????????????  ???????????? ??????????????????
        if(CollectionUtils.isEmpty(ids) || ids.size() < urlList.size()){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"???????????????????????????????????????");
        }
        // 3. ??????id??????  newsId  type  ?????????????????? ??? wm_news_material
        wmNewsMaterialMapper.saveRelations(ids,newsId,type);
//        for (WmMaterial wmMaterial : wmMaterials) {
//            WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
//            wmNewsMaterial.setMaterialId(wmMaterial.getId());
//            wmNewsMaterial.setNewsId(newsId);
//            wmNewsMaterial.setType(type);
//            wmNewsMaterialMapper.insert(wmNewsMaterial);
//        }
    }

    @Autowired
    WmNewsMaterialMapper wmNewsMaterialMapper;

    private void saveWmNews(WmNews wmNews) {
        // 1. ????????????
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        // ?????????
        wmNews.setEnable(WemediaConstants.WM_NEWS_UP);
        // 2. ??????id????????????  ??????????????? ????????????
        if(wmNews.getId() == null){
            this.save(wmNews);
        }else {
            // 3. ????????????id  ????????????????????????????????????  ??? ??????
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,wmNews.getId()));
            this.updateById(wmNews);
        }
    }

    /**
     * ??????????????? ???????????????
     *
     * @param images
     * @return
     */
    private String imagesToStr(List<String> images) {
        return images.stream().map(url-> url.replaceAll(webSite,""))
                        .collect(Collectors.joining(","));
    }


    /**
     * ???????????????????????????
     * @param status 2  ????????????  4 ????????????
     * @param dto
     * @return
     */
    @Override
    public ResponseResult updateStatus(Short status, NewsAuthDTO dto) {
        //1.????????????
        if(dto == null || dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.????????????
        WmNews wmNews = getById(dto.getId());
        if(wmNews == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // ?????????????????? ?????????9  ?????????
        if (wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW,"???????????????");
        }
        //3.??????????????????
        wmNews.setStatus(status);
        if(StringUtils.isNotBlank(dto.getMsg())){
            wmNews.setReason(dto.getMsg());
        }
        updateById(wmNews);
        // ????????????????????????
        if (status.shortValue() == WmNews.Status.ADMIN_SUCCESS.getCode()) {
            // ??????????????????
            long publishTime = wmNews.getPublishTime().getTime();
            // ??????????????????
            long nowTime = System.currentTimeMillis();
            // ???????????? - ???????????? = ???????????????????????????
            long remainTime = publishTime - nowTime;
            // ??????rabbittemplate ??????????????????
            rabbitTemplate.convertAndSend(
                    PublishArticleConstants.DELAY_DIRECT_EXCHANGE,
                    PublishArticleConstants.PUBLISH_ARTICLE_ROUTE_KEY,
                    wmNews.getId(),
                    message -> {
                        message.getMessageProperties().setHeader("x-delay",remainTime<=0?0:remainTime);
                        return message;
                    }
            );
            log.info("????????????  ????????????????????????   ??????id: {}    ????????????: {}",wmNews.getId(), LocalDateTime.now());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
