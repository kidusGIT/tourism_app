import express from "express";
import multer from "multer";

// Image upload
const storage = multer.diskStorage({
    destination:(req, file, callback) =>{
        callback(null, 'src/hotel_cover')
    }, filename:(req, file, callback) => {
        callback(null, Date.now() + '-' + file.originalname)
    }
})

const upload = multer({
    storage:storage
})

const router = express.Router()

// IMPORT USER BUILT MODUE HERE
import { getAllHotels, getCertainHotels, getHotel, createHotel, updateHotel, deleteHotel } from '../controllers/hotel.js'

// GET ALL HOTELS
router.get('/', getAllHotels);

// GET CERTAIN HOTELS
router.get('/certain/:trId', getCertainHotels);

// GET DETAIL HOTELS
router.get('/:id', getHotel);

// CREATE HOTELS
router.post('/',  upload.single('img'), createHotel);

// UPDATE HOTELS
router.put('/update-hotel/:id', upload.single('img'), updateHotel);

// DELETE HOTELS
router.delete('/delete-hotel/:id', deleteHotel)

export default router;