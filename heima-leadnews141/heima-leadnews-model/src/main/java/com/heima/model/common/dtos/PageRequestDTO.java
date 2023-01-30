package com.heima.model.common.dtos;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 分页查询参数
 * 查询频道列表   频道名称  page   size
 *                      page   size
 *
 *
 *             ChannelDto extends PageRequestDto {  频道名称 }
 *               page
 *               size
 */
@Data
@Slf4j
public class PageRequestDTO {
    @ApiModelProperty(value="当前页",required = true)
    protected Integer size;
    @ApiModelProperty(value="每页显示条数",required = true)
    protected Integer page;


    public void checkParam() {
        if (this.page == null || this.page <= 0) {
            setPage(1);
        }
        if (this.size == null || this.size <= 0 || this.size > 100) {
            setSize(10);
        }
    }
}
