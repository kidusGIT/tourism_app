import express from "express";
import multer from "multer";

import { verfiyToken } from '../verifyToken.js'

// Image upload
const storage = multer.diskStorage({
    destination:(req, file, callback) =>{
        callback(null, 'src/tousim_cover')
    }, filename:(req, file, callback) => {
        callback(null, Date.now() + '-' + file.originalname)
    }
})

const upload = multer({
    storage:storage
})

const router = express.Router();

// IMPORT USER BUILT MODULE HERE
import { getAllToursims, getToursim, createToursim, updateToursim, deleteToursim, searchTourism, getUserAllToursims } from '../controllers/toursim.js';

// GET ALL TOURSIMS
router.get('/', verfiyToken, getAllToursims);

router.get('/users', getUserAllToursims);

// GET DETAIL TOURSIM
router.get('/:id', getToursim)

// CREATE A TOURSIM
router.post('/create-toursim', upload.single('cover_image'), createToursim)

// UPDATE A TROUSIM
router.put('/update-toursim/:id', upload.single('cover_image'), updateToursim)

// DELETE A TOURSIM
router.delete('/delete-toursim/:id', deleteToursim)

// SEARCH TOURISM
router.get('/search/tourism', searchTourism)


export default router;