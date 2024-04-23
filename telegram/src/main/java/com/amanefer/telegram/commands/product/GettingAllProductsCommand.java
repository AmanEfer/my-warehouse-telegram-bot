package com.amanefer.telegram.commands.product;

import com.amanefer.telegram.util.Button;
import com.amanefer.telegram.cache.UserStateCache;
import com.amanefer.telegram.commands.Command;
import com.amanefer.telegram.dto.ProductDto;
import com.amanefer.telegram.dto.ProductQuantityDto;
import com.amanefer.telegram.services.RestToCrud;
import com.amanefer.telegram.util.BotState;
import com.amanefer.telegram.util.UpdateTransferData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GettingAllProductsCommand implements Command {

    private final RestToCrud restToCrud;
    private final UserStateCache userStateCache;


    @Override
    public boolean support(String command) {

        return command.equals(Button.GET_ALL_PRODUCTS_BUTTON.toString());
    }

    @Override
    public PartialBotApiMethod<Message> process(UpdateTransferData updateTransferData) {

        List<ProductDto> products = restToCrud.getAllProducts();

        String textMessage = buildTextMessage(products);

        userStateCache.putInCache(updateTransferData.getUserId(), BotState.PRIMARY);

        return SendMessage.builder()
                .chatId(updateTransferData.getChatId())
                .text(textMessage)
                .build();
    }

    private String buildTextMessage(List<ProductDto> products) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < products.size(); i++) {

            ProductDto product = products.get(i);

            String article = product.getArticle();
            String title = product.getTitle();
            BigDecimal purchaseLastPrice = product.getPurchaseLastPrice();
            BigDecimal saleLastPrice = product.getSaleLastPrice();
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
                    .append(purchaseLastPrice == null ? "" : String.format("Purchase last price: %.2f\n",
                            purchaseLastPrice))
                    .append(saleLastPrice == null ? "" : String.format("Sale last price: %.2f\n",
                            saleLastPrice))
                    .append(warehousesLeftovers)
                    .append(String.format("Common quantity: %d", commonQuantity))
                    .append("\n\n");
        }

        return stringBuilder.toString();
    }

}
