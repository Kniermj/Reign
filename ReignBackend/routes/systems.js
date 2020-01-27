const express = require('express'),
    router = express.Router(),
    methodOverride = require('method-override'),
    crypto = require('crypto');

var admin = require("firebase-admin");
const db = admin.firestore();

router.use(methodOverride((req) => {
    if (req.body && typeof req.body == 'object' && '_method' in req.body) {
        const method = req.body._method;
        delete req.body._method;
        return method;
    }
}));

function handleError(err, res, msg, statusCode) {
    res.status(statusCode);
    err.status = statusCode;
    err.message = msg;
    res.json({ message: err.status + ' ' + err });
}

function makeRandomString(length){
    return crypto.randomBytes(Math.ceil(length/2))
            .toString('hex')
            .slice(0,length); 
};

function makeHash(password, salt){
    var sha256Hash= crypto.createHmac('sha512', salt);
    sha256Hash.update(password);
    var value = sha256Hash.digest('hex');
    return {
        salt: salt,
        hash: value
    };
};

function saltHashPassword(unsaltedPassword) {
    var salt = makeRandomString(16);
    var hashedPassword = makeHash(unsaltedPassword, salt);
    return hashedPassword;
}

function isSamePassword(passwordRequect, user){
    const requestHash = makeHash(passwordRequect, user.identify.salt);
    return requestHash.hash === user.identify.hash;
}

function createSystem(req, res){
    let docRef = db.collection('systems').doc();
    let item = docRef.set({
        name: req.body.name || "Not Given",
        RefreshTime: req.body.refreshTime || 3,
        active: req.body.active || false,
        identify: saltHashPassword(`${req.body.password}`),
        commandQueue: [],
        commandHistory: [],
        systemResourceSnapshots: [],
        activeprocesses: []
    });
    console.log(item)
    res.json(item)
}

function updateSystem(req, res, system){
    let oldInfo = db.collection('systems').doc(system).get()
    .then(snapshot => {
        let docRef = db.collection('systems').doc(system);  
        let item = docRef.set({
            name: req.body.name || snapshot.name,
            RefreshTime: req.body.refreshTime || snapshot.RefreshTime,
            active: req.body.active || snapshot.RefreshTime,
            identify: saltHashPassword(`${req.body.password}`),
            commandQueue: [],
            commandHistory: [],
            systemResourceSnapshots: [],
            activeprocesses: []
        });
      })
      .catch(err => {
        console.log('Error getting documents', err);
        res.json("error getting document");
      });
    
    res.json("worked")
}

router.route('/')
    .get((req, res) => {
        let docRef = db.collection('systems').doc();

        let setAda = docRef.set({
            name: 'testsys',
            RefreshTime: 3,
            active: true,
            passwordHash: '',
            passwordSalt: ''
        });
        res.json(setAda)
    });

router.route('/register')
    .post((req, res) => {
        createSystem(req, res)
    });

router.route('/:systemID')
    .put((req, res) => {
        if (req.params && req.params.systemID) {
            updateSystem(req, res, req.params.systemID)
        }
        
    })
    .get((req, res) => {

    });

module.exports = router;


   