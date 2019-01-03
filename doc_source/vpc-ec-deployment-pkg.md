# Step 2\.1: Create a Deployment Package<a name="vpc-ec-deployment-pkg"></a>

The following example Python code reads and writes an item to your ElastiCache cluster\. 

1. Open a text editor, and then copy the following code\.

   ```
   from __future__ import print_function
   import time
   import uuid
   import sys
   import socket
   import elasticache_auto_discovery
   from pymemcache.client.hash import HashClient
   
   #elasticache settings
   elasticache_config_endpoint = "your-elasticache-cluster-endpoint:port"
   nodes = elasticache_auto_discovery.discover(elasticache_config_endpoint)
   nodes = map(lambda x: (x[1], int(x[2])), nodes)
   memcache_client = HashClient(nodes)
   
   def handler(event, context):
       """
       This function puts into memcache and get from it.
       Memcache is hosted using elasticache
       """
   
       #Create a random UUID... this will the sample element we add to the cache.
       uuid_inserted = uuid.uuid4().hex
       #Put the UUID to the cache.
       memcache_client.set('uuid', uuid_inserted)
       #Get item (UUID) from the cache.
       uuid_obtained = memcache_client.get('uuid')
       if uuid_obtained.decode("utf-8") == uuid_inserted:
           # this print should go to the CloudWatch Logs and Lambda console.
           print ("Success: Fetched value %s from memcache" %(uuid_inserted))
       else:
           raise Exception("Value is not the same as we put :(. Expected %s got %s" %(uuid_inserted, uuid_obtained))
   
       return "Fetched value from memcache: " + uuid_obtained.decode("utf-8")
   ```

1. Save the file as `app.py`\. 

1. Install the following library dependencies using **pip**:
   + `pymemcache` – The Lambda function code uses this library to create a `HashClient` object to set and get items from memcache \(see [pymemcache](https://pypi.python.org/pypi/pymemcache)\)\. 
   + `elasticache-auto-discovery` – The Lambda function uses this library to get the nodes in your Amazon ElastiCache cluster \(see [elasticache\-auto\-discovery](https://pypi.python.org/pypi/elasticache-auto-discovery)\)\.

1. Zip all of these files into a file named `app.zip` to create your deployment package\. For step\-by\-step instructions, see [Creating a Deployment Package \(Python\)](lambda-python-how-to-create-deployment-package.md)\. 

To use Python Redis, see [Python Redis](https://redislabs.com/lp/python-redis/)\.