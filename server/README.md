# AnimeDiary Server
The server for the AnimeDiary project is written in [express](https://expressjs.com/) with [MongoDB](https://www.mongodb.com/) as data source.

## Getting Started
Use the following steps to get the project and start the server locally.

### Cloning the project

```
$ git clone https://github.com/rodriguez-maria/AnimeDiary.git
```

### Installing MongoDB

Visit https://docs.mongodb.com/manual/installation/ and follow the instructions to install MongoDB to your computer.


### Starting MongoDB
Before you start your web server, MongoDB needs to be running. Run the following command to start MongoDB.

```
$ mongod --config /usr/local/etc/mongod.conf
```

### Seeding the databse with sample data
You will need to have a sample list of animes to use the project. We have compiled a [sample list](https://github.com/rodriguez-maria/AnimeDiary/blob/master/server/seed/sample_animes.json) from https://kitsu.io that you can use. Run the following command to seed your local Mongo installation with this sample list.

```
$ mongoimport --db animediary --collection animes --file ./server/seed/sample_animes.json --jsonArray
```
You will need to do this only once.

### Creating required environment variables 
Create a copy of `sample.env` and rename it to `.env`. Change the settings in the `.env` if needed.


# Running the server
Start the web server by running the following command from `./server` directory.

````
$ npm start
````

For the first time or if there is any error, you may need to run the following command.

```
$ npm install
```
