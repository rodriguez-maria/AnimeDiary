# Run the server
````
$ npm install
$ node app.js
````

# Seed the db with sample list of animes
This sample list is downloaded from https://kitsu.io
mongoimport --db animediary --collection animes --file ./seed/sample_animes.json --jsonArray

# Install mongodb go to https://docs.mongodb.com/ for updated instruction

# Start mongodb server

$ mongod --config /usr/local/etc/mongod.conf