package com.amanefer.telegram.commands.product;

import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.cache.ProductStateCache;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.dto.ProductDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.BotState;
import com.amanefer.telegram.util.UpdateTransferData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class SaveProductCommand implements Command {

    public static final String INPUT_PRODUCT_ARTICLE = "Please, input the article of the new product";
    public static final String INPUT_PRODUCT_TITLE = "Now input the product title";
    public static final String PRODUCT_SAVE_MESSAGE = "Product '%s' was saved successfully!";
    public static final String PRODUCT_SAVE_FAIL = "Product wasn't saved, try again";

    private final RestToCrud rest;
    private final UserStateCache userStateCache;
    private final ProductStateCache productStateCache;

    @Override
    public boolean support(String command) {

        return command.equals(Button.SAVE_NEW_PRODUCT.toString());
    }

    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        long chatId = updateTransferData.getChatId();
        long userId = updateTransferData.getUserId();
        String dataForDto = updateTransferData.getDataForDto();
        BotState userState = userStateCache.getFromCache(userId);

        if (userState == BotState.PRIMARY) {
            userStateCache.putInCache(userId, BotState.SAVE_PRODUCT_INPUT_PRODUCT_TITLE);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(INPUT_PRODUCT_ARTICLE)
                    .build();

        } else if (userState == BotState.SAVE_PRODUCT_INPUT_PRODUCT_TITLE) {
            ProductDto product = new ProductDto();
            product.setArticle(dataForDto);

            productStateCache.putInCache(userId, product);
            userStateCache.putInCache(userId, BotState.SAVE_PRODUCT);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(INPUT_PRODUCT_TITLE)
                    .build();

        } else if (userState == BotState.SAVE_PRODUCT) {
            ProductDto product = productStateCache.getFromCache(userId);
            product.setTitle(dataForDto);

            String title = rest.saveNewProduct(product).getTitle();

            userStateCache.putInCache(userId, BotState.PRIMARY);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format(PRODUCT_SAVE_MESSAGE, title))
                    .build();

        } else {
            userStateCache.putInCache(userId, BotState.PRIMARY);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(PRODUCT_SAVE_FAIL)
                    .build();
        }
    }
}
