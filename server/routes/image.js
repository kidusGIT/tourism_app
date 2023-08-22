import express from "express";
import multer from "multer";

// Image upload
const storage = multer.diskStorage({
    destination:(req, file, callback) =>{
        callback(null, 'src/tourism_images')
    }, filename:(req, file, callback) => {
        callback(null, Date.now() + '-' + file.originalname)
    }
})

const upload = multer({
    storage:storage
})

const router = express.Router()

// IMPORT USER BUILT MODULE HERE    
import { getAllImages, getCertainImages, getImage, createImage, deleteImage } from '../controllers/image.js'

// GET ALL IMAGES
router.get('/', getAllImages);

// GET CERTAIN IMAGE
router.get('/certain/:trId', getCertainImages);

// GET DEATIL IMAGE
router.get('/:id', getImage);

// CREATE IMAGE
router.post('/', upload.single('img_url'), createImage);

// DELETE IMAGE
router.delete('/:id', deleteImage)

export default router