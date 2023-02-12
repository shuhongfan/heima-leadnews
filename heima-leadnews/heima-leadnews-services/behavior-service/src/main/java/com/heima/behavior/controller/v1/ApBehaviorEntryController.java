package com.heima.behavior.controller.v1;

import com.heima.behavior.service.ApBehaviorEntryService;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/behavior_entry")
public class ApBehaviorEntryController {
    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;
    @GetMapping("/one")
    public ResponseResult<ApBehaviorEntry> findByUserIdOrEquipmentId
            (@RequestParam(value = "userId",required = false) Integer userId,
             @RequestParam(value = "equipmentId",required = false) Integer equipmentId){

        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(userId, equipmentId);
        return ResponseResult.okResult(apBehaviorEntry);
    }
}