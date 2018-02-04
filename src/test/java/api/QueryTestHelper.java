package api;

import util.DbUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryTestHelper {

    private static Connection connection;
    private static Statement statement;

    public static void createTables() {

        String queryCreateSchema = "CREATE DATABASE IF NOT EXISTS pivot; " +
                "USE pivot;";
        String queryDropTables = "DROP TABLE IF EXISTS `Sales`;\n " +
                "DROP TABLE IF EXISTS `Product`;\n " +
                "DROP TABLE IF EXISTS `ProductType`;\n " +
                "DROP TABLE IF EXISTS `Year`;\n " +
                "DROP TABLE IF EXISTS `Country`;\n " +
                "DROP TABLE IF EXISTS `Client`;";
        String queryCreateClient = "CREATE TABLE `Client` (" +
                "  `Id` int(11) NOT NULL," +
                "  `Name` varchar(45) DEFAULT NULL," +
                "  PRIMARY KEY (`Id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        String queryCreateCountry = "CREATE TABLE `Country` (\n" +
                "  `Id` int(11) NOT NULL,\n" +
                "  `Name` varchar(45) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`Id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        String queryCreateYear = "CREATE TABLE `Year` (\n" +
                "  `Id` int(11) NOT NULL,\n" +
                "  `Name` varchar(45) DEFAULT NULL,\n" +
                "  `SortId` int(11) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`Id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        String queryCreateProductType = "CREATE TABLE `ProductType` (\n" +
                "  `Id` int(11) NOT NULL,\n" +
                "  `Name` varchar(45) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`Id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        String queryCreateProduct = "CREATE TABLE `Product` (\n" +
                "  `Id` int(11) NOT NULL,\n" +
                "  `Name` varchar(45) DEFAULT NULL,\n" +
                "  `ProductTypeId` int(11) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`Id`),\n" +
                "  KEY `fk_Product_1_idx` (`ProductTypeId`),\n" +
                "  CONSTRAINT `fk_Product_1` FOREIGN KEY (`ProductTypeId`) REFERENCES `ProductType` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        String queryCreateSales = "CREATE TABLE `Sales` (\n" +
                "  `ProductId` int(11) NOT NULL,\n" +
                "  `YearId` int(11) NOT NULL,\n" +
                "  `ClientId` int(11) NOT NULL,\n" +
                "  `CountryId` int(11) NOT NULL,\n" +
                "  `Count` int(11) DEFAULT NULL,\n" +
                "  `TotalCost` double DEFAULT NULL,\n" +
                "  PRIMARY KEY (`ProductId`,`YearId`,`ClientId`,`CountryId`),\n" +
                "  KEY `fk_Sales_YearId_idx` (`YearId`),\n" +
                "  KEY `fk_Sales_ClientId_idx` (`ClientId`),\n" +
                "  KEY `fk_Sales_CountryId_idx` (`CountryId`),\n" +
                "  CONSTRAINT `fk_Sales_CountryId` FOREIGN KEY (`CountryId`) REFERENCES `Country` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `fk_Sales_ProductId` FOREIGN KEY (`ProductId`) REFERENCES `Product` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `fk_Sales_YearId` FOREIGN KEY (`YearId`) REFERENCES `Year` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        String query = queryCreateSchema + queryDropTables + queryCreateClient +
                queryCreateCountry + queryCreateYear +
                queryCreateProductType + queryCreateProduct +
                queryCreateSales;

        executeQuery(query);
    }

    public static void insertValues() {
        String queryInsertIntoClient = "USE pivot; INSERT INTO `Client` (`Id`, `Name`) VALUES ('1', 'Niki');\n" +
                "INSERT INTO `Client` (`Id`, `Name`) VALUES ('2', 'John');";
        String queryInsertIntoCountry = "INSERT INTO `Country` (`Id`, `Name`) VALUES ('1', 'USA');\n" +
                "INSERT INTO `Country` (`Id`, `Name`) VALUES ('2', 'Canada');";
        String queryInsertIntoYear = "INSERT INTO `Year` (`Id`, `Name`, `SortId`) VALUES ('1', '2001', '1');\n" +
                "INSERT INTO `Year` (`Id`, `Name`, `SortId`) VALUES ('2', '2002', '2');\n" +
                "INSERT INTO `Year` (`Id`, `Name`, `SortId`) VALUES ('3', '2003', '3');\n" +
                "INSERT INTO `Year` (`Id`, `Name`, `SortId`) VALUES ('4', '2004', '4');\n" +
                "INSERT INTO `Year` (`Id`, `Name`, `SortId`) VALUES ('5', '2005', '5');\n" +
                "INSERT INTO `Year` (`Id`, `Name`, `SortId`) VALUES ('6', '2006', '6');";
        String queryInsertIntoProductType = "INSERT INTO `ProductType` (`Id`, `Name`) VALUES ('1', 'Food');\n" +
                "INSERT INTO `ProductType` (`Id`, `Name`) VALUES ('2', 'Drink');";
        String queryInsertIntoProduct = "INSERT INTO `Product` (`Id`, `Name`, `ProductTypeId`) VALUES ('1', 'Wine', '2');\n" +
                "INSERT INTO `Product` (`Id`, `Name`, `ProductTypeId`) VALUES ('2', 'Bred', '1');\n" +
                "INSERT INTO `Product` (`Id`, `Name`, `ProductTypeId`) VALUES ('3', 'Butter', '1');\n" +
                "INSERT INTO `Product` (`Id`, `Name`, `ProductTypeId`) VALUES ('4', 'Meat', '1');";
        String queryInsertIntoSales = "INSERT INTO `Sales` (`ProductId`, `YearId`, `ClientId`, `CountryId`, `Count`, `TotalCost`) \n" +
                "VALUES ('1', '1', '1', '1', '1', '564');\n" +
                "INSERT INTO `Sales` (`ProductId`, `YearId`, `ClientId`, `CountryId`, `Count`, `TotalCost`) \n" +
                "VALUES ('1', '2', '1', '1', '1', '12.5');\n" +
                "INSERT INTO `Sales` (`ProductId`, `YearId`, `ClientId`, `CountryId`, `Count`, `TotalCost`) \n" +
                "VALUES ('2', '6', '2', '2', '2', '74123');\n" +
                "INSERT INTO `Sales` (`ProductId`, `YearId`, `ClientId`, `CountryId`, `Count`, `TotalCost`) \n" +
                "VALUES ('3', '3', '1', '2', '1', '102');\n" +
                "INSERT INTO `Sales` (`ProductId`, `YearId`, `ClientId`, `CountryId`, `Count`, `TotalCost`) \n" +
                "VALUES ('3', '6', '1', '1', '1', '100');\n" +
                "INSERT INTO `Sales` (`ProductId`, `YearId`, `ClientId`, `CountryId`, `Count`, `TotalCost`) \n" +
                "VALUES ('3', '6', '2', '1', '1', '326');\n";

        String query = queryInsertIntoClient + queryInsertIntoCountry +
                queryInsertIntoYear + queryInsertIntoProductType +
                queryInsertIntoProduct + queryInsertIntoSales;

        executeQuery(query);
    }

    private static void executeQuery(String query) {
        try {
            connection = DbUtils.getConnection();
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private static void close() {
        try {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException ex) { }
    }
}
