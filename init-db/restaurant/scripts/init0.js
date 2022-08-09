db = new Mongo().getDB("restaurant-db");
db.createCollection("restaurant", { capped: false });
db.createCollection("menu", { capped: false });