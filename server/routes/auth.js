import express from 'express'

import { signin, createUser } from '../controllers/auth.js'

const router = express.Router();

// registration 
router.post('/sign-up', createUser);

// login
router.post('/sign-in', signin);

export default router;



