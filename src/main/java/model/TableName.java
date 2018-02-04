package model;

public enum TableName {
    YEAR ("Year"),
    CLIENT("Client"),
    PRODUCT("Product"),
    PRODUCT_TYPE("ProductType"),
    COUNTRY("Country");

    private final String name;

    TableName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
