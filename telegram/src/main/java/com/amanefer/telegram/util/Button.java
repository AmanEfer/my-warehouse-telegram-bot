package com.amanefer.telegram.util;

import lombok.Getter;

@Getter
public enum Button {
    START_BUTTON("/start", "start"),
    USERS_BUTTON("/users", "users"),
    GET_MY_DATA_BUTTON("/get my data", "get my data"),
    GET_ALL_USERS_BUTTON("/get all users", "get all users"),
    STOCKS_BUTTON("/stocks", "stocks"),
    CREATE_NEW_STOCK("/create new stock", "create new stock"),
    GET_ALL_STOCKS_BUTTON("/get all stocks", "get all stocks"),
    PRODUCTS_BUTTON("/products", "products"),
    SAVE_NEW_PRODUCT("/save new product", "save new product"),
    GET_ALL_PRODUCTS_BUTTON("/get all products", "get all products"),
    MOVE_PRODUCTS_BUTTON("/move products", "move products"),
    REPORTS_BUTTON("/reports", "reports"),
    EXPORT_FILE_BUTTON("/export file","export file"),
    HELP_BUTTON("/help", "help"),
    YES_MOVE_PRODUCT_BUTTON("YES_MOVE_PRODUCT_BUTTON", "yes"),
    NO_MOVE_PRODUCT_BUTTON("NO_MOVE_PRODUCT_BUTTON", "no");

    private final String menuName;
    private final String keyboardName;

    Button(String menuName, String keyboardName) {
        this.keyboardName = keyboardName;
        this.menuName = menuName;
    }
}
