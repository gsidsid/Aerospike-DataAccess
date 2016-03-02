# Aerospike-DataAccess

This is a simple Aerospike data access library that helps with faster development of Aerospike applications. You just need data model classes and use provided generic repository class to store and retrieve data from Aerospike database. Main advantages of this approach are:

Ease of Use:

1. Requires only model classes that extend the Entity class. There is no need to develop row mapping methods to convert record to an entity object and vice versa. In addition, bin names can be independant of model class attributes. Model class attributes can be longer as required. See an example product Category class in the example module.

2. Takes care of mapping various data types to supported Aerospike types. For example, Aerospike does not support Boolean, BigDecimal, Float, and Timstamp. All these types are supported in this library. Internally, they are mapped to supported types and converted back to proper type on retrieval. 

3. Supports converting objects to JSON string and store in the repository using fasterxml.jackson library. 

4. Maintains meta data in the class including mandatory attributes. This meta data helps to generate UI forms dynamically.

5. Aerospike configuration is maintained in a JSON file. These parameters can be changed as required including list hosts.

Performance:

1. Uses HashMap, bins, as internal data structure to store attribute and value pairs. This approach provides flexibility in creating an entity object using a constructor with HashMap that is obtained from Aerospike client record object. There is no need to duplicate data. Bins in the record are directly mapped to an entity object using reflection without requiring a row mapper. This eliminates developing an unnecessary mapper and additional memory allocation. Uses HashMap, bins, to update a record as well.

2. Keeps track of changed fields for efficient update operations. For example, if an user object is retrieved from data base and user password is changed, an update method call will update only password bin value.


Important Classes:

1. Entity: All model classes should extend this class. This is more or less similar to Aerospike client record class.

2. AerospikeConfig: This class maintains the configuration data. Configuration data is stored in a JSON file, AerospikeConfig.json. It is used to create Aerospike client.

3. AerospikeClientUtil: Singleton class that loads configuration and creates a client.

4. AerospikeRepository: This is generic repository class that implements methods in the Repository interface. This is similar to Aerospike Spring data implementation but uses HashMap structure to maintain entity data.

Example: 

Review Product and Product Category classes in example module. There is a Catalog class which shows how to instantiate Product and Category classes and store them in the database.


How to use?

This is an IntelliJ project. Please check out the code and integrate it. 
