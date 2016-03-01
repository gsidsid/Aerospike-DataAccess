/*
 * Copyright 2016 Infinira Software Private Limited.
 *
 * Portions may be licensed to Infinira Software Private Limited. under one or more contributor
 * license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.infinira.aerospike.example;


import com.infinira.aerospike.dataaccess.repository.AerospikeRepository;

/**
 * Created by Siddharth Garimella on 12/16/2015.
 */
public final class RepositoryFactory {

    // Aerospike repositories
    private static AerospikeRepository<Category> categoryRepository;
    private static ProductRepository productRepository;

    public RepositoryFactory() {
    }

    // Return default Category repository
    public static AerospikeRepository<Category> getCategoryRepository() throws InstantiationException, IllegalAccessException {
        if (categoryRepository == null)
            categoryRepository = new AerospikeRepository<Category>(Category.class);
        return categoryRepository;
    }

    // Return Custom Product repository
    public static ProductRepository getProductRepository() throws InstantiationException, IllegalAccessException {
        if (productRepository == null)
            productRepository = new ProductRepository(Product.class);
        return productRepository;
    }
}
