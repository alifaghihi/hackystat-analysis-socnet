# Introduction #
SocNet services communicate with each other using [REST](http://en.wikipedia.org/wiki/Representational_State_Transfer/) architectural principles. This means that storing, accessing, and managing data in the persistent store are done by HTTP calls of GET, PUT, POST, and DELETE.

The resources in the persistent store are
  * nodetypes - Constant values used to identify different types of nodes, such as a Twitter account or a Facebook friend.
  * relationshiptypes - Constant values used to identify different types of relationships, such as a Facebook friend relationship or an Ohloh contributes to relationship.
  * clients - Authenticated users who are allowed to manipulate data in the store.
  * nodes - The vertices of the social media graph, representing people, projects, and other objects.
  * relationships - The edges of the social media graph, representing the connections between people, projects, and other objects.

# Details #
|GET |{host}/nodetypes          |Returns a list of all of the node types in the graph|
|:---|:-------------------------|:---------------------------------------------------|
|GET |{host}/nodetypes/{nodetypename}|Returns a representation of the named node type     |
|PUT |{host}/nodetypes/{nodetypename}|Create a representation of the named node type (admin only)|
|GET |{host}/relationshiptypes  |Returns a list of all the relationship types in the graph|
|GET |{host}/relationshiptypes/{relationshiptypename}|Returns a representation of the named relationship  |
|PUT |{host}/relationshiptypes/{relationshiptypename}|Create a representation of the named relationship type (admin)|
|GET |{host}/clients            |Returns a list of all of the clients using the server |
|PUT |{host}/clients/{client}   |Returns a representation of the named client        |
|POST|{host}/clients/{client}    |Updates the representation of the named client      |
|DELETE|{host}/clients/{client}   |Deletes the specified client from the database      |
|GET |{host}/nodes              |Returns a list of all the nodes in the graph        |
|GET |{host}/nodes/{nodetype}   |Returns a list of all the nodes of that type in the graph|
|GET |{host}/nodes/{nodetype}/{nodename}/|Returns the node specified by the given name and type|
|GET |{host}/nodes/{nodetype}/{nodename}/{relationshiptype}/{relationshipdirection}|Returns a list of all of the nodes connected to that node by the specified relationship and relationship direction |
|GET |{host}/nodes/{nodetype}/{nodename}/{relationshiptype}/{relationshipdirection}/nodes?startTime={tstamp}&endTime={tstamp}|Returns a list of all nodes that were connected to the named node by the specified relationship and relationship direction during the specified time period |
|GET |{host}/nodes/{nodetype}/{nodename}/nodes?startTime={tstamp}&endTime={tstamp}|Returns a list of all of the nodes of the specified type that were connected to the named node by a relationship between the start time and the end time|
|GET |{host}/nodes/{nodetype}/{nodename}/relationships?startTime={tstamp}&endTime={tstamp}|Returns a list of all of the relationships that were connected to the named node during the specified time interval|
|PUT |{host}/nodes/{nodetype}/{nodename}/|Creates a representation of the named node          |
|POST|{host}/nodes/{nodetype}/{nodename}/|Updates the representation of the named node        |
|DELETE|{host}/nodes/{nodetype}/{nodename}/|Deletes the node specified by the given type and relationship|