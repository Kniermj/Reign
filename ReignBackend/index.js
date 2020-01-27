const express = require('express');
const logger = require('morgan');
const bodyParser = require('body-parser');

const port = process.env.PORT || 3000;
console.log(port);



const app = express();

//firebase connection start

var admin = require("firebase-admin");

var serviceAccount = "keys/reign-76b44-firebase-adminsdk-npgy4-141c9e4d7b.json";

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://reign-76b44.firebaseio.com"
});

//friebase connection end

const users = require('./routes/users');
const systems = require('./routes/systems');


app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.use('/users', users);
app.use('/systems', systems);

app.listen(port, () => {
    console.log(`Listening on port ${port}`);
});

module.exports = app;