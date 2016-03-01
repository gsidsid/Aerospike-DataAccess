package com.infinira.aerospike.example;

/**
 * Created by Siddharth Garimella on 2/23/2016.
 */

import com.aerospike.client.query.IndexType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.infinira.aerospike.dataaccess.repository.SetDelete;
import com.infinira.aerospike.dataaccess.util.Utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Catalog creates 1000 products and 100 categories.
 */
public class Catalog {
    private static int CATEGORY_MAX_COUNT = 100;
    private static int PRODUCT_MAX_COUNT = 1000;
    private static Category[] categories = new Category[CATEGORY_MAX_COUNT];
    private static Product[] products = new Product[PRODUCT_MAX_COUNT];

    // Create product categories
    private static void createCategories() throws JsonProcessingException {   // Create CATEGORY_MAX_COUNT categories.
        for (int i = 0; i <CATEGORY_MAX_COUNT ; i++) {
            Category category = new Category();
            category.setCategoryId("Category-"+i);
            category.setParentId(null);
            category.setName("CategoryName:"+i);
            category.setProducts(new ArrayList<String>());
            category.setSource("TEST_DATA");
            category.setCoverage("North America");
            category.setCreator("Siddharth");
            category.setPublisher("Atlas Publisher");
            category.setContributor("Gary");
            category.setCreationDate(Utils.now());
            category.setModifiedDate(Utils.now());
            category.setImgType("PNG");
            category.setImgName("Electronics");
            category.setImgUrl("http://www.testurl.com/"+i);
            category.setImgAltText("Category-"+i);
            category.setShortDesc("Category-"+i);
            category.setLongDesc("Category-"+i);
            category.setCustomProp(null);
            String jsonStr = category.toJSONString();
            //System.out.println(jsonStr);
            categories[i] = category;
        }
    }

    // Create a product
    private static Product createProduct(String productId)
    {
        Product product = new Product();
        product.setProductId(productId);
        product.setSku("SKU-"+productId);
        product.setName("Product Name for " + productId);
        product.setCode("1234");
        ArrayList<String> category_ids = new ArrayList<String>();
        int cCount = (int)(Math.random()*10) +1;
        for (int i = 0; i <cCount ; i++) {
            int cat_id =  (int)(Math.random()*CATEGORY_MAX_COUNT);
            category_ids.add("Category-"+cat_id);
            // Add product to that category as well.
            categories[cat_id].getProducts().add(productId);
        }
        //System.out.println(category_ids.toString());
        product.setCategories(category_ids);
        product.setInStock(true);
        product.setVisible(true);
        product.setTaxId("STATE_TAX");
        product.setType("ELECTRONICS");
        product.setMsrpCurrency("USD");
        BigDecimal msrp = new BigDecimal(87.99, new MathContext(5));
        product.setMsrpAmount(msrp);
        product.setPriceCurrency("USD");
        BigDecimal price = new BigDecimal(127.99, new MathContext(5));
        product.setPriceAmount(price);
        product.setNotes(Utils.now());
        product.setShortDesc("Test product");
        product.setLongDesc("Test product");
        product.setImgType("PNG");
        product.setImgName("Product Image");
        product.setImgUrl("http://www.testproduct.com/" + productId);
        product.setImgAltText("Alt text for " + productId);
        product.setCreatedBy("Gary");
        product.setCreatedDate(Utils.now());
        product.setModifiedDate(Utils.now());
        // Set some custom properties.
        HashMap<String,Object> customProp = new HashMap<String,Object>();
        customProp.put("Height", (int)(Math.random()*50.0+5));
        customProp.put("Width", (int)(Math.random()*25.0+5));
        customProp.put("Weight",(int)(Math.random()*20.0+5));
        product.setCustomProp(customProp);
        return product;
    }

    // Create product objects
    private static void createProducts()
    {
        // Create products
        for (int i = 0; i < PRODUCT_MAX_COUNT; i++) {
            products[i] = createProduct("Product-" + i);
        }
    }
    public static void main(String[] args) throws Exception {
        SetDelete.clearSet("test","Product");
        SetDelete.clearSet("test","Category");
        // Create index to search on product_id
        RepositoryFactory.getProductRepository().createIndex("ProductIdIndex","product_id", IndexType.STRING);
        // Create categories
        Catalog.createCategories();
        Catalog.createProducts();
        // Insert Categories
        RepositoryFactory.getCategoryRepository().insertAll(new ArrayList<Category>(Arrays.asList(categories)));
        // Insert Products
        RepositoryFactory.getProductRepository().insertAll(new ArrayList<Product>(Arrays.asList(products)));

        // Find a product
        Product product =  RepositoryFactory.getProductRepository().findOne("Product-1");
        // Retrieve associated product categories.
        RepositoryFactory.getProductRepository().getProductCategories(product.getProductId());
    }
}
