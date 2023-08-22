
// import user module here
import Image from "../models/images.js";

// GET ALL IMAGES
export const getAllImages = async (req, res) => {
    const images = await Image.findAll();

    res.status(200).json(images);
}

// GET CERTAIN IMAGE
export const getCertainImages = async (req, res) => {
    const images = await Image.findAll({
        where:{
            ToursimId:req.params.trId,
        }
    });

    if(images) return res.status(200).json(images);

    res.status(400).send('No images for this toursim');
}

// GET DEATIL IMAGE
export const getImage = async (req, res) => {
    const image = await Image.findByPk(req.params.id);

    if(image) return res.status(200).json(image);
    res.status(400).send('No image found'); 
}

// CREATE IMAGE
export const createImage = async (req, res) => {
    const image = new Image(req.body);
    try {
        if(!req.file){
            console.log("File not uploaded")
            await image.save();
        } else {
            console.log("files: %s", req.file.path)
            const image = new Image({
                img_url:req.file.path,
                ToursimId:req.body.ToursimId,
            });
            await image.save();
        }
       
    } catch (err) {
        return res.status(200).json('No toursim found with this id.');
    }
    
    res.status(200).json(image);
}

// DELETE IMAGE
export const deleteImage = async (req, res) => {
    try {
        await Image.destroy({
            where:{
                id:req.params.id,
            }
        })

        return res.status(200).send('Image has deleted Succfuly');
    } catch (err) {
        return res.status(200).send(`We can not find image with this id ${req.params.id}`);
    }
}