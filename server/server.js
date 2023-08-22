import express from 'express';
import { fileURLToPath } from 'url'
import path from 'path';
import dotenv from 'dotenv';

const _filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(_filename);

const app = express();

// IMPORT USER BUILT MODULE HERE
import hotelRoutes from './routes/hotel.js';
import toursimRoutes from './routes/toursim.js';
import imageRoutes from './routes/image.js';
import userRoutes from './routes/auth.js';

// MIDDELWARE
app.use(express.json());
app.use('*/src', express.static(__dirname + '/src'))
dotenv.config();

// ROUTES
app.use('/api/hotels', hotelRoutes);
app.use('/api/toursim', toursimRoutes);
app.use('/api/image', imageRoutes);
app.use('/api/auth', userRoutes);

const HOST = '192.168.43.202'

app.listen(8800, HOST, () => {
     console.log(`running on server ${HOST}:8800`)
});
