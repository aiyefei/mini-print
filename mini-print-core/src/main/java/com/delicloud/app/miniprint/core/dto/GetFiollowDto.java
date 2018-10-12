package com.delicloud.app.miniprint.core.dto;

import com.delicloud.platform.common.lang.bo.page.ReqPage;
import lombok.Data;

@Data
public class GetFiollowDto extends ReqPage {

    /**
     * 关注人Id
     */
    private Long fromUid;
}
