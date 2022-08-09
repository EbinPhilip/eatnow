db = new Mongo().getDB("restaurant-db");
db.restaurant.createIndex( { "location" : "2dsphere" } )
db.menu.createIndex( { "restaurantId" : 1, "items.itemIndex" : 1 } )