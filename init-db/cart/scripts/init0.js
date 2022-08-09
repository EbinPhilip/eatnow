db = new Mongo().getDB("cart-db");
db.createCollection("cart", { capped: false });
