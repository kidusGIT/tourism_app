import { Op } from "sequelize";

// import user module here
import Toursim from "../models/toursims.js";

// GET ALL TOURSIMS
export const getAllToursims = async (req, res) => {
    const toursims = await Toursim.findAll({
        order:[
            ['createdAt', 'DESC']
        ]
    })

    res.status(200).json(toursims);
}

export const getUserAllToursims = async (req, res) => {
    const toursims = await Toursim.findAll({
        order:[
            ['createdAt', 'DESC']
        ]
    })

    res.status(200).json(toursims);
}

// GET DETAIL TOURSIM
export const getToursim = async (req, res) => {
    console.log(req.params.id)
    const toursim = await Toursim.findOne({
        where:{
            id:req.params.id,
            
        },
       
    })
    if(toursim){
        return res.status(200).json(toursim);
    } else {
        return res.status(400).send('No Toursim Found')
    }
}

// CREATE A TOURSIM
export const createToursim = async (req, res) => {
    
    const toursim = new Toursim(req.body);
    if(!req.file){
        console.log("File not uploaded")
        await toursim.save();
    } else {
        console.log("files: %s", req.file.path)
        const toursim = new Toursim({
            name:req.body.name,
            desc:req.body.desc,
            cover_image:req.file.path,
        });
        await toursim.save();
    }

    res.status(200).send(toursim);
}

// UPDATE A TROUSIM
export const updateToursim = async (req, res) => {
   console.log('header: ', req.headers.token);

    if(req.file){
        console.log("File uploade uploaded")
        const toursim = await Toursim.update({
            name:req.body.name,
            desc:req.body.desc,
            cover_image:req.file.path,
        }, {
            where:{
                id:req.params.id
            }
        });

        return res.status(200).json(toursim)
        
    } 
    const toursim = await Toursim.update( req.body, {
        where:{
            id:req.params.id
        }
    });

    res.status(200).json(toursim);
}

// DELETE A TOURSIM
export const deleteToursim = async (req, res) => {
    await Toursim.destroy({
        where:{
            id:req.params.id
        }
    })

    res.status(200).send('Toursim has succsfully deleted');
}

// SEARCH TOURISM 
export const searchTourism = async (req, res) => {
    const search = req.query.q;
   
    const tourism = await Toursim.findAll({
        where:{
            name: {
                [Op.like]:`%${search}%`
            }
        }
    });

    res.status(200).json(tourism);
}