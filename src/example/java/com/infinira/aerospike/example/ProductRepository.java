package com.infinira.aerospike.example;

import com.infinira.aerospike.dataaccess.repository.AerospikeRepository;
import com.infinira.aerospike.dataaccess.util.Utils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siddharth Garimella on 2/23/2016.
 */

/**
 * Extending standard repository with custom APIs.
 */
public class ProductRepository extends AerospikeRepository<Product> {
    public ProductRepository(Class<Product> domainType) {
        super(domainType);
    }

    public ProductRepository(String nameSpace, Class<Product> domainType) {
        super(nameSpace, domainType);
    }

    /**
     * Get product category objects for a given productid. This shows how to extend a respository if custom methods are required.
     * @param productId   Product id (key)
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public ArrayList<Category> getProductCategories(String productId) throws IOException, IllegalAccessException, InstantiationException {
        Assert.notNull(productId, "Product Id cannot be null or empty");
        if (exists(productId))
        {
            // Get the product category id list
            // Get the product category list JSON from DB
            String categoryJSON = (String) get(productId,Product.CATEGORIES);
            // Convert them into list of string.
            ArrayList<String> categoryIds = (ArrayList<String>) Utils.getObjectMapper().readValue(categoryJSON, List.class);
            // Retrieve Category objects from Category table.
            ArrayList<Category> categories = RepositoryFactory.getCategoryRepository().findAll((String[]) categoryIds.toArray(new String[categoryIds.size()]));
            return categories;
        }
        else return null;
    }
}
