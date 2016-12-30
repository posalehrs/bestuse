const mongoose = require('mongoose');
const Schema = mongoose.Schema

const ProductSchema = Schema({
    name: { type: String, required: true },
    description: { type: String, required: true },
    image: String,
    old_price: {type: Number, default: 0},
    new_price: {type: Number, default: 0, required:true},
    category: {type: String, enum: ['Kẹo', 'Sữa', 'Thực phẩm khô','Thực phẩm tươi']},
    expiration_date: {type:Number, default:0},
    up_date: { type: Date, default: Date.now },
    amount: {type: Number, default: 0,required:true},
    address: String,
    selling: {type: Number, default: 1},
    _user:[{ type: Schema.Types.ObjectId, ref: 'User' }]
});

module.exports = mongoose.model('Product', ProductSchema);