package com.amanefer.telegram.commands.product;

import com.amanefer.telegram.cache.ProductListCache;
import com.amanefer.telegram.cache.ProductStateCache;
import com.amanefer.telegram.cache.ProductTransferDataCache;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.dto.ProductDto;
import com.amanefer.telegram.dto.ProductQuantityDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.UserState;
import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.util.ProductTransferData;
import com.amanefer.telegram.util.UpdateTransferData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovingProductCommand implements Command {

    public static final String INPUT_INVOICE_NUMBER = "Please, input the invoice number";
    public static final String INPUT_STOCK_FROM = "Now input warehouse's title from which the products pick up";
    public static final String INPUT_STOCK_TO = "Input warehouse's title to which we deliver products";
    public static final String INPUT_PRODUCT_QUANTITY = "Input the quantity of the item you want to move";
    public static final String INPUT_PRODUCT_ARTICLE = "Input product's article you want to move";
    public static final String ADD_ANOTHER_PRODUCT = "Do you want to add one more product to list to move?";
    public static final String PRODUCTS_MOVE_FAIL = "Products weren't moved, try again";

    private final RestToCrud restToCrud;
    private final UserStateCache userStateCache;
    private final ProductStateCache productStateCache;
    private final ProductListCache productListCache;
    private final ProductTransferDataCache productTransferDataCache;


    @Override
    public boolean support(String command) {

        return command.equals(Button.MOVE_PRODUCTS_BUTTON.toString());
    }

    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        long userId = updateTransferData.getUserId();
        long chatId = updateTransferData.getChatId();
        String dataForDto = updateTransferData.getDataForDto();
        UserState userState = userStateCache.getFromCache(userId);

        if (userState == UserState.PRIMARY) {
            userStateCache.putInCache(userId, UserState.MOVE_PRODUCT_INPUT_INVOICE_NUMBER);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(INPUT_INVOICE_NUMBER)
                    .build();

        } else if (userState == UserState.MOVE_PRODUCT_INPUT_INVOICE_NUMBER) {
            productTransferDataCache.putInCache(userId, new ProductTransferData(dataForDto));
            userStateCache.putInCache(userId, UserState.MOVE_PRODUCT_INPUT_STOCK_FROM);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(INPUT_STOCK_FROM)
                    .build();

        } else if (userState == UserState.MOVE_PRODUCT_INPUT_STOCK_FROM) {
            ProductTransferData productTransferData = productTransferDataCache.getFromCache(userId);
            productTransferData.setStockNameFrom(dataForDto);

            List<ProductDto> products = new ArrayList<>();
            productListCache.putInCache(userId, products);

            userStateCache.putInCache(userId, UserState.MOVE_PRODUCT_INPUT_STOCK_TO);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(INPUT_STOCK_TO)
                    .build();

        } else if (userState == UserState.MOVE_PRODUCT_INPUT_STOCK_TO) {
            ProductTransferData productTransferData = productTransferDataCache.getFromCache(userId);
            productTransferData.setStockNameTo(dataForDto);

            userStateCache.putInCache(userId, UserState.MOVE_PRODUCT_INPUT_PRODUCT_ARTICLE);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(INPUT_PRODUCT_ARTICLE)
                    .build();

        } else if (userState == UserState.MOVE_PRODUCT_INPUT_PRODUCT_ARTICLE) {
            productStateCache.putInCache(userId, new ProductDto(dataForDto));
            userStateCache.putInCache(userId, UserState.MOVE_PRODUCT_INPUT_PRODUCT_QUANTITY);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(INPUT_PRODUCT_QUANTITY)
                    .build();

        } else if (userState == UserState.MOVE_PRODUCT_INPUT_PRODUCT_QUANTITY) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();

            InlineKeyboardButton yesButton = InlineKeyboardButton.builder()
                    .text(Button.YES_MOVE_PRODUCT_BUTTON.getKeyboardName())
                    .callbackData(Button.YES_MOVE_PRODUCT_BUTTON.toString())
                    .build();

            InlineKeyboardButton noButton = InlineKeyboardButton.builder()
                    .text(Button.NO_MOVE_PRODUCT_BUTTON.getKeyboardName())
                    .callbackData(Button.NO_MOVE_PRODUCT_BUTTON.toString())
                    .build();

            row.add(yesButton);
            row.add(noButton);

            keyboard.add(row);
            inlineKeyboardMarkup.setKeyboard(keyboard);


            ProductDto product = productStateCache.getFromCache(userId);
            product.setCommonQuantity(Integer.parseInt(dataForDto));
            List<ProductDto> products = productListCache.getFromCache(userId);
            products.add(product);

            userStateCache.putInCache(userId, UserState.MOVE_PRODUCT);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(ADD_ANOTHER_PRODUCT)
                    .replyMarkup(inlineKeyboardMarkup)
                    .build();

        } else if (userState == UserState.MOVE_PRODUCT) {
            ProductTransferData transferData = productTransferDataCache.getFromCache(userId);

            List<ProductDto> movedProducts = restToCrud.moveProducts(
                    transferData.getInvoiceNumber(),
                    transferData.getStockNameFrom(),
                    transferData.getStockNameTo(),
                    productListCache.getFromCache(userId)
            );

            String textMessage = buildTextMessage(movedProducts);
            userStateCache.putInCache(userId, UserState.PRIMARY);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(textMessage)
                    .build();

        } else {
            userStateCache.putInCache(userId, UserState.PRIMARY);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(PRODUCTS_MOVE_FAIL)
                    .build();
        }
    }

    private String buildTextMessage(List<ProductDto> products) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < products.size(); i++) {

            ProductDto product = products.get(i);

            String article = product.getArticle();
            String title = product.getTitle();
            String warehousesLeftovers = "";
            int commonQuantity = 0;

            if (product.getQuantityList() != null && !product.getQuantityList().isEmpty()) {

                warehousesLeftovers = product.getQuantityList().stream()
                        .map(q -> String.format("There are %d items in the '%s'",
                                q.getQuantity(), q.getStock().getStockName()))
                        .collect(Collectors.joining("\n", "", "\n"));

                commonQuantity = product.getQuantityList().stream()
                        .mapToInt(ProductQuantityDto::getQuantity)
                        .sum();
            }

            stringBuilder
                    .append(String.format("%d) Article: %s\n", i + 1, article))
                    .append(String.format("Title: %s\n", title))
                    .append(warehousesLeftovers)
                    .append(String.format("Common quantity: %d", commonQuantity))
                    .append("\n\n");
        }

        return stringBuilder.toString();
    }
}
