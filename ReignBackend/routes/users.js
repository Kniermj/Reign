const express = require('express'),
    router = express.Router(),
    methodOverride = require('method-override');

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

router.route('/')
    .get((req, res) => {
        let docRef = db.collection('users').doc('alovelace');

        let setAda = docRef.set({
            name: 'testsys',
            RefreshTime: 'Lovelace',
            born: 1815
        });
        res.json(setAda)
    });

module.exports = router;


   