package com.amanefer.telegram.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransferData {

    private long chatId;
    private long userId;
    private String text;
    private String userName;
    private String dataForDto;

}
