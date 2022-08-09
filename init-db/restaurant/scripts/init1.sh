#!/bin/bash

echo "########### Loading data to Mongo DB ###########"
mongoimport --jsonArray --db restaurant-db --collection restaurant --file /tmp/data/restaurant.json
mongoimport --jsonArray --db restaurant-db --collection menu --file /tmp/data/menu.json
