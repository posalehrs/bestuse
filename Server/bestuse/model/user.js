const mongoose = require('mongoose');
const Schema = mongoose.Schema

const UserSchema = Schema({
    name: {type: String, required: true},
    password: {type: String, required: true},
    email: {type: String, required: true},
    address: String,
    number_phone: String,
    image: String
});

module.exports = mongoose.model('User', UserSchema);