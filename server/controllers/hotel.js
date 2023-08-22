
// import user built module here
import Hotel from "../models/hotels.js"

// GET ALL HOTELS
export const getAllHotels = async (req, res) => {
    const hotels = await Hotel.findAll();

    res.status(200).json(hotels);
}

// GET CERTAIN HOTELS
export const getCertainHotels = async (req, res) => {
    const hotels = await Hotel.findAll({
        where:{
            ToursimId:req.params.trId,
        },
        order: [
            ['createdAt', 'DESC']
        ]
    });

    if(!hotels) return res.status(400).send('No hotels for this toursim');

    res.status(200).json(hotels);
}

// GET DETAIL HOTELS
export const getHotel = async (req, res) => {
    const hotel = await Hotel.findByPk(req.params.id);

    res.status(200).json(hotel);
}

// CREATE HOTELS
export const createHotel = async (req, res) => {
    const hotel = new Hotel(req.body);
    console.log(req.body.ToursimId)
     
    try {
        if(!req.file){
            console.log("File not uploaded")
            await hotel.save();
        } else {
            console.log("files: %s", req.file.path)
            const toursim = new Hotel({
                name:req.body.name,
                desc:req.body.desc,
                img:req.file.path,
                ToursimId:req.body.ToursimId,
                latitude:req.body.latitude,
                longtude:req.body.longtude
            });
            await toursim.save();
        }
    } catch (err) {
        console.log(err)
        return res.status(400).send('Foregn key constraint');
    }
    res.status(200).send(hotel);
}

// UPDATE HOTELS
export const updateHotel = async (req, res) => {
   try {
        if(!req.file){
            const hotel = await Hotel.update(req.body, {
                where:{
                    id:req.params.id,
                }
            })
            
            return res.status(200).json(hotel);
        } else {
             const hotel = await Hotel.update({
                name:req.body.name,
                desc:req.body.desc,
                img:req.file.path,
                ToursimId:req.body.ToursimId,
                latitude:req.body.latitude,
                longtude:req.body.longtude
            }, {
                where:{
                    id:req.params.id,
                }
            });

            return res.status(200).json(hotel);
        }
       
        
   } catch (err) {
    res.status(400).send('No hotel to update');
   }

}

// DELETE HOTELS
export const deleteHotel = async (req, res) => {
    try {
        await Hotel.destroy({
            where:{
                id:req.params.id,
            }
        });
        res.status(400).send('Hotel deleted succesfully');
    } catch (err) {
        res.status(400).send('No hotel to delete');
    }
}
