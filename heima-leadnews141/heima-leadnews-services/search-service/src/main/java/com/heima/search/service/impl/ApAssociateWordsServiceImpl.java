package com.heima.search.service.impl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dtos.UserSearchDTO;
import com.heima.model.search.pojos.ApAssociateWords;
import com.heima.search.service.ApAssociateWordsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ApAssociateWordsServiceImpl implements ApAssociateWordsService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public ResponseResult findAssociate(UserSearchDTO userSearchDto) {
        //1 参数检查
        if(StringUtils.isBlank(userSearchDto.getSearchWords())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2 执行查询 模糊查询
        List<ApAssociateWords> wordsList = mongoTemplate.find(Query.query(Criteria.where("associateWords")
                .regex(".*" +userSearchDto.getSearchWords()+ ".*")), ApAssociateWords.class);
        return ResponseResult.okResult(wordsList);
    }
}