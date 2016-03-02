# Aerospike-DataAccess

This is a simple Aerospike data access library that helps expedite the development of Aerospike applications. You only need data model classes and the provided generic repository class to store and retrieve data from the Aerospike database. The main advantages of this approach are: 


Ease of Use:

1. Requires only model classes that extend the Entity class. This eliminates the need to develop row mapping methods that convert records to/from entity objects. In addition, bin names can be independent of model class attributes. Model class attributes are also no longer required. See an example product Category class in the example module.

2. Takes care of mapping various data types to supported Aerospike types. For example, Aerospike currently does not support Boolean, BigDecimal, Float, and Timestamp. With this library, all of these types are supported. Internally, they are mapped to supported types and converted back to a proper type upon their retrieval. 

3. Supports converting objects to JSON string and store in the repository using fasterxml.jackson library. 

4. Maintains meta data in the class including mandatory attributes. This meta data helps to generate UI forms dynamically.

5. The Aerospike configuration is maintained in a JSON file. These parameters can be changed as required including list hosts.


Performance:

1. Uses HashMap, bins, as an internal data structure to store attribute and value pairs. This approach provides flexibility in creating an entity object using a constructor with HashMap that is obtained from Aerospike client record object. There is no need to duplicate data. Bins in the record are directly mapped to an entity object using reflection without requiring a row mapper. This eliminates developing an unnecessary mapper and additional memory allocation. Also uses HashMap to update a record.

2. Keeps track of changed fields for efficient update operations. For example, if an user object is retrieved from data base and user password is changed, an update method call will update only the password bin's value.


Important Classes:

1. Entity: All model classes should extend this class. This is relatively similar to the Aerospike client's record class.

2. AerospikeConfig: This class maintains the configuration data. This configuration data is stored in a JSON file, AerospikeConfig.json. It is used to create the Aerospike client.

3. AerospikeClientUtil: Singleton class that loads configuration and creates a client.

4. AerospikeRepository: This is generic repository class that implements methods in the Repository interface. This is similar to the Aerospike Spring data implementation but uses a HashMap structure to maintain entity data.

Example: 

The Review Product and Product Category classes can be found in the example module. There is also a Catalog class which shows how to instantiate Product and Category classes and store them in the database.


How to use?

This is an IntelliJ project. Please check out the code and integrate it. 
